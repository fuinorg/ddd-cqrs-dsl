package org.fuin.dsl.cqrs.intellij.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Materializes remote {@code .cqrs} models and caches them under a {@code .dependencies-cache}
 * directory next to the {@code dependencies.json} catalog. Each Maven artifact is cached once in its
 * own {@code <artifactId>-<version>-<sha1(gav)>} sub-directory holding every {@code .cqrs} unpacked
 * from the {@code tar.gz}. Keying the cache by the Maven coordinate (rather than by namespace) means
 * an artifact that provides several namespaces is downloaded and unpacked only once and shared by all
 * of them. When an entry declares a {@code local} directory the {@code .cqrs} files are read straight
 * from that folder (resolved relative to the catalog when not absolute); nothing is downloaded or
 * cached. Mirrors the Eclipse {@code RemoteScopeCache}: same directory naming and {@code index.json}
 * shape, so the on-disk cache is interoperable.
 */
public final class RemoteScopeCache {

    private static final Logger LOG = Logger.getInstance(RemoteScopeCache.class);

    public static final String CACHE_DIR_NAME = ".dependencies-cache";
    public static final String INDEX_FILE_NAME = "index.json";

    public record CacheEntry(String source, String dir) {
    }

    /** In-memory index per cache directory: source (the Maven GAV) -> entry. */
    private final Map<Path, Map<String, CacheEntry>> indexByDir = new ConcurrentHashMap<>();

    /**
     * Returns the cached model files providing {@code namespace}, or an empty list when nothing is
     * cached (and downloading is disabled or fails). When {@code allowDownload} is {@code false} this
     * never touches the network &mdash; safe to call on the resolve path (EDT / read action). A
     * {@code local} entry is always read directly from its folder regardless of {@code allowDownload}.
     */
    public List<Path> getCachedModelFiles(@Nullable Path catalogDir, String namespace,
                                          @Nullable RemoteScopeEntry entry, boolean allowDownload) {
        if (catalogDir == null || entry == null) {
            return List.of();
        }
        // A local directory overrides the artifact: read its models directly, no download or cache.
        if (entry.getLocal() != null && !entry.getLocal().isEmpty()) {
            Path local = Path.of(entry.getLocal());
            return cqrsFiles(local.isAbsolute() ? local : catalogDir.resolve(local));
        }
        String source = entry.getSourceId();
        if (source == null || source.isEmpty()) {
            return List.of();
        }
        Path cacheDir = catalogDir.resolve(CACHE_DIR_NAME);
        String dirName = entry.getArtifactId() + "-" + entry.getVersion() + "-" + sha1(source);
        Path targetDir = cacheDir.resolve(dirName);

        Map<String, CacheEntry> index = indexFor(cacheDir);
        CacheEntry existing = index.get(source);
        if (existing != null && existing.source().equals(source) && Files.isDirectory(targetDir)) {
            List<Path> cached = cqrsFiles(targetDir);
            if (!cached.isEmpty()) {
                return cached;
            }
        }
        if (!allowDownload) {
            // Stale or missing, but we must not block on the network here; serve whatever is on disk.
            return cqrsFiles(targetDir);
        }

        try {
            materialize(entry, targetDir);
        } catch (Exception ex) {
            LOG.warn("Failed to materialize remote CQRS source '" + source + "'", ex);
            return cqrsFiles(targetDir);
        }
        index.put(source, new CacheEntry(source, dirName));
        persist(cacheDir, index);
        return cqrsFiles(targetDir);
    }

    /** Downloads and unpacks the artifact's model(s) into {@code targetDir}, replacing stale content. */
    private static void materialize(RemoteScopeEntry entry, Path targetDir) throws Exception {
        cleanDir(targetDir);
        Files.createDirectories(targetDir);
        try (InputStream in = new MavenArtifactResolver().openArtifact(entry.getGroupId(),
                entry.getArtifactId(), entry.getVersion())) {
            TarGz.extractCqrsFiles(in, targetDir.toFile());
        }
    }

    /** All {@code .cqrs} files in {@code dir}, sorted by name for a stable order. */
    private static List<Path> cqrsFiles(Path dir) {
        if (!Files.isDirectory(dir)) {
            return List.of();
        }
        try (Stream<Path> s = Files.list(dir)) {
            return s.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".cqrs"))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }

    private static void cleanDir(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return;
        }
        try (Stream<Path> s = Files.list(dir)) {
            for (Path p : (Iterable<Path>) s::iterator) {
                Files.deleteIfExists(p);
            }
        }
    }

    private Map<String, CacheEntry> indexFor(Path cacheDir) {
        return indexByDir.computeIfAbsent(cacheDir, RemoteScopeCache::loadIndex);
    }

    private static Map<String, CacheEntry> loadIndex(Path cacheDir) {
        Map<String, CacheEntry> map = new LinkedHashMap<>();
        Path indexFile = cacheDir.resolve(INDEX_FILE_NAME);
        if (!Files.isRegularFile(indexFile)) {
            return new ConcurrentHashMap<>();
        }
        try (Reader reader = Files.newBufferedReader(indexFile, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray entries = root.getAsJsonArray("entries");
            if (entries != null) {
                for (var element : entries) {
                    JsonObject obj = element.getAsJsonObject();
                    if (obj.has("source") && obj.has("dir")) {
                        CacheEntry entry = new CacheEntry(obj.get("source").getAsString(),
                                obj.get("dir").getAsString());
                        map.put(entry.source(), entry);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.warn("Failed to read remote scope cache index '" + indexFile + "'", ex);
        }
        // Use a concurrent map so background warming and resolution can share it safely.
        return new ConcurrentHashMap<>(map);
    }

    private static void persist(Path cacheDir, Map<String, CacheEntry> index) {
        try {
            Files.createDirectories(cacheDir);
            JsonArray array = new JsonArray();
            for (CacheEntry entry : index.values()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("source", entry.source());
                obj.addProperty("dir", entry.dir());
                array.add(obj);
            }
            JsonObject root = new JsonObject();
            root.add("entries", array);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (Writer writer = Files.newBufferedWriter(cacheDir.resolve(INDEX_FILE_NAME),
                    StandardCharsets.UTF_8)) {
                gson.toJson(root, writer);
            }
        } catch (IOException ex) {
            LOG.warn("Failed to persist remote scope cache index in '" + cacheDir + "'", ex);
        }
    }

    private static String sha1(String value) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-1").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new IllegalStateException("SHA-1 not available", ex);
        }
    }

    /** Drops the in-memory index for a cache directory (e.g. after the catalog changed). */
    public void invalidate() {
        indexByDir.clear();
    }
}

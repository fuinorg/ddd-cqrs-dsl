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
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Materializes remote {@code .cqrs} models and caches them under a {@code .dependencies-cache}
 * directory next to the {@code dependencies.json} catalog. Each resolved namespace is cached in its
 * own sub-directory holding one or more {@code .cqrs} files: a {@code simple} source uses
 * {@code <namespace>-<sha1(source)>} with the single downloaded file, a {@code maven} source uses
 * {@code <namespace>-<version>-<sha1(source)>} with every {@code .cqrs} unpacked from the
 * {@code tar.gz} (the version is part of the name so versions stay distinct on disk). Mirrors the
 * Eclipse {@code RemoteScopeCache}: same directory naming, same {@code index.json} shape and the
 * same per-entry layout, so the on-disk cache is interoperable.
 */
public final class RemoteScopeCache {

    private static final Logger LOG = Logger.getInstance(RemoteScopeCache.class);

    public static final String CACHE_DIR_NAME = ".dependencies-cache";
    public static final String INDEX_FILE_NAME = "index.json";

    /** File name of the single model cached for a {@code simple} source. */
    public static final String SIMPLE_FILE_NAME = "model.cqrs";

    public record CacheEntry(String namespace, String source, String dir) {
    }

    /** In-memory index per cache directory: namespace -> entry. */
    private final Map<Path, Map<String, CacheEntry>> indexByDir = new ConcurrentHashMap<>();

    /**
     * Returns the cached model files providing {@code namespace}, or an empty list when nothing is
     * cached (and downloading is disabled or fails). When {@code allowDownload} is {@code false} this
     * never touches the network &mdash; safe to call on the resolve path (EDT / read action).
     */
    public List<Path> getCachedModelFiles(@Nullable Path catalogDir, String namespace,
                                          @Nullable RemoteScopeEntry entry, boolean allowDownload) {
        if (catalogDir == null || entry == null) {
            return List.of();
        }
        String source = entry.getSourceId();
        if (source == null || source.isEmpty()) {
            return List.of();
        }
        Path cacheDir = catalogDir.resolve(CACHE_DIR_NAME);
        String ns = RemoteScopeCatalog.stripWildcard(namespace);
        String dirName = RemoteScopeEntry.TYPE_MAVEN.equals(entry.getType())
                ? ns + "-" + entry.getVersion() + "-" + sha1(source)
                : ns + "-" + sha1(source);
        Path targetDir = cacheDir.resolve(dirName);

        Map<String, CacheEntry> index = indexFor(cacheDir);
        CacheEntry existing = index.get(ns);
        if (existing != null && existing.source().equals(source)
                && Files.isDirectory(targetDir) && upToDate(entry, targetDir)) {
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
        index.put(ns, new CacheEntry(ns, source, dirName));
        persist(cacheDir, index);
        return cqrsFiles(targetDir);
    }

    /** Downloads / unpacks the entry's model(s) into {@code targetDir}, replacing any stale content. */
    private static void materialize(RemoteScopeEntry entry, Path targetDir) throws Exception {
        cleanDir(targetDir);
        Files.createDirectories(targetDir);
        if (RemoteScopeEntry.TYPE_SIMPLE.equals(entry.getType())) {
            download(entry.getUrl(), targetDir.resolve(SIMPLE_FILE_NAME));
        } else if (RemoteScopeEntry.TYPE_MAVEN.equals(entry.getType())) {
            try (InputStream in = new MavenArtifactResolver().openArtifact(entry.getGroupId(),
                    entry.getArtifactId(), entry.getVersion())) {
                TarGz.extractCqrsFiles(in, targetDir.toFile());
            }
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
                    if (obj.has("namespace") && obj.has("source") && obj.has("dir")) {
                        CacheEntry entry = new CacheEntry(obj.get("namespace").getAsString(),
                                obj.get("source").getAsString(), obj.get("dir").getAsString());
                        map.put(entry.namespace(), entry);
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
                obj.addProperty("namespace", entry.namespace());
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

    /**
     * A cached {@code simple} entry whose source is a local {@code file:} is stale once that file has
     * been modified more recently than the cached copy. All other sources ({@code http(s):} and
     * {@code maven}) are treated as up to date (delete the cache directory to force a refresh).
     */
    private static boolean upToDate(RemoteScopeEntry entry, Path targetDir) {
        if (!RemoteScopeEntry.TYPE_SIMPLE.equals(entry.getType())) {
            return true;
        }
        Path source = sourceFile(entry.getUrl());
        if (source == null) {
            return true;
        }
        Path cached = targetDir.resolve(SIMPLE_FILE_NAME);
        try {
            return Files.isRegularFile(cached) && Files.getLastModifiedTime(source).toMillis()
                    <= Files.getLastModifiedTime(cached).toMillis();
        } catch (IOException e) {
            return true;
        }
    }

    private static @Nullable Path sourceFile(String url) {
        try {
            URI uri = new URI(url);
            return "file".equals(uri.getScheme()) ? Path.of(uri) : null;
        } catch (Exception ex) {
            return null;
        }
    }

    private static void download(String url, Path target) throws Exception {
        Files.createDirectories(target.getParent());
        URL source = new URI(url).toURL();
        try (var in = source.openStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
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

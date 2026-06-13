package org.fuin.dsl.cqrs.intellij.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Downloads remote {@code .cqrs} models and caches them under a {@code .remote-scope-cache}
 * directory next to the {@code .remote-scope.json} catalog. Mirrors the Eclipse
 * {@code RemoteScopeCache}: same directory name, same {@code index.json} shape and the same
 * {@code <namespace>-<sha1(url)>.cqrs} file naming, so the on-disk cache is interoperable.
 */
public final class RemoteScopeCache {

    private static final Logger LOG = Logger.getInstance(RemoteScopeCache.class);

    public static final String CACHE_DIR_NAME = ".remote-scope-cache";
    public static final String INDEX_FILE_NAME = "index.json";

    public record CacheEntry(String namespace, String url, String file) {
    }

    /** In-memory index per cache directory: namespace -> entry. */
    private final Map<Path, Map<String, CacheEntry>> indexByDir = new ConcurrentHashMap<>();

    /**
     * Returns the cached model file for {@code namespace}, or {@code null} when nothing is cached
     * (and downloading is disabled or fails). When {@code allowDownload} is {@code false} this never
     * touches the network — safe to call on the resolve path (EDT / read action).
     */
    public @Nullable Path getCachedModelFile(Path catalogDir, String namespace, String url,
                                             boolean allowDownload) {
        if (catalogDir == null || url == null || url.isEmpty()) {
            return null;
        }
        Path cacheDir = catalogDir.resolve(CACHE_DIR_NAME);
        String ns = RemoteScopeCatalog.stripWildcard(namespace);

        Map<String, CacheEntry> index = indexFor(cacheDir);
        CacheEntry existing = index.get(ns);
        if (existing != null && existing.url().equals(url)) {
            Path cached = cacheDir.resolve(existing.file());
            if (Files.isRegularFile(cached) && upToDate(url, cached)) {
                return cached;
            }
        }
        if (!allowDownload) {
            // Stale or missing, but we must not block on the network here.
            return existing != null && Files.isRegularFile(cacheDir.resolve(existing.file()))
                    ? cacheDir.resolve(existing.file()) : null;
        }

        String fileName = ns + "-" + sha1(url) + ".cqrs";
        Path target = cacheDir.resolve(fileName);
        try {
            download(url, target);
        } catch (Exception ex) {
            LOG.warn("Failed to download remote CQRS model '" + url + "'", ex);
            return Files.isRegularFile(target) ? target : null;
        }
        index.put(ns, new CacheEntry(ns, url, fileName));
        persist(cacheDir, index);
        return target;
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
                    CacheEntry entry = new CacheEntry(obj.get("namespace").getAsString(),
                            obj.get("url").getAsString(), obj.get("file").getAsString());
                    map.put(entry.namespace(), entry);
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
                obj.addProperty("url", entry.url());
                obj.addProperty("file", entry.file());
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

    /** A cached file is current unless a {@code file:} source has been modified more recently. */
    private static boolean upToDate(String url, Path cached) {
        Path source = sourceFile(url);
        if (source == null) {
            return true; // http(s) is always treated as up to date (manual delete forces refresh)
        }
        try {
            return Files.getLastModifiedTime(source).toMillis()
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

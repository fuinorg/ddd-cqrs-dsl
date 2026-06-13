package org.fuin.dsl.cqrs.intellij.remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reads the {@code .remote-scope.json} catalog that maps a fully qualified {@code context.namespace}
 * to the URL of the remote {@code .cqrs} model providing it. Discovered by walking up the directory
 * tree from a model file. Mirrors the Eclipse {@code RemoteScopeCatalog} contract so the on-disk
 * layout is interoperable.
 *
 * <p>The catalog is a JSON array of single-entry objects:</p>
 * <pre>
 * [ { "com.acme.billing": "http://models.acme.com/billing.cqrs" } ]
 * </pre>
 */
public final class RemoteScopeCatalog {

    private static final Logger LOG = Logger.getInstance(RemoteScopeCatalog.class);

    public static final String DEFAULT_FILE_NAME = ".remote-scope.json";
    public static final String FILE_NAME_PROPERTY = "cqrs.remote.scope.file";

    /** Sentinel stored in the discovery cache to mean "searched, none found" (no null values). */
    private static final Path NONE = Path.of("__cqrs_no_remote_scope_catalog__");

    /** Discovered catalog path per directory ({@link #NONE} means "searched, none found"). */
    private final Map<Path, Path> catalogPathByDir = new ConcurrentHashMap<>();
    /** Parsed catalog with the file's last-modified stamp, so edits are re-read. */
    private final Map<Path, Parsed> parsedByPath = new ConcurrentHashMap<>();

    private record Parsed(long stamp, Map<String, String> namespaceToUrl) {
    }

    public static String fileName() {
        return System.getProperty(FILE_NAME_PROPERTY, DEFAULT_FILE_NAME);
    }

    public static String stripWildcard(String namespace) {
        if (namespace != null && namespace.endsWith(".*")) {
            return namespace.substring(0, namespace.length() - 2);
        }
        return namespace;
    }

    /** Directory containing the catalog for a model in {@code startDir}, or {@code null}. */
    public @Nullable Path rootDir(@Nullable Path startDir) {
        Path catalog = catalogPath(startDir);
        return catalog != null ? catalog.getParent() : null;
    }

    /** URL of the remote model providing {@code namespace}, or {@code null} if not configured. */
    public @Nullable String lookupUrl(@Nullable Path startDir, String namespace) {
        Map<String, String> catalog = catalog(startDir);
        if (catalog == null) {
            return null;
        }
        return catalog.get(stripWildcard(namespace));
    }

    private @Nullable Map<String, String> catalog(@Nullable Path startDir) {
        Path catalog = catalogPath(startDir);
        if (catalog == null) {
            return null;
        }
        long stamp = lastModified(catalog);
        Parsed cached = parsedByPath.get(catalog);
        if (cached != null && cached.stamp() == stamp) {
            return cached.namespaceToUrl();
        }
        Map<String, String> parsed = parse(catalog);
        parsedByPath.put(catalog, new Parsed(stamp, parsed));
        return parsed;
    }

    private @Nullable Path catalogPath(@Nullable Path startDir) {
        if (startDir == null) {
            return null;
        }
        Path cached = catalogPathByDir.get(startDir);
        if (cached != null) {
            if (cached == NONE) {
                return null; // previously searched, nothing found
            }
            if (Files.isRegularFile(cached)) {
                return cached;
            }
            // a previously found catalog was deleted/moved -> search again
        }
        return findAndCache(startDir);
    }

    private @Nullable Path findAndCache(Path startDir) {
        String name = fileName();
        for (Path dir = startDir; dir != null; dir = dir.getParent()) {
            Path candidate = dir.resolve(name);
            if (Files.isRegularFile(candidate)) {
                catalogPathByDir.put(startDir, candidate);
                return candidate;
            }
        }
        catalogPathByDir.put(startDir, NONE);
        return null;
    }

    private static long lastModified(Path path) {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            return 0L;
        }
    }

    private static Map<String, String> parse(Path catalog) {
        Map<String, String> result = new LinkedHashMap<>();
        try {
            String content = Files.readString(catalog, StandardCharsets.UTF_8);
            JsonElement root = JsonParser.parseString(content);
            if (!root.isJsonArray()) {
                throw new IllegalStateException("Expected a JSON array at the root");
            }
            JsonArray array = root.getAsJsonArray();
            for (JsonElement element : array) {
                if (!element.isJsonObject()) {
                    throw new IllegalStateException("Expected a JSON object entry, but got '" + element + "'");
                }
                JsonObject obj = element.getAsJsonObject();
                for (Entry<String, JsonElement> entry : obj.entrySet()) {
                    result.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        } catch (Exception ex) {
            LOG.warn("Failed to parse remote scope catalog '" + catalog + "'; ignoring it", ex);
            return Map.of();
        }
        return result;
    }
}

package org.fuin.dsl.cqrs.intellij.remote;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reads the {@code dependencies.json} catalog that maps a fully qualified {@code context.namespace}
 * to the typed source ({@link RemoteScopeEntry}) of the remote {@code .cqrs} model(s) providing it.
 * Discovered by walking up the directory tree from a model file. Mirrors the Eclipse
 * {@code RemoteScopeCatalog} contract so the on-disk layout is interoperable.
 *
 * <p>The catalog is a JSON array of typed objects. Each entry lists the namespaces it provides in a
 * {@code namespaces} array, which lets a single {@code .cqrs} file or Maven artifact that holds
 * several contexts/namespaces be declared once:</p>
 * <pre>
 * [
 *   { "type": "simple", "namespaces": ["com.acme.billing", "com.acme.catalog"],
 *     "data": { "url": "http://models.acme.com/billing.cqrs" } },
 *   { "type": "maven", "namespaces": ["com.acme.common"],
 *     "data": { "groupId": "org.fuin.dsl.cqrs.contexts",
 *               "artifactId": "cqrs-common-model", "version": "0.1.0-SNAPSHOT" } }
 * ]
 * </pre>
 */
public final class RemoteScopeCatalog {

    private static final Logger LOG = Logger.getInstance(RemoteScopeCatalog.class);

    public static final String DEFAULT_FILE_NAME = "dependencies.json";
    public static final String FILE_NAME_PROPERTY = "cqrs.dependencies.file";

    /** Sentinel stored in the discovery cache to mean "searched, none found" (no null values). */
    private static final Path NONE = Path.of("__cqrs_no_remote_scope_catalog__");

    /** Discovered catalog path per directory ({@link #NONE} means "searched, none found"). */
    private final Map<Path, Path> catalogPathByDir = new ConcurrentHashMap<>();
    /** Parsed catalog with the file's last-modified stamp, so edits are re-read. */
    private final Map<Path, Parsed> parsedByPath = new ConcurrentHashMap<>();

    private record Parsed(long stamp, Map<String, RemoteScopeEntry> namespaceToEntry) {
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

    /** The typed source providing {@code namespace}, or {@code null} if not configured. */
    public @Nullable RemoteScopeEntry lookupEntry(@Nullable Path startDir, String namespace) {
        Map<String, RemoteScopeEntry> catalog = catalog(startDir);
        if (catalog == null) {
            return null;
        }
        return catalog.get(stripWildcard(namespace));
    }

    private @Nullable Map<String, RemoteScopeEntry> catalog(@Nullable Path startDir) {
        Path catalog = catalogPath(startDir);
        if (catalog == null) {
            return null;
        }
        long stamp = lastModified(catalog);
        Parsed cached = parsedByPath.get(catalog);
        if (cached != null && cached.stamp() == stamp) {
            return cached.namespaceToEntry();
        }
        Map<String, RemoteScopeEntry> parsed = parse(catalog);
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

    private static Map<String, RemoteScopeEntry> parse(Path catalog) {
        Map<String, RemoteScopeEntry> result = new LinkedHashMap<>();
        try {
            String content = Files.readString(catalog, StandardCharsets.UTF_8);
            JsonElement root = JsonParser.parseString(content);
            if (!root.isJsonArray()) {
                throw new IllegalStateException("Expected a JSON array at the root");
            }
            for (JsonElement element : root.getAsJsonArray()) {
                if (!element.isJsonObject()) {
                    throw new IllegalStateException("Expected a JSON object entry, but got '" + element + "'");
                }
                JsonObject obj = element.getAsJsonObject();
                RemoteScopeEntry entry = toEntry(obj);
                if (entry != null) {
                    for (String ns : namespaces(obj)) {
                        result.put(ns, entry);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.warn("Failed to parse remote scope catalog '" + catalog + "'; ignoring it", ex);
            return Map.of();
        }
        return result;
    }

    /** Builds a typed source entry from {@code { type, namespaces, data }}; {@code null} for an unknown type. */
    private static @Nullable RemoteScopeEntry toEntry(JsonObject obj) {
        String type = string(obj, "type");
        JsonObject data = obj.has("data") && obj.get("data").isJsonObject() ? obj.getAsJsonObject("data") : null;
        if (type == null || data == null) {
            throw new IllegalStateException("Each catalog entry needs 'type', 'namespaces' and 'data': " + obj);
        }
        switch (type) {
            case RemoteScopeEntry.TYPE_SIMPLE:
                return RemoteScopeEntry.simple(required(data, "url"));
            case RemoteScopeEntry.TYPE_MAVEN:
                return RemoteScopeEntry.maven(required(data, "groupId"),
                        required(data, "artifactId"), required(data, "version"));
            default:
                LOG.warn("Ignoring remote scope entry with unknown type '" + type + "': " + obj);
                return null;
        }
    }

    /** Reads the required non-empty {@code namespaces} string array of a catalog object. */
    private static List<String> namespaces(JsonObject obj) {
        JsonElement element = obj.has("namespaces") ? obj.get("namespaces") : null;
        if (element == null || !element.isJsonArray() || element.getAsJsonArray().isEmpty()) {
            throw new IllegalStateException("Each catalog entry needs a non-empty 'namespaces' array: " + obj);
        }
        List<String> result = new ArrayList<>();
        for (JsonElement ns : element.getAsJsonArray()) {
            if (!ns.isJsonPrimitive()) {
                throw new IllegalStateException("'namespaces' must contain only strings: " + obj);
            }
            result.add(ns.getAsString());
        }
        return result;
    }

    private static @Nullable String string(JsonObject obj, String name) {
        return obj.has(name) && obj.get(name).isJsonPrimitive() ? obj.get(name).getAsString() : null;
    }

    private static String required(JsonObject data, String name) {
        String value = string(data, name);
        if (value == null) {
            throw new IllegalStateException("Missing '" + name + "' in 'data': " + data);
        }
        return value;
    }
}

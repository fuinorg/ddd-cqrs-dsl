package org.fuin.dsl.cqrs.validation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.fuin.dsl.cqrs.cqrsDsl.JSON;
import org.fuin.dsl.cqrs.cqrsDsl.JsonArray;
import org.fuin.dsl.cqrs.cqrsDsl.JsonBoolean;
import org.fuin.dsl.cqrs.cqrsDsl.JsonMember;
import org.fuin.dsl.cqrs.cqrsDsl.JsonNumber;
import org.fuin.dsl.cqrs.cqrsDsl.JsonObject;
import org.fuin.dsl.cqrs.cqrsDsl.JsonString;

import com.github.erosb.jsonsKema.Schema;
import com.github.erosb.jsonsKema.SchemaLoader;
import com.github.erosb.jsonsKema.ValidationFailure;
import com.github.erosb.jsonsKema.Validator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

/**
 * Validates the JSON payload of a hint against the JSON schema that matches the hint's name. The schema
 * files are shared resources on the classpath ("/org/fuin/dsl/cqrs/schema"); this validation logic is
 * duplicated in the IntelliJ plugin (the two plugins have no common jar), only the schema files are
 * shared.
 * <p>
 * The parsed EMF JSON tree of the hint is first turned into a normalized, strict JSON value (using gson)
 * and then validated with the "json-sKema" (draft 2020-12) validator. Building the value from the tree -
 * instead of re-parsing the hint's source text - avoids the DSL's JSON dialect (single-quoted strings,
 * numbers with underscores/hex/type suffixes) tripping up a strict JSON parser.
 */
public final class CqrsHintJson {

    private static final String SCHEMA_BASE = "/org/fuin/dsl/cqrs/schema/";

    private static final Gson GSON = new Gson();

    private static final URI INSTANCE_URI = URI.create("mem://hint-instance");

    private CqrsHintJson() {
    }

    /**
     * Returns the classpath location of the schema that validates a hint with the given name, or
     * <code>null</code> when no schema is known for it (the hint is then not schema-validated). The
     * hint name is an FQN; only its simple (last) segment is matched.
     *
     * @param name Hint name (may be <code>null</code>).
     *
     * @return Classpath resource path of the matching schema, or <code>null</code>.
     */
    public static String schemaForHintName(final String name) {
        final String simple = simpleName(name);
        if ("JpaHint".equals(simple)) {
            return SCHEMA_BASE + "jpa-hint.schema.json";
        }
        if ("SrcGen4J".equals(simple)) {
            return SCHEMA_BASE + "srcgen4j-hint.schema.json";
        }
        return null;
    }

    /**
     * Validates the given hint JSON against the schema at the given classpath location.
     *
     * @param json JSON value of the hint (may be <code>null</code>).
     * @param schemaResourcePath Classpath location of the schema (may be <code>null</code>).
     *
     * @return One message per schema violation - Never <code>null</code>, empty when the JSON is valid.
     */
    public static List<String> validate(final JSON json, final String schemaResourcePath) {
        final List<String> messages = new ArrayList<>();
        if (json == null || schemaResourcePath == null) {
            return messages;
        }
        try {
            final Schema schema = new SchemaLoader(readResource(schemaResourcePath)).load();
            final Validator validator = Validator.forSchema(schema);
            final String instance = GSON.toJson(toGson(json));
            final ValidationFailure failure = validator.validate(instance, INSTANCE_URI);
            if (failure != null) {
                for (final ValidationFailure leaf : failure.flatten()) {
                    messages.add(describe(leaf));
                }
            }
        } catch (final IOException | RuntimeException ex) {
            messages.add("Could not validate the JSON against '" + schemaResourcePath + "': " + ex.getMessage());
        }
        return messages;
    }

    private static String describe(final ValidationFailure failure) {
        String pointer = null;
        try {
            pointer = failure.getInstance().getLocation().getPointer().toString();
        } catch (final RuntimeException ex) {
            pointer = null;
        }
        if (pointer == null || pointer.isEmpty() || "#".equals(pointer)) {
            return failure.getMessage();
        }
        return failure.getMessage() + " (at " + pointer + ")";
    }

    private static String simpleName(final String name) {
        if (name == null) {
            return null;
        }
        final int p = name.lastIndexOf('.');
        return p < 0 ? name : name.substring(p + 1);
    }

    private static String readResource(final String resourcePath) throws IOException {
        try (InputStream in = CqrsHintJson.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Schema resource not found on the classpath: " + resourcePath);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // ---- EMF JSON tree -> gson element (a normalized, strict JSON value) ----

    private static JsonElement toGson(final JSON json) {
        if (json instanceof JsonObject) {
            final com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
            for (final JsonMember member : ((JsonObject) json).getMembers()) {
                obj.add(member.getKey(), toGson(member.getValue()));
            }
            return obj;
        }
        if (json instanceof JsonArray) {
            final com.google.gson.JsonArray arr = new com.google.gson.JsonArray();
            for (final JSON element : ((JsonArray) json).getElements()) {
                arr.add(toGson(element));
            }
            return arr;
        }
        if (json instanceof JsonString) {
            return new JsonPrimitive(((JsonString) json).getValue());
        }
        if (json instanceof JsonNumber) {
            return numberElement(((JsonNumber) json).getValue());
        }
        if (json instanceof JsonBoolean) {
            return new JsonPrimitive(Boolean.valueOf("true".equals(((JsonBoolean) json).getValue())));
        }
        // JsonNull (or null)
        return JsonNull.INSTANCE;
    }

    /**
     * Turns a DSL number literal (which may use underscores, a hex "0x" prefix or a type suffix such as
     * "L"/"bd"/"f") into a strict JSON number, falling back to a JSON string when it is not
     * representable (so the schema's type check reports it rather than the validator throwing).
     */
    private static JsonElement numberElement(final String raw) {
        if (raw == null) {
            return JsonNull.INSTANCE;
        }
        final String t = raw.trim().replace("_", "");
        try {
            if (t.startsWith("0x") || t.startsWith("0X")) {
                String hex = t.substring(2);
                final int hash = hex.indexOf('#');
                if (hash >= 0) {
                    hex = hex.substring(0, hash);
                }
                return new JsonPrimitive(Long.valueOf(Long.parseLong(hex, 16)));
            }
            // Strip a trailing type suffix (e.g. "L", "bd", "f", "#bi"); the exponent keeps its digits.
            int end = t.length();
            while (end > 0) {
                final char c = t.charAt(end - 1);
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '#') {
                    end--;
                } else {
                    break;
                }
            }
            final String num = t.substring(0, end);
            if (num.indexOf('.') >= 0 || num.indexOf('e') >= 0 || num.indexOf('E') >= 0) {
                return new JsonPrimitive(Double.valueOf(Double.parseDouble(num)));
            }
            return new JsonPrimitive(Long.valueOf(Long.parseLong(num)));
        } catch (final NumberFormatException ex) {
            return new JsonPrimitive(raw);
        }
    }
}

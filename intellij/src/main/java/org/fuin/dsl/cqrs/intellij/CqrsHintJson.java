package org.fuin.dsl.cqrs.intellij;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.fuin.dsl.cqrs.intellij.psi.CqrsJson;
import org.fuin.dsl.cqrs.intellij.psi.CqrsJsonArray;
import org.fuin.dsl.cqrs.intellij.psi.CqrsJsonMember;
import org.fuin.dsl.cqrs.intellij.psi.CqrsJsonNumber;
import org.fuin.dsl.cqrs.intellij.psi.CqrsJsonObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsJsonString;

import com.github.erosb.jsonsKema.Schema;
import com.github.erosb.jsonsKema.SchemaLoader;
import com.github.erosb.jsonsKema.ValidationFailure;
import com.github.erosb.jsonsKema.Validator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

/**
 * Validates the JSON payload of a hint against the JSON schema that matches the hint's name. This is the
 * IntelliJ counterpart of the Xtext {@code org.fuin.dsl.cqrs.validation.CqrsHintJson}; the two plugins
 * have no common jar, so the logic is duplicated - only the schema files (copied into the plugin's
 * resources from the Eclipse bundle) are shared.
 * <p>
 * The Grammar-Kit PSI tree of the hint is turned into a normalized, strict JSON value (using gson) and
 * validated with the "json-sKema" (draft 2020-12) validator. Walking the tree - instead of re-parsing
 * the raw source - avoids the DSL's JSON dialect (single-quoted strings, numbers with underscores /
 * hex / type suffixes) tripping up a strict JSON parser; string tokens are unescaped here because the
 * PSI returns them raw and quoted.
 */
public final class CqrsHintJson {

    private static final String SCHEMA_BASE = "/org/fuin/dsl/cqrs/schema/";

    private static final Gson GSON = new Gson();

    private static final URI INSTANCE_URI = URI.create("mem://hint-instance");

    private CqrsHintJson() {
    }

    /**
     * Returns the classpath location of the schema that validates a hint with the given name, or
     * {@code null} when no schema is known for it. The hint name is an FQN; only its simple (last)
     * segment is matched.
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

    /** Returns the simple (last dot-separated segment) of a possibly qualified hint name. */
    public static String simpleName(final String name) {
        if (name == null) {
            return null;
        }
        final int p = name.lastIndexOf('.');
        return p < 0 ? name : name.substring(p + 1);
    }

    /**
     * Validates the given hint JSON against the schema at the given classpath location.
     *
     * @return One message per schema violation - never {@code null}, empty when the JSON is valid.
     */
    public static List<String> validate(final CqrsJson json, final String schemaResourcePath) {
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

    private static String readResource(final String resourcePath) throws IOException {
        try (InputStream in = CqrsHintJson.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Schema resource not found on the classpath: " + resourcePath);
            }
            return new String(readAll(in), StandardCharsets.UTF_8);
        }
    }

    private static byte[] readAll(final InputStream in) throws IOException {
        final java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        final byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        return out.toByteArray();
    }

    // ---- Grammar-Kit PSI JSON tree -> gson element (a normalized, strict JSON value) ----

    private static JsonElement toGson(final CqrsJson json) {
        if (json == null) {
            return JsonNull.INSTANCE;
        }
        final CqrsJsonObject obj = json.getJsonObject();
        if (obj != null) {
            final com.google.gson.JsonObject g = new com.google.gson.JsonObject();
            for (final CqrsJsonMember member : obj.getJsonMemberList()) {
                g.add(unescape(member.getString().getText()), toGson(member.getJson()));
            }
            return g;
        }
        final CqrsJsonArray arr = json.getJsonArray();
        if (arr != null) {
            final com.google.gson.JsonArray g = new com.google.gson.JsonArray();
            for (final CqrsJson element : arr.getJsonList()) {
                g.add(toGson(element));
            }
            return g;
        }
        final CqrsJsonString str = json.getJsonString();
        if (str != null) {
            return new JsonPrimitive(unescape(str.getString().getText()));
        }
        final CqrsJsonNumber num = json.getJsonNumber();
        if (num != null) {
            return numberElement(num.getNumber().getText());
        }
        if (json.getJsonBoolean() != null) {
            return new JsonPrimitive(Boolean.valueOf("true".equals(json.getJsonBoolean().getText().trim())));
        }
        // JsonNull (or empty)
        return JsonNull.INSTANCE;
    }

    /**
     * Strips the outer quote (single or double, as the DSL allows both) and resolves the JSON/Java
     * escape sequences of a raw string token.
     */
    static String unescape(final String raw) {
        if (raw == null) {
            return "";
        }
        String body = raw;
        if (raw.length() >= 2) {
            final char q = raw.charAt(0);
            if ((q == '"' || q == '\'') && raw.charAt(raw.length() - 1) == q) {
                body = raw.substring(1, raw.length() - 1);
            }
        }
        final StringBuilder sb = new StringBuilder(body.length());
        for (int i = 0; i < body.length(); i++) {
            final char c = body.charAt(i);
            if (c == '\\' && i + 1 < body.length()) {
                final char n = body.charAt(++i);
                switch (n) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'u':
                        if (i + 4 < body.length()) {
                            try {
                                sb.append((char) Integer.parseInt(body.substring(i + 1, i + 5), 16));
                                i += 4;
                            } catch (final NumberFormatException ex) {
                                sb.append(n);
                            }
                        } else {
                            sb.append(n);
                        }
                        break;
                    default: sb.append(n); // ", ', \, /, and anything else: keep the escaped char
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Turns a DSL number literal (which may use underscores, a hex "0x" prefix or a type suffix such as
     * "L"/"bd"/"f") into a strict JSON number, falling back to a JSON string when it is not
     * representable (so the schema's type check reports it rather than the validator throwing).
     */
    static JsonElement numberElement(final String raw) {
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

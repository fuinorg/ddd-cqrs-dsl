package org.fuin.dsl.ddd.gen.script;

import java.util.ArrayList;
import java.util.List;

import org.fuin.dsl.cqrs.cqrsDsl.Hint;
import org.fuin.dsl.cqrs.cqrsDsl.JSON;
import org.fuin.dsl.cqrs.cqrsDsl.JsonArray;
import org.fuin.dsl.cqrs.cqrsDsl.JsonMember;
import org.fuin.dsl.cqrs.cqrsDsl.JsonObject;
import org.fuin.dsl.cqrs.cqrsDsl.JsonString;

/**
 * Reads the fields of a "SrcGen4J" hint. The hint holds nothing but the two script links:
 *
 * <pre>
 * hint SrcGen4J {
 *     "model2JavaPackage": "scripts/model2JavaPackage.js",
 *     "artifact2Target":   "scripts/artifact2Target.js"
 * }
 * </pre>
 *
 * The value is the path of the script, relative to the ".cqrs" file that declares the hint, and the
 * function the script has to export is named after the field.
 * <p>
 * The hint also carries the {@link #CONSTRAINT_MAPPINGS} array, which maps DSL constraints to Java
 * validation annotations. That is unrelated to the package and target mapping and stays declarative.
 */
public final class SrcGen4JHintFields {

    /** Field holding the mappings of DSL constraints to Java validation annotations. */
    public static final String CONSTRAINT_MAPPINGS = "constraintMappings";

    private SrcGen4JHintFields() {
        throw new UnsupportedOperationException("It's not allowed to create an instance of this utility class");
    }

    /**
     * Value of a string field of the hint.
     *
     * @param hint Hint to read - may be <code>null</code>.
     * @param field Field name - cannot be <code>null</code>.
     *
     * @return Value, or <code>null</code> when the hint is null, is not an object, or has no such field.
     */
    public static String value(final Hint hint, final String field) {
        if (hint == null || !(hint.getJson() instanceof JsonObject)) {
            return null;
        }
        for (final JsonMember member : ((JsonObject) hint.getJson()).getMembers()) {
            if (field.equals(member.getKey())) {
                final JSON value = member.getValue();
                if (value instanceof JsonString) {
                    return ((JsonString) value).getValue();
                }
                throw new IllegalStateException("The \"SrcGen4J\" hint field '" + field
                        + "' must be a string holding the script path, but was: " + value.eClass().getName());
            }
        }
        return null;
    }

    /**
     * Values of a string array field of the hint.
     *
     * @param hint Hint to read - may be <code>null</code>.
     * @param field Field name - cannot be <code>null</code>.
     *
     * @return Values, never <code>null</code> but empty when the field is not set.
     */
    public static List<String> values(final Hint hint, final String field) {
        final List<String> result = new ArrayList<>();
        if (hint == null || !(hint.getJson() instanceof JsonObject)) {
            return result;
        }
        for (final JsonMember member : ((JsonObject) hint.getJson()).getMembers()) {
            if (field.equals(member.getKey()) && member.getValue() instanceof JsonArray) {
                for (final JSON element : ((JsonArray) member.getValue()).getElements()) {
                    if (element instanceof JsonString) {
                        result.add(((JsonString) element).getValue());
                    }
                }
            }
        }
        return result;
    }

}

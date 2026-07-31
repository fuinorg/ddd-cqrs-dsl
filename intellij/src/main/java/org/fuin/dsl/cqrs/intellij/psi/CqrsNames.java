package org.fuin.dsl.cqrs.intellij.psi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Helpers for the caret ('^') keyword escape: a leading caret marks an identifier that would
 * otherwise be a keyword (e.g. {@code ^event}). The caret is part of the source text but not part of
 * the logical name, so it is stripped when reading names/references and re-added when writing a name
 * that collides with a keyword.
 */
public final class CqrsNames {

    /**
     * Word keywords that need escaping when used as an identifier. Hyphenated keywords (e.g.
     * {@code value-object}) can never collide with an ID and are therefore omitted. Keep in sync
     * with the keyword rules in {@code CqrsDsl.flex}.
     */
    private static final Set<String> KEYWORDS = Set.of(
            "context", "module", "import", "dependency", "local", "type", "element", "generics", "constraint", "input",
            "exception", "annotation", "cid", "message", "base", "identifies", "enum", "instances",
            "deprecated", "event", "entity", "identifier", "root", "aggregate", "constructor",
            "fires", "returns", "method", "ref", "slabel", "label", "tooltip", "prompt", "examples",
            "invariants", "preconditions", "service", "command", "target", "sla", "handles", "uses",
            "projection", "view", "consistency", "acceptable", "detection", "resolution", "optional",
            "millis", "seconds", "minutes", "hours", "days", "weak", "strong", "never", "manually",
            "automatic", "workflow", "true", "false", "null");

    private CqrsNames() {
    }

    /** Removes a single leading caret from a simple name segment. */
    @Contract("null -> null")
    public static @Nullable String unescape(@Nullable String segment) {
        if (segment != null && segment.startsWith("^")) {
            return segment.substring(1);
        }
        return segment;
    }

    /** Removes the caret escape from every dot-separated segment (a trailing {@code .*} is kept). */
    @Contract("null -> null")
    public static @Nullable String unescapeQualified(@Nullable String qualifiedName) {
        if (qualifiedName == null || qualifiedName.indexOf('^') < 0) {
            return qualifiedName;
        }
        String[] segments = qualifiedName.split("\\.", -1);
        StringBuilder sb = new StringBuilder(qualifiedName.length());
        for (int i = 0; i < segments.length; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append(unescape(segments[i]));
        }
        return sb.toString();
    }

    /** Prepends a caret if the (simple) name would otherwise be parsed as a keyword. */
    @Contract("null -> null")
    public static @Nullable String escape(@Nullable String name) {
        return name != null && KEYWORDS.contains(name) ? "^" + name : name;
    }

    /** Escapes every keyword segment of a (possibly dotted) name. */
    @Contract("null -> null")
    public static @Nullable String escapeQualified(@Nullable String qualifiedName) {
        if (qualifiedName == null || qualifiedName.isEmpty()) {
            return qualifiedName;
        }
        String[] segments = qualifiedName.split("\\.", -1);
        StringBuilder sb = new StringBuilder(qualifiedName.length());
        for (int i = 0; i < segments.length; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append(escape(segments[i]));
        }
        return sb.toString();
    }
}

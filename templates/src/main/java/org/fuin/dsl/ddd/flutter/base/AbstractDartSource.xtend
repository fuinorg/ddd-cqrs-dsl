package org.fuin.dsl.ddd.flutter.base

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Type
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*

/**
 * Base of every factory in the Dart target.
 *
 * <p>It sits beside <code>org.fuin.dsl.ddd.gen</code> rather than inside it: "gen" has always meant the
 * Java target here, so a reader looking for "how is Dart generated" gets one root to open. What both
 * targets need - the type keys, the artifact factory contract, the module walk - stays in
 * <code>gen.base</code> and is inherited, rather than drifting into a Dart-flavoured copy.
 */
abstract class AbstractDartSource<TYPE> extends AbstractSource<TYPE> {

    /** Name of the variable naming the Dart package the contract is published as. */
    public static val String KEY_DART_PACKAGE = "dartPackage"

    /**
     * Name of the variable naming the package the hand-written runtime lives in.
     *
     * <p>The descriptor types, the constraint types and the JSON helpers are what every generated file
     * speaks, and exactly one package holds them. A project generating its own contract imports them
     * from wherever it takes them; the package that owns them names itself.
     */
    public static val String KEY_DART_RUNTIME_PACKAGE = "dartRuntimePackage"

    /**
     * Name of the variable mapping a dependency model's context to the Dart package that publishes it.
     *
     * <p>Written as <code>context=package</code>, several separated by commas or whitespace, e.g.
     * <code>org.fuin.dsl.cqrs.common=cqrs_common</code>. A context named here is one this project
     * <em>imports</em> rather than generates: its types are somebody else's published package, exactly
     * as the JVM side takes them from a jar. A context not named here falls back to the local package,
     * which is what a project does when it has no published Dart package to point at.
     */
    public static val String KEY_DART_PACKAGES = "dartPackages"

    /** What that package is called when a model does not say. */
    public static val String DEFAULT_DART_PACKAGE = "contract"

    var String dartPackage = DEFAULT_DART_PACKAGE

    var String configuredRuntimePackage = null

    var Map<String, String> dartPackages = new LinkedHashMap<String, String>()

    override init(ArtifactFactoryConfig config) {
        super.init(config)
        val configured = config?.varMap?.get(KEY_DART_PACKAGE)
        if (configured !== null && !configured.toString.empty) {
            dartPackage = configured.toString
        }
        val runtime = config?.varMap?.get(KEY_DART_RUNTIME_PACKAGE)
        if (runtime !== null && !runtime.toString.empty) {
            configuredRuntimePackage = runtime.toString
        }
        dartPackages = parsePackages(config?.varMap?.get(KEY_DART_PACKAGES))
    }

    /** The Dart package this project's own generated code belongs to. */
    protected def String getDartPackage() {
        dartPackage
    }

    /** The Dart package the hand-written runtime lives in - this one, unless the config says otherwise. */
    protected def String getDartRuntimePackage() {
        configuredRuntimePackage ?: dartPackage
    }

    /**
     * The import of a generated type, from whichever package publishes it.
     *
     * <p>A type this model declares is in this project's own package. One it only reaches into belongs
     * to the context that declares it, and if that context publishes a Dart package the import names
     * it - which is the whole point of publishing one. This is where "the JVM takes it from a jar"
     * turns into something Dart can say.
     *
     * @param type Type to import.
     *
     * @return Import URI.
     */
    protected def String importOf(Type type) {
        if (isForeign(type)) {
            // Through its public library, and only that: everything below it is the other package's
            // own business, and its barrel already offers what a consumer is meant to reach.
            return publicLibraryOf(packageOf(type))
        }
        return DartNames.importOf(packageOf(type), type.module, type.name)
    }

    /** The one library another package publishes, named after the package as Dart expects. */
    protected def String publicLibraryOf(String packageName) {
        return "package:" + packageName + "/" + packageName + ".dart"
    }

    /**
     * The import of a hand-written runtime file, which lives in one package wherever it is used.
     *
     * <p>Precisely when the runtime is this package's own. When it belongs to another, its public
     * library is what gets imported instead: <code>lib/src</code> is private to the package that owns
     * it, and reaching into it is something Dart tells you not to do rather than something it stops.
     *
     * @param path Path below <code>lib/</code>, e.g. <code>src/json/json.dart</code>.
     *
     * @return Import URI.
     */
    protected def String runtimeImport(String path) {
        val runtime = getDartRuntimePackage()
        if (runtime != dartPackage) {
            return publicLibraryOf(runtime)
        }
        return "package:" + runtime + "/" + path
    }

    /** Which Dart package publishes a type. */
    protected def String packageOf(Type type) {
        if (isPrimary(type)) {
            return dartPackage
        }
        return dartPackages.get(contextNameOf(type)) ?: dartPackage
    }

    /** Whether the type belongs to a context this project imports rather than generates. */
    protected def boolean isForeign(Type type) {
        return !isPrimary(type) && dartPackages.containsKey(contextNameOf(type))
    }

    def private static String contextNameOf(Type type) {
        return type?.module?.context?.name
    }

    def private static Map<String, String> parsePackages(Object configured) {
        val out = new LinkedHashMap<String, String>()
        if (configured === null) {
            return out
        }
        for (entry : configured.toString.split("[,\\s]+")) {
            val idx = entry.indexOf('=')
            if (idx > 0) {
                out.put(entry.substring(0, idx).trim, entry.substring(idx + 1).trim)
            }
        }
        return out
    }

    /**
     * Renders the model's documentation as a Dart doc comment, wrapped at a readable width.
     *
     * <p>Triple-slash lines rather than the block form Java uses, and the model's text verbatim:
     * documentation worth having in the model is worth reading in the generated file, and a generator
     * that drops it makes the output look machine-shaped for no gain.
     *
     * @param rawDoc Documentation as the parser hands it over, markers and all (may be
     *            <code>null</code>).
     * @param indent Spaces to put before each line.
     *
     * @return Doc comment lines, or an empty string when there is nothing to say.
     */
    protected static def String dartDoc(String rawDoc, String indent) {
        val text = rawDoc?.text
        if (text === null || text.trim.empty) {
            return ""
        }
        val out = new StringBuilder()
        for (line : wrap(text.trim.replaceAll("\\s+", " "), 96 - indent.length)) {
            if (out.length > 0) {
                out.append("\n")
            }
            out.append(indent).append("/// ").append(line)
        }
        return out.toString
    }

    /** The documentation as plain text, markers stripped, or <code>null</code> when there is none. */
    protected static def String docText(String rawDoc) {
        val text = rawDoc?.text
        return if(text === null || text.trim.empty) null else text.trim.replaceAll("\\s+", " ")
    }

    /** Whether a block of wording states anything at all. */
    protected static def boolean states(TypeMetaInfo meta) {
        return meta !== null && (meta.slabel !== null || meta.label !== null || meta.tooltip !== null
            || meta.prompt !== null)
    }

    /**
     * Tidies generated source before it is written.
     *
     * <p>Xtend indents every line a template produces, blank ones included, so a blank line between two
     * blocks arrives carrying the block's indentation. Nothing reads it and `dart format` would strip
     * it, but a fixture comparison is exact - and trailing whitespace is the kind of difference that
     * makes a diff unreadable for no reason.
     *
     * @param source Generated source.
     *
     * @return The same source with no trailing whitespace on any line.
     */
    protected static def String tidy(String source) {
        return source.replaceAll("[ \\t]+(?=\\n)", "").replaceAll("[ \\t]+$", "")
    }

    /**
     * A string literal, split over several lines when it would otherwise be too long.
     *
     * <p>Dart concatenates adjacent literals, so a long sentence becomes a stack of short ones. Worth
     * doing because the alternative is generated code that <code>dart format</code> would reflow the
     * first time anybody ran it - and then the file on disk no longer matches what the generator emits,
     * which is exactly the drift that generated code exists to prevent.
     *
     * @param value Text to emit (may be <code>null</code>).
     * @param indent Indentation of the continuation lines, relative to the first.
     *
     * @return A Dart expression, or <code>null</code>.
     */
    protected static def String dartStringWrapped(String value, String indent) {
        if (value === null) {
            return "null"
        }
        val literal = dartString(value)
        if (literal.length <= 92) {
            return literal
        }
        val parts = new ArrayList<String>()
        var line = new StringBuilder()
        for (word : value.split(" ")) {
            if (line.length > 0 && line.length + 1 + word.length > 88) {
                parts.add(line.toString + " ")
                line = new StringBuilder()
            }
            if (line.length > 0) {
                line.append(" ")
            }
            line.append(word)
        }
        if (line.length > 0) {
            parts.add(line.toString)
        }
        val out = new StringBuilder()
        for (var i = 0; i < parts.size; i++) {
            if (i > 0) {
                out.append("\n").append(indent)
            }
            out.append(dartString(parts.get(i)))
        }
        return out.toString
    }

    /** Escapes a value for a single-quoted Dart string literal. */
    protected static def String dartString(String value) {
        if (value === null) {
            return "null"
        }
        return "'" + value.replace("\\", "\\\\").replace("'", "\\'").replace("$", "\\$") + "'"
    }

    /**
     * A Dart string literal that keeps <code>${...}</code> as written.
     *
     * <p>A command's message template is interpolated by the *server*, against the event's own
     * attributes - <code>Create ${kind} category '${name}'</code>. Dart must not try to interpolate it,
     * so it is emitted as a raw string rather than escaped one character at a time.
     */
    protected static def String dartStringRaw(String value) {
        if (value === null) {
            return "null"
        }
        if (!value.contains("$")) {
            // Nothing to protect from Dart's interpolation, so an ordinary literal reads better.
            return dartString(value)
        }
        return if(value.contains("'")) "r\"" + value + "\"" else "r'" + value + "'"
    }

    /** Escapes a value for a single-quoted Dart literal, or emits <code>null</code> when absent. */
    protected static def String dartStringOrNull(String value) {
        return if(value === null) "null" else dartString(value)
    }

    private static def List<String> wrap(String text, int width) {
        val lines = new ArrayList<String>()
        var line = new StringBuilder()
        for (word : text.split(" ")) {
            if (line.length > 0 && line.length + 1 + word.length > width) {
                lines.add(line.toString)
                line = new StringBuilder()
            }
            if (line.length > 0) {
                line.append(" ")
            }
            line.append(word)
        }
        if (line.length > 0) {
            lines.add(line.toString)
        }
        return lines
    }

}

package org.fuin.dsl.ddd.gen.script;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Hint;
import org.fuin.srcgen4j.core.emf.PrimaryResources;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * Runs the two JavaScript functions a model declares in its "SrcGen4J" hint:
 * <ul>
 * <li><code>model2JavaPackage(element, typeKey)</code> - the Java package a generated type lands in,</li>
 * <li><code>artifact2Target(element, typeKey, artifactFactory)</code> - the Maven module and the folder
 * its file is written to.</li>
 * </ul>
 * <p>
 * The package function is resolved <b>per declaring context</b>: the package of an element always comes
 * from the script of the context that element belongs to. For a locally declared type that is this
 * project's script, for an imported type the one shipped inside the dependency's jar - which is how a
 * consumer names an imported type the way its producer generated it.
 * <p>
 * The target function is resolved <b>from this build's own models</b> instead. A module name only means
 * something to the generator configuration of the project doing the generating, so taking it from a
 * dependency would name modules this build never declared.
 * <p>
 * A script path is written relative to the ".cqrs" file that declares the hint and is read through the
 * resource set's URI converter, so <code>file:</code> and
 * <code>archive:file:/....jar!/model/scripts/x.js</code> are handled identically - a script inside a
 * dependency jar is read in place, exactly as its models are. When a model declares no "SrcGen4J" hint
 * (or not the field in question), the preset shipped with these templates is used.
 * <p>
 * Scripts are compiled once per source and cached; the two functions are called for every generated
 * element, so re-evaluating per call is not an option.
 */
public final class CqrsScripts {

    /** Name of the generator hint that carries the two script links. */
    public static final String HINT_NAME = "SrcGen4J";

    /** Hint field naming the script, and the function it must export, for the package mapping. */
    public static final String MODEL_2_JAVA_PACKAGE = "model2JavaPackage";

    /** Hint field naming the script, and the function it must export, for the target mapping. */
    public static final String ARTIFACT_2_TARGET = "artifact2Target";

    /** Classpath location of the preset scripts used when a model declares none. */
    private static final String PRESET_BASE = "/org/fuin/dsl/ddd/gen/script/";

    /** Evaluated scope per script source, keyed by its identifier. */
    private static final Map<String, Scriptable> SCOPES = new HashMap<>();

    private CqrsScripts() {
        throw new UnsupportedOperationException("It's not allowed to create an instance of this utility class");
    }

    /**
     * Java package the given kind of artifact for the given element is generated into.
     *
     * @param element Model element - cannot be <code>null</code>.
     * @param typeKey Kind of artifact, see {@code TypeKeys} - cannot be <code>null</code>.
     *
     * @return Package name, never <code>null</code> or empty.
     */
    public static String model2JavaPackage(final EObject element, final String typeKey) {
        final Object result = call(element, MODEL_2_JAVA_PACKAGE, new Object[] { element, typeKey });
        final String pkg = asString(result);
        if (pkg == null || pkg.isEmpty()) {
            throw new IllegalStateException("'" + MODEL_2_JAVA_PACKAGE + "' returned no package for type key '"
                    + typeKey + "' of " + describe(element));
        }
        return pkg;
    }

    /**
     * Maven module and folder the given artifact is written to.
     *
     * @param element Model element - cannot be <code>null</code>.
     * @param typeKey Kind of artifact, see {@code TypeKeys} - cannot be <code>null</code>.
     * @param artifactFactory Class name of the factory creating it - cannot be <code>null</code>.
     *
     * @return Target, never <code>null</code>.
     */
    public static Target artifact2Target(final EObject element, final String typeKey, final String artifactFactory) {
        final Object result = call(element, ARTIFACT_2_TARGET, new Object[] { element, typeKey, artifactFactory });
        if (!(result instanceof Scriptable)) {
            throw new IllegalStateException("'" + ARTIFACT_2_TARGET + "' must return an object with 'module' and "
                    + "'folder', but returned '" + asString(result) + "' for type key '" + typeKey + "' of "
                    + describe(element));
        }
        final Scriptable obj = (Scriptable) result;
        final String module = asString(ScriptableObject.getProperty(obj, "module"));
        final String folder = asString(ScriptableObject.getProperty(obj, "folder"));
        if (module == null || module.isEmpty() || folder == null || folder.isEmpty()) {
            throw new IllegalStateException("'" + ARTIFACT_2_TARGET + "' returned module='" + module + "' folder='"
                    + folder + "' for type key '" + typeKey + "' of " + describe(element)
                    + " - both are required");
        }
        return new Target(module, folder);
    }

    /**
     * Mappings of DSL constraints to Java validation annotations, from the "SrcGen4J" hint of the
     * context the element belongs to. Taken from the declaring context like the package is, so a model
     * that only uses a constraint of a dependency maps it exactly as the dependency does.
     *
     * @param element Model element - may be <code>null</code>.
     *
     * @return Mappings in the form "DSL=JAVA", never <code>null</code> but possibly empty.
     */
    public static java.util.List<String> constraintMappings(final EObject element) {
        return SrcGen4JHintFields.values(hintFor(element), SrcGen4JHintFields.CONSTRAINT_MAPPINGS);
    }

    /** Forgets every compiled script, so the next call evaluates again. Intended for tests. */
    public static synchronized void invalidate() {
        SCOPES.clear();
    }

    // ---- Calling ------------------------------------------------------------------------------

    private static Object call(final EObject element, final String function, final Object[] args) {
        if (element == null) {
            throw new IllegalArgumentException("Argument 'element' cannot be null");
        }
        final Source source = sourceOf(element, function);
        final org.mozilla.javascript.Context cx = org.mozilla.javascript.Context.enter();
        try {
            final Scriptable scope = scope(cx, source);
            final Object fn = ScriptableObject.getProperty(scope, function);
            if (!(fn instanceof Function)) {
                throw new IllegalStateException(
                        "Script '" + source.id() + "' does not declare a function '" + function + "'");
            }
            final Object[] wrapped = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                wrapped[i] = org.mozilla.javascript.Context.javaToJS(args[i], scope);
            }
            return ((Function) fn).call(cx, scope, scope, wrapped);
        } catch (final RhinoException ex) {
            throw new IllegalStateException("Error in '" + function + "' of script '" + source.id() + "' at line "
                    + ex.lineNumber() + ": " + ex.details() + " (" + describe(element) + ")", ex);
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }

    private static synchronized Scriptable scope(final org.mozilla.javascript.Context cx, final Source source) {
        Scriptable scope = SCOPES.get(source.id());
        if (scope != null) {
            return scope;
        }
        scope = cx.initStandardObjects();
        try (Reader reader = new InputStreamReader(source.open(), StandardCharsets.UTF_8)) {
            cx.evaluateReader(scope, reader, source.id(), 1, null);
        } catch (final IOException ex) {
            throw new IllegalStateException("Could not read script '" + source.id() + "': " + ex.getMessage(), ex);
        } catch (final RhinoException ex) {
            throw new IllegalStateException(
                    "Could not evaluate script '" + source.id() + "' at line " + ex.lineNumber() + ": "
                            + ex.details(), ex);
        }
        SCOPES.put(source.id(), scope);
        return scope;
    }

    // ---- Locating the script ------------------------------------------------------------------

    /**
     * Where the given function's script comes from: the path declared by the "SrcGen4J" hint of the
     * element's own context, or the preset when there is no such hint or field.
     */
    private static Source sourceOf(final EObject element, final String field) {
        final Hint hint = ARTIFACT_2_TARGET.equals(field) ? localHint(element) : hintFor(element);
        final String path = hint == null ? null : SrcGen4JHintFields.value(hint, field);
        if (path == null || path.isEmpty()) {
            requireOwnPackageMapping(element, field);
            return new Source(PRESET_BASE + field + ".js", null, null);
        }
        final Resource resource = hint.eResource();
        final URI base = resource == null ? null : resource.getURI();
        if (base == null) {
            throw new IllegalStateException("The \"" + HINT_NAME + "\" hint declaring '" + field + "' = '" + path
                    + "' has no location on disk, so the script cannot be found");
        }
        final URI uri = URI.createURI(path).resolve(base);
        return new Source(uri.toString(), uri, resource.getResourceSet());
    }

    /**
     * Refuses to name a package for a type of a <em>dependency</em> with this build's preset.
     * <p>
     * The preset is the default for the models a project parses itself. Applying it to an imported type
     * would answer with this project's layout for a type generated by somebody else's - silently, and
     * almost always wrongly. A model published before the mapping became a script carries no
     * <code>model2JavaPackage</code>, so this is exactly what an unmigrated dependency looks like.
     */
    private static void requireOwnPackageMapping(final EObject element, final String field) {
        if (!MODEL_2_JAVA_PACKAGE.equals(field) || PrimaryResources.isPrimary(element)) {
            return;
        }
        final Context context = contextOf(element);
        if (isDeclaredLocally(context, element)) {
            // The same context may be spread over a model this build parses and one it only pulled in;
            // then it is this build's context after all and its mapping - preset or not - applies.
            return;
        }
        throw new IllegalStateException("The model of context '" + (context == null ? "?" : context.getName())
                + "' declares no '" + MODEL_2_JAVA_PACKAGE + "', so the Java package of its type "
                + describe(element) + " cannot be known. It was built before the mapping moved into a"
                + " script: publish it again with a '" + MODEL_2_JAVA_PACKAGE + "' in its \"" + HINT_NAME
                + "\" hint. Falling back to this project's own mapping would put the imported type in a"
                + " package it was never generated into.");
    }

    /** Whether a context of the given name is declared by a model this build parses itself. */
    private static boolean isDeclaredLocally(final Context context, final EObject element) {
        if (context == null || context.getName() == null) {
            return false;
        }
        final Resource resource = element.eResource();
        final ResourceSet rs = resource == null ? null : resource.getResourceSet();
        if (rs == null) {
            return false;
        }
        for (final Resource other : rs.getResources()) {
            if (!PrimaryResources.isPrimary(other)) {
                continue;
            }
            for (final EObject content : other.getContents()) {
                if (content instanceof DomainModel) {
                    for (final Context ctx : ((DomainModel) content).getContexts()) {
                        if (context.getName().equals(ctx.getName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * The "SrcGen4J" hint of <em>this</em> build, used to place the artifacts it generates.
     * <p>
     * Unlike the package mapping, the target is never taken from the context that declares the element:
     * a module name only means something to the generator configuration of the project doing the
     * generating, so a script travelling inside a dependency jar would name modules this build does not
     * declare. Only primary resources - the models this build parses, as opposed to the ones pulled in
     * to resolve references - are considered. Among them the element's own context wins, so a project
     * holding several contexts still places each one's artifacts by its own rules.
     *
     * @param element Element an artifact is generated for.
     *
     * @return Hint, or <code>null</code> when no local model declares one.
     */
    private static Hint localHint(final EObject element) {
        final Resource resource = element.eResource();
        final ResourceSet rs = resource == null ? null : resource.getResourceSet();
        if (rs == null) {
            return hintFor(element);
        }
        final Context own = contextOf(element);
        final String preferred = own == null ? null : own.getName();
        Hint fallback = null;
        for (final Resource other : rs.getResources()) {
            if (!PrimaryResources.isPrimary(other)) {
                continue;
            }
            for (final EObject content : other.getContents()) {
                if (!(content instanceof DomainModel)) {
                    continue;
                }
                for (final Context ctx : ((DomainModel) content).getContexts()) {
                    final Hint hint = hintOf(ctx);
                    if (hint == null) {
                        continue;
                    }
                    if (ctx.getName() != null && ctx.getName().equals(preferred)) {
                        return hint;
                    }
                    if (fallback == null) {
                        fallback = hint;
                    }
                }
            }
        }
        return fallback;
    }

    /**
     * The "SrcGen4J" hint that applies to the given element. A context may be split across several
     * ".cqrs" files, and all blocks with the same name denote the same logical context, so the hint may
     * sit in any of them - the lookup searches every same-named context in the resource set.
     *
     * @param element Element - may be <code>null</code>.
     *
     * @return Hint, or <code>null</code> when there is no enclosing context or no such hint.
     */
    public static Hint hintFor(final EObject element) {
        final Context context = contextOf(element);
        if (context == null) {
            return null;
        }
        final Resource resource = element.eResource();
        final ResourceSet rs = resource == null ? null : resource.getResourceSet();
        if (rs == null) {
            return hintOf(context);
        }
        for (final Resource other : rs.getResources()) {
            for (final EObject content : other.getContents()) {
                if (content instanceof DomainModel) {
                    for (final Context ctx : ((DomainModel) content).getContexts()) {
                        if (context.getName() != null && context.getName().equals(ctx.getName())) {
                            final Hint hint = hintOf(ctx);
                            if (hint != null) {
                                return hint;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Hint hintOf(final Context context) {
        for (final Hint hint : context.getHints()) {
            if (HINT_NAME.equals(hint.getName())) {
                return hint;
            }
        }
        return null;
    }

    private static Context contextOf(final EObject element) {
        EObject current = element;
        while (current != null) {
            if (current instanceof Context) {
                return (Context) current;
            }
            current = current.eContainer();
        }
        return null;
    }

    // ---- Helpers ------------------------------------------------------------------------------

    private static String asString(final Object value) {
        if (value == null || value == Undefined.instance || value == Scriptable.NOT_FOUND) {
            return null;
        }
        return org.mozilla.javascript.Context.toString(value);
    }

    private static String describe(final EObject element) {
        if (element == null) {
            return "<null>";
        }
        final Resource resource = element.eResource();
        final String where = resource == null ? "" : " in " + resource.getURI();
        return element.eClass().getName() + where;
    }

    /** A script to evaluate: either a model relative file or a preset on the classpath. */
    private static final class Source {

        private final String id;

        private final URI uri;

        private final ResourceSet resourceSet;

        private Source(final String id, final URI uri, final ResourceSet resourceSet) {
            this.id = id;
            this.uri = uri;
            this.resourceSet = resourceSet;
        }

        String id() {
            return id;
        }

        InputStream open() throws IOException {
            if (uri == null) {
                final InputStream in = CqrsScripts.class.getResourceAsStream(id);
                if (in == null) {
                    throw new IOException("Preset script not found on the classpath: " + id);
                }
                return in;
            }
            try {
                return resourceSet.getURIConverter().createInputStream(uri);
            } catch (final IOException ex) {
                throw new IOException("Script not found: " + uri + " (" + ex.getMessage() + ")", ex);
            }
        }

    }

}

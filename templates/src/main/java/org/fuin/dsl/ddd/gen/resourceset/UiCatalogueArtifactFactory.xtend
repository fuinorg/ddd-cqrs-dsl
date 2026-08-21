package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.Iterator
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Creates the application's <b>UI catalogue</b> from the model: what to call a {@code module}, a
 * {@code view} and a {@code method} on screen.
 *
 * <p>These three carry the same {@code slabel}/{@code label}/{@code tooltip} block a type and an
 * attribute carry, and for the same reason - the name in the model is an identifier, not a caption. A
 * module name is a single lower case word, so no rule a client could write turns "businesspartners"
 * into "Business partners"; a view name is a type name, and a method name is camel case. A client that
 * derives navigation wording from those identifiers is inventing it.
 *
 * <p><b>Why this is data and not annotations.</b> An attribute's wording is generated as
 * {@code @ShortLabel}/{@code @Label}/{@code @Tooltip} onto the field it describes, and an enum
 * instance's onto the constant. That route is closed here twice over: the annotations are declared
 * {@code @Target({TYPE, FIELD})}, so neither a method nor a package can carry one - and a module has no
 * Java element at all to carry it, being a unit of the model rather than of the generated code. The
 * wording is therefore emitted the way the permission catalogue is emitted: as one generated class,
 * read as data.
 *
 * <p><b>The keys line up with the permission catalogue on purpose.</b> A method's key is
 * {@code "«View».«method»"} - the very id {@code PermissionIds} uses - so a client that has just been
 * told what it may call can caption it without a second lookup table:
 * {@code UiCatalogue.METHODS.get(PermissionIds.RECEIPT_VIEW_LIST_RECEIPTS)}. A view's key is the view
 * name, as in {@code PermissionIds.VIEW_METHODS}.
 *
 * <p>Only {@code slabel}, {@code label} and {@code tooltip} are carried. {@code prompt} and
 * {@code examples} describe how to fill a field in, which is not a question that can be asked about a
 * module, a view or an operation.
 *
 * <p>A model that states no wording anywhere gets no catalogue - an empty class naming a target module
 * the project may have no reason to own is worse than no class. The grouping ({@code MODULE_VIEWS}) is
 * emitted in full once anything is captioned, because a client that renders tabs needs it whether or
 * not every module got as far as being named.
 */
class UiCatalogueArtifactFactory extends AbstractSource<ResourceSet> {

    /** Module the catalogue is written to - the same one the permission catalogue goes to. */
    static val MODULE = "shared"

    /** Name of the generated Java class holding the catalogue. */
    static val CLASS_NAME = "UiCatalogue"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.JAVA_UI_CATALOGUE
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        // Keyed by module name, not a plain list: one module may be declared in more than one file -
        // splitting a module over a public and a private half is an ordinary way to lay a model out -
        // and every map below is keyed by that name. Two entries for one name is a duplicate key, which
        // Map.ofEntries rejects at class initialization, so the catalogue would fail on first touch
        // rather than at build time.
        val Map<String, ModuleEntry> modules = new LinkedHashMap<String, ModuleEntry>()
        var String project = null

        val Iterator<EObject> it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val EObject container = it.next
            if (container instanceof Module) {
                project = container.context.name
                val entry = moduleEntry(container)
                val existing = modules.get(entry.name)
                if (existing === null) {
                    modules.put(entry.name, entry)
                } else {
                    existing.merge(entry)
                }
            }
        }

        if (project === null || !modules.values.exists[carriesText]) {
            return null
        }

        // Sorted so the generated file is stable in version control: the order elements come out of the
        // resource set is not guaranteed, and a catalogue that reshuffles on every build is unreviewable.
        val sorted = modules.values.sortBy[name].toList
        return List.of(javaConstants(project, sorted))
    }

    /**
     * Collects one module, the views it declares and the methods of each.
     * <p>
     * Everything a module declares shares the module's resource bundle, which is the same one the
     * value objects of that module already carry - one properties file per module, as today.
     */
    private def ModuleEntry moduleEntry(Module module) {
        val entry = new ModuleEntry()
        entry.name = module.name
        entry.bundle = bundleName(module)
        entry.text = module.metaInfo
        for (element : module.elements) {
            if (element instanceof View) {
                val view = new ViewEntry()
                view.name = element.name
                view.bundle = entry.bundle
                view.text = element.metaInfo
                for (Method method : element.methods) {
                    val m = new MethodEntry()
                    m.id = element.name + "." + method.name
                    m.bundle = entry.bundle
                    m.text = method.metaInfo
                    view.methods.add(m)
                }
                entry.views.add(view)
            }
        }
        return entry
    }

    // ---- Java constants ------------------------------------------------------------------------

    /** Creates the catalogue as one generated class. */
    private def GeneratedArtifact javaConstants(String project, List<ModuleEntry> modules) {
        val pkg = project + ".shared.domain"
        val ctx = new SimpleCodeSnippetContext(null)
        ctx.requiresImport("java.util.List")
        ctx.requiresImport("java.util.Locale")
        ctx.requiresImport("java.util.Map")
        ctx.requiresImport("java.util.MissingResourceException")
        ctx.requiresImport("java.util.ResourceBundle")

        val views = modules.map[views].flatten.sortBy[name].toList
        val methods = views.map[methods].flatten.sortBy[id].toList

        val src = '''
            /**
             * What to call this application's modules, views and view methods on screen, as the model
             * states it.
             *
             * <p>Generated from the model on every build. A caption derived from an identifier - turning
             * "businesspartners" into "Businesspartners" - is a caption the model was never asked about,
             * and it is wrong as often as it is right. Everything here was written in the model instead.
             *
             * <p>The keys are the ones {@code PermissionIds} uses: a view is keyed by its name, a method
             * by {@code View.method}. A client that knows what it may call can therefore caption it
             * without a second lookup. As there, a view name is assumed unique across the context.
             *
             * <p>An element that states no wording has <b>no entry</b>. That is deliberately different
             * from an entry holding empty strings: it lets a caller tell "the model says nothing" from
             * "the model says this", and decide for itself what to fall back to.
             *
             * <p><b>It translates the way field labels translate.</b> Every entry carries the resource
             * bundle of the module that declares it and the key to look up in it, so a
             * {@code «'«'»Module»_de.properties} on the classpath localizes navigation exactly as it
             * localizes the labels generated onto the fields. The key is the catalogue key itself, and
             * the suffix is the one the field annotations use - {@code .slabel}, {@code .label},
             * {@code .tooltip}.
             */
            public final class «CLASS_NAME» {

                /**
                 * What to call one model element on screen, in the language the model was written in
                 * and in any language a resource bundle supplies.
                 *
                 * <p>Any of the three texts may be {@code null} - a model states the wording it has a
                 * use for, not all of it. <b>A bundle translates what the model states, it does not add
                 * to it</b>: an element with no {@code tooltip} in the model has none in any language,
                 * which is how the generated field annotations behave for the same reason - nothing is
                 * emitted where there was no value.
                 */
                public static final class Text {

                    private final String bundle;

                    private final String key;

                    private final String shortLabel;

                    private final String label;

                    private final String tooltip;

                    /**
                     * Constructor with all data.
                     *
                     * @param bundle Base name of the resource bundle holding the translations.
                     * @param key Key to look up in it, without the per-text suffix.
                     * @param shortLabel Wording where space is tight, or {@code null}.
                     * @param label Wording in a menu or a heading, or {@code null}.
                     * @param tooltip Sentence explaining the element, or {@code null}.
                     */
                    public Text(final String bundle, final String key, final String shortLabel,
                            final String label, final String tooltip) {
                        this.bundle = bundle;
                        this.key = key;
                        this.shortLabel = shortLabel;
                        this.label = label;
                        this.tooltip = tooltip;
                    }

                    /**
                     * Returns the wording for a place where space is tight - a tab or a bottom bar - as
                     * the model states it.
                     *
                     * @return Short label, or {@code null} when the model states none.
                     */
                    public String getShortLabel() {
                        return shortLabel;
                    }

                    /**
                     * Returns the wording for a menu, a navigation rail or a heading, as the model
                     * states it.
                     *
                     * @return Label, or {@code null} when the model states none.
                     */
                    public String getLabel() {
                        return label;
                    }

                    /**
                     * Returns the sentence explaining what this is, as the model states it.
                     *
                     * @return Tooltip, or {@code null} when the model states none.
                     */
                    public String getTooltip() {
                        return tooltip;
                    }

                    /**
                     * Returns the wording for a place where space is tight, in the given language.
                     *
                     * @param locale Language to look up.
                     *
                     * @return Short label, or {@code null} when the model states none.
                     */
                    public String getShortLabel(final Locale locale) {
                        return resolve(locale, ".slabel", shortLabel);
                    }

                    /**
                     * Returns the wording for a menu, a navigation rail or a heading, in the given
                     * language.
                     *
                     * @param locale Language to look up.
                     *
                     * @return Label, or {@code null} when the model states none.
                     */
                    public String getLabel(final Locale locale) {
                        return resolve(locale, ".label", label);
                    }

                    /**
                     * Returns the sentence explaining what this is, in the given language.
                     *
                     * @param locale Language to look up.
                     *
                     * @return Tooltip, or {@code null} when the model states none.
                     */
                    public String getTooltip(final Locale locale) {
                        return resolve(locale, ".tooltip", tooltip);
                    }

                    /**
                     * Looks one text up in the resource bundle, falling back to what the model states.
                     *
                     * <p>A missing bundle and a missing key are the same non-event - the model's own
                     * wording is returned - so a build shipping no properties file at all behaves
                     * exactly as if this method did not exist.
                     *
                     * <p><b>Mind the JVM default locale.</b> {@link ResourceBundle} searches the
                     * default locale's candidates before giving up, so on a German machine asking for
                     * {@link Locale#ENGLISH} returns the German text when only a {@code _de} bundle
                     * exists. That is stock {@code ResourceBundle} behaviour and the generated field
                     * annotations resolve through exactly the same call, so navigation and the labels
                     * beside it are at least wrong together rather than disagreeing on one screen. Set
                     * the default locale, or ship a bundle for every language offered.
                     *
                     * @param locale Language to look up.
                     * @param suffix Which of the three texts is wanted.
                     * @param fallback What the model states, returned as-is when it states nothing.
                     *
                     * @return Translated text, the model's text, or {@code null}.
                     */
                    private String resolve(final Locale locale, final String suffix, final String fallback) {
                        if (fallback == null) {
                            return null;
                        }
                        try {
                            return ResourceBundle.getBundle(bundle, locale).getString(key + suffix);
                        } catch (final MissingResourceException ex) {
                            return fallback;
                        }
                    }

                }

                /** What to call each module, keyed by module name. Modules stating no wording are absent. */
                public static final Map<String, Text> MODULES = Map.ofEntries(
                    «FOR m : modules.filter[text.states] SEPARATOR ","»
                        Map.entry("«m.name»", «newText(m.bundle, m.name, m.text)»)
                    «ENDFOR»
                );

                /**
                 * The views each module declares, keyed by module name and in the order a menu should
                 * offer them. Emitted for every module that has one, captioned or not: a client renders
                 * a module's tabs from this, and a view with no wording of its own still exists.
                 */
                public static final Map<String, List<String>> MODULE_VIEWS = Map.ofEntries(
                    «FOR m : modules.filter[!it.views.empty] SEPARATOR ","»
                        Map.entry("«m.name»", List.of(«FOR v : m.views SEPARATOR ", "»"«v.name»"«ENDFOR»))
                    «ENDFOR»
                );

                /** What to call each view, keyed by view name. Views stating no wording are absent. */
                public static final Map<String, Text> VIEWS = Map.ofEntries(
                    «FOR v : views.filter[text.states] SEPARATOR ","»
                        Map.entry("«v.name»", «newText(v.bundle, v.name, v.text)»)
                    «ENDFOR»
                );

                /**
                 * What to call each view method, keyed by the permission id of that method. Methods
                 * stating no wording are absent.
                 */
                public static final Map<String, Text> METHODS = Map.ofEntries(
                    «FOR m : methods.filter[text.states] SEPARATOR ","»
                        Map.entry("«m.id»", «newText(m.bundle, m.id, m.text)»)
                    «ENDFOR»
                );

                private «CLASS_NAME»() {
                    throw new UnsupportedOperationException("It's not allowed to create an instance of this utility class");
                }

            }
        '''
        return newArtifact(filename(pkg, CLASS_NAME),
            new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString.getBytes("UTF-8"), MODULE, "genMainJava")
    }

    /**
     * The {@code new Text(...)} expression for one block of wording.
     * <p>
     * The resource key is the catalogue key itself - the module name, the view name, or the
     * "«'«'»View».«'«'»method»" id - so a translator reads the same identifier the client looks up.
     */
    private def String newText(String bundle, String key, TypeMetaInfo meta) {
        return "new Text(" + bundle.javaString + ", " + key.javaString + ", " + meta.slabel.javaString +
            ", " + meta.label.javaString + ", " + meta.tooltip.javaString + ")"
    }

    /** A model string as a Java string literal, or the literal {@code null} when there is none. */
    private def String javaString(String value) {
        if (value === null) {
            return "null"
        }
        return '"' + value.replace("\\", "\\\\").replace('"', '\\"') + '"'
    }

    /** Whether a module, one of its views, or one of their methods states any wording at all. */
    /**
     * Folds a second declaration of the same module into this one.
     * <p>
     * A module split over several files is one module to everything that reads the catalogue, so the
     * halves have to become one entry. The views simply add up - a name can only be declared once, so
     * they cannot collide. The wording is taken from whichever half states any, and the first half that
     * does wins: a module captioned twice has said the same thing twice, and picking either is the same
     * answer.
     *
     * @param other Further declaration of the module this entry already describes.
     */
    private def void merge(ModuleEntry entry, ModuleEntry other) {
        entry.views.addAll(other.views)
        if (!entry.text.states) {
            entry.text = other.text
        }
    }

    private def boolean carriesText(ModuleEntry module) {
        return module.text.states || module.views.exists[text.states || methods.exists[text.states]]
    }

    /** Whether a block of wording states anything at all. An absent block and an empty one are the same. */
    private def boolean states(TypeMetaInfo meta) {
        return meta !== null && (meta.slabel !== null || meta.label !== null || meta.tooltip !== null)
    }

    private def String filename(String pkg, String className) {
        (pkg + "." + className).replace('.', '/') + ".java"
    }

    // ---- Collected model data ------------------------------------------------------------------

    /** One module, its wording and the views it declares. */
    private static class ModuleEntry {
        public String name
        public String bundle
        public TypeMetaInfo text
        public List<ViewEntry> views = new ArrayList<ViewEntry>()
    }

    /** One view, its wording and the methods it declares. */
    private static class ViewEntry {
        public String name
        public String bundle
        public TypeMetaInfo text
        public List<MethodEntry> methods = new ArrayList<MethodEntry>()
    }

    /** One view method and its wording, keyed by the permission id of that method. */
    private static class MethodEntry {
        public String id
        public String bundle
        public TypeMetaInfo text
    }

}

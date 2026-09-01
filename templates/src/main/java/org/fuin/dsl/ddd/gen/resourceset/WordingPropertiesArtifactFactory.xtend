package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.TreeMap
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.ddd.flutter.resourceset.DartArbArtifactFactory
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

/**
 * Creates the model's wording as <b>JVM resource bundles</b>: one <code>«Bundle».properties</code> per
 * bundle, in the language the model is written in.
 *
 * <p>Nothing on the JVM has to change to read them. Every generated field already carries
 * <code>@Label(bundle = "Categories", key = "CategoryName.label", value = "Name")</code>, and
 * <code>UiCatalogue</code> already resolves through
 * <code>ResourceBundle.getBundle(bundle, locale).getString(key + suffix)</code> with the annotation's
 * own value as the fallback. Both were built and never fed, because no properties file existed
 * anywhere. This is the file they were waiting for.
 *
 * <p><b>The entries are the ARB's entries.</b> Flutter reads one file for the whole application, so its
 * keys carry the bundle: <code>Categories.CategoryName.label</code>. The JVM reads one file per bundle,
 * so the same entry is <code>CategoryName.label</code> in <code>Categories.properties</code> - the
 * bundle became the file name. Splitting one walk rather than writing a second is the point: a key is
 * decided once, and a caption written under a key nothing looks up is the drift that walk was written
 * to end.
 *
 * <p><b>UTF-8, unescaped.</b> Since Java 9 a <code>PropertyResourceBundle</code> reads properties as
 * UTF-8 and falls back to ISO-8859-1 only when that fails, so an umlaut travels as itself and the file
 * stays readable to whoever translates it.
 */
class WordingPropertiesArtifactFactory extends AbstractSource<ResourceSet> {

    /** Module the bundles are written to - wording is read on every side. */
    static val MODULE = "shared"

    /** Language the model is written in, and the one the base bundle holds. */
    static val LOCALE = "en"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.RES_WORDING_PROPERTIES
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (preparationRun) {
            return null
        }

        val byBundle = new TreeMap<String, TreeMap<String, String>>()
        for (entry : DartArbArtifactFactory.wordingEntries(resourceSet).entrySet) {
            val idx = entry.key.indexOf('.')
            if (idx > 0) {
                val bundle = entry.key.substring(0, idx)
                byBundle.computeIfAbsent(bundle, [new TreeMap<String, String>()])
                    .put(entry.key.substring(idx + 1), entry.value)
            }
        }
        if (byBundle.empty) {
            return null
        }

        val artifacts = new ArrayList<GeneratedArtifact>()
        for (bundle : byBundle.entrySet) {
            artifacts.add(newArtifact(bundle.key + ".properties",
                properties(bundle.key, bundle.value).getBytes("UTF-8"), MODULE, "genMainRes"))
            artifacts.add(newArtifact(bundle.key + "_" + LOCALE + ".properties",
                explicitLocale(bundle.key).getBytes("UTF-8"), MODULE, "genMainRes"))
        }
        return artifacts
    }

    /** One bundle, keys in order, so a diff between two releases reads. */
    def private static String properties(String bundle, Map<String, String> entries) {
        val out = new StringBuilder()
        out.append("# ").append(bundle).append(" - what the model states, in the language it is written in.\n")
        out.append("# Generated from the model on every build. A translation is a sibling file: ")
            .append(bundle).append("_de.properties.\n")
        for (entry : entries.entrySet) {
            out.append(escapeKey(entry.key)).append("=").append(escapeValue(entry.value)).append("\n")
        }
        return out.toString
    }

    /**
     * An empty bundle for the language the model is written in, whose only job is to exist.
     *
     * <p><code>ResourceBundle</code> searches the <b>default locale's</b> candidates before falling back
     * to the base file, so on a German machine asking for English returns German when only a
     * <code>_de</code> bundle exists - stock behaviour, and a trap this catalogue would otherwise walk
     * into on every server whose container locale is not English. A present <code>_en</code> file ends
     * the search at English, and inherits every value from the base file through the ordinary parent
     * chain, so nothing is written twice.
     *
     * @param bundle Bundle this belongs to.
     *
     * @return File content: a comment saying why it is empty.
     */
    def private static String explicitLocale(String bundle) {
        return "# " + bundle + " in the language the model is written in.\n"
            + "# Deliberately empty: every value is inherited from " + bundle + ".properties through the\n"
            + "# ResourceBundle parent chain. The file exists so that asking for English ends the search\n"
            + "# here rather than falling through to the machine's default locale.\n"
    }

    /** A key ends at the first unescaped space, equals or colon, so those three are escaped. */
    def private static String escapeKey(String key) {
        return key.replace("\\", "\\\\").replace(" ", "\\ ").replace("=", "\\=").replace(":", "\\:")
    }

    /** A value runs to the end of the line, so only a backslash and the line break itself are escaped. */
    def private static String escapeValue(String value) {
        return value.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r")
    }

}

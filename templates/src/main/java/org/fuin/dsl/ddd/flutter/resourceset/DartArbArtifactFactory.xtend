package org.fuin.dsl.ddd.flutter.resourceset

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.TreeMap
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Creates the ARB file carrying every word the model states.
 *
 * <p>An ARB - Application Resource Bundle - is what Flutter localizes from: one JSON file per locale,
 * which <code>gen_l10n</code> turns into typed accessors. Emitting one keyed the way the descriptors are
 * keyed is what makes a German build a second file and <b>no widget touched</b>.
 *
 * <p>The keys are <code>&lt;bundle&gt;.&lt;key&gt;.&lt;suffix&gt;</code>, where the suffixes are the ones
 * the JVM side's annotations use - <code>slabel</code>, <code>label</code>, <code>tooltip</code>,
 * <code>prompt</code>. The bundle is part of the key because Flutter has one file for the whole
 * application where the JVM has one properties file per module, and two modules may each caption
 * something "name".
 *
 * <p>A bundle translates what the model states; it does not add to it. Wording the model never gave has
 * no entry here, in any language.
 */
class DartArbArtifactFactory extends AbstractDartSource<ResourceSet> {

    /** Language the model itself is written in. */
    static val LOCALE = "en"

    static val MODULE = "flutter.contract"

    static val FOLDER = "genMainDart"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.RES_DART_ARB
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (preparationRun) {
            return null
        }

        val entries = new TreeMap<String, String>()
        var String project = null
        val it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val container = it.next
            if (container instanceof Module) {
                project = container.context.name
                collect(container, entries)
            }
        }
        if (project === null || entries.empty) {
            return null
        }

        return List.of(newArtifact("l10n/melkheftken_" + LOCALE + ".arb",
            arb(entries).toString.getBytes("UTF-8"), MODULE, FOLDER))
    }

    def private void collect(Module module, Map<String, String> entries) {
        val bundle = bundleName(module)
        put(entries, bundle, module.name, module.metaInfo)
        for (element : module.elements) {
            switch (element) {
                View: {
                    put(entries, bundle, element.name, element.metaInfo)
                    for (method : element.methods) {
                        put(entries, bundle, element.name + "." + method.name, method.metaInfo)
                        for (parameter : method.parameters) {
                            put(entries, bundle, parameter.name, parameter.overridden?.metaInfo)
                        }
                    }
                }
                ValueObject: {
                    put(entries, bundle, element.name, element.metaInfo)
                    for (Attribute attribute : element.attributes) {
                        put(entries, bundle, attribute.name, attribute.overridden?.metaInfo)
                    }
                }
                EnumObject: {
                    put(entries, bundle, element.name, element.metaInfo)
                    for (instance : element.instances) {
                        put(entries, bundle, instance.name, instanceMeta(instance))
                    }
                }
                Command: {
                    // The command's own wording is what a menu entry and a button read, so it is
                    // keyed by the command itself - the way a view or a value object is.
                    put(entries, bundle, element.name, element.metaInfo)
                    for (Attribute attribute : element.attributes) {
                        put(entries, bundle, attribute.name, attribute.overridden?.metaInfo)
                    }
                }
            }
        }
    }

    def private static void put(Map<String, String> entries, String bundle, String key,
            TypeMetaInfo meta) {
        if (meta === null) {
            return
        }
        add(entries, bundle, key, "slabel", meta.slabel)
        add(entries, bundle, key, "label", meta.label)
        add(entries, bundle, key, "tooltip", meta.tooltip)
        add(entries, bundle, key, "prompt", meta.prompt)
    }

    def private static void add(Map<String, String> entries, String bundle, String key, String suffix,
            String value) {
        if (value !== null) {
            entries.put(bundle + "." + key + "." + suffix, value)
        }
    }

    def private arb(Map<String, String> entries) {
        '''
        {
          "@@locale": "«LOCALE»",
          «FOR entry : entries.entrySet SEPARATOR ","»
          "«entry.key»": «json(entry.value)»
          «ENDFOR»
        }
        '''
    }

    def private static String json(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\""
    }

}

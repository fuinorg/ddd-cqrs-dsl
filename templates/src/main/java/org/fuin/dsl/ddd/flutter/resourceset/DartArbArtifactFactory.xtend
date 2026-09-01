package org.fuin.dsl.ddd.flutter.resourceset

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.TreeMap
import java.util.TreeSet
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartAttribute
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsBusinessRulesExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.EventExtensions.*

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
        val modules = new ArrayList<Module>()
        var String project = null
        val it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val container = it.next
            if (container instanceof Module) {
                project = container.context.name
                modules.add(container)
            }
        }
        val referenced = new TreeSet<String>()
        for (module : modules) {
            collectReferences(module, referenced)
        }
        for (module : modules) {
            collect(module, entries, referenced)
        }
        collectModules(modules, entries)
        if (project === null || entries.empty) {
            return null
        }

        return List.of(newArtifact("l10n/melkheftken_" + LOCALE + ".arb",
            arb(entries).toString.getBytes("UTF-8"), MODULE, FOLDER))
    }

    /**
     * Writes what a module contributes, once per group a user navigates by.
     *
     * <p>A module is routinely split over a public and a private file, and a context is routinely split
     * over a top-level module and the view modules under it - but the client's hub has one entry per
     * <em>group</em>, so only one of them is ever looked up. Which one is not this file's decision to
     * take twice: the rule is the catalogue's - the top-level module's wording when it states any, and
     * the first sub-module that states otherwise, by name. Writing an entry for each of them instead is
     * what put wording in the bundle that no descriptor could ever ask for.
     */
    def private void collectModules(List<Module> modules, Map<String, String> entries) {
        val chosen = new LinkedHashMap<String, Module>()
        for (module : modules.sortBy[m|m.name].filter[states(metaInfo)]) {
            val group = groupName(module.name)
            if (module.name == group || !chosen.containsKey(group)) {
                chosen.put(group, module)
            }
        }
        for (module : chosen.values) {
            put(entries, bundleName(module), module.name, module.metaInfo)
        }
    }

    /** The hub entry a module belongs to: everything before the first dot. */
    def private static String groupName(String moduleName) {
        val idx = moduleName.indexOf('.')
        return if(idx < 0) moduleName else moduleName.substring(0, idx)
    }

    /**
     * Notes every type some attribute is keyed to, so a type's caption is written where it is read and
     * nowhere else.
     */
    def private void collectReferences(Module module, java.util.Set<String> referenced) {
        val bundle = bundleName(module)
        for (element : module.elements) {
            // A refusal's wording is read where a rule is shown as the reason an action is disabled,
            // which is a descriptor a command carries and only for a rule a client can answer. Walking
            // the commands rather than the operations is what makes that true: an operation no command
            // targets ships no descriptor, so its refusal is never on screen. Noting it here, rather
            // than writing every exception's message, keeps the bundle to what something asks for - the
            // same rule the types below follow.
            if (element instanceof Command) {
                val target = element.target
                if (target !== null && target.businessRules !== null) {
                    for (instance : target.businessRules.businessRuleInstances.nullSafe) {
                        val exception = if (instance.clientAnswerable) instance.declaredRule?.exception
                        if (exception !== null) {
                            referenced.add(bundleName(exception.module) + "." + exception.name)
                        }
                    }
                }
            }
            val variables = switch (element) {
                View: element.methods.map[parameters].flatten.toList
                ValueObject: element.attributes.nullSafe.toList
                Command: element.commandVariables.nullSafe
                default: null
            }
            for (variable : variables.nullSafe) {
                val dart = new DartAttribute(variable)
                val type = dart.wordingTypeName
                if (type !== null) {
                    referenced.add(wordingBundle(dart, bundle) + "." + type)
                }
            }
        }
    }

    def private void collect(Module module, Map<String, String> entries, java.util.Set<String> referenced) {
        val bundle = bundleName(module)
        for (element : module.elements) {
            switch (element) {
                View: {
                    put(entries, bundle, element.name, element.metaInfo)
                    for (method : element.methods) {
                        val methodId = element.name + "." + method.name
                        put(entries, bundle, methodId, method.metaInfo)
                        for (parameter : method.parameters) {
                            putVariable(entries, bundle, methodId, parameter)
                        }
                    }
                }
                ValueObject: {
                    putType(entries, bundle, element.name, element.metaInfo, referenced)
                    // A value object over a base with one attribute is a scalar on the wire and in
                    // Dart - `ModuleName`, not `{value: ...}` - so it gets no descriptor for that one
                    // attribute, and wording written for it could never be read.
                    if (!single(element)) {
                        for (Attribute attribute : element.attributes) {
                            putVariable(entries, bundle, element.name, attribute)
                        }
                    }
                }
                EnumObject: {
                    putType(entries, bundle, element.name, element.metaInfo, referenced)
                    for (instance : element.instances) {
                        put(entries, bundle, element.name + "." + instance.name, instanceMeta(instance))
                    }
                }
                Command: {
                    // The command's own wording is what a menu entry and a button read, so it is
                    // keyed by the command itself - the way a view or a value object is.
                    put(entries, bundle, element.name, element.metaInfo)
                    // The message is the sentence a confirmation dialog asks, so it is wording too -
                    // and the one piece of it a command nearly always states, where slabel and label
                    // are usually left to fall back on the documentation. Its placeholders travel as
                    // they stand: what substitutes them runs after the lookup, not before it.
                    add(entries, bundle, element.name, "message", element.message)
                    // A command usually declares no attributes of its own: it names an operation, and
                    // takes what that operation takes. Reading `attributes` alone therefore misses the
                    // wording of every command that does what commands normally do.
                    for (variable : element.commandVariables.nullSafe) {
                        putVariable(entries, bundle, element.name, variable)
                    }
                }
                // A refusal states wording like anything else, and it is the one caption a screen shows
                // that the model writes as a sentence rather than a label - so it is keyed to the
                // exception and written where the exception is declared. Only the ones a descriptor
                // points at, which is what `referenced` collected.
                org.fuin.dsl.cqrs.cqrsDsl.Exception: {
                    if (referenced.contains(bundle + "." + element.name)) {
                        add(entries, bundle, element.name, "message", element.message)
                    }
                }
                // An id states wording like anything else, and an attribute typed by one is keyed to it
                // rather than to itself. Leaving ids out is what left those keys pointing at nothing.
                AbstractEntityId:
                    putType(entries, bundle, element.name, element.metaInfo, referenced)
                EntityIdPathType:
                    putType(entries, bundle, element.name, element.metaInfo, referenced)
            }
        }
    }

    /**
     * Writes what an attribute or parameter contributes, under the key the descriptor will look it up by.
     *
     * <p>Delegates to {@link DartAttribute} rather than deciding again: the descriptor and this file have
     * to agree about both the key and the wording behind it, and the only way they cannot drift is if one
     * function answers for both. Wording inherited from a type contributes nothing here - the type writes
     * its own entry, once, wherever it is declared.
     */
    def private static void putVariable(Map<String, String> entries, String bundle, String owner,
            Variable variable) {
        val dart = new DartAttribute(variable)
        put(entries, wordingBundle(dart, bundle), dart.metaKey(owner), variable.overridden?.metaInfo)
    }

    /** Whether the value object is a single value in disguise, rendered as its base rather than a row. */
    def private static boolean single(ValueObject vo) {
        return vo.base !== null && vo.attributes !== null && vo.attributes.size === 1
    }

    /** Writes a type's own wording, but only where some attribute is keyed to it. */
    def private static void putType(Map<String, String> entries, String bundle, String key,
            TypeMetaInfo meta, java.util.Set<String> referenced) {
        if (referenced.contains(bundle + "." + key)) {
            put(entries, bundle, key, meta)
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

package org.fuin.dsl.ddd.flutter.resourceset

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Creates one file per bounded context, re-exporting everything that context generates.
 *
 * <p><b>Generated because it is the public API.</b> A hand-kept barrel is a list somebody has to
 * remember to add to, and the day they forget, a type the model declares is simply not there for a
 * consumer - with no error anywhere, because the file it lives in still compiles. That matters little
 * inside one application, where a caller can reach past the barrel to the file. It matters a great deal
 * for a package taken from git by other projects, where the barrel is the whole of what they see.
 *
 * <p><b>Per context, not one flat file.</b> Dart has no namespaces - a library is the namespace - and
 * two bounded contexts are entitled to the same simple name. A model with a `CompanyName` in both its
 * partner context and its master-data context is not confused; it is doing what bounded contexts are
 * for. One file exporting both would be a name clash the model does not have, so the context is what a
 * caller imports, and the two `CompanyName`s stay as distinct as the model says they are.
 *
 * <p>These sit beside the generated code and export only that. The package's own entry point is a
 * hand-written file above them that names the contexts it offers and the hand-written half beside it -
 * because nothing in the model knows the runtime types exist, and a generator that guessed at their
 * filenames would be inventing rather than deriving.
 *
 * <p>The target module and folder come from this factory's own configuration rather than being named
 * here: a shared library and an application's contract are different modules and both want this file.
 */
class DartLibraryArtifactFactory extends AbstractDartSource<ResourceSet> {


    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.DART_LIBRARY
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (preparationRun) {
            return null
        }

        val groups = exportsOf(resourceSet)
        if (groups.empty) {
            return null
        }

        val out = new ArrayList<GeneratedArtifact>()
        for (group : groups.entrySet) {
            out.add(newArtifact(group.key + ".dart",
                tidy(library(group.key, group.value).toString).getBytes("UTF-8")))
        }
        return out
    }

    def private library(String group, TreeSet<String> exports) {
        '''
        /// Everything the «group» context offers, in one import.
        ///
        /// Every export earns its place by being in the model: a type is reachable here the moment it
        /// compiles, and stops being reachable in the same step it is dropped. That is what makes this
        /// file answer "what does this context offer" honestly, which a hand-kept list cannot.
        ///
        /// One file per context on purpose - a library is Dart's namespace, and two contexts may name
        /// the same thing differently or the same. Import the one you mean.
        library;

        «FOR export : exports»
        export '«export»';
        «ENDFOR»
        '''
    }

    /**
     * Every generated file worth naming, grouped by bounded context and keyed by it.
     *
     * <p>Only what a consumer can use: a type, a command, a view client. The hand-written half of the
     * package exports itself, because nothing here knows it is there.
     */
    def private Map<String, TreeSet<String>> exportsOf(ResourceSet resourceSet) {
        val out = new LinkedHashMap<String, TreeSet<String>>()
        val it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val element = it.next
            switch (element) {
                ValueObject: add(out, element.module, element.name)
                EnumObject: add(out, element.module, element.name)
                AbstractEntityId: add(out, element.module, element.name)
                Command: add(out, element.module, element.name)
                View: {
                    add(out, element.module, element.name + "Client")
                    add(out, element.module, element.name + "Descriptor")
                }
            }
        }
        return out
    }

    def private static void add(Map<String, TreeSet<String>> out,
            org.fuin.dsl.cqrs.cqrsDsl.Module module, String name) {
        var exports = out.get(groupOf(module))
        if (exports === null) {
            exports = new TreeSet<String>()
            out.put(groupOf(module), exports)
        }
        // Relative to the barrel, which sits at the root of the generated tree.
        exports.add(DartNames.file(module, name))
    }

    /**
     * The context a module belongs to, which is its first segment.
     *
     * <p>The same rollup the module catalogue does: the model splits a context into an aggregate module
     * and a read-side one, and both are the same context to anybody outside.
     */
    def private static String groupOf(org.fuin.dsl.cqrs.cqrsDsl.Module module) {
        val idx = module.name.indexOf('.')
        return if(idx < 0) module.name else module.name.substring(0, idx)
    }

}

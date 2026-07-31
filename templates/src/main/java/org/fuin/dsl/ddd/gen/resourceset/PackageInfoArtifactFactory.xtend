package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.Iterator
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement
import org.fuin.dsl.cqrs.cqrsDsl.Context
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

/**
 * Creates a "package-info.java" annotated with JSpecify's {@code @NullMarked} for every package a
 * module generates code into. A module may feed more than one module - for example its value
 * objects go to a "shared" module while its aggregate goes to a "command" module - and each of those
 * packages needs its own "package-info.java". The target packages are therefore derived from the
 * module's own elements and not from this factory's own hint entry: only the elements know which
 * module and group, and hence which package, they end up in. Whether an already existing file is
 * overwritten is controlled by the target folder's "override" setting, not by this factory.
 */
class PackageInfoArtifactFactory extends AbstractSource<ResourceSet> {

    override getModelType() {
        typeof(ResourceSet)
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val List<GeneratedArtifact> artifacts = new ArrayList<GeneratedArtifact>()
        // Every element lives in a module, and a module needs one "package-info.java" per
        // target package it generates into.
        val Iterator<EObject> it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val EObject container = it.next
            if (container.isElementContainer && container.generatesCode) {
                artifacts.addAll(createArtifacts(container))
            }
        }

        return artifacts
    }

    /**
     * Creates one artifact per package the given element container generates code into.
     *
     * @param container Module to create the "package-info.java" file(s) for.
     *
     * @return At least one artifact.
     */
    private def List<GeneratedArtifact> createArtifacts(EObject container) {
        val List<GeneratedArtifact> artifacts = new ArrayList<GeneratedArtifact>()
        val Map<String, String> packagesByModule = container.targetPackages
        if (packagesByModule.empty) {
            // No "SrcGen4J" hint, or no hint entry for any element: single package from the fallback
            val String pkg = container.asPackage
            artifacts.add(newArtifact(pkg.asFilename, create(pkg).getBytes("UTF-8"), container))
        } else {
            val String fold = container.targetFolder
            for (entry : packagesByModule.entrySet) {
                val String pkg = entry.value
                artifacts.add(newArtifact(pkg.asFilename, create(pkg).getBytes("UTF-8"), entry.key, fold))
            }
        }
        return artifacts
    }

    /**
     * Collects the packages the elements of the given container are generated into, keyed by target module.
     * A module appears only once: elements that share a module also share a package, because the package
     * pattern is expanded from that same "module" and "group".
     *
     * @param container Module to inspect.
     *
     * @return Package per module, in declaration order - Empty if nothing could be resolved from the hint.
     */
    private def Map<String, String> targetPackages(EObject container) {
        val Map<String, String> result = new LinkedHashMap<String, String>()
        val hint = srcGen4JHint(container)
        if (hint === null) {
            return result
        }
        for (element : container.elements) {
            if (!(element instanceof ExternalType)) {
                val type = typeForElement(hint, element)
                if (type !== null && type.module !== null) {
                    result.putIfAbsent(type.module, expandPackage(hint, type, container))
                }
            }
        }
        return result
    }

    private def String asFilename(String pkg) {
        pkg.replace('.', '/') + "/package-info.java"
    }

    /**
     * Determines if the given container holds at least one element that produces generated code. A
     * container that only declares external types (or is empty) produces no Java source and is
     * therefore skipped.
     *
     * @param container Module to check.
     *
     * @return TRUE if the container generates at least one source file.
     */
    def boolean generatesCode(EObject container) {
        container.elements.exists[!(it instanceof ExternalType)]
    }

    /**
     * Determines whether the given object is an element container that needs "package-info.java"
     * files. Every element lives in a module, so a context never is one - its modules are.
     *
     * @param obj Object to check.
     *
     * @return TRUE if the object directly contains model elements.
     */
    private def boolean isElementContainer(EObject obj) {
        obj instanceof Module
    }

    /**
     * Returns the model elements a module holds directly, or an empty list for anything else.
     *
     * @param container Element container.
     *
     * @return Direct model elements, never <code>null</code>.
     */
    private def List<AbstractElement> elements(EObject container) {
        if (container instanceof Module) {
            return container.elements
        }
        return emptyList
    }

    def String create(String pkg) {
        '''
        «copyrightHeader»
        @NullMarked
        package «pkg»;

        import org.jspecify.annotations.NullMarked;
        '''
    }

}

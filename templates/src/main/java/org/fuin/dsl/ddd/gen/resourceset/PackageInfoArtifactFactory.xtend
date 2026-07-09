package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.Iterator
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.Namespace
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

/**
 * Creates a "package-info.java" annotated with JSpecify's {@code @NullMarked} for every package a
 * namespace generates code into. A namespace may feed more than one module - for example its value
 * objects go to a "shared" module while its aggregate goes to a "command" module - and each of those
 * packages needs its own "package-info.java". The target packages are therefore derived from the
 * namespace's own elements and not from this factory's own hint entry: only the elements know which
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
        val Iterator<Namespace> it = resourceSet.allContents.filter(typeof(Namespace)).filter[isPrimary(it)]
        while (it.hasNext) {
            val Namespace ns = it.next
            if (ns.generatesCode) {
                artifacts.addAll(createArtifacts(ns))
            }
        }

        return artifacts
    }

    /**
     * Creates one artifact per package the given namespace generates code into.
     *
     * @param ns Namespace to create the "package-info.java" file(s) for.
     *
     * @return At least one artifact.
     */
    private def List<GeneratedArtifact> createArtifacts(Namespace ns) {
        val List<GeneratedArtifact> artifacts = new ArrayList<GeneratedArtifact>()
        val Map<String, String> packagesByModule = ns.targetPackages
        if (packagesByModule.empty) {
            // No "SrcGen4J" hint, or no hint entry for any element: single package from the fallback
            val String pkg = ns.asPackage
            artifacts.add(newArtifact(pkg.asFilename, create(pkg).getBytes("UTF-8"), ns))
        } else {
            val String fold = ns.targetFolder
            for (entry : packagesByModule.entrySet) {
                val String pkg = entry.value
                artifacts.add(newArtifact(pkg.asFilename, create(pkg).getBytes("UTF-8"), entry.key, fold))
            }
        }
        return artifacts
    }

    /**
     * Collects the packages the elements of the given namespace are generated into, keyed by target module.
     * A module appears only once: elements of a namespace that share a module also share a package, because
     * the package pattern is expanded from that same "module" and "group".
     *
     * @param ns Namespace to inspect.
     *
     * @return Package per module, in declaration order - Empty if nothing could be resolved from the hint.
     */
    private def Map<String, String> targetPackages(Namespace ns) {
        val Map<String, String> result = new LinkedHashMap<String, String>()
        val hint = srcGen4JHint(ns)
        if (hint === null) {
            return result
        }
        for (element : ns.elements) {
            if (!(element instanceof ExternalType)) {
                val type = typeForElement(hint, element)
                if (type !== null && type.module !== null) {
                    result.putIfAbsent(type.module, expandPackage(hint, type, ns))
                }
            }
        }
        return result
    }

    private def String asFilename(String pkg) {
        pkg.replace('.', '/') + "/package-info.java"
    }

    /**
     * Determines if a namespace contains at least one element that produces generated code. A
     * namespace that only declares external types (or is empty) produces no Java source and is
     * therefore skipped.
     *
     * @param ns Namespace to check.
     *
     * @return TRUE if the namespace generates at least one source file.
     */
    def boolean generatesCode(Namespace ns) {
        ns.elements.exists[!(it instanceof ExternalType)]
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

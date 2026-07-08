package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.Iterator
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.Namespace
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

/**
 * Creates a "package-info.java" annotated with JSpecify's {@code @NullMarked} once for every
 * generated package. A package is derived from a {@link Namespace} (see {@link #asPackage}). Whether
 * an already existing file is overwritten is controlled by the target folder's "override" setting,
 * not by this factory.
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
                val String pkg = asPackage(ns)
                val String filename = pkg.replace('.', '/') + "/package-info.java"
                artifacts.add(newArtifact(filename, create(pkg).getBytes("UTF-8"), ns))
            }
        }

        return artifacts
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

package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.Namespace
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.commons.Replacer
import org.fuin.srcgen4j.core.emf.PrimaryResources

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

abstract class AbstractSource<T> implements ArtifactFactory<T> {

    String artifactName;

    String project;

    String folder;

    Map<String, String> varMap;

    GenerateOptions options;

    List<Replacer> importReplacers;

    override init(ArtifactFactoryConfig config) {
        artifactName = config.getArtifact()
        project = config.getProject()
        folder = config.getFolder()
        varMap = config.varMap
        options = new GenerateOptions(varMap)
        val configReplacers = config.replacers
        if (configReplacers === null) {
            importReplacers = newArrayList
        } else {
            importReplacers = configReplacers.filter[extension == "import"].toList
        }
    }

    /**
     * Creates a generated artifact for the current factory. The unique artifact name, the target
     * project and the target folder are taken from the {@link ArtifactFactoryConfig} captured in
     * {@link #init(ArtifactFactoryConfig)}.
     *
     * @param filename Relative path and filename to write the source code to.
     * @param data Generated data.
     *
     * @return New generated artifact.
     */
    protected def GeneratedArtifact newArtifact(String filename, byte[] data) {
        return new GeneratedArtifact(artifactName, filename, data, project, folder)
    }

    override isIncremental() {
        true
    }

    /**
     * Determines if an object originates from a model in the source directory (and not from a
     * remotely resolved dependency model). Only source models should produce generated artifacts.
     *
     * @param obj Object to check.
     *
     * @return TRUE if the object is part of a primary (source directory) resource.
     */
    def boolean isPrimary(EObject obj) {
        PrimaryResources.isPrimary(obj)
    }

    def String getArtifactName() {
        return artifactName
    }

    def GenerateOptions getOptions() {
        return options
    }

    def String getCopyrightHeader() {
        return options.copyrightHeader
    }

    protected def String getVar(String key, String defaultVal) {
        val str = this.varMap.nullSafe.get(key)
        if (str === null) {
            return defaultVal
        }
        return str
    }

    def String asPackage(Namespace ns) {
        var String pkg
        if (!isPrimary(ns)) {
            // External (remotely resolved) element: import it from its own context.namespace, without
            // the local model's base package or pkg.
            pkg = joinPackage(ns.context.name, ns.name)
        } else {
            pkg = joinPackage(getOptions().getBasePkg(), ns.context.name, getOptions().getPkg(), ns.name)
        }
        // Configured replacers with extension "import" may remap the resulting package.
        for (replacer : importReplacers) {
            pkg = replacer.replace(pkg)
        }
        return pkg
    }

    /**
     * Joins the given package segments with a dot, skipping segments that are not set (null or
     * empty). This means an unset base package does not result in a "null." prefix - the package
     * name then simply starts with the context name.
     *
     * @param parts Package segments in order (e.g. base package, context, package, namespace).
     *
     * @return Dot separated package name built from the non-empty segments.
     */
    protected def String joinPackage(String... parts) {
        val segments = new ArrayList<String>()
        for (part : parts) {
            if (part !== null && !part.empty) {
                segments.add(part)
            }
        }
        return segments.join(".")
    }

}

package org.fuin.dsl.ddd.gen.base

import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.Namespace
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.core.emf.PrimaryResources

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

abstract class AbstractSource<T> implements ArtifactFactory<T> {

    String artifactName;

    Map<String, String> varMap;
    
    GenerateOptions options;

    override init(ArtifactFactoryConfig config) {
        artifactName = config.getArtifact()
        varMap = config.varMap
        options = new GenerateOptions(varMap)
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

    def String contextPkg(String ctxName) {
        if (getOptions().getPkg() === null) {
            return getOptions().getBasePkg() + "." + ctxName
        }
        return getOptions().getBasePkg() + "." + ctxName + "." + getOptions().getPkg()
    }

    def String asPackage(Namespace ns) {
        if (getOptions().getPkg() === null) {
            return getOptions().getBasePkg() + "." + ns.context.name + "." + ns.name;
        }
        return getOptions().getBasePkg() + "." + ns.context.name + "." + getOptions().getPkg() + "." + ns.name;
    }

}

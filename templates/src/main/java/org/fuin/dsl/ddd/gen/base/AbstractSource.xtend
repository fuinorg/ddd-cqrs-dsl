package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.ddd.gen.script.CqrsScripts
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.PrimaryResources

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

abstract class AbstractSource<T> implements ArtifactFactory<T> {

    String artifactName;

    protected String factoryClassName;

    String module;

    String folder;

    Map<String, String> varMap;

    GenerateOptions options;

    override init(ArtifactFactoryConfig config) {
        initFrom(config, config.getFactoryClassName())
    }

    /**
     * Initializes the factory from the given configuration, but looks the "SrcGen4J" hint up with the
     * given factory class name instead of the one from the configuration. A factory that delegates to
     * other factories passes its own configuration on to them; without this, every delegate would match
     * the hint entry of the delegating factory and inherit its target module and folder instead of using
     * its own.
     *
     * @param config Configuration to take the artifact name, module, folder and variables from.
     * @param hintFactoryClassName Factory class name the hint lookup matches against.
     */
    def void initFrom(ArtifactFactoryConfig config, String hintFactoryClassName) {
        artifactName = config.getArtifact()
        factoryClassName = hintFactoryClassName
        module = config.getModule()
        folder = config.getFolder()
        varMap = config.varMap
        options = new GenerateOptions(varMap)
    }

    /**
     * Creates a generated artifact for the current factory. The unique artifact name, the target
     * module and the target folder are taken from the {@link ArtifactFactoryConfig} captured in
     * {@link #init(ArtifactFactoryConfig)}.
     *
     * @param filename Relative path and filename to write the source code to.
     * @param data Generated data.
     *
     * @return New generated artifact.
     */
    protected def GeneratedArtifact newArtifact(String filename, byte[] data) {
        return new GeneratedArtifact(artifactName, filename, data, module, folder)
    }

    /**
     * Creates a generated artifact, asking the model's <code>artifact2Target</code> script where it
     * belongs: it answers with the target Maven module and the folder inside it.
     *
     * @param filename Relative path and filename to write the source code to.
     * @param data Generated data.
     * @param el Element the artifact is generated for.
     *
     * @return New generated artifact.
     */
    protected def GeneratedArtifact newArtifact(String filename, byte[] data, EObject el) {
        return newArtifact(filename, data, el, getTypeKey())
    }

    /**
     * Creates a generated artifact of a kind other than this factory's own, for a factory that emits
     * more than one.
     *
     * @param filename Relative path and filename to write the source code to.
     * @param data Generated data.
     * @param el Element the artifact is generated for.
     * @param typeKey Kind of artifact - see {@link TypeKeys}.
     *
     * @return New generated artifact.
     */
    protected def GeneratedArtifact newArtifact(String filename, byte[] data, EObject el, String typeKey) {
        val target = CqrsScripts.artifact2Target(el, typeKey, factoryClassName)
        return newArtifact(filename, data, target.module, target.folder)
    }

    /**
     * Creates a generated artifact for an explicitly given target module and folder. Used by factories that
     * write into more than one module, where the module cannot be derived from a single hint type entry.
     *
     * @param filename Relative path and filename to write the source code to.
     * @param data Generated data.
     * @param module Name of the target module.
     * @param folder Name of the target folder inside the module.
     *
     * @return New generated artifact.
     */
    protected def GeneratedArtifact newArtifact(String filename, byte[] data, String module, String folder) {
        return new GeneratedArtifact(artifactName, filename, data, module, folder)
    }

    /**
     * Determines the target folder for artifacts of this factory, asking the model's
     * <code>artifact2Target</code> script.
     *
     * @param el Element the artifact is generated for.
     *
     * @return Folder name.
     */
    protected def String targetFolder(EObject el) {
        return CqrsScripts.artifact2Target(el, getTypeKey(), factoryClassName).folder
    }

    /**
     * The kind of artifact this factory creates, as one of the keys of {@link TypeKeys}. It selects the
     * package (<code>model2JavaPackage</code>) and the target (<code>artifact2Target</code>) of
     * everything the factory emits, and names the type in the code reference registry.
     *
     * @return Type key, or <code>null</code> for a factory that emits nothing of its own.
     */
    def String getTypeKey()

    override isIncremental() {
        true
    }

    /**
     * The fully qualified name a type was registered under, failing when nothing is registered.
     * <p>
     * {@link org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext} answers an unknown key with the key
     * itself, which turns a missing registration into an import of the model's own unique name - a
     * silently wrong import that nothing reports. Everything that must resolve goes through here
     * instead.
     *
     * @param refReg Registry to look the key up in.
     * @param refKey Key built with {@link TypeKeys#refKey(String, String)}.
     *
     * @return Fully qualified name, never <code>null</code>.
     */
    protected def String requiredReference(CodeReferenceRegistry refReg, String refKey) {
        val fqn = refReg.getReference(refKey)
        if (fqn === null) {
            throw new IllegalStateException("Nothing is registered under '" + refKey +
                "'. Either the artifact factory creating that type is not configured, or the type key " +
                "used to reference it differs from the one it was registered with.")
        }
        return fqn
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

    /**
     * The Java package this factory's artifact for the given element is generated into.
     * <p>
     * The answer comes from the <code>model2JavaPackage</code> script of the context the element belongs
     * to - this project's for a locally declared element, the one shipped inside the archive for an imported
     * one, which is how a reference to an imported type gets the package its producer generated it into.
     *
     * @param el Element to build the package for.
     *
     * @return Package name, never <code>null</code>.
     */
    def String asPackage(EObject el) {
        return asPackage(el, getTypeKey())
    }

    /**
     * The Java package of a type of a kind other than this factory's own.
     *
     * @param el Element to build the package for.
     * @param typeKey Kind of artifact - see {@link TypeKeys}.
     *
     * @return Package name, never <code>null</code>.
     */
    def String asPackage(EObject el, String typeKey) {
        return CqrsScripts.model2JavaPackage(el, typeKey)
    }

    /**
     * Joins the given package segments with a dot, skipping segments that are not set (null or
     * empty). This means an unset base package does not result in a "null." prefix - the package
     * name then simply starts with the context name.
     *
     * @param parts Package segments in order (e.g. base package, context, package, module).
     *
     * @return Dot separated package name built from the non-empty segments.
     */
    /**
     * Resource bundle base name for elements of a module: its <em>last</em> segment, capitalized.
     * A module name is an FQN, so a grouping that used to be a nested module now reads
     * "outer.inner" - the bundle stays "Inner", the name the properties file is looked up under.
     *
     * @param ns Module (may be <code>null</code>).
     *
     * @return Bundle base name, or <code>null</code> when there is no module.
     */
    /**
     * The bounded context a module belongs to: the <em>first</em> segment of its name. A module
     * name is an FQN, so "journal" is its own bounded context while "journal.view" is a sub grouping
     * of "journal". Used for artifacts that exist once per bounded context, such as the
     * "&lt;Context&gt;EntityIdFactory".
     *
     * @param ns Module (may be <code>null</code>).
     *
     * @return First segment of the module name, or <code>null</code> when there is no module.
     */
    protected static def String contextSegment(Module ns) {
        val name = ns?.name
        if (name === null) {
            return null
        }
        val idx = name.indexOf('.')
        return if(idx < 0) name else name.substring(0, idx)
    }

    /**
     * The sub grouping of a module inside its bounded context: everything after the first segment
     * of its name, or <code>null</code> when the module <em>is</em> the bounded context. For
     * "journal.view" this is "view"; for "journal" it is <code>null</code>.
     *
     * @param ns Module (may be <code>null</code>).
     *
     * @return Module name without its first segment, or <code>null</code> when there is none.
     */
    protected static def String subModule(Module ns) {
        val name = ns?.name
        if (name === null) {
            return null
        }
        val idx = name.indexOf('.')
        return if(idx < 0) null else name.substring(idx + 1)
    }

    protected static def String bundleName(Module ns) {
        val name = ns?.name
        if (name === null) {
            return null
        }
        val idx = name.lastIndexOf('.')
        return (if(idx < 0) name else name.substring(idx + 1)).toFirstUpper
    }

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

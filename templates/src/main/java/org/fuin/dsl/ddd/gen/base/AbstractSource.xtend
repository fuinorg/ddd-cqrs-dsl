package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Hint
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.PrimaryResources

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

abstract class AbstractSource<T> implements ArtifactFactory<T> {

    String artifactName;

    String factoryClassName;

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
     * Creates a generated artifact, taking the target module and folder from the project's "SrcGen4J"
     * generator hint when a matching type entry exists: the hint type's "module" becomes the target
     * module and the matching artifact's "folder" becomes the target folder. When there is no matching
     * hint, the module and folder from the {@link ArtifactFactoryConfig} are used as a fallback.
     *
     * @param filename Relative path and filename to write the source code to.
     * @param data Generated data.
     * @param ns Namespace the generated element belongs to (drives the hint lookup).
     *
     * @return New generated artifact.
     */
    protected def GeneratedArtifact newArtifact(String filename, byte[] data, EObject el) {
        var String mod = module
        val type = matchingType(srcGen4JHint(el))
        if (type !== null && type.module !== null) {
            mod = type.module
        }
        return newArtifact(filename, data, mod, targetFolder(el))
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
     * Determines the target folder for artifacts of this factory: the folder of the matching hint artifact
     * entry, or the folder from the {@link ArtifactFactoryConfig} as a fallback.
     *
     * @param ns Namespace the generated element belongs to (drives the hint lookup).
     *
     * @return Folder name.
     */
    protected def String targetFolder(EObject el) {
        val type = matchingType(srcGen4JHint(el))
        if (type !== null) {
            val factoryName = factoryClassName
            val artifact = type.artifacts.findFirst[artifactFactory == factoryName]
            if (artifact !== null && artifact.folder !== null) {
                return artifact.folder
            }
        }
        return folder
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

    def String asPackage(EObject el) {
        // Primary path: derive the package from the "SrcGen4J" generator hint of the project the
        // element belongs to (this handles remote and local elements the same way).
        var String pkg = hintPackage(el)
        if (pkg === null) {
            // Fallback (no matching SrcGen4J hint for this factory/type): the same for primary and
            // remotely resolved elements - project.context[.namespace]. The namespace is optional;
            // joinPackage skips it when the element is declared directly in a context.
            pkg = joinPackage(el.project?.name, el.context?.name, el.namespace?.name)
        }
        return pkg
    }

    /**
     * Builds the package from the "SrcGen4J" generator hint of the project the given namespace belongs
     * to. The type entry whose name matches this factory's model type ({@link #getModelType}) and whose
     * artifacts contain this factory's class supplies the "module" and "group"; the hint's "package"
     * pattern is then expanded by replacing the variables with the current project/context/namespace
     * names and that type's module/group.
     *
     * @param ns Namespace to build the package for.
     *
     * @return Package name, or <code>null</code> if there is no project, no "SrcGen4J" hint, or no type
     *         entry matching both this factory's model type and its class (caller falls back).
     */
    protected def String hintPackage(EObject el) {
        val hint = srcGen4JHint(el)
        val type = matchingType(hint)
        if (type === null) {
            return null
        }
        return expandPackage(hint, type, el)
    }

    /**
     * Expands the hint's "package" pattern for the given type entry and namespace. The type entry supplies
     * the "module" and "group", the namespace the project, context and namespace names.
     *
     * @param hint Effective hint that provides the pattern.
     * @param type Type entry that supplies "module" and "group".
     * @param ns Namespace to build the package for.
     *
     * @return Package name.
     */
    protected def String expandPackage(SrcGen4JHint hint, SrcGen4JType type, EObject el) {
        val values = newLinkedHashMap(
            "project" -> (el.project?.name ?: ""),
            "module" -> (type.module ?: ""),
            "group" -> (type.group ?: ""),
            "context" -> (el.context?.name ?: ""),
            "namespace" -> (el.namespace?.name ?: "")
        )
        return expandPattern(hint.packagePattern, values)
    }

    /**
     * Expands a package pattern. Each <code>${var}</code> placeholder is replaced with its value
     * from the given map. A group wrapped in square brackets - e.g. <code>[.${namespace}]</code> -
     * is optional: it is removed entirely (including the leading separator inside the brackets) when
     * any placeholder inside it resolves to an empty value, otherwise the brackets are dropped and
     * the content is kept. This lets the optional namespace segment disappear for elements that are
     * declared directly in a context.
     *
     * @param pattern Package pattern, possibly containing <code>${var}</code> placeholders and
     *                optional <code>[...]</code> groups.
     * @param values Placeholder values by name (a missing or empty value drops its optional group).
     *
     * @return Expanded package name.
     */
    protected def String expandPattern(String pattern, Map<String, String> values) {
        // 1. Resolve optional "[ ... ]" groups: drop a group when any placeholder inside is empty.
        var String result = pattern
        var int open = result.indexOf("[")
        while (open >= 0) {
            val close = result.indexOf("]", open)
            if (close < 0) {
                open = -1 // Unbalanced bracket: leave the remainder untouched.
            } else {
                val group = result.substring(open + 1, close)
                val replacement = if (groupHasEmptyValue(group, values)) "" else group
                result = result.substring(0, open) + replacement + result.substring(close + 1)
                open = result.indexOf("[")
            }
        }
        // 2. Substitute the remaining "${var}" placeholders.
        for (e : values.entrySet) {
            result = result.replace("${" + e.key + "}", e.value ?: "")
        }
        return result
    }

    /** TRUE if the given pattern fragment references a placeholder whose value is null or empty. */
    private def boolean groupHasEmptyValue(String group, Map<String, String> values) {
        for (e : values.entrySet) {
            if (group.contains("${" + e.key + "}") && (e.value === null || e.value.empty)) {
                return true
            }
        }
        return false
    }

    /**
     * Finds the hint type entry that describes the given model element. A model's own hint entries are
     * merged in front of the preset's (see {@link SrcGen4JHint#merge}), so an override wins.
     *
     * @param hint Effective hint.
     * @param element Model element to look up.
     *
     * @return Type entry, or <code>null</code> if the hint has no entry for the element's type.
     */
    protected def SrcGen4JType typeForElement(SrcGen4JHint hint, EObject element) {
        if (hint === null || element === null) {
            return null
        }
        val typeName = element.eClass.instanceTypeName
        return hint.types.findFirst[name == typeName]
    }

    /** Lazily loaded "srcgen4j-default.json" preset, shared by all factory instances. */
    static SrcGen4JHint defaultHint

    /**
     * Returns the "srcgen4j-default.json" preset, loading it from the classpath on first use.
     *
     * @return Default preset (never <code>null</code>; an empty hint if the resource is missing).
     */
    private def static synchronized SrcGen4JHint defaultHint() {
        if (defaultHint === null) {
            defaultHint = SrcGen4JHint.loadDefault()
        }
        return defaultHint
    }

    /**
     * Resolves the effective "SrcGen4J" hint for the project the given namespace belongs to. The
     * "srcgen4j-default.json" preset is always used as the base; when the project defines its own
     * "SrcGen4J" hint, that hint is merged on top so its values overwrite the preset's (see
     * {@link SrcGen4JHint#merge}).
     *
     * @param ns Namespace (may be <code>null</code>).
     *
     * @return Effective hint - the preset alone when there is no project or no model hint, otherwise the
     *         preset with the model hint merged on top.
     */
    protected def SrcGen4JHint srcGen4JHint(EObject el) {
        val preset = defaultHint()
        val hint = modelHint(el)
        if (hint === null) {
            return preset
        }
        return SrcGen4JHint.merge(preset, SrcGen4JHint.parse(hint))
    }

    /**
     * Finds the "SrcGen4J" hint that applies to the given namespace. A project - like a context or a
     * namespace - may be split across several ".cqrs" files; all blocks with the same name denote the
     * same logical project, so the hint may be declared in any of them. The lookup therefore searches
     * every same-named project in the resource set, not only the project block the namespace is
     * physically nested in.
     *
     * @param ns Namespace (may be <code>null</code>).
     *
     * @return The "SrcGen4J" hint, or <code>null</code> if there is no enclosing project or no such hint.
     */
    private def Hint modelHint(EObject el) {
        val project = el?.project
        if (project === null) {
            return null
        }
        val rs = el.eResource?.resourceSet
        if (rs === null) {
            return project.hints.findFirst[name == "SrcGen4J"]
        }
        val projectName = project.name
        return rs.resources
            .map[contents].flatten
            .filter(DomainModel)
            .map[projects].flatten
            .filter[name == projectName]
            .map[hints].flatten
            .findFirst[name == "SrcGen4J"]
    }

    /**
     * Finds the hint type entry whose name matches this factory's model type ({@link #getModelType})
     * and whose artifacts contain this factory's class.
     *
     * @param hint Parsed "SrcGen4J" hint (may be <code>null</code>).
     *
     * @return Matching type entry, or <code>null</code> if the hint is null or nothing matches.
     */
    private def SrcGen4JType matchingType(SrcGen4JHint hint) {
        if (hint === null) {
            return null
        }
        val modelTypeName = getModelType.name
        val factoryName = factoryClassName
        return hint.types.findFirst [ t |
            t.name == modelTypeName && t.artifacts.exists[artifactFactory == factoryName]
        ]
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

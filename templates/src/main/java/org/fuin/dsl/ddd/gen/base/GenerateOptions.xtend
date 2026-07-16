package org.fuin.dsl.ddd.gen.base

import java.util.Arrays
import java.util.Collections
import java.util.List
import java.util.Map

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Options used in the generation process. 
 */
class GenerateOptions {

    /** Key to retrieve the copyright header. */
    public val static KEY_COPYRIGHT_HEADER = "copyrightHeader"

    /** Key for the name of the base package to prepend right before the context from the DSL (Type: String). */
    public val static KEY_BASE_PKG = "basepkg"

    /** Key for the name of a package to insert right after the context from the DSL (Type: String). */
    public val static KEY_PKG = "pkg"

    /** Key if to generate JPA annotations (Type: Boolean). */
    public val static KEY_JPA = "jpa"

    /** Key if to generate JAX-B annotations (Type: Boolean). */
    public val static KEY_JAXB = "jaxb"

    /** Key if to generate JAX-B elements instead of attributes. Used to harmonize JSON and XML structures (Type: Boolean). */
    public val static KEY_JAXB_ELEMENTS = "jaxb_elements"

    /** Key if to generate JSON-B annotations (Type: Boolean). */
    public val static KEY_JSONB = "jsonb"

    /** Key if to generate Jackson annotations (Type: Boolean). */
    public val static KEY_JACKSON = "jackson"

    /**
     * Key for the model namespaces that contain the built-in constraints. Constraints from those namespaces are
     * generated as Jakarta Validation annotations (like '@Size') instead of an annotation of their own. The value is a
     * comma separated list of namespaces, each of them either a "project.context.namespace" or a "project.context"
     * (Type: String).
     */
    public val static KEY_BUILTIN_CONSTRAINT_NAMESPACES = "builtinConstraintNamespaces"

    /** Namespace of the built-in constraints used if {@link #KEY_BUILTIN_CONSTRAINT_NAMESPACES} is not set. */
    public val static DEFAULT_BUILTIN_CONSTRAINT_NAMESPACES = "org.fuin.constr"

    var String basePkg

    var String pkg

    var boolean jpa

    var boolean jaxb

    var boolean jaxbElements

    var boolean jsonb

    var boolean jackson

    var String copyrightHeader

    var List<String> builtinConstraintNamespaces

    /**
     * Default constructor.
     */
    private new() {
        super()
        builtinConstraintNamespaces = parseNamespaces(null)
    }

    /**
     * Constructor with map to copy.
     * 
     * @param varMap Variables to use for retrieving the options.
     */
    protected new(Map<String, String> varMap) {
        super()

        basePkg = varMap.nullSafe.get(KEY_BASE_PKG)
        pkg = varMap.nullSafe.get(KEY_PKG)
        jpa = Boolean.valueOf(varMap.nullSafe.get(KEY_JPA))
        jaxb = Boolean.valueOf(varMap.nullSafe.get(KEY_JAXB))
        jaxbElements = Boolean.valueOf(varMap.nullSafe.get(KEY_JAXB_ELEMENTS))
        jsonb = Boolean.valueOf(varMap.nullSafe.get(KEY_JSONB))
        jackson = Boolean.valueOf(varMap.nullSafe.get(KEY_JACKSON))
        builtinConstraintNamespaces = parseNamespaces(varMap.nullSafe.get(KEY_BUILTIN_CONSTRAINT_NAMESPACES))

        val String header = varMap.nullSafe.get(KEY_COPYRIGHT_HEADER)
        if (header === null) {
            copyrightHeader = ""
        }
        copyrightHeader = header

    }

    /**
     * Determines if JPA annotations should be generated.
     * 
     * @return {@code true} if JPA annotations should be generated.
     */
    def boolean getJpa() {
        return jpa
    }

    /**
     * Determines if JAX-B annotations should be generated.
     * 
     * @return {@code true} if XML binding annotations should be generated.
     */
    def boolean getJaxb() {
        return jaxb
    }

    /**
     * Determines if '@XmlElement' annotations should be generated instead of '@XmlAttribute' for JAX-B.
     * 
     * @return {@code true} if element annotations should be generated.
     */
    def boolean getJaxbElements() {
        return jaxbElements
    }

    /**
     * Determines if JSON-B annotations should be generated.
     * 
     * @return {@code true} if JSON binding annotations should be generated.
     */
    def boolean getJsonb() {
        return jsonb
    }

    /**
     * Determines if Jackson annotations should be generated.
     * 
     * @return {@code true} if Jackson annotations should be generated.
     */
    def boolean getJackson() {
        return jackson
    }

    /**
     * Returns the copyright header to use.
     *
     * @return Copyright header for source files.
     */
    def String getCopyrightHeader() {
        return copyrightHeader
    }

    /**
     * Returns the model namespaces that contain the built-in constraints.
     *
     * @return Unmodifiable list of "project.context.namespace" and "project.context" names, never empty.
     */
    def List<String> getBuiltinConstraintNamespaces() {
        return builtinConstraintNamespaces
    }

    /**
     * Splits a comma separated list of namespaces. Falls back to {@link #DEFAULT_BUILTIN_CONSTRAINT_NAMESPACES} if
     * nothing is defined.
     *
     * @param str Comma separated list of namespaces or {@literal null}.
     *
     * @return Unmodifiable list of trimmed namespaces, never empty.
     */
    private static def List<String> parseNamespaces(String str) {
        val String value = if (str === null || str.trim.empty) DEFAULT_BUILTIN_CONSTRAINT_NAMESPACES else str
        return Collections.unmodifiableList(Arrays.asList(value.split(",")).map[trim].filter[!empty].toList)
    }

    /** 
     * Returns a new builder instance. Convenience method to shorten the builder creation in the code.
     * 
     * @return New builder instance.
     */
    static def Builder builder() {
        return new Builder()
    }

    /** 
     * Returns an empty instance.
     * 
     * @return New instance.
     */
    static def GenerateOptions empty() {
        return new GenerateOptions()
    }

    static class Builder {

        GenerateOptions obj

        new() {
            super();
            obj = GenerateOptions.empty()
        }

        new(GenerateOptions other) {
            this();
            obj.basePkg = other.basePkg
            obj.pkg = other.pkg
            obj.jpa = other.jpa
            obj.jaxb = other.jaxb
            obj.jaxbElements = other.jaxbElements
            obj.jsonb = other.jsonb
            obj.jackson = other.jackson
            obj.copyrightHeader = other.copyrightHeader
            obj.builtinConstraintNamespaces = other.builtinConstraintNamespaces
        }

        def Builder withBasePkg(String basePkg) {
            obj.basePkg = basePkg
            return this
        }

        def Builder withPkg(String pkg) {
            obj.pkg = pkg
            return this
        }

        def Builder withJpa(boolean jpa) {
            obj.jpa = jpa
            return this
        }

        def Builder withJpa() {
            obj.jpa = true
            return this
        }

        def Builder withJaxb(boolean jaxb) {
            obj.jaxb = jaxb
            return this
        }

        def Builder withJaxb() {
            obj.jaxb = true
            return this
        }

        def Builder withJaxbElements(boolean jaxbElements) {
            obj.jaxbElements = jaxbElements
            return this
        }

        def Builder withJaxbElements() {
            obj.jaxbElements = true
            return this
        }

        def Builder withJsonb(boolean jsonb) {
            obj.jsonb = jsonb
            return this
        }

        def Builder withJsonb() {
            obj.jsonb = true
            return this
        }

        def Builder withJackson(boolean jackson) {
            obj.jackson = jackson
            return this
        }

        def Builder withJackson() {
            obj.jackson = true
            return this
        }

        def Builder withCopyrightHeader(String header) {
            obj.copyrightHeader = header
            return this
        }

        /**
         * Sets the model namespaces that contain the built-in constraints.
         *
         * @param namespaces Comma separated list of namespaces. Uses the default if {@literal null} or empty.
         */
        def Builder withBuiltinConstraintNamespaces(String namespaces) {
            obj.builtinConstraintNamespaces = parseNamespaces(namespaces)
            return this
        }

        def GenerateOptions create() {
            val GenerateOptions options = obj
            obj = GenerateOptions.empty()
            return options
        }

    }

}

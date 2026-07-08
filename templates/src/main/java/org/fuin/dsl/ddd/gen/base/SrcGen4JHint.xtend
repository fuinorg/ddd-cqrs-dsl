package org.fuin.dsl.ddd.gen.base

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Hint
import org.fuin.dsl.cqrs.cqrsDsl.JSON
import org.fuin.dsl.cqrs.cqrsDsl.JsonArray
import org.fuin.dsl.cqrs.cqrsDsl.JsonObject
import org.fuin.dsl.cqrs.cqrsDsl.JsonString

/**
 * Strongly typed view of a "SrcGen4J" code generation {@link Hint} (see the "SrcGen4J" hint in
 * "dsl-examples/22-hint.cqrs"). It walks the parsed EMF JSON model of the hint and maps it to plain
 * typed objects.
 * <p>
 * Expected structure:
 * <pre>
 * hint SrcGen4J {
 *     "package": "...",
 *     "types": [
 *         {
 *             "name":    "org.fuin.dsl.cqrs.cqrsDsl.ValueObject",
 *             "module":  "shared",
 *             "group":   "domain",
 *             "artifacts": [
 *                 { "artifactFactory": "org.fuin.dsl.ddd.gen.valueobject.AbstractValueObject", "folder": "genJava" },
 *                 ...
 *             ]
 *         }
 *     ]
 * }
 * </pre>
 */
class SrcGen4JHint {

    val String packagePattern

    val List<SrcGen4JType> types

    /**
     * Constructor with all data.
     *
     * @param packagePattern Package name pattern (value of the "package" key) - May be <code>null</code>.
     * @param types Type configurations (value of the "types" array) - Never <code>null</code>.
     */
    new(String packagePattern, List<SrcGen4JType> types) {
        this.packagePattern = packagePattern
        this.types = types
    }

    /**
     * Returns the package name pattern.
     *
     * @return Value of the "package" key or <code>null</code> if not set.
     */
    def getPackagePattern() {
        packagePattern
    }

    /**
     * Returns the type configurations.
     *
     * @return Value of the "types" array - Never <code>null</code>, but may be empty.
     */
    def getTypes() {
        types
    }

    /**
     * Parses the JSON of the given hint into a strongly typed object.
     *
     * @param hint Hint to parse - Cannot be <code>null</code>.
     *
     * @return Strongly typed variant of the hint's JSON.
     */
    def static SrcGen4JHint parse(Hint hint) {
        parse(hint.json)
    }

    /**
     * Parses the given JSON value (expected to be an object) into a strongly typed object.
     *
     * @param json JSON object to parse - Cannot be <code>null</code>.
     *
     * @return Strongly typed variant of the JSON.
     */
    def static SrcGen4JHint parse(JSON json) {
        val root = json.asObject
        new SrcGen4JHint(
            root.stringValue("package"),
            root.arrayValues("types").map[asObject.parseType]
        )
    }

    def private static SrcGen4JType parseType(JsonObject obj) {
        new SrcGen4JType(
            obj.stringValue("name"),
            obj.stringValue("module"),
            obj.stringValue("group"),
            obj.arrayValues("artifacts").map[asObject.parseArtifact]
        )
    }

    def private static SrcGen4JArtifact parseArtifact(JsonObject obj) {
        new SrcGen4JArtifact(
            obj.stringValue("artifactFactory"),
            obj.stringValue("folder")
        )
    }

    // ---- JSON navigation helpers (used as extension methods) ----

    def private static JsonObject asObject(JSON json) {
        if (json instanceof JsonObject) {
            json
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON object, but was: " + json?.eClass?.name)
        }
    }

    def private static JSON member(JsonObject obj, String key) {
        obj.members.findFirst[member|member.key == key]?.value
    }

    def private static String stringValue(JsonObject obj, String key) {
        val value = obj.member(key)
        if (value === null) {
            null
        } else if (value instanceof JsonString) {
            value.value
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON string for key '" + key + "', but was: " + value.eClass.name)
        }
    }

    def private static List<JSON> arrayValues(JsonObject obj, String key) {
        val value = obj.member(key)
        if (value === null) {
            newArrayList
        } else if (value instanceof JsonArray) {
            value.elements
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON array for key '" + key + "', but was: " + value.eClass.name)
        }
    }

}

/**
 * A single entry of the "types" array: maps a DSL model type name to the module/group it belongs
 * to and the list of artifacts that should be generated for it.
 */
class SrcGen4JType {

    val String name

    val String module

    val String group

    val List<SrcGen4JArtifact> artifacts

    /**
     * Constructor with all data.
     *
     * @param name Model type name (value of the "name" key), e.g. "ValueObject" - May be <code>null</code>.
     * @param module Module the type belongs to (value of the "module" key) - May be <code>null</code>.
     * @param group Group the type belongs to (value of the "group" key) - May be <code>null</code>.
     * @param artifacts Artifacts to generate (value of the "artifacts" array) - Never <code>null</code>.
     */
    new(String name, String module, String group, List<SrcGen4JArtifact> artifacts) {
        this.name = name
        this.module = module
        this.group = group
        this.artifacts = artifacts
    }

    /** @return Model type name (value of the "name" key) or <code>null</code> if not set. */
    def getName() {
        name
    }

    /** @return Module the type belongs to (value of the "module" key) or <code>null</code> if not set. */
    def getModule() {
        module
    }

    /** @return Group the type belongs to (value of the "group" key) or <code>null</code> if not set. */
    def getGroup() {
        group
    }

    /** @return Artifacts to generate - Never <code>null</code>, but may be empty. */
    def getArtifacts() {
        artifacts
    }

}

/**
 * A single entry of the "artifacts" array: the name of an artifact factory class and the target
 * folder its output should be written to.
 */
class SrcGen4JArtifact {

    val String artifactFactory

    val String folder

    /**
     * Constructor with all data.
     *
     * @param artifactFactory Artifact factory class name (value of the "artifactFactory" key) - May be <code>null</code>.
     * @param folder Target folder (value of the "folder" key) - May be <code>null</code>.
     */
    new(String artifactFactory, String folder) {
        this.artifactFactory = artifactFactory
        this.folder = folder
    }

    /** @return Artifact factory class name (value of the "artifactFactory" key) or <code>null</code> if not set. */
    def getArtifactFactory() {
        artifactFactory
    }

    /** @return Target folder (value of the "folder" key) or <code>null</code> if not set. */
    def getFolder() {
        folder
    }

}

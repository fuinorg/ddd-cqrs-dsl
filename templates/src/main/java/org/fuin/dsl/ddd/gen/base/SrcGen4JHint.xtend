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
 *     "constraintMappings": [
 *         "org.fuin.dsl.cqrs.common.constraints.Length(min,max)=jakarta.validation.constraints.Size(min=min,max=max)",
 *         ...
 *     ],
 *     "types": [
 *         {
 *             "name":    "org.fuin.dsl.cqrs.cqrsDsl.ValueObject",
 *             "module":  "shared",
 *             "group":   "domain",
 *             "artifacts": [
 *                 { "artifactFactory": "org.fuin.dsl.ddd.gen.valueobject.AbstractValueObject", "folder": "genJava" },
 *                 { "artifactFactory": "org.fuin.dsl.ddd.gen.valueobject.FinalValueObject", "folder": "mainJava",
 *                   "module": "api", "group": "dto" },
 *                 ...
 *             ]
 *         }
 *     ]
 * }
 * </pre>
 * The "constraintMappings" belong to the model that declares the constraints, so a model that only uses them
 * (as a dependency) maps them in exactly the same way without repeating anything.
 */
class SrcGen4JHint {

    val String packagePattern

    val List<String> constraintMappings

    val List<SrcGen4JType> types

    /**
     * Constructor with all data.
     *
     * @param packagePattern Package name pattern (value of the "package" key) - May be <code>null</code>.
     * @param constraintMappings Mappings of DSL constraints to Java validation annotations (value of the
     *            "constraintMappings" array) - Never <code>null</code>.
     * @param types Type configurations (value of the "types" array) - Never <code>null</code>.
     */
    new(String packagePattern, List<String> constraintMappings, List<SrcGen4JType> types) {
        this.packagePattern = packagePattern
        this.constraintMappings = constraintMappings
        this.types = types
    }

    /**
     * Returns the mappings of DSL constraints to Java validation annotations.
     *
     * @return Value of the "constraintMappings" array - Never <code>null</code>, but may be empty.
     */
    def getConstraintMappings() {
        constraintMappings
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
            root.arrayValues("constraintMappings").map[asStringValue],
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
            obj.stringValue("folder"),
            obj.stringValue("module"),
            obj.stringValue("group")
        )
    }

    /** Classpath location of the default preset used when a model has no "SrcGen4J" hint. */
    public static val String DEFAULT_RESOURCE = "/srcgen4j-default.json"

    /**
     * Loads the "srcgen4j-default.json" preset from the classpath. This is the base configuration used
     * when a model has no "SrcGen4J" hint and the values a model's hint is merged on top of.
     *
     * @return Parsed preset, or an empty hint if the resource is not on the classpath.
     */
    def static SrcGen4JHint loadDefault() {
        val stream = typeof(SrcGen4JHint).getResourceAsStream(DEFAULT_RESOURCE)
        if (stream === null) {
            return new SrcGen4JHint(null, newArrayList, newArrayList)
        }
        try {
            val reader = jakarta.json.Json.createReader(stream)
            try {
                return parseJson(reader.readObject)
            } finally {
                reader.close
            }
        } finally {
            stream.close
        }
    }

    /**
     * Merges a model's "SrcGen4J" hint onto the default preset: the model's "package" wins when set and
     * its type entries take precedence (they are matched before the preset's), while entries only present
     * in the preset still apply. This lets a model overwrite individual values from
     * "srcgen4j-default.json" without repeating the whole configuration.
     *
     * @param preset Base configuration (may be <code>null</code>).
     * @param overrides Model hint whose values win (may be <code>null</code>).
     *
     * @return Merged hint (may be <code>null</code> if both arguments are <code>null</code>).
     */
    def static SrcGen4JHint merge(SrcGen4JHint preset, SrcGen4JHint overrides) {
        if (overrides === null) {
            return preset
        }
        if (preset === null) {
            return overrides
        }
        val types = <SrcGen4JType>newArrayList
        for (type : overrides.types) {
            if (type.artifacts.nullOrEmpty) {
                // A model type that overrides only "module" / "group" (no "artifacts") still applies to
                // the preset's artifacts for that type: inherit them so the override's module/group win
                // while the artifacts and their target folders keep coming from the preset.
                val inherited = preset.types.filter[name == type.name].map[artifacts].flatten.toList
                types.add(new SrcGen4JType(type.name, type.module, type.group, inherited))
            } else {
                types.add(type)
            }
        }
        types.addAll(preset.types)
        // A mapping of the model wins over one of the preset for the same constraint, so it has to come last
        val mappings = <String>newArrayList
        mappings.addAll(preset.constraintMappings)
        mappings.addAll(overrides.constraintMappings)
        return new SrcGen4JHint(overrides.packagePattern ?: preset.packagePattern, mappings, types)
    }

    // ---- Raw JSON (JSON-P) parsing of the default preset resource ----

    def private static SrcGen4JHint parseJson(jakarta.json.JsonObject root) {
        new SrcGen4JHint(
            root.jsonString("package"),
            root.jsonArray("constraintMappings").map[(it as jakarta.json.JsonString).string],
            root.jsonArray("types").map[asJsonObject.parseJsonType]
        )
    }

    def private static SrcGen4JType parseJsonType(jakarta.json.JsonObject obj) {
        new SrcGen4JType(
            obj.jsonString("name"),
            obj.jsonString("module"),
            obj.jsonString("group"),
            obj.jsonArray("artifacts").map[asJsonObject.parseJsonArtifact]
        )
    }

    def private static SrcGen4JArtifact parseJsonArtifact(jakarta.json.JsonObject obj) {
        new SrcGen4JArtifact(
            obj.jsonString("artifactFactory"),
            obj.jsonString("folder"),
            obj.jsonString("module"),
            obj.jsonString("group")
        )
    }

    def private static String jsonString(jakarta.json.JsonObject obj, String key) {
        if (obj.containsKey(key) && obj.get(key).valueType === jakarta.json.JsonValue.ValueType.STRING) {
            obj.getString(key)
        } else {
            null
        }
    }

    def private static List<jakarta.json.JsonValue> jsonArray(jakarta.json.JsonObject obj, String key) {
        if (obj.containsKey(key) && obj.get(key).valueType === jakarta.json.JsonValue.ValueType.ARRAY) {
            obj.getJsonArray(key)
        } else {
            <jakarta.json.JsonValue>newArrayList
        }
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

    def private static String asStringValue(JSON json) {
        if (json instanceof JsonString) {
            json.value
        } else {
            throw new IllegalArgumentException("Expected a JSON string, but was: " + json?.eClass?.name)
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
 * folder its output should be written to. The optional "module" and "group" override the values of the
 * enclosing {@link SrcGen4JType} for this artifact only; when they are not set the type's values are
 * used as the default.
 */
class SrcGen4JArtifact {

    val String artifactFactory

    val String folder

    val String module

    val String group

    /**
     * Constructor with all data.
     *
     * @param artifactFactory Artifact factory class name (value of the "artifactFactory" key) - May be <code>null</code>.
     * @param folder Target folder (value of the "folder" key) - May be <code>null</code>.
     * @param module Module override (value of the "module" key) - <code>null</code> inherits the type's module.
     * @param group Group override (value of the "group" key) - <code>null</code> inherits the type's group.
     */
    new(String artifactFactory, String folder, String module, String group) {
        this.artifactFactory = artifactFactory
        this.folder = folder
        this.module = module
        this.group = group
    }

    /** @return Artifact factory class name (value of the "artifactFactory" key) or <code>null</code> if not set. */
    def getArtifactFactory() {
        artifactFactory
    }

    /** @return Target folder (value of the "folder" key) or <code>null</code> if not set. */
    def getFolder() {
        folder
    }

    /** @return Module override (value of the "module" key) or <code>null</code> to inherit the type's module. */
    def getModule() {
        module
    }

    /** @return Group override (value of the "group" key) or <code>null</code> to inherit the type's group. */
    def getGroup() {
        group
    }

}

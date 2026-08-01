package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.Collections
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Constraint
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance
import org.fuin.dsl.ddd.gen.script.CqrsScripts

import static extension org.fuin.dsl.cqrs.extensions.CqrsLiteralExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Maps constraints from the DSL to Java validation annotations. A mapping has the form "DSL=JAVA":
 * <ul>
 * <li>DSL is "context.module.Name(parameter names)".
 * The parameter list may be omitted if the constraint has no parameters.</li>
 * <li>JAVA is the fully qualified name of one or more annotations, each of them with optional parameters in
 * the form "java parameter name = DSL parameter name". Multiple annotations are separated by a comma.</li>
 * </ul>
 * Example:
 *
 * <pre>
 * org.fuin.dsl.cqrs.common.constraints.NotEmpty=jakarta.validation.constraints.NotEmpty
 * org.fuin.dsl.cqrs.common.constraints.Length(min,max)=jakarta.validation.constraints.Size(min=min,max=max)
 * org.fuin.dsl.cqrs.common.constraints.ValueRange(min,max)=jakarta.validation.constraints.Min(value=min),jakarta.validation.constraints.Max(value=max)
 * </pre>
 *
 * A constraint without a mapping is generated as an annotation of its own by the code generator.
 */
class ConstraintMappings {

    val Map<String, Mapping> mappings

    private new(Map<String, Mapping> mappings) {
        this.mappings = mappings
    }

    /**
     * Returns the mappings that apply to a constraint. They are taken from the "SrcGen4J" hint of the model
     * that declares the constraint (merged onto the "srcgen4j-default.json" preset), so a model that only
     * uses the constraint as a dependency maps it in exactly the same way without repeating anything.
     *
     * @param constr Constraint to return the mappings for.
     *
     * @return Mappings, never {@literal null}, but may be empty.
     */
    static def ConstraintMappings of(Constraint constr) {
        return parse(CqrsScripts.constraintMappings(constr))
    }

    /**
     * Parses a list of mappings.
     *
     * @param mappings Single mappings, each of them in the form "DSL=JAVA".
     *
     * @return Mappings, never {@literal null}, but may be empty.
     */
    static def ConstraintMappings parse(List<String> mappings) {
        val Map<String, Mapping> map = new LinkedHashMap<String, Mapping>()
        if (mappings !== null) {
            for (String entry : mappings) {
                if (entry !== null && !entry.trim.empty) {
                    val Mapping mapping = parseMapping(entry.trim)
                    map.put(mapping.dslName, mapping)
                }
            }
        }
        return new ConstraintMappings(map)
    }


    /**
     * Determines if there is a mapping for the given constraint.
     *
     * @param constr Constraint to find a mapping for.
     *
     * @return {@code true} if the constraint is mapped to Java validation annotations.
     */
    def boolean mapped(Constraint constr) {
        return mappings.containsKey(constr.dslName)
    }

    /**
     * Returns the fully qualified names of the Java annotations the given constraint is mapped to.
     *
     * @param constr Mapped constraint.
     *
     * @return Names to import, never {@literal null}.
     */
    def List<String> imports(Constraint constr) {
        return mapping(constr).annotations.map[name].toList
    }

    /**
     * Creates the source code of the Java annotations a constraint instance is mapped to. The values are
     * taken from the parameters of the instance. Multiple annotations are separated by a line break.
     *
     * @param ci Instance of a mapped constraint.
     *
     * @return Source code like "@Size(min=3, max=320)".
     */
    def String annotations(ConstraintInstance ci) {
        val Mapping mapping = mapping(ci.constraint)
        val List<String> result = new ArrayList<String>()
        for (JavaAnnotation annotation : mapping.annotations) {
            result.add(annotation.asSource(mapping, ci))
        }
        return result.join("\n")
    }

    private def Mapping mapping(Constraint constr) {
        val Mapping mapping = mappings.get(constr.dslName)
        if (mapping === null) {
            throw new IllegalStateException("There is no mapping for constraint '" + constr.dslName + "'")
        }
        return mapping
    }

    /**
     * Returns the unique name of a constraint in the DSL.
     *
     * @return "context.module.Name".
     */
    private static def String dslName(Constraint constr) {
        return constr.context.name + "." + constr.module.name + "." + constr.name
    }

    private static def Mapping parseMapping(String entry) {
        val int idx = entry.indexOf("=")
        if (idx < 1) {
            throw new IllegalArgumentException(
                "Expected a mapping in the form 'DSL=JAVA', but was: '" + entry + "'")
        }
        val String dsl = entry.substring(0, idx)
        val String java = entry.substring(idx + 1)
        val List<JavaAnnotation> annotations = new ArrayList<JavaAnnotation>()
        for (String str : splitTopLevel(java)) {
            annotations.add(parseAnnotation(str, entry))
        }
        if (annotations.empty) {
            throw new IllegalArgumentException("The mapping has no Java annotation: '" + entry + "'")
        }
        return new Mapping(nameOf(dsl), paramsOf(dsl, entry), annotations)
    }

    private static def JavaAnnotation parseAnnotation(String str, String entry) {
        val String name = nameOf(str)
        val Map<String, String> params = new LinkedHashMap<String, String>()
        for (String param : paramsOf(str, entry)) {
            val int idx = param.indexOf("=")
            if (idx < 1) {
                throw new IllegalArgumentException("Expected a Java annotation parameter in the form " +
                    "'javaName=dslName', but was '" + param + "' in mapping: '" + entry + "'")
            }
            params.put(param.substring(0, idx).trim, param.substring(idx + 1).trim)
        }
        return new JavaAnnotation(name, params)
    }

    /** Returns the part before the optional parameter list. */
    private static def String nameOf(String str) {
        val int idx = str.indexOf("(")
        if (idx < 0) {
            return str.trim
        }
        return str.substring(0, idx).trim
    }

    /** Returns the comma separated content of the optional parameter list. */
    private static def List<String> paramsOf(String str, String entry) {
        val int open = str.indexOf("(")
        if (open < 0) {
            return Collections.emptyList
        }
        if (!str.trim.endsWith(")")) {
            throw new IllegalArgumentException("The parameter list of '" + str + "' is not closed with a " +
                "')' in mapping: '" + entry + "'")
        }
        val String content = str.trim.substring(open + 1, str.trim.length - 1)
        if (content.trim.empty) {
            return Collections.emptyList
        }
        return content.split(",").map[trim].filter[!empty].toList
    }

    /**
     * Splits a list of Java annotations at the commas that are not inside a parameter list. This is what makes
     * "Size(min=min,max=max)" a single annotation with two parameters and "Min(value=min),Max(value=max)" two
     * annotations.
     */
    private static def List<String> splitTopLevel(String str) {
        val List<String> result = new ArrayList<String>()
        var int depth = 0
        var int start = 0
        for (var int i = 0; i < str.length; i++) {
            val char ch = str.charAt(i)
            if (ch == "(".charAt(0)) {
                depth = depth + 1
            } else if (ch == ")".charAt(0)) {
                depth = depth - 1
            } else if (ch == ",".charAt(0) && depth == 0) {
                result.add(str.substring(start, i))
                start = i + 1
            }
        }
        result.add(str.substring(start))
        return result.map[trim].filter[!empty].toList
    }

    /** A single "DSL=JAVA" mapping. */
    private static class Mapping {

        val String dslName
        val List<String> dslParams
        val List<JavaAnnotation> annotations

        new(String dslName, List<String> dslParams, List<JavaAnnotation> annotations) {
            this.dslName = dslName
            this.dslParams = dslParams
            this.annotations = annotations
        }

        /**
         * Returns the value of a DSL parameter. The parameters of an instance are positional, so the name is
         * resolved using the parameter list declared by the mapping.
         */
        def String value(String dslParam, ConstraintInstance ci) {
            val int idx = dslParams.indexOf(dslParam)
            if (idx < 0) {
                throw new IllegalStateException("The mapping of constraint '" + dslName +
                    "' has no parameter '" + dslParam + "': " + dslParams)
            }
            if (ci.params.size !== dslParams.size) {
                throw new IllegalStateException("The mapping of constraint '" + dslName + "' declares " +
                    dslParams.size + " parameter(s) " + dslParams + ", but the constraint is used with " +
                    ci.params.size + " parameter(s)")
            }
            return ci.params.get(idx).str
        }

    }

    /** A single Java annotation of the "JAVA" part of a mapping. */
    private static class JavaAnnotation {

        val String name
        val Map<String, String> params

        new(String name, Map<String, String> params) {
            this.name = name
            this.params = params
        }

        def String getName() {
            return name
        }

        def String asSource(Mapping mapping, ConstraintInstance ci) {
            val String simpleName = name.substring(name.lastIndexOf(".") + 1)
            if (params.empty) {
                return "@" + simpleName
            }
            val List<String> assignments = new ArrayList<String>()
            for (Map.Entry<String, String> param : params.entrySet) {
                assignments.add(param.key + "=" + mapping.value(param.value, ci))
            }
            return "@" + simpleName + "(" + assignments.join(", ") + ")"
        }

    }

}

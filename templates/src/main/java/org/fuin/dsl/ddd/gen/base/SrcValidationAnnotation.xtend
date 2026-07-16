package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Constraint
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance
import org.fuin.dsl.cqrs.cqrsDsl.Literal
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsLiteralExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*

/**
 * Creates source code for a validation annotation. A constraint that is mapped to one or more Java
 * validation annotations (see {@link GenerateOptions#KEY_CONSTRAINT_MAPPINGS}) is created from that mapping.
 * In all other cases the annotation generated for the constraint itself is used.
 */
class SrcValidationAnnotation implements CodeSnippet {

    var ConstraintInstance ci
    var Constraint constraint
    var List<Attribute> vars
    var List<Literal> params
    var GenerateOptions options

    new(CodeSnippetContext ctx, GenerateOptions options, ConstraintInstance ci) {
        this.ci = ci;
        constraint = ci.constraint;
        vars = constraint.attributes;
        params = ci.params;
        this.options = options;

        if (options.constraintMappings.mapped(constraint)) {
            for (String name : options.constraintMappings.imports(constraint)) {
                ctx.requiresReference(name)
            }
        } else {
            ctx.requiresReference(constraint.uniqueName)
            if (vars !== null) {
                for (Attribute v : vars) {
                    ctx.requiresReference(v.type.uniqueName)
                }
            }
        }
    }

    override toString() {
        if (options.constraintMappings.mapped(constraint)) {
            return options.constraintMappings.annotations(ci)
        } else {
            if (vars.size !== params.size) {
                throw new IllegalStateException(
                    "Number of variables and parameters of constraint '" + constraint.uniqueName +
                        "' do not match: vars=" + vars + ", params=" + params);
            }
            if (vars.size == 0) {
                return '''@«constraint.name»''';
            } else if (vars.size == 1) {
                return '''@«constraint.name»(«params.lastOrNull.str»)''';
            } else if (vars.size() > 1) {
                var List<String> list = new ArrayList<String>();
                var int i = 0;
                do {
                    var String name = vars.get(i).name;
                    var String value = params.get(i).str;
                    list.add(name + " = " + value);
                    i = i + 1;
                } while (i < vars.size());
                return '''@«constraint.name»(«FOR str : list SEPARATOR ', '»«str»«ENDFOR»)''';
            }
        }

    }

}

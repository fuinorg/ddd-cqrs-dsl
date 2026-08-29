package org.fuin.dsl.ddd.gen.rule

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Exception
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcGetters
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.SrcParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcParamsDecl
import org.fuin.dsl.ddd.gen.base.SrcVarsDecl
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * The body of a class that verifies one rule: the values it decides from, the condition, and the
 * refusal it throws when the condition does not hold.
 *
 * <p>Shared by the two things that produce one. A <code>business-rule</code> states its attributes and
 * its condition; a <code>key</code> states neither and derives both. Rendering them through one
 * template is what stops a derived rule quietly becoming a different kind of object from a declared
 * one - the constructor a usage calls, the getters a test reads and the refusal a client catches are
 * the same either way.</p>
 */
class SrcRuleVerifierClass implements CodeSnippet {

    val CodeSnippetContext ctx
    val String className
    val CharSequence doc
    val List<Attribute> attributes
    val Exception exception
    val String condition

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context to add imports to.
     * @param className Name of the generated class.
     * @param doc Documentation of the rule, as a single sentence.
     * @param attributes Values the rule decides from, in constructor order.
     * @param exception Refusal thrown when the condition does not hold.
     * @param condition Java expression that is true when the rule is satisfied.
     */
    new(CodeSnippetContext ctx, String className, CharSequence doc, List<Attribute> attributes,
        Exception exception, String condition) {
        this.ctx = ctx
        this.className = className
        this.doc = doc
        this.attributes = attributes
        this.exception = exception
        this.condition = condition
    }

    override toString() {
        val String arguments = exceptionArguments()
        '''
        /**
         * «doc»
         */
        public final class «className» implements BusinessRule {

            «new SrcVarsDecl(ctx, "private", GenerateOptions.empty(), attributes)»
            «new SrcJavaDocMethod(ctx, "Constructor with the values this rule decides from.", null, attributes.asParameters, null)»
            public «className»(«new SrcParamsDecl(ctx, GenerateOptions.empty(), attributes.asParameters)») {
                «new SrcParamsAssignment(ctx, attributes.asParameters)»
            }

            @Override
            public void verify() throws «exception.name» {
                if (!(«condition»)) {
                    throw new «exception.name»(«arguments»);
                }
            }

            «new SrcGetters(ctx, GenerateOptions.empty(), "public", attributes)»
        }
        '''
    }

    /**
     * The values the refusal is constructed from, taken from the rule's own attributes by name.
     *
     * <p>An exception declares what it needs in order to name the thing it refused, and a rule declares
     * what it decides from; the overlap is by name, which is why a rule commonly carries an attribute
     * that plays no part in its condition. An exception asking for something the rule does not hold is
     * a model error rather than something to fill with a placeholder - it would compile as a missing
     * argument in generated code, which is a poor place to read the problem.
     */
    def private String exceptionArguments() throws GenerateException {
        val names = attributes.map[name].toList
        val args = newArrayList
        for (attribute : exception.attributes.nullSafe) {
            if (!names.contains(attribute.name)) {
                throw new GenerateException(exception.name + " needs '" + attribute.name
                    + "', which " + className + " does not hold - a rule can only name what it holds")
            }
            args.add(attribute.name)
        }
        return args.join(", ")
    }

}

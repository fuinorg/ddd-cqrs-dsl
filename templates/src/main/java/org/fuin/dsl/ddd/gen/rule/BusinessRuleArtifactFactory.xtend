package org.fuin.dsl.ddd.gen.rule

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcGetters
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.SrcParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcParamsDecl
import org.fuin.dsl.ddd.gen.base.SrcVarsDecl
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates the class that verifies one <code>business-rule</code>.
 *
 * <p><b>The rule decides; the caller fetches.</b> A rule never consults a repository, a service or the
 * aggregate: it is constructed with values and answers one question about them. That is what makes the
 * rules needing outside data ordinary - a rule about a name already being taken is not handed a
 * repository to search, it is handed the <code>Boolean</code> answer - and it is what makes a rule a
 * unit-testable object rather than a passage inside an aggregate method.
 *
 * <p><b>A rule with no <code>requires</code> is not generated at all.</b> Some conditions the language
 * deliberately cannot express, and for those the class is written by hand. Emitting a stub instead
 * would be worse than emitting nothing: srcgen4j's <code>override=false</code> stops a file being
 * overwritten but not being created, so a stub for a newly declared rule would appear on its own and
 * the build would stay green with the rule unenforced. Referencing a class nobody has written does not
 * compile, and the generated constructor call says exactly what to provide.
 */
class BusinessRuleArtifactFactory extends AbstractSource<BusinessRule> {

    override getModelType() {
        typeof(BusinessRule)
    }

    override getTypeKey() {
        TypeKeys.JAVA_BUSINESS_RULE
    }

    override create(BusinessRule rule, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val className = rule.name
        val pkg = rule.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java"

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(rule), fqn)

        if (rule.requires === null) {
            // A custom rule. The reference above still stands, so whatever wants to construct it can be
            // generated; the class itself is the author's.
            return null
        }

        if (preparationRun) {
            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(rule)
        ctx.addReferences(rule)

        return List.of(newArtifact(filename,
            create(ctx, rule, pkg, className).toString.getBytes("UTF-8"), rule))
    }

    def addImports(CodeSnippetContext ctx, BusinessRule rule) {
        ctx.requiresImport("org.fuin.dsl.cqrs.common.rules.BusinessRule")
        if (!rule.attributes.nullSafe.filter[optional === null].empty) {
            ctx.requiresImport("org.fuin.objects4j.common.Contract")
        }
    }

    def addReferences(CodeSnippetContext ctx, BusinessRule rule) {
        ctx.requiresReference(TypeKeys.refKey(rule.exception))
        for (attribute : rule.attributes.nullSafe) {
            ctx.requiresReference(TypeKeys.refKey(attribute.type))
        }
    }

    def create(SimpleCodeSnippetContext ctx, BusinessRule rule, String pkg, String className) {

        // Rendered before the template, because rendering is what decides whether "Objects" is imported.
        val String condition = new SrcRuleCondition(ctx, rule.requires).toString
        val String arguments = exceptionArguments(rule)

        val String src = '''
            /**
             * «rule.doc.text»
             */
            public final class «className» implements BusinessRule {

                «new SrcVarsDecl(ctx, "private", GenerateOptions.empty(), rule.attributes.nullSafe.toList)»
                «new SrcJavaDocMethod(ctx, "Constructor with the values this rule decides from.", null, rule.attributes.nullSafe.toList.asParameters, null)»
                public «className»(«new SrcParamsDecl(ctx, GenerateOptions.empty(), rule.attributes.nullSafe.toList.asParameters)») {
                    «new SrcParamsAssignment(ctx, rule.attributes.nullSafe.toList.asParameters)»
                }

                @Override
                public void verify() throws «rule.exception.name» {
                    if (!(«condition»)) {
                        throw new «rule.exception.name»(«arguments»);
                    }
                }

                «new SrcGetters(ctx, GenerateOptions.empty(), "public", rule.attributes.nullSafe.toList)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
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
    def private String exceptionArguments(BusinessRule rule) throws GenerateException {
        val names = rule.attributes.nullSafe.map[name].toList
        val args = newArrayList
        for (attribute : rule.exception.attributes.nullSafe) {
            if (!names.contains(attribute.name)) {
                throw new GenerateException(rule.exception.name + " needs '" + attribute.name
                    + "', which " + rule.name + " does not declare - a rule can only name what it holds")
            }
            args.add(attribute.name)
        }
        return args.join(", ")
    }

}

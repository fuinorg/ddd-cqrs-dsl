package org.fuin.dsl.ddd.gen.valueobject

import java.io.Serializable
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcConstructorsWithParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcGetters
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcVarsDecl
import org.fuin.dsl.ddd.gen.base.SrcVoBaseOptionalExtends
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractVOExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.TypeExtensions.*
import java.util.List

class AbstractValueObjectArtifactFactory extends AbstractSource<ValueObject> {

    override getModelType() {
        typeof(ValueObject)
    }

    override create(ValueObject valueObject, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = valueObject.abstractName
        val pkg = valueObject.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(valueObject.uniqueAbstractName, fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports
        ctx.addReferences(valueObject)

        return List.of(newArtifact(filename,
            create(ctx, valueObject, pkg, className).toString().getBytes("UTF-8"), valueObject));
    }

    def addImports(CodeSnippetContext ctx) {
        ctx.requiresImport("java.io.Serial")
        ctx.requiresImport(org.fuin.objects4j.common.ValueObject.name)
        ctx.requiresImport(Serializable.name)
    }

    def addReferences(CodeSnippetContext ctx, ValueObject valueObject) {
        // Not needed
    }

    /**
     * Returns the "ValueObjectWithBaseType<Base>, " interface prefix for a base-typed value object
     * that has NO dedicated AbstractXValueObject base class (only String/UUID/Integer/Long have one -
     * see SrcVoBaseOptionalExtends). Those base classes already declare asBaseType(); for any other
     * base type (e.g. BigDecimal) the interface must be implemented so the final class's
     * @Override asBaseType() overrides a declared method. Returns "" when a base class covers it or
     * the value object has no base.
     */
    def String baseTypeInterface(CodeSnippetContext ctx, ValueObject vo) {
        if (vo.base === null) {
            return ""
        }
        val bn = vo.base.name
        if (bn == "String" || bn == "UUID" || bn == "Integer" || bn == "Long") {
            return ""
        }
        ctx.requiresImport("org.fuin.objects4j.common.ValueObjectWithBaseType")
        return "ValueObjectWithBaseType<" + vo.baseType.simpleName(ctx) + ">, "
    }

    def create(SimpleCodeSnippetContext ctx, ValueObject vo, String pkg, String className) {
        val GenerateOptions localOptions = new GenerateOptions.Builder()
            .withJaxb(vo.base === null && options.jaxb)
            .withJaxbElements(options.jaxbElements)
            .withJsonb((vo.base === null && options.jsonb))
            .withJackson((vo.base === null && options.jackson))
            .create();
        val String src = ''' 
            «new SrcJavaDocType(vo)»
            public abstract class «className» «new SrcVoBaseOptionalExtends(ctx, vo.base)»implements «baseTypeInterface(ctx, vo)»ValueObject, Serializable {
            
                @Serial
                private static final long serialVersionUID = 1000L;
                
                «new SrcVarsDecl(ctx, "private", localOptions, vo)»
                «new SrcConstructorsWithParamsAssignment(ctx, GenerateOptions.empty(), vo, true)»
                «new SrcGetters(ctx, GenerateOptions.empty(), "public final", vo.attributes)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}

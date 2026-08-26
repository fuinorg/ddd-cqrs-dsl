package org.fuin.dsl.ddd.gen.except

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
import org.fuin.dsl.cqrs.cqrsDsl.Exception
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcGetters
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.SrcKeyValueReplace
import org.fuin.dsl.ddd.gen.base.SrcParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcParamsDecl
import org.fuin.dsl.ddd.gen.base.SrcVarsDecl
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import java.util.List

class ExceptionArtifactFactory extends AbstractSource<Exception> {

    override getModelType() {
        typeof(Exception)
    }

    override getTypeKey() {
        TypeKeys.JAVA_EXCEPTION
    }

    override create(Exception ex, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = ex.getName()
        val pkg = ex.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(ex), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(ex)
        ctx.addReferences(ex)

        return List.of(newArtifact(filename,
            create(ctx, ex, pkg, className).toString().getBytes("UTF-8"), ex));
    }

    def addImports(CodeSnippetContext ctx, Exception ex) {
        ctx.requiresImport("java.io.Serial")
        if (ex.namedByARule) {
            ctx.requiresImport("org.fuin.dsl.cqrs.common.rules." + ex.baseClass)
        } else if (ex.cid > 0) {
            ctx.requiresImport("org.fuin.objects4j.common.UniquelyNumberedException")
        }
        if (!ex.attributes.empty) {
            ctx.requiresImport("java.util.Objects")
        }
    }

    def addReferences(CodeSnippetContext ctx, Exception ex) {
        for (v : ex.attributes) {
            ctx.requiresReference(TypeKeys.refKey(v.type))
        }
    }

    def create(SimpleCodeSnippetContext ctx, Exception ex, String pkg, String className) {
        val String src = ''' 
            /**
             * «ex.doc.text»
             */
            public final class «className» extends «_uniquelyNumberedException(ex)» {
            
                @Serial
                private static final long serialVersionUID = 1000L;
            
                «new SrcVarsDecl(ctx, "private", GenerateOptions.empty(), ex)»
                «new SrcJavaDocMethod(ctx, "Constructs a new instance of the exception.", null, ex.attributes.asParameters, null)»
                public «ex.name»(«new SrcParamsDecl(ctx, GenerateOptions.empty(), ex.attributes.asParameters)») {
                    super(«IF ex.cid > 0»«ex.cid», «ENDIF»«IF !ex.attributes.empty»Objects.requireNonNull(«new SrcKeyValueReplace(ctx, ex.message, ex.attributes.asNames)»)«ELSE»«new SrcKeyValueReplace(ctx, ex.message, ex.attributes.asNames)»«ENDIF»);
                    «new SrcParamsAssignment(ctx, ex.attributes.asParameters)»
                }
            
                «new SrcGetters(ctx, GenerateOptions.empty(), "public final", ex.attributes)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

    def _uniquelyNumberedException(Exception ex) {
        return ex.baseClass
    }

    /**
     * What the refusal extends.
     *
     * <p>A refusal a business rule names extends the rules' own base, because the generated rule class
     * implements <code>BusinessRule</code> and its <code>verify()</code> may only throw what that
     * interface declares. It also gives a caller one type to catch for "a rule refused", which is the
     * question a command handler asks. Everything else keeps the plain base it always had.
     */
    def private String baseClass(Exception ex) {
        if (ex.namedByARule) {
            return if (ex.cid > 0) "UniquelyNumberedBusinessRuleViolationException"
                else "BusinessRuleViolationException"
        }
        return if(ex.cid > 0) "UniquelyNumberedException" else "Exception"
    }

    /**
     * Whether any business rule in the model names this exception.
     *
     * <p>Searched across the whole resource set rather than the exception's own resource: a context is
     * commonly split over a public file declaring the refusals and a private one declaring the rules
     * that raise them, and those are two resources.
     */
    def private boolean namedByARule(Exception ex) {
        val resource = ex.eResource
        if (resource === null || resource.resourceSet === null) {
            return false
        }
        for (other : resource.resourceSet.resources) {
            for (rule : other.allContents.toIterable.filter(BusinessRule)) {
                if (rule.exception === ex) {
                    return true
                }
            }
        }
        return false
    }

}

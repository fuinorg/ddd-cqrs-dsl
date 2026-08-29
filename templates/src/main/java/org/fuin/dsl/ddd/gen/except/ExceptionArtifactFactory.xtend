package org.fuin.dsl.ddd.gen.except

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AbstractBusinessRule
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
import org.fuin.dsl.ddd.gen.extensions.TypeExtensions
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
        if (shortId(ex) !== null) {
            ctx.requiresImport("org.fuin.objects4j.common.ExceptionShortIdentifable")
        }
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
            public final class «className» extends «_uniquelyNumberedException(ex)»«IF shortId(ex) !== null» implements ExceptionShortIdentifable«ENDIF» {
            
                @Serial
                private static final long serialVersionUID = 1000L;
            «IF carriesData»
            
                /** Name this exception is transported under. */
                public static final String ELEMENT_NAME = "«elementName(ex)»";
            «ENDIF»
            «IF shortId(ex) !== null»
            
                /** Unique short identifier of this exception. */
                public static final String SHORT_ID = "«shortId(ex)»";
            «ENDIF»
            
                «new SrcVarsDecl(ctx, "private", GenerateOptions.empty(), ex)»
                «new SrcJavaDocMethod(ctx, "Constructs a new instance of the exception.", null, ex.attributes.asParameters, null)»
                public «ex.name»(«new SrcParamsDecl(ctx, GenerateOptions.empty(), ex.attributes.asParameters)») {
                    super(«IF ex.cid > 0»«ex.cid», «ENDIF»«IF !ex.attributes.empty»Objects.requireNonNull(«new SrcKeyValueReplace(ctx, ex.message, ex.attributes.asNames)»)«ELSE»«new SrcKeyValueReplace(ctx, ex.message, ex.attributes.asNames)»«ENDIF»);
                    «new SrcParamsAssignment(ctx, ex.attributes.asParameters)»
                }
            
            «IF shortId(ex) !== null»
                @Override
                public final String getShortId() {
                    return SHORT_ID;
                }
            
            «ENDIF»
                «new SrcGetters(ctx, GenerateOptions.empty(), "public final", ex.attributes)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

    /**
     * The short identifier of an exception, or <code>null</code> where the model configures no prefix.
     *
     * <p>What a support desk quotes, which is what the <code>code</code> of a result is for: the
     * project's prefix and the exception's own name, the way the library writes
     * <code>DDD4J-AGGREGATE_NOT_FOUND</code>. "Exception" is dropped because every one of them ends
     * that way and it says nothing.
     *
     * <p>Without a prefix nothing is generated and the refusal is identified by its class name instead,
     * so a model that says nothing keeps the behaviour it had.
     */
    /**
     * Whether a class carrying this exception's data is generated beside it, which is what needs the
     * element name. Same condition the data factory itself applies.
     */
    def private boolean carriesData() {
        return options.jackson
    }

    def private String shortId(Exception ex) {
        val prefix = options.shortIdPrefix
        if (prefix === null || prefix.empty) {
            return null
        }
        return prefix + "-" + TypeExtensions.asEntityTypeConstant(withoutExceptionSuffix(ex.name))
    }

    /** The name the exception is transported under, as the library writes "aggregate-not-found-exception". */
    def private static String elementName(Exception ex) {
        return ex.name.replaceAll("(?<!^)(?=[A-Z])", "-").toLowerCase
    }

    def private static String withoutExceptionSuffix(String name) {
        return if(name.endsWith("Exception") && name.length > "Exception".length) name.substring(0, name.length - "Exception".length) else name
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
    // A business key names its refusal the same way a rule does, and derives a rule that throws it, so
    // the two are asked about together - a key's exception dropping to the plain base would make the
    // generated rule's verify() throw something BusinessRule does not declare.
    def private boolean namedByARule(Exception ex) {
        val resource = ex.eResource
        if (resource === null || resource.resourceSet === null) {
            return false
        }
        for (other : resource.resourceSet.resources) {
            for (rule : other.allContents.toIterable.filter(AbstractBusinessRule)) {
                if (rule.exception === ex) {
                    return true
                }
            }
        }
        return false
    }

}

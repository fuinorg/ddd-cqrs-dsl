package org.fuin.dsl.ddd.gen.except

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Exception
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.VariableExtensions.*

/**
 * Creates the class that carries an exception's data to a client.
 *
 * <p><b>What the refusal was about, not merely that there was one.</b> A result says what kind of
 * problem it is in its <code>code</code>; the data says which values it concerned, so a form can put
 * the message under the field it belongs to instead of above the form. The attributes are the
 * exception's own, so nothing has to be decided twice.
 *
 * <p>It is a class of its own rather than a method on the exception because an exception cannot build
 * it: the exception is declared once while its data exists per serialization flavour, in a module the
 * exception does not depend on. What pairs the two at runtime is the type argument of
 * {@link org.fuin.ddd4j.core.ExceptionData}, which is what <code>JandexExceptionDataRegistry</code>
 * reads off the classpath.
 *
 * <p><b>The message is not carried.</b> A generated exception builds its message from its attributes,
 * so recreating the exception recreates the message - and one string that can disagree with the values
 * beside it is one too many.
 */
class ExceptionDataArtifactFactory extends AbstractSource<Exception> {

    override getModelType() {
        typeof(Exception)
    }

    override getTypeKey() {
        TypeKeys.JAVA_EXCEPTION_DATA
    }

    override create(Exception ex, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (!options.jackson) {
            // Only the flavour the model asks for. A data class is per serialization framework - the
            // library ships one each for Jackson, JAX-B and JSON-B - and generating a flavour nothing
            // reads would put a class on the classpath for the registry to find and nobody to use.
            return null
        }

        val className = ex.name + "Data"
        val pkg = ex.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java"

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(ex, TypeKeys.JAVA_EXCEPTION_DATA), fqn)

        if (preparationRun) {
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(ex)
        ctx.addReferences(ex)

        return List.of(newArtifact(filename, create(ctx, ex, pkg, className).toString.getBytes("UTF-8"), ex))
    }

    def addImports(CodeSnippetContext ctx, Exception ex) {
        ctx.requiresImport("com.fasterxml.jackson.annotation.JsonIgnore")
        ctx.requiresImport("com.fasterxml.jackson.annotation.JsonIgnoreProperties")
        ctx.requiresImport("com.fasterxml.jackson.annotation.JsonProperty")
        ctx.requiresImport("java.io.Serial")
        ctx.requiresImport("java.util.Objects")
        ctx.requiresImport("org.fuin.ddd4j.core.ExceptionData")
        ctx.requiresImport("org.fuin.objects4j.common.ImmutableAfterUnmarshal")
    }

    def addReferences(CodeSnippetContext ctx, Exception ex) {
        ctx.requiresReference(TypeKeys.refKey(ex))
        for (v : ex.attributes) {
            ctx.requiresReference(TypeKeys.refKey(v.type))
        }
    }

    def create(SimpleCodeSnippetContext ctx, Exception ex, String pkg, String className) {

        val String src = '''
            /**
             * Data of a «ex.name», carried to a client so a refusal can say what it was about.
             *
             * The exception is recreated from it rather than transported with its stack trace.
             */
            @ImmutableAfterUnmarshal
            @JsonIgnoreProperties(ignoreUnknown = true)
            @SuppressWarnings("NullAway.Init")
            public final class «className» implements ExceptionData<«ex.name»> {
            
                @Serial
                private static final long serialVersionUID = 1000L;
            «FOR v : ex.attributes»
            
                @JsonProperty("«v.name»")
                private «v.type(ctx)» «v.name»;
            «ENDFOR»
            
                /**
                 * Constructor only for marshalling/unmarshalling.
                 */
                protected «className»() {
                    super();
                }
            
                «new SrcJavaDocMethod(ctx, "Constructor with the exception to copy the data from.", null, null, null)»
                public «className»(final «ex.name» ex) {
                    super();
                    Objects.requireNonNull(ex, "ex==null");
                    «FOR v : ex.attributes»
                    this.«v.name» = ex.get«v.name.toFirstUpper»();
                    «ENDFOR»
                }
            
                @Override
                @JsonIgnore
                public String getDataElement() {
                    return «ex.name».ELEMENT_NAME;
                }
            «FOR v : ex.attributes»
            
                /**
                 * Returns: «v.superDocOrName»
                 *
                 * @return Current value.
                 */
                @JsonIgnore
                public «v.type(ctx)» get«v.name.toFirstUpper»() {
                    return «v.name»;
                }
            «ENDFOR»
            
                @Override
                public «ex.name» toException() {
                    return new «ex.name»(«ex.attributes.map[name].join(", ")»);
                }
            
                @Override
                public int hashCode() {
                    return Objects.hash(«ex.attributes.map[name].join(", ")»);
                }
            
                @Override
                public boolean equals(final Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (obj == null || getClass() != obj.getClass()) {
                        return false;
                    }
                    «IF ex.attributes.empty»
                    return true;
                    «ELSE»
                    final «className» other = («className») obj;
                    return «ex.attributes.map["Objects.equals(" + name + ", other." + name + ")"].join("\n    && ")»;
                    «ENDIF»
                }
            
                @Override
                public String toString() {
                    return "«className» [«ex.attributes.map[name + "=" + '" + ' + name + ' + "'].join(", ")»]";
                }
            
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

    def private static String superDocOrName(org.fuin.dsl.cqrs.cqrsDsl.Variable v) {
        return if(v.doc === null) v.name else v.doc.text
    }

    def private static String toFirstUpper(String value) {
        return value.substring(0, 1).toUpperCase + value.substring(1)
    }

}

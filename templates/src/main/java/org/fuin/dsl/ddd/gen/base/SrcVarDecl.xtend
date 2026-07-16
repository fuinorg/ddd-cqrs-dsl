package org.fuin.dsl.ddd.gen.base

import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsVariableExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.VariableExtensions.*
import org.fuin.dsl.cqrs.cqrsDsl.Variable

/**
 * Creates source code for a single attribute declaration.
 */
class SrcVarDecl implements CodeSnippet {

    val CodeSnippetContext ctx
    val String modifiers
    val GenerateOptions options
    val Variable variable
    val boolean builderPopulated

    /**
     * Constructor with all mandatory data.
     * 
     * @param ctx Context.
     * @param modifiers Modifiers for the attribute.
     * @param options Options to use.
     * @param variable Attribute or Parameter.
     */
    new(CodeSnippetContext ctx, String modifiers, GenerateOptions options, Variable variable) {
        this(ctx, modifiers, options, variable, false)
    }

    /**
     * Constructor that allows marking the attribute as assigned by a builder.
     * 
     * @param ctx Context.
     * @param modifiers Modifiers for the attribute.
     * @param options Options to use.
     * @param variable Attribute or Parameter.
     * @param builderPopulated TRUE if a builder assigns the attribute after construction.
     */
    new(CodeSnippetContext ctx, String modifiers, GenerateOptions options, Variable variable, boolean builderPopulated) {
        this.ctx = ctx
        this.modifiers = modifiers
        this.options = options
        this.variable = variable
        this.builderPopulated = builderPopulated

        if (variable.optional !== null && !variable.isPrimitive(ctx)) {
            ctx.requiresImport("org.jspecify.annotations.Nullable")
        }
        addRequiredReferences(variable, ctx)
    }

    override toString() {
        '''
            «validationAnnotations»
            «xmlAnnotations»
            «jsonAnnotations»
            «new SrcMetaAnnotations(ctx, variable.overriddenMeta, null, variable.name)»
            «nullnessSuppression»
            «modifiers» «variable.type(ctx)» «variable.name»;
        '''
    }

    /**
     * A builder assigns the attribute after construction, so no constructor initializes it. Only a non-optional, non-primitive
     * attribute needs the suppression: an optional one is already annotated with {@code @Nullable} and a primitive can never be
     * null.
     */
    private def nullnessSuppression() {
        '''
            «IF builderPopulated && variable.optional === null && !variable.isPrimitive(ctx)»
                @SuppressWarnings("NullAway.Init")
            «ENDIF»
        '''
    }

    private def validationAnnotations() {
        '''
            «FOR cc : variable.constraints SEPARATOR ' '»
                «new SrcValidationAnnotation(ctx, options, cc)»
            «ENDFOR»
            «IF variable.optional !== null && !variable.isPrimitive(ctx)»
                @Nullable
            «ENDIF»
        '''
    }

    private def xmlAnnotations() {
        '''
            «IF options.jaxb»
                «new SrcXmlAttributeOrElement(ctx, variable, options.jaxbElements)»
            «ENDIF»
        '''
    }

    private def jsonAnnotations() {
        '''
            «IF options.jsonb»
                «new SrcJsonProperty(ctx, variable)»
            «ENDIF»
        '''
    }

}

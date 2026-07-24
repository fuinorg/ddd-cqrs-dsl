package org.fuin.dsl.ddd.gen.base

import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

/**
 * Creates a constructor of an aggregate or entity: the signature and the super call exactly as
 * {@link SrcConstructorWithParamsAssignment} does, but with the domain body skeleton (see
 * {@link SrcDomainMethodBody}) in place of the field assignments. The final class decides for itself
 * which state it keeps, so there is nothing for the generator to assign.
 */
class SrcConstructorWithDomainBody implements CodeSnippet {

    val CodeSnippetContext ctx
    val GenerateOptions options
    val ConstructorData constructorData
    val Constructor constructor

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param options Options to use.
     * @param constructorData Data of the constructor to create.
     * @param constructor Model element the body is created from.
     */
    new(CodeSnippetContext ctx, GenerateOptions options, ConstructorData constructorData, Constructor constructor) {
        this.ctx = ctx
        this.options = options
        this.constructorData = constructorData
        this.constructor = constructor
    }

    override toString() {
        '''
            «new SrcJavaDocMethod(ctx, constructorData.doc, null, constructorData.parameters, constructorData.exceptions)»
            «new SrcConstructorSignature(ctx, options, constructorData)» {
                «new SrcParamsSuperCall(ctx, constructorData.superCallParameters)»

                «new SrcDomainMethodBody(ctx, constructor)»
            }
        '''
    }

}

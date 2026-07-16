package org.fuin.dsl.ddd.gen.service

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Service
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Creates source code for a service.
 */
class SrcServices implements CodeSnippet {

    val CodeSnippetContext ctx
    val GenerateOptions options
    val List<Service> services

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param options Options to use.
     * @param services Services to create the source code for.
     */
    new(CodeSnippetContext ctx, GenerateOptions options, List<Service> services) {
        this.ctx = ctx
        this.options = options
        this.services = services
    }

    override toString() {
        '''
        «FOR service : services.nullSafe»
            «new SrcService(ctx, options, service).toString»
            
        «ENDFOR»
        '''
    }

}

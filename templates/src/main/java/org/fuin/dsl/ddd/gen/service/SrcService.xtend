package org.fuin.dsl.ddd.gen.service

import org.fuin.dsl.cqrs.cqrsDsl.Service
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcMethodSignature
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Creates source code for a service.
 */
class SrcService implements CodeSnippet {

    val CodeSnippetContext ctx
    val GenerateOptions options
    val Service service

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param options Options to use.
     * @param service Service to create the source code for.
     */
    new(CodeSnippetContext ctx, GenerateOptions options, Service service) {
        this.ctx = ctx
        this.options = options
        this.service = service
    }

    override toString() {
        '''    
        «new SrcJavaDocType(service)»
        public interface «service.name» {
            
            «FOR method : service.methods.nullSafe»
                «new SrcJavaDocMethod(ctx, method).toString»
                «new SrcMethodSignature(ctx, "public", false, options.mappingsOnly, method).toString»;
                
            «ENDFOR»
        }
        '''
    }

}

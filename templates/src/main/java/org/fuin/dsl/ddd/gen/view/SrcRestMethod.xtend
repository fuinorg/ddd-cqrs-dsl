package org.fuin.dsl.ddd.gen.view

import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

/**
 * Creates source code for a single REST operation of a view - either the declaration in the contract
 * interface or the generate-once stub in the class implementing it. Both are produced from the same
 * model method so the signatures can never drift apart.
 */
class SrcRestMethod implements CodeSnippet {

    val CodeSnippetContext ctx
    val Method method
    val String runtime
    val boolean declaration

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param method Method to create the source for.
     * @param runtime Either "quarkus" or "spring".
     * @param declaration TRUE for the interface declaration or FALSE for the implementing stub.
     */
    new(CodeSnippetContext ctx, Method method, String runtime, boolean declaration) {
        this.ctx = ctx
        this.method = method
        this.runtime = runtime
        this.declaration = declaration
    }

    /** Returns the type the operation produces - the entity itself for JAX-RS, wrapped for Spring. */
    private def String returnType() {
        val type = ViewRestSupport.returnType(ctx, method)
        if (runtime == "quarkus") {
            return type
        }
        "ResponseEntity<" + type + ">"
    }

    override toString() {
        val params = ViewRestSupport.params(ctx, method, runtime)
        if (declaration) {
            if (runtime == "quarkus") {
                return '''
                    «new SrcJavaDocMethod(ctx, method)»
                    @GET
                    @Path("«ViewRestSupport.restPath(method)»")
                    @Produces(MediaType.APPLICATION_JSON)
                    «returnType» «method.name»(«params»);
                '''.toString
            }
            return '''
                «new SrcJavaDocMethod(ctx, method)»
                @GetExchange("«ViewRestSupport.restPath(method)»")
                «returnType» «method.name»(«params»);
            '''.toString
        }
        '''
            @Override
            public «returnType» «method.name»(«params») {
                // TODO Implement: query the read model and return the result.
                throw new UnsupportedOperationException("TODO: implement «method.name»()");
            }
        '''.toString
    }

}

package org.fuin.dsl.ddd.gen.view

import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.SrcMethodSignature
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

/**
 * Creates source code for a single operation of a view, in one of the five forms it takes - see
 * {@link ViewMethodShape}. Rendering all of them from the same model method is what keeps the service
 * contract, the REST contract and the three implementations from drifting apart.
 *
 * <p>The two type systems meet here, and the difference is deliberate:
 * <ul>
 * <li>The <b>service</b> shapes go through {@link SrcMethodSignature}, so an {@code optional} result
 * becomes an {@code Optional<X>} and a non-optional one becomes a Java primitive where there is one.
 * Its parameters carry no binding annotations.</li>
 * <li>The <b>REST</b> shapes go through {@link ViewRestSupport}, which never wraps in an
 * {@code Optional}: over HTTP an absent value is a 404, not an empty body. Their parameters carry the
 * binding annotations of the runtime.</li>
 * </ul>
 * The delegating shapes convert between the two.
 */
class SrcViewMethod implements CodeSnippet {

    val CodeSnippetContext ctx
    val Method method
    val String runtime
    val ViewMethodShape shape
    val String target
    val String viewName

    /**
     * Constructor for a shape that needs no delegation target.
     *
     * @param ctx Context.
     * @param method Method to create the source for.
     * @param runtime Either "quarkus" or "spring" - ignored by the service shapes.
     * @param shape Form to render.
     */
    new(CodeSnippetContext ctx, Method method, String runtime, ViewMethodShape shape) {
        this(ctx, method, runtime, shape, null, null)
    }

    /**
     * Constructor for a delegating shape that needs no permission id.
     *
     * @param ctx Context.
     * @param method Method to create the source for.
     * @param runtime Either "quarkus" or "spring" - ignored by the service shapes.
     * @param shape Form to render.
     * @param target Name of the field a delegating shape forwards to.
     */
    new(CodeSnippetContext ctx, Method method, String runtime, ViewMethodShape shape, String target) {
        this(ctx, method, runtime, shape, target, null)
    }

    /**
     * Constructor with all data.
     *
     * @param ctx Context.
     * @param method Method to create the source for.
     * @param runtime Either "quarkus" or "spring" - ignored by the service shapes.
     * @param shape Form to render.
     * @param target Name of the field a delegating shape forwards to.
     * @param viewName Name of the owning view as the model spells it (e.g. {@code PersonListView}), used
     *                 to build the permission id. Required by {@link ViewMethodShape#REST_DELEGATE}.
     */
    new(CodeSnippetContext ctx, Method method, String runtime, ViewMethodShape shape, String target,
        String viewName) {
        this.ctx = ctx
        this.method = method
        this.runtime = runtime
        this.shape = shape
        this.target = target
        this.viewName = viewName
    }

    /** The type the REST operation produces - the value itself for JAX-RS, wrapped for Spring. */
    private def String restReturnType() {
        val type = ViewRestSupport.returnType(ctx, method)
        if (runtime == "quarkus") {
            return type
        }
        "ResponseEntity<" + type + ">"
    }

    /** The signature of the service shapes - identical for the declaration and both implementations. */
    private def String serviceSignature() {
        new SrcMethodSignature(ctx, "public", false, GenerateOptions.empty(), method).toString
    }

    override toString() {
        switch (shape) {
            case ViewMethodShape.SERVICE_DECL: serviceDecl()
            case ViewMethodShape.SERVICE_IMPL_STUB: serviceImplStub()
            case ViewMethodShape.REST_DECL: restDecl()
            case ViewMethodShape.REST_DELEGATE: restDelegate()
            case ViewMethodShape.REST_CLIENT_DELEGATE: restClientDelegate()
        }
    }

    private def String serviceDecl() {
        '''
            «new SrcJavaDocMethod(ctx, method)»
            «serviceSignature»;
        '''.toString
    }

    private def String serviceImplStub() {
        '''
            @Override
            «serviceSignature» {
                // TODO Implement: query the read model and return the result.
                throw new UnsupportedOperationException("TODO: implement «method.name»()");
            }
        '''.toString
    }

    private def String restDecl() {
        val params = ViewRestSupport.params(ctx, method, runtime)
        if (runtime == "quarkus") {
            return '''
                «new SrcJavaDocMethod(ctx, method)»
                @GET
                @Path("«ViewRestSupport.restPath(method)»")
                @Produces(MediaType.APPLICATION_JSON)
                «restReturnType» «method.name»(«params»);
            '''.toString
        }
        '''
            «new SrcJavaDocMethod(ctx, method)»
            @GetExchange("«ViewRestSupport.restPath(method)»")
            «restReturnType» «method.name»(«params»);
        '''.toString
    }

    private def String restDelegate() {
        // The binding annotations are re-declared on the implementation: Spring does not reliably inherit
        // parameter annotations from the interface, and JAX-RS does not inherit them at all.
        val params = ViewRestSupport.params(ctx, method, runtime)
        val args = ViewRestSupport.args(method)
        '''
            @Override
            public «restReturnType» «method.name»(«params») {
                «authorizationCheck»
                «restDelegateBody(args)»
            }
        '''.toString
    }

    /**
     * The authorization check that opens every generated view operation.
     *
     * <p>It is generated rather than left to the developer because there is no choke point on the query
     * side: commands all pass through one dispatcher, while queries get one controller method per view
     * method. A view method somebody forgets to guard is an unchecked read, and it looks exactly like a
     * working one.
     *
     * <p>The permission id is a compile-time literal, built the same way the permission catalogue builds
     * it - {@code «View».«method»} - so the two cannot drift and the generated source can be grepped
     * against {@code PERMISSIONS.md}.
     */
    private def String authorizationCheck() {
        if (viewName === null) {
            throw new IllegalStateException(
                "A REST_DELEGATE needs the view name to build the permission id, but none was given for '"
                    + method.name + "'. Leaving the check out would generate an unchecked read.")
        }
        ctx.requiresImport("org.fuin.cqrs4j.core.QueryAuthorization")
        '''QueryAuthorization.require(«ViewRestDelegateArtifactFactory.AUTHORIZER», "«viewName».«method.name»", «ViewRestDelegateArtifactFactory.CONTEXT_PROVIDER».current());'''
    }

    private def String restDelegateBody(String args) {
        if (runtime == "quarkus") {
            if (method.returnType === null) {
                return '''
                    «target».«method.name»(«args»);
                    return null;'''.toString
            }
            if (ViewRestSupport.isOptional(method)) {
                ctx.requiresImport("jakarta.ws.rs.NotFoundException")
                return '''return «target».«method.name»(«args»).orElseThrow(NotFoundException::new);'''
            }
            return '''return «target».«method.name»(«args»);'''
        }
        if (method.returnType === null) {
            return '''
                «target».«method.name»(«args»);
                return ResponseEntity.ok().build();'''.toString
        }
        if (ViewRestSupport.isOptional(method)) {
            // An absent value is a 404 with no body - the same answer a hand-written controller gave.
            return '''
                return «target».«method.name»(«args»).map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());'''.toString
        }
        '''return ResponseEntity.ok(«target».«method.name»(«args»));'''
    }

    private def String restClientDelegate() {
        val args = ViewRestSupport.args(method)
        '''
            @Override
            «serviceSignature» {
                «restClientBody(args)»
            }
        '''.toString
    }

    private def String restClientBody(String args) {
        if (method.returnType === null) {
            return '''«target».«method.name»(«args»);'''
        }
        val type = ViewRestSupport.returnType(ctx, method)
        if (ViewRestSupport.isOptional(method)) {
            // Both branches are needed: a proxy built the usual way throws on a 404, but one configured
            // with a permissive status handler hands the status back instead.
            ctx.requiresImport("org.springframework.web.client.HttpClientErrorException")
            return '''
                try {
                    final ResponseEntity<«type»> response = «target».«method.name»(«args»);
                    if (response.getStatusCode().value() == 404) {
                        return Optional.empty();
                    }
                    return Optional.ofNullable(response.getBody());
                } catch (final HttpClientErrorException.NotFound ex) {
                    return Optional.empty();
                }'''.toString
        }
        '''
            final ResponseEntity<«type»> response = «target».«method.name»(«args»);
            final «type» body = response.getBody();
            if (body == null) {
                throw new IllegalStateException(
                        "The query side answered '«method.name»' with an empty body");
            }
            return body;'''.toString
    }

}

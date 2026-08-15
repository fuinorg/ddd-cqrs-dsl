package org.fuin.dsl.ddd.gen.view

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ArtifactNames
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates the class implementing a view's REST contract: {@code <Base>Controller} for Spring or
 * {@code <Base>Resource} for Quarkus, selected by the {@code runtime} generator option.
 *
 * <p>It is regenerated on every build rather than written once, because there is nothing in it to
 * write: every operation forwards to {@code <Base>Service} and does nothing else. Exposing the read
 * model over HTTP is a mechanical translation of a contract the model already states - the queries
 * themselves live in the hand-written {@code <Base>ServiceImpl}, which is where a developer works.
 *
 * <p>The class name deliberately does not start with {@code Final}: that prefix is what the preset
 * {@code artifact2Target} script routes to {@code src/main/java}, and this artifact belongs in
 * {@code src-gen/main/java} with the rest of the derived code.
 */
class ViewRestDelegateArtifactFactory extends AbstractSource<View> {

    /** Name of the field the generated class forwards to. */
    static val TARGET = "service"

    /** Name of the field holding the authorizer every operation consults. */
    package static val AUTHORIZER = "authorizer"

    /** Name of the field holding the provider that says who is calling. */
    package static val CONTEXT_PROVIDER = "contextProvider"

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.JAVA_VIEW_REST_IMPL
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {
            return null
        }

        val runtime = getVar("runtime", "spring")
        val baseName = ArtifactNames.viewBaseName(view.name)
        val className = baseName + (if (runtime == "quarkus") "Resource" else "Controller")
        val pkg = view.asPackage
        val filename = (pkg + "." + className).replace('.', '/') + ".java"

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        val src = createDelegate(refReg, view, pkg, baseName, className, runtime)
        return List.of(newArtifact(filename, src.getBytes("UTF-8"), view))
    }

    private def String createDelegate(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String className, String runtime) {

        val restPath = if (view.restPath === null) "/" + ViewRestSupport.kebabCase(baseName) else view.restPath
        val serviceName = baseName + "Service"
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("java.util.Objects")
        // Both types are runtime neutral and live in the core module, so the same generated body compiles
        // under Spring and under Quarkus. Each runtime supplies its own provider implementation.
        ctx.requiresImport("org.fuin.cqrs4j.core.QueryAuthorizer")
        ctx.requiresImport("org.fuin.cqrs4j.core.QueryExecutionContextProvider")
        // The service contract and the REST contract both live in their own module and package.
        ctx.requiresReference(TypeKeys.refKey(view, TypeKeys.JAVA_VIEW_SERVICE))
        ctx.requiresReference(TypeKeys.refKey(view,
            if ("quarkus" == runtime) TypeKeys.JAVA_VIEW_REST_API_QUARKUS else TypeKeys.JAVA_VIEW_REST_API_SPRING))

        if (runtime == "quarkus") {
            val apiName = baseName + "ResourceApi"
            ctx.requiresImport("jakarta.enterprise.context.ApplicationScoped")
            ctx.requiresImport("jakarta.ws.rs.Path")
            val src = '''
                /**
                 * Exposes the «baseName» read model over REST by forwarding to {@link «serviceName»}.
                 * Implements {@link «apiName»}; the class-level {@code @Path} is re-declared because
                 * JAX-RS does not inherit it from the interface. Holds no logic of its own and is
                 * regenerated on every build - implement the queries in «baseName»ServiceImpl.
                 */
                @ApplicationScoped
                @Path("«restPath»")
                public class «className» implements «apiName» {

                    private final «serviceName» «TARGET»;

                    private final QueryAuthorizer «AUTHORIZER»;

                    private final QueryExecutionContextProvider «CONTEXT_PROVIDER»;

                    /**
                     * Constructor with all mandatory dependencies.
                     *
                     * @param «TARGET» Read model this resource exposes.
                     * @param «AUTHORIZER» Decides whether the caller may invoke an operation.
                     * @param «CONTEXT_PROVIDER» Says who is calling.
                     */
                    public «className»(final «serviceName» «TARGET», final QueryAuthorizer «AUTHORIZER»,
                            final QueryExecutionContextProvider «CONTEXT_PROVIDER») {
                        this.«TARGET» = Objects.requireNonNull(«TARGET», "«TARGET»==null");
                        this.«AUTHORIZER» = Objects.requireNonNull(«AUTHORIZER», "«AUTHORIZER»==null");
                        this.«CONTEXT_PROVIDER» = Objects.requireNonNull(«CONTEXT_PROVIDER», "«CONTEXT_PROVIDER»==null");
                    }

                    «FOR method : view.methods»
                        «new SrcViewMethod(ctx, method, runtime, ViewMethodShape.REST_DELEGATE, TARGET, view.name).toString»

                    «ENDFOR»
                }
            '''
            return new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
        }

        // Spring: @RestController is required (it is not inherited from the interface) and the base path
        // comes from the interface's @HttpExchange. No @Transactional here - the transaction belongs
        // around the query, which runs in the service.
        val apiName = baseName + "ControllerApi"
        ctx.requiresImport("org.springframework.http.ResponseEntity")
        ctx.requiresImport("org.springframework.web.bind.annotation.RestController")
        val src = '''
            /**
             * Exposes the «baseName» read model over REST by forwarding to {@link «serviceName»}.
             * Implements {@link «apiName»} and adds {@code @RestController} (required - not inherited
             * from the interface). Holds no logic of its own and is regenerated on every build -
             * implement the queries in «baseName»ServiceImpl.
             */
            @RestController
            public class «className» implements «apiName» {

                private final «serviceName» «TARGET»;

                private final QueryAuthorizer «AUTHORIZER»;

                private final QueryExecutionContextProvider «CONTEXT_PROVIDER»;

                /**
                 * Constructor with all mandatory dependencies. A single constructor is autowired
                 * implicitly.
                 *
                 * @param «TARGET» Read model this controller exposes.
                 * @param «AUTHORIZER» Decides whether the caller may invoke an operation.
                 * @param «CONTEXT_PROVIDER» Says who is calling.
                 */
                public «className»(final «serviceName» «TARGET», final QueryAuthorizer «AUTHORIZER»,
                        final QueryExecutionContextProvider «CONTEXT_PROVIDER») {
                    this.«TARGET» = Objects.requireNonNull(«TARGET», "«TARGET»==null");
                    this.«AUTHORIZER» = Objects.requireNonNull(«AUTHORIZER», "«AUTHORIZER»==null");
                    this.«CONTEXT_PROVIDER» = Objects.requireNonNull(«CONTEXT_PROVIDER», "«CONTEXT_PROVIDER»==null");
                }

                «FOR method : view.methods»
                    «new SrcViewMethod(ctx, method, runtime, ViewMethodShape.REST_DELEGATE, TARGET, view.name).toString»

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

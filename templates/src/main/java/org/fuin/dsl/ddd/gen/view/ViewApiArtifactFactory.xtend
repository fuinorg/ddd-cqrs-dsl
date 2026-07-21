package org.fuin.dsl.ddd.gen.view

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ArtifactNames
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates the view's REST contract interface ({@code <Base>ControllerApi} for Spring /
 * {@code <Base>ResourceApi} for Quarkus). It is a separate factory so it can be routed to its own module
 * (e.g. {@code query.api}) via a per-artifact "module" override, while the view implementation and the
 * concrete controller stay in {@code query.core}. The interface carries only routing annotations (no
 * persistence) and is usable by both a REST client and the server class. Runtime is selected by the
 * {@code runtime} generator option ({@code spring} default | {@code quarkus}).
 */
class ViewApiArtifactFactory extends AbstractSource<View> {

    override getModelType() {
        typeof(View)
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val runtime = getVar("runtime", "spring")
        val baseName = ArtifactNames.viewBaseName(view.name)
        val apiName = baseName + (if (runtime == "quarkus") "ResourceApi" else "ControllerApi")
        val pkg = view.asPackage
        val fqn = pkg + "." + apiName

        // Register the interface FQN so FinalViewArtifactFactory (a different module/package) can import it.
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(ArtifactNames.restApiRefKey(view.uniqueName), fqn)

        if (preparationRun) {
            return null
        }

        val filename = fqn.replace('.', '/') + ".java"
        return List.of(newArtifact(filename, createControllerApi(refReg, view, pkg, baseName, apiName, runtime).getBytes("UTF-8"), view))
    }

    private def String createControllerApi(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String apiName, String runtime) {
        val restPath = if (view.restPath === null) "/" + baseName.toLowerCase else view.restPath
        val ctx = new SimpleCodeSnippetContext(refReg)
        if (runtime == "quarkus") {
            ctx.requiresImport("jakarta.ws.rs.GET")
            ctx.requiresImport("jakarta.ws.rs.Path")
            ctx.requiresImport("jakarta.ws.rs.Produces")
            ctx.requiresImport("jakarta.ws.rs.core.MediaType")
            val src = '''
                /**
                 * REST contract for the "«baseName»" view: usable as a MicroProfile REST client and
                 * implemented by the «baseName»Resource server class. Declares the operations that MUST be
                 * provided - no implementation and no persistence assumptions. Regenerated on every build.
                 *
                 * <p>JAX-RS does not inherit class-level annotations, so the server class re-declares
                 * {@code @Path}; the method annotations below are inherited by the implementation.
                 */
                @Path("«restPath»")
                public interface «apiName» {

                    «FOR method : view.methods»
                        «new SrcRestMethod(ctx, method, runtime, true).toString»

                    «ENDFOR»
                }
            '''
            return new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
        }
        // Spring: an @HttpExchange interface is a client/server-neutral contract (Spring 6.1+). It is
        // usable by an HTTP-interface client and implemented by the @RestController server class.
        ctx.requiresImport("org.springframework.http.ResponseEntity")
        ctx.requiresImport("org.springframework.web.service.annotation.GetExchange")
        ctx.requiresImport("org.springframework.web.service.annotation.HttpExchange")
        val src = '''
            /**
             * REST contract for the "«baseName»" view: an {@code @HttpExchange} interface usable by an
             * HTTP-interface client and implemented by the «baseName»Controller server class (which adds
             * {@code @RestController}). Declares the operations that MUST be provided - no implementation
             * and no persistence assumptions. Regenerated on every build.
             */
            @HttpExchange("«restPath»")
            public interface «apiName» {

                «FOR method : view.methods»
                    «new SrcRestMethod(ctx, method, runtime, true).toString»

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

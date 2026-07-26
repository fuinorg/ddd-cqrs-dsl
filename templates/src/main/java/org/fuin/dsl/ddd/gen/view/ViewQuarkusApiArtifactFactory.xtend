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
 * Generates the Quarkus (JAX-RS) flavour of a view's REST contract interface ({@code <Base>ResourceApi}).
 * It is a separate factory so it can be routed to its own module (e.g. {@code query.api}) via a
 * per-artifact "module" override, while the view implementation and the concrete resource stay in
 * {@code query.core}.
 *
 * <p>The interface is a MicroProfile REST Client: it carries {@code @RegisterRestClient} in addition to
 * the JAX-RS annotations, so a consumer injects it with {@code @RestClient} and configures the base URI
 * through {@code quarkus.rest-client.<configKey>.url}. The same interface is what the generated
 * {@code <Base>Resource} implements server-side - the server re-declares {@code @Path} because JAX-RS
 * does not inherit class-level annotations.
 *
 * <p>This factory does <em>not</em> look at the {@code runtime} generator option: the Quarkus and the
 * Spring contract ({@link ViewSpringApiArtifactFactory}) are always generated side by side. Both are pure
 * annotated interfaces, so the api module compiles against {@code jakarta.ws.rs-api} +
 * {@code microprofile-rest-client-api} and {@code spring-web} as <em>optional</em> dependencies and a
 * consumer adds only the ones belonging to the interface it picks. The {@code runtime} option still
 * selects which of the two the generated server class implements.
 */
class ViewQuarkusApiArtifactFactory extends AbstractSource<View> {

    override getModelType() {
        typeof(View)
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val baseName = ArtifactNames.viewBaseName(view.name)
        val apiName = baseName + "ResourceApi"
        val pkg = view.asPackage
        val fqn = pkg + "." + apiName

        // Register the interface FQN so FinalViewArtifactFactory (a different module/package) can import it.
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(ArtifactNames.restApiRefKey(view.uniqueName, "quarkus"), fqn)

        if (preparationRun) {
            return null
        }

        val filename = fqn.replace('.', '/') + ".java"
        return List.of(newArtifact(filename, createResourceApi(refReg, view, pkg, baseName, apiName).getBytes("UTF-8"), view))
    }

    private def String createResourceApi(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String apiName) {
        val restPath = if (view.restPath === null) "/" + ViewRestSupport.kebabCase(baseName) else view.restPath
        val configKey = ViewRestSupport.kebabCase(baseName)
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.ws.rs.GET")
        ctx.requiresImport("jakarta.ws.rs.Path")
        ctx.requiresImport("jakarta.ws.rs.Produces")
        ctx.requiresImport("jakarta.ws.rs.core.MediaType")
        ctx.requiresImport("org.eclipse.microprofile.rest.client.inject.RegisterRestClient")
        val src = '''
            /**
             * REST contract for the "«baseName»" view: a MicroProfile REST Client interface, also
             * implemented by the «baseName»Resource server class. Declares the operations that MUST be
             * provided - no implementation and no persistence assumptions. Regenerated on every build.
             *
             * <p>As a client, inject it with {@code @RestClient} and point it at a server with
             * {@code quarkus.rest-client.«configKey».url=...}. JAX-RS does not inherit class-level
             * annotations, so the server class re-declares {@code @Path}; the method annotations below are
             * inherited by the implementation.
             *
             * <p>Quarkus flavour - requires {@code jakarta.ws.rs:jakarta.ws.rs-api} and
             * {@code org.eclipse.microprofile.rest.client:microprofile-rest-client-api}, which the module
             * owning this interface declares as <em>optional</em> dependencies (a Quarkus consumer gets
             * both from {@code quarkus-rest-client}). Add them to whatever uses this interface. The Spring
             * flavour {@link «baseName»ControllerApi} is generated alongside it and declares the same
             * operations; use one or the other, not both.
             */
            @Path("«restPath»")
            @RegisterRestClient(configKey = "«configKey»")
            public interface «apiName» {

                «FOR method : view.methods»
                    «new SrcRestMethod(ctx, method, "quarkus", true).toString»

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

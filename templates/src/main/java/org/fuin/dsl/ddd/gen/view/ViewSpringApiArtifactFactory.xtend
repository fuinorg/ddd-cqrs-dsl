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
import org.fuin.dsl.ddd.gen.base.TypeKeys
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates the Spring flavour of a view's REST contract interface ({@code <Base>ControllerApi}). It is a
 * separate factory so it can be routed to its own module (e.g. {@code query.api}) via a per-artifact
 * "module" override, while the view implementation and the concrete controller stay in {@code query.core}.
 *
 * <p>This factory does <em>not</em> look at the {@code runtime} generator option: the Spring and the
 * Quarkus contract ({@link ViewQuarkusApiArtifactFactory}) are always generated side by side. Both are
 * pure annotated interfaces, so the api module compiles against {@code spring-web} and
 * {@code jakarta.ws.rs-api} as <em>optional</em> dependencies and a consumer adds only the one belonging
 * to the interface it picks. The {@code runtime} option still selects which of the two the generated
 * server class implements.
 */
class ViewSpringApiArtifactFactory extends AbstractSource<View> {

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.JAVA_VIEW_REST_API_SPRING
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val baseName = ArtifactNames.viewBaseName(view.name)
        val apiName = baseName + "ControllerApi"
        val pkg = view.asPackage
        val fqn = pkg + "." + apiName

        // Register the interface FQN so FinalViewArtifactFactory (a different module/package) can import it.
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(view, TypeKeys.JAVA_VIEW_REST_API_SPRING), fqn)

        if (preparationRun) {
            return null
        }

        val filename = fqn.replace('.', '/') + ".java"
        return List.of(newArtifact(filename, createControllerApi(refReg, view, pkg, baseName, apiName).getBytes("UTF-8"), view))
    }

    private def String createControllerApi(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String apiName) {
        val restPath = if (view.restPath === null) "/" + ViewRestSupport.kebabCase(baseName) else view.restPath
        val ctx = new SimpleCodeSnippetContext(refReg)
        // An @HttpExchange interface is a client/server-neutral contract (Spring 6.1+). It is usable by an
        // HTTP-interface client and implemented by the @RestController server class.
        ctx.requiresImport("org.springframework.http.ResponseEntity")
        ctx.requiresImport("org.springframework.web.service.annotation.GetExchange")
        ctx.requiresImport("org.springframework.web.service.annotation.HttpExchange")
        val src = '''
            /**
             * REST contract for the "«baseName»" view: an {@code @HttpExchange} interface usable by an
             * HTTP-interface client and implemented by the «baseName»Controller server class (which adds
             * {@code @RestController}). Declares the operations that MUST be provided - no implementation
             * and no persistence assumptions. Regenerated on every build.
             *
             * <p>Spring flavour - requires {@code org.springframework:spring-web}, which the module owning
             * this interface declares as an <em>optional</em> dependency. Add that dependency to whatever
             * uses this interface. The Quarkus flavour {@link «baseName»ResourceApi} is generated alongside
             * it and declares the same operations; use one or the other, not both.
             */
            @HttpExchange("«restPath»")
            public interface «apiName» {

                «FOR method : view.methods»
                    «new SrcViewMethod(ctx, method, "spring", ViewMethodShape.REST_DECL).toString»

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

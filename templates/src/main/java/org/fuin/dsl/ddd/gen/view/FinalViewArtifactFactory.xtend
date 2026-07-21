package org.fuin.dsl.ddd.gen.view

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Event
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
 * Generates the write-once classes for a {@code view}: one event handler stub per projection input
 * event and the REST controller (Spring) / resource (Quarkus). The fully generated view and the
 * abstract controller come from {@link ViewArtifactFactory}. The runtime is selected by the
 * {@code runtime} generator option ({@code spring} default | {@code quarkus}).
 */
class FinalViewArtifactFactory extends AbstractSource<View> {

    override getModelType() {
        typeof(View)
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {
            return null
        }

        val runtime = getVar("runtime", "spring")
        val baseName = ArtifactNames.viewBaseName(view.name)
        val pkg = view.asPackage
        val events = if (view.projection === null) newArrayList else view.projection.events

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        val List<GeneratedArtifact> artifacts = new ArrayList

        for (Event event : events) {
            val handlerName = event.name + "Handler"
            artifacts.add(artifact(pkg, handlerName, createHandler(refReg, event, pkg, handlerName), view))
        }

        val ctrlName = baseName + (if (runtime == "quarkus") "Resource" else "Controller")
        artifacts.add(artifact(pkg, ctrlName, createController(refReg, view, pkg, baseName, ctrlName, runtime), view))

        return artifacts
    }

    private def GeneratedArtifact artifact(String pkg, String className, String src, View view) {
        val filename = (pkg + "." + className).replace('.', '/') + ".java"
        newArtifact(filename, src.getBytes("UTF-8"), view)
    }

    private def String createHandler(CodeReferenceRegistry refReg, Event event, String pkg, String handlerName) {
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("org.fuin.cqrs4j.core.EventHandler")
        ctx.requiresImport("org.fuin.ddd4j.core.EventType")
        ctx.requiresReference(event.uniqueName)
        val src = '''
            /**
             * Handles the {@link «event.name»} by updating the read model. TODO Implement the update.
             */
            public class «handlerName» implements EventHandler<«event.name»> {

                @Override
                public EventType getEventType() {
                    return «event.name».EVENT_TYPE;
                }

                @Override
                public void handle(final EntityManager em, final «event.name» event) {
                    // TODO Update the read model for this event.
                }

            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

    private def String createController(CodeReferenceRegistry refReg, View view, String pkg, String baseName, String ctrlName, String runtime) {
        val restPath = if (view.restPath === null) "/" + baseName.toLowerCase else view.restPath
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        // The REST contract interface lives in its own module/package - import it.
        ctx.requiresReference(ArtifactNames.restApiRefKey(view.uniqueName))
        if (runtime == "quarkus") {
            val apiName = baseName + "ResourceApi"
            ctx.requiresImport("jakarta.inject.Inject")
            ctx.requiresImport("jakarta.ws.rs.Path")
            val src = '''
                /**
                 * REST resource providing the «baseName» read model. Implements {@link «apiName»}; the
                 * class-level {@code @Path} is re-declared because JAX-RS does not inherit it from the
                 * interface. This is a generate-once stub - TODO implement the queries against your read model.
                 */
                @Path("«restPath»")
                public class «ctrlName» implements «apiName» {

                    @Inject
                    EntityManager em;

                    «FOR method : view.methods»
                        «new SrcRestMethod(ctx, method, runtime, false).toString»

                    «ENDFOR»
                }
            '''
            return new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
        }
        // Spring: implements the @HttpExchange contract; @RestController is required (not inherited) and
        // the base path comes from the interface's @HttpExchange.
        val apiName = baseName + "ControllerApi"
        ctx.requiresImport("org.springframework.beans.factory.annotation.Autowired")
        ctx.requiresImport("org.springframework.http.ResponseEntity")
        ctx.requiresImport("org.springframework.transaction.annotation.Transactional")
        ctx.requiresImport("org.springframework.web.bind.annotation.RestController")
        val src = '''
            /**
             * REST controller providing the «baseName» read model. Implements {@link «apiName»} and adds
             * {@code @RestController} (required - not inherited from the interface). This is a generate-once
             * stub - TODO implement the queries against your read model.
             */
            @RestController
            @Transactional(readOnly = true)
            public class «ctrlName» implements «apiName» {

                @Autowired
                private EntityManager em;

                «FOR method : view.methods»
                    «new SrcRestMethod(ctx, method, runtime, false).toString»

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

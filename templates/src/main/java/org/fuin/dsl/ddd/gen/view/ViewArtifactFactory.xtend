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
 * Generates the regenerated (write-every-build) artifacts for a {@code view}: the concrete view
 * implementing {@code org.fuin.cqrs4j.core.View} and the REST contract interface (Spring
 * {@code @HttpExchange} / Quarkus JAX-RS) that both a REST client and the server class can use. The view
 * carries no hand-written code, so it is fully generated (no abstract/final split); the write-once event
 * handlers and the concrete controller/resource implementing the contract come from
 * {@link FinalViewArtifactFactory}. The runtime is selected by the {@code runtime} generator option
 * ({@code spring} default | {@code quarkus}).
 */
class ViewArtifactFactory extends AbstractSource<View> {

    override getModelType() {
        typeof(View)
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val runtime = getVar("runtime", "spring")
        val baseName = ArtifactNames.viewBaseName(view.name)
        val viewName = baseName + "View"
        val pkg = view.asPackage
        val events = if (view.projection === null) newArrayList else view.projection.events

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(view.uniqueName, pkg + "." + viewName)

        if (preparationRun) {
            return null
        }

        val List<GeneratedArtifact> artifacts = new ArrayList

        // --- View (fully generated, no customization point) --------------------------------------
        // The REST contract interface is emitted separately by ViewApiArtifactFactory (own module).
        artifacts.add(artifact(pkg, viewName, createView(refReg, view, pkg, baseName, viewName, events, runtime), view))

        return artifacts
    }

    private def GeneratedArtifact artifact(String pkg, String className, String src, View view) {
        val filename = (pkg + "." + className).replace('.', '/') + ".java"
        newArtifact(filename, src.getBytes("UTF-8"), view)
    }

    private def String createView(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String viewName, List<Event> events, String runtime) {
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("org.fuin.cqrs4j.core.View")
        ctx.requiresImport("org.fuin.cqrs4j.esc.JpaEventDispatcher")
        ctx.requiresImport("org.fuin.cqrs4j.esc.SimpleJpaEventDispatcher")
        ctx.requiresImport("org.fuin.ddd4j.core.Event")
        ctx.requiresImport("org.fuin.ddd4j.core.EventType")
        ctx.requiresImport("org.fuin.objects4j.common.ThreadSafe")
        ctx.requiresImport("java.util.List")
        ctx.requiresImport("java.util.Set")
        for (Event event : events) {
            ctx.requiresReference(event.uniqueName)
        }
        val cron = if (view.cron === null) "* * * * * *" else view.cron
        val projectionName = runtime + "-qry-" + baseName.toLowerCase
        val classAnnotations = if (runtime == "quarkus") {
                ctx.requiresImport("jakarta.enterprise.context.Dependent")
                ctx.requiresImport("jakarta.inject.Inject")
                ctx.requiresImport("jakarta.inject.Named")
                '''
                    @ThreadSafe
                    @Dependent
                    @Named(«viewName».BEAN_NAME)'''
            } else {
                ctx.requiresImport("org.springframework.beans.factory.config.BeanDefinition")
                ctx.requiresImport("org.springframework.context.annotation.Scope")
                ctx.requiresImport("org.springframework.stereotype.Component")
                '''
                    @ThreadSafe
                    @Component(«viewName».BEAN_NAME)
                    @Scope(BeanDefinition.SCOPE_PROTOTYPE)'''
            }
        val ctorAnnotation = if (runtime == "quarkus") "@Inject" else ""
        val src = '''
            /**
             * View with the list of «baseName». Implements {@link View} and is discovered by the query
             * runtime as a bean. Fully generated - regenerated on every build.
             */
            «classAnnotations»
            public class «viewName» implements View {

                /** Unique name of the view / projection. */
                public static final String NAME = "«projectionName»";

                /** Name of the bean. */
                public static final String BEAN_NAME = "«viewName»";

                private final EntityManager em;

                private final JpaEventDispatcher eventDispatcher;

                /**
                 * Constructor with the injected entity manager.
                 *
                 * @param em Entity manager used to store the read model.
                 */
                «ctorAnnotation»
                public «viewName»(final EntityManager em) {
                    this.em = em;
                    this.eventDispatcher = new SimpleJpaEventDispatcher(
                        «FOR event : events SEPARATOR ','»
                            new «event.name»Handler()
                        «ENDFOR»
                    );
                }

                @Override
                public String getName() {
                    return NAME;
                }

                @Override
                public String getBeanName() {
                    return BEAN_NAME;
                }

                @Override
                public Class<? extends View> getBeanClass() {
                    return «viewName».class;
                }

                @Override
                public Set<EventType> getEventTypes() {
                    return Set.of(«FOR event : events SEPARATOR ', '»«event.name».EVENT_TYPE«ENDFOR»);
                }

                @Override
                public String getCron() {
                    return "«cron»";
                }

                @Override
                public void handleEvents(final List<Event> events) {
                    eventDispatcher.dispatchEvents(em, events);
                }

            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

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
import org.fuin.dsl.ddd.gen.base.TypeKeys
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates the write-once classes for a {@code view} - the ones a developer owns and the generator
 * must never overwrite: one event handler stub per projection input event, and the implementation of
 * the view's service contract ({@code <Base>ServiceImpl}), which is where the queries against the read
 * model are written.
 *
 * <p>The derived classes come from elsewhere: the view itself from {@link ViewArtifactFactory}, the
 * service contract from {@link ViewServiceApiArtifactFactory}, and the REST class that forwards to it
 * from {@link ViewRestDelegateArtifactFactory}. The runtime is selected by the {@code runtime}
 * generator option ({@code spring} default | {@code quarkus}).
 */
class FinalViewArtifactFactory extends AbstractSource<View> {

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.JAVA_VIEW_SERVICE_IMPL
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {
            return null
        }

        val runtime = getVar("runtime", "spring")
        val baseName = ArtifactNames.viewBaseName(view.name)
        val pkg = view.asPackage
        val handlerPkg = asPackage(view, TypeKeys.JAVA_VIEW_EVENT_HANDLER)
        val events = if (view.projection === null) newArrayList else view.projection.events

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        val List<GeneratedArtifact> artifacts = new ArrayList

        for (Event event : events) {
            val handlerName = event.name + "Handler"
            artifacts.add(artifact(handlerPkg, handlerName, createHandler(refReg, event, handlerPkg, handlerName),
                view, TypeKeys.JAVA_VIEW_EVENT_HANDLER))
        }

        val implName = baseName + "ServiceImpl"
        artifacts.add(artifact(pkg, implName, createServiceImpl(refReg, view, pkg, baseName, implName, runtime),
            view, TypeKeys.JAVA_VIEW_SERVICE_IMPL))

        return artifacts
    }

    private def GeneratedArtifact artifact(String pkg, String className, String src, View view, String typeKey) {
        val filename = (pkg + "." + className).replace('.', '/') + ".java"
        newArtifact(filename, src.getBytes("UTF-8"), view, typeKey)
    }

    private def String createHandler(CodeReferenceRegistry refReg, Event event, String pkg, String handlerName) {
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("org.fuin.cqrs4j.core.EventHandler")
        ctx.requiresImport("org.fuin.ddd4j.core.EventType")
        ctx.requiresReference(TypeKeys.refKey(event))
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

    private def String createServiceImpl(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String implName, String runtime) {

        val serviceName = baseName + "Service"
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("java.util.Objects")
        // The service contract lives in its own module and package.
        ctx.requiresReference(TypeKeys.refKey(view, TypeKeys.JAVA_VIEW_SERVICE))

        // The transaction belongs here rather than on the REST class: this is what touches the entity
        // manager, and a read model row is mapped to its published form inside the same transaction.
        if (runtime == "quarkus") {
            ctx.requiresImport("jakarta.enterprise.context.ApplicationScoped")
            ctx.requiresImport("jakarta.transaction.Transactional")
        } else {
            ctx.requiresImport("org.springframework.transaction.annotation.Transactional")
        }
        val annotations = if (runtime == "quarkus") {
                "@ApplicationScoped\n@Transactional"
            } else {
                "@Transactional(readOnly = true)"
            }

        val src = '''
            /**
             * Answers the «baseName» read model's queries against the database. Implements
             * {@link «serviceName»}, which the generated «baseName»«IF runtime == "quarkus"»Resource«ELSE»Controller«ENDIF» exposes over REST.
             *
             * <p>This is a generate-once stub and yours from here on - the generator will not overwrite
             * it. TODO implement the queries against your read model.
             */
            «annotations»
            public class «implName» implements «serviceName» {

                private final EntityManager em;

                /**
                 * Constructor with all mandatory dependencies. A single constructor is injected
                 * implicitly, and it is what lets a test drive this service against an in-memory
                 * database without starting a container.
                 *
                 * @param em Entity manager of the read model.
                 */
                public «implName»(final EntityManager em) {
                    this.em = Objects.requireNonNull(em, "em==null");
                }

                «FOR method : view.methods»
                    «new SrcViewMethod(ctx, method, runtime, ViewMethodShape.SERVICE_IMPL_STUB).toString»

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

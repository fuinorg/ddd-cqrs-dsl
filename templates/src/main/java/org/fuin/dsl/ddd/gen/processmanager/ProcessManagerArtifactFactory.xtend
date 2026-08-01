package org.fuin.dsl.ddd.gen.processmanager

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.cqrsDsl.ProcessManager
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
 * Generates the write-once concrete class for a {@code process-manager}: the runtime-annotated
 * process-manager view with one reaction-method stub per input event. The regenerated base comes from
 * {@link AbstractProcessManagerArtifactFactory}. The runtime is selected by the {@code runtime}
 * generator option ({@code spring} default | {@code quarkus}).
 */
class ProcessManagerArtifactFactory extends AbstractSource<ProcessManager> {

    override getModelType() {
        typeof(ProcessManager)
    }

    override getTypeKey() {
        TypeKeys.JAVA_PROCESS_MANAGER
    }

    override create(ProcessManager pm, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {
            return null
        }

        val runtime = getVar("runtime", "spring")
        val baseName = ArtifactNames.processManagerBaseName(pm.name)
        val viewName = baseName + "ProcessManagerView"
        val pkg = pm.asPackage

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        val List<GeneratedArtifact> artifacts = new ArrayList
        artifacts.add(artifact(pkg, viewName, createView(refReg, pm, pkg, viewName, "Abstract" + viewName, runtime), pm))
        return artifacts
    }

    private def GeneratedArtifact artifact(String pkg, String className, String src, ProcessManager pm) {
        val filename = (pkg + "." + className).replace('.', '/') + ".java"
        newArtifact(filename, src.getBytes("UTF-8"), pm)
    }

    private def List<Event> distinctEvents(ProcessManager pm) {
        val List<Event> result = new ArrayList
        val List<String> seen = new ArrayList
        for (reaction : pm.reactions) {
            val event = reaction.event
            if (event !== null && !seen.contains(event.uniqueName)) {
                seen.add(event.uniqueName)
                result.add(event)
            }
        }
        return result
    }

    private def String createView(CodeReferenceRegistry refReg, ProcessManager pm, String pkg, String viewName,
        String abstractViewName, String runtime) {
        val events = distinctEvents(pm)
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("org.fuin.cqrs4j.core.CommandOutbox")
        ctx.requiresImport("org.fuin.objects4j.common.ThreadSafe")
        for (event : events) {
            ctx.requiresReference(TypeKeys.refKey(event))
        }
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
             * The "«pm.name»" process manager. Register it with the process-manager runtime
             * («IF runtime == "quarkus"»Quarkus CDI«ELSE»Spring, importing {@code ProcessManagerConfig}«ENDIF»).
             */
            «classAnnotations»
            public class «viewName» extends «abstractViewName» {

                /**
                 * Constructor with the injected collaborators.
                 *
                 * @param outbox Command outbox (same transaction as the state update).
                 * @param em Entity manager used to store the process state.
                 */
                «ctorAnnotation»
                public «viewName»(final CommandOutbox outbox, final EntityManager em) {
                    super(outbox, em);
                }

                «FOR event : events»
                    @Override
                    protected void on«event.name»(final «event.name» event) {
                        // TODO Correlate to a running process, update the state, send commands and
                        // arm/cancel timeouts.
                    }

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

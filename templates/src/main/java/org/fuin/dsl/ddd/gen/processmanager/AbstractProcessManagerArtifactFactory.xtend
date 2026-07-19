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
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates the regenerated (write-every-build) base classes for a {@code process-manager}: the
 * abstract process-manager view implementing {@code org.fuin.cqrs4j.core.ProcessManagerView} and the
 * state enum (from {@code process-states}). The abstract view is runtime-neutral (it uses the
 * {@code CommandOutbox} SPI); only the concrete subclass differs per runtime (see
 * {@link ProcessManagerArtifactFactory}).
 */
class AbstractProcessManagerArtifactFactory extends AbstractSource<ProcessManager> {

    override getModelType() {
        typeof(ProcessManager)
    }

    override create(ProcessManager pm, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val baseName = ArtifactNames.processManagerBaseName(pm.name)
        val viewName = baseName + "ProcessManagerView"
        val abstractViewName = "Abstract" + viewName
        val stateEnumName = baseName + "State"
        val pkg = pm.asPackage

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(pm.uniqueName, pkg + "." + abstractViewName)

        if (preparationRun) {
            return null
        }

        val List<GeneratedArtifact> artifacts = new ArrayList
        artifacts.add(artifact(pkg, abstractViewName, createAbstractView(refReg, pm, pkg, baseName, viewName, abstractViewName), pm))
        if (!pm.states.empty) {
            artifacts.add(artifact(pkg, stateEnumName, createStateEnum(refReg, pm, pkg, stateEnumName), pm))
        }
        return artifacts
    }

    private def GeneratedArtifact artifact(String pkg, String className, String src, ProcessManager pm) {
        val filename = (pkg + "." + className).replace('.', '/') + ".java"
        newArtifact(filename, src.getBytes("UTF-8"), pm)
    }

    /** Distinct reaction events, in declaration order (an event may be reacted to more than once). */
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

    private def String createAbstractView(CodeReferenceRegistry refReg, ProcessManager pm, String pkg, String baseName,
        String viewName, String abstractViewName) {
        val events = distinctEvents(pm)
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("org.fuin.cqrs4j.core.CommandOutbox")
        ctx.requiresImport("org.fuin.cqrs4j.core.ProcessManagerView")
        ctx.requiresImport("org.fuin.cqrs4j.core.View")
        ctx.requiresImport("org.fuin.ddd4j.core.Event")
        ctx.requiresImport("org.fuin.ddd4j.core.EventType")
        ctx.requiresImport("java.util.HashMap")
        ctx.requiresImport("java.util.List")
        ctx.requiresImport("java.util.Map")
        ctx.requiresImport("java.util.Set")
        ctx.requiresImport("java.util.function.Consumer")
        for (event : events) {
            ctx.requiresReference(event.uniqueName)
        }
        val cron = if (pm.cron === null) "* * * * * *" else pm.cron
        val src = '''
            /**
             * Base class for the "«baseName»" process manager. Implements {@link ProcessManagerView};
             * the concrete «viewName» adds the runtime-specific bean annotations and the reaction
             * bodies. Regenerated on every build.
             */
            public abstract class «abstractViewName» implements ProcessManagerView {

                /** Unique name of the process manager / projection. */
                public static final String NAME = "«baseName»ProcessManager";

                /** Name of the bean. */
                public static final String BEAN_NAME = "«viewName»";

                /** Outbox used to enqueue commands produced by this process manager. */
                protected final CommandOutbox outbox;

                /** Entity manager used to store the process state. */
                protected final EntityManager em;

                /** Reaction dispatch by exact event class, populated in the constructor. */
                private final Map<Class<? extends Event>, Consumer<Event>> reactions = new HashMap<>();

                /**
                 * Constructor with the injected collaborators.
                 *
                 * @param outbox Command outbox (same transaction as the state update).
                 * @param em Entity manager used to store the process state.
                 */
                protected «abstractViewName»(final CommandOutbox outbox, final EntityManager em) {
                    this.outbox = outbox;
                    this.em = em;
                    «FOR event : events»
                        reactions.put(«event.name».class, event -> on«event.name»((«event.name») event));
                    «ENDFOR»
                }

                @Override
                public CommandOutbox getCommandOutboxService() {
                    return outbox;
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
                    for (final Event event : events) {
                        final Consumer<Event> reaction = reactions.get(event.getClass());
                        if (reaction == null) {
                            throw new IllegalStateException("Cannot handle event: " + event);
                        }
                        reaction.accept(event);
                    }
                }

                «FOR event : events»
                    /**
                     * Reacts to a {@link «event.name»}: correlate to a running process, update the state,
                     * enqueue commands via {@code send(...)} and arm/cancel timeouts.
                     *
                     * @param event Event that arrived.
                     */
                    protected abstract void on«event.name»(«event.name» event);

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

    private def String createStateEnum(CodeReferenceRegistry refReg, ProcessManager pm, String pkg, String stateEnumName) {
        val ctx = new SimpleCodeSnippetContext(refReg)
        val src = '''
            /**
             * States the "«pm.name»" process manager can be in.
             */
            public enum «stateEnumName» {

                «FOR state : pm.states SEPARATOR ','»
                    «state.name»
                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}

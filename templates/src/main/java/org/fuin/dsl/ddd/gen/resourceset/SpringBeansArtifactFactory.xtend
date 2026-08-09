package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.Iterator
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.Context
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.ProcessManager
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ArtifactNames
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import org.fuin.dsl.ddd.gen.script.CqrsScripts

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates one Spring {@code @Configuration} per side that registers every generated bean explicitly,
 * so an application never has to component-scan the generated packages.
 *
 * <p>Two kinds of bean are handled differently, and the difference matters:
 * <ul>
 * <li>A <b>view</b> (read model or process manager) is looked up by <i>name</i> - the view manager
 * calls {@code beanFactory.getBean(view.getBeanName(), ...)} - and is prototype scoped. It therefore
 * gets an explicit {@code @Bean(BEAN_NAME)} method; an {@code @Import} would register it under its
 * fully qualified class name and the lookup would fail at runtime.</li>
 * <li>A <b>controller</b>, a <b>service implementation</b> or a <b>repository factory</b> is only ever
 * injected by type, so a plain {@code @Import} is enough.</li>
 * </ul>
 *
 * <p>The service implementations are imported by class although they are hand-written. That is safe by
 * sequencing rather than by luck: the same generation run that writes this configuration writes their
 * stubs, and the core module is built before the starter. A missing one is therefore a compile error
 * here, naming the class - which is a better failure than a {@code NoSuchBeanDefinitionException} at
 * the first request, and the generated controller that injects it could not have been satisfied
 * either.
 *
 * <p>The generated configurations are routed to the {@code *.starter} modules, which is where an
 * application picks them up by depending on the starter alone. A side the model says nothing about -
 * no view, no aggregate, no process manager - gets no configuration, and therefore needs no
 * {@code *.starter} module to exist in the first place.
 */
class SpringBeansArtifactFactory extends AbstractSource<ResourceSet> {

    /** Modules the generated configurations are written to, keyed by side. */
    static val MODULE_QUERY = "query.starter"
    static val MODULE_COMMAND = "command.starter"
    static val MODULE_PROCESS = "process.starter"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.JAVA_SPRING_CONFIG
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val List<String> views = new ArrayList<String>()
        val List<String> controllers = new ArrayList<String>()
        val List<String> serviceImpls = new ArrayList<String>()
        val List<String> repositoryFactories = new ArrayList<String>()
        val List<String> processManagerViews = new ArrayList<String>()
        var String project = null

        val Iterator<EObject> it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val EObject container = it.next
            if (container.isElementContainer) {
                for (element : container.elements) {
                    val typeKey = TypeKeys.primaryTypeKey(element)
                    if (typeKey !== null) {
                        val pkg = CqrsScripts.model2JavaPackage(element, typeKey)
                        if (element instanceof View) {
                            project = container.projectName
                            val baseName = ArtifactNames.viewBaseName(element.name)
                            views.add(pkg + "." + element.name)
                            // Each through its own type key: they land in the same package today, but a
                            // model that routes them apart still resolves.
                            controllers.add(CqrsScripts.model2JavaPackage(element, TypeKeys.JAVA_VIEW_REST_IMPL)
                                + "." + baseName + "Controller")
                            serviceImpls.add(CqrsScripts.model2JavaPackage(element, TypeKeys.JAVA_VIEW_SERVICE_IMPL)
                                + "." + baseName + "ServiceImpl")
                        } else if (element instanceof Aggregate) {
                            project = container.projectName
                            repositoryFactories.add(
                                CqrsScripts.model2JavaPackage(element, TypeKeys.JAVA_AGGREGATE_REPOSITORY_FACTORY)
                                    + "." + element.name + "RepositoryFactory")
                        } else if (element instanceof ProcessManager) {
                            project = container.projectName
                            processManagerViews.add(pkg + "." + element.name + "ManagerView")
                        }
                    }
                }
            }
        }

        if (project === null) {
            return null
        }

        // A side that has nothing to register gets no configuration at all. Emitting one anyway is not
        // merely a useless empty class: the artifact names its target module, so the generator then
        // demands a "<side>.starter" module from every project - and a project without that side has
        // none. It fails with "Couldn't find target folder 'genMainJava' in module 'process.starter'",
        // which reads like a misconfiguration and is in fact this factory asking for a module the model
        // gave it no reason to write to.
        val List<GeneratedArtifact> artifacts = new ArrayList<GeneratedArtifact>()
        if (!views.isEmpty) {
            artifacts.add(queryConfig(project, views, controllers, serviceImpls))
        }
        if (!repositoryFactories.isEmpty) {
            artifacts.add(commandConfig(project, repositoryFactories))
        }
        if (!processManagerViews.isEmpty) {
            artifacts.add(processConfig(project, processManagerViews))
        }
        return artifacts
    }

    /** Returns the name of the project the given element container belongs to. */
    private def String projectName(EObject container) {
        container.context.name
    }

    /**
     * Creates the read side configuration: named prototype view beans, plus the controllers and the
     * service implementations they forward to.
     */
    private def GeneratedArtifact queryConfig(String project, List<String> views, List<String> controllers,
        List<String> serviceImpls) {
        val pkg = project + ".query.starter"
        val name = "QueryBeansConfiguration"
        val ctx = new SimpleCodeSnippetContext(null)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("org.springframework.beans.factory.config.BeanDefinition")
        ctx.requiresImport("org.springframework.context.annotation.Bean")
        ctx.requiresImport("org.springframework.context.annotation.Configuration")
        ctx.requiresImport("org.springframework.context.annotation.Import")
        ctx.requiresImport("org.springframework.context.annotation.Scope")
        views.forEach[ctx.requiresImport(it)]
        controllers.forEach[ctx.requiresImport(it)]
        serviceImpls.forEach[ctx.requiresImport(it)]
        val imported = new ArrayList<String>(controllers)
        imported.addAll(serviceImpls)
        val src = '''
            /**
             * Registers the read side beans explicitly, replacing a component scan of the generated
             * packages. Regenerated on every build.
             *
             * <p>Each view contributes two: the generated controller exposing it over REST, and the
             * hand-written service implementation the controller forwards to. Both are injected by
             * type, so importing the classes is enough.
             */
            @Configuration
            @Import({«FOR c : imported SEPARATOR ", "»«c.simpleName».class«ENDFOR»})
            public class «name» {

                «FOR v : views»
                    /**
                     * Creates a «v.simpleName» instance. The view manager looks the bean up by the
                     * name the view reports, and creates one instance per projection run, hence the
                     * prototype scope.
                     *
                     * @param em Entity manager used to store the read model.
                     *
                     * @return New view instance.
                     */
                    @Bean(«v.simpleName».BEAN_NAME)
                    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
                    public «v.simpleName» «v.simpleName.decapitalize»(final EntityManager em) {
                        return new «v.simpleName»(em);
                    }

                «ENDFOR»
            }
        '''
        newArtifact(filename(pkg, name), new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString.getBytes("UTF-8"),
            MODULE_QUERY, "genMainJava")
    }

    /** Creates the write side configuration: the aggregate repository factories. */
    private def GeneratedArtifact commandConfig(String project, List<String> repositoryFactories) {
        val pkg = project + ".command.starter"
        val name = "CommandBeansConfiguration"
        val ctx = new SimpleCodeSnippetContext(null)
        ctx.requiresImport("org.springframework.context.annotation.Configuration")
        ctx.requiresImport("org.springframework.context.annotation.Import")
        repositoryFactories.forEach[ctx.requiresImport(it)]
        val src = '''
            /**
             * Registers the write side beans explicitly, replacing a component scan of the generated
             * packages. Each imported class is itself a {@code @Configuration} producing one
             * aggregate repository. Regenerated on every build.
             */
            @Configuration
            @Import({«FOR f : repositoryFactories SEPARATOR ", "»«f.simpleName».class«ENDFOR»})
            public class «name» {

            }
        '''
        newArtifact(filename(pkg, name), new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString.getBytes("UTF-8"),
            MODULE_COMMAND, "genMainJava")
    }

    /** Creates the process side configuration: the named prototype process manager views. */
    private def GeneratedArtifact processConfig(String project, List<String> processManagerViews) {
        val pkg = project + ".process.starter"
        val name = "ProcessBeansConfiguration"
        val ctx = new SimpleCodeSnippetContext(null)
        ctx.requiresImport("jakarta.persistence.EntityManager")
        ctx.requiresImport("org.fuin.cqrs4j.core.CommandOutbox")
        ctx.requiresImport("org.springframework.beans.factory.config.BeanDefinition")
        ctx.requiresImport("org.springframework.context.annotation.Bean")
        ctx.requiresImport("org.springframework.context.annotation.Configuration")
        ctx.requiresImport("org.springframework.context.annotation.Scope")
        processManagerViews.forEach[ctx.requiresImport(it)]
        val src = '''
            /**
             * Registers the process side beans explicitly, replacing a component scan of the
             * generated packages. Regenerated on every build.
             */
            @Configuration
            public class «name» {

                «FOR v : processManagerViews»
                    /**
                     * Creates a «v.simpleName» instance. The view manager looks the bean up by the
                     * name the view reports, and creates one instance per projection run, hence the
                     * prototype scope.
                     *
                     * @param outbox Outbox the process manager sends its commands through.
                     * @param em Entity manager used to store the process state.
                     *
                     * @return New view instance.
                     */
                    @Bean(«v.simpleName».BEAN_NAME)
                    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
                    public «v.simpleName» «v.simpleName.decapitalize»(final CommandOutbox outbox, final EntityManager em) {
                        return new «v.simpleName»(outbox, em);
                    }

                «ENDFOR»
            }
        '''
        newArtifact(filename(pkg, name), new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString.getBytes("UTF-8"),
            MODULE_PROCESS, "genMainJava")
    }

    private def String filename(String pkg, String className) {
        (pkg + "." + className).replace('.', '/') + ".java"
    }

    private def String simpleName(String fqn) {
        fqn.substring(fqn.lastIndexOf('.') + 1)
    }

    private def String decapitalize(String name) {
        Character.toLowerCase(name.charAt(0)) + name.substring(1)
    }

    /**
     * Determines whether the given object holds model elements directly: a module, or a context
     * that holds elements without an enclosing module.
     *
     * @param obj Object to check.
     *
     * @return TRUE if the object directly contains model elements.
     */
    private def boolean isElementContainer(EObject obj) {
        obj instanceof Module
    }

    /**
     * Returns the model elements a container holds directly.
     *
     * @param container Element container.
     *
     * @return Direct model elements, never <code>null</code>.
     */
    private def List<AbstractElement> elements(EObject container) {
        if (container instanceof Module) {
            return container.elements
        }
        return emptyList
    }

}

package org.fuin.dsl.ddd.gen.aggregate

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import java.util.List

class ESRepositoryFactoryArtifactFactory extends AbstractSource<Aggregate> implements ArtifactFactory<Aggregate> {

    override getModelType() {
        return typeof(Aggregate)
    }

    override getTypeKey() {
        TypeKeys.JAVA_AGGREGATE_REPOSITORY_FACTORY
    }

    override create(Aggregate aggregate, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val repositoryName = aggregate.name + "Repository"
        val className = repositoryName + "Factory"
        val pkg = aggregate.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(aggregate, TypeKeys.JAVA_AGGREGATE_REPOSITORY_FACTORY), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports
        ctx.addReferences(aggregate)

        return List.of(newArtifact(filename,
            create(ctx, aggregate, pkg, className, repositoryName).toString().getBytes("UTF-8"), aggregate));
    }

    def addImports(CodeSnippetContext ctx) {
        ctx.requiresImport("org.fuin.esc.api.EventStore")
        // Runtime-specific bean wiring: CDI for Quarkus, Spring's @Configuration/@Bean otherwise.
        if (getVar("runtime", "spring") == "quarkus") {
            ctx.requiresImport("jakarta.enterprise.context.Dependent")
            ctx.requiresImport("jakarta.enterprise.inject.Produces")
        } else {
            ctx.requiresImport("org.springframework.context.annotation.Configuration")
            ctx.requiresImport("org.springframework.context.annotation.Bean")
        }
    }

    def addReferences(CodeSnippetContext ctx, Aggregate aggregate) {
        ctx.requiresReference(TypeKeys.refKey(aggregate, TypeKeys.JAVA_AGGREGATE_REPOSITORY))
    }

    def create(SimpleCodeSnippetContext ctx, Aggregate aggregate, String pkg, String className, String repositoryName) {
        val quarkus = getVar("runtime", "spring") == "quarkus"
        val classAnnotation = if (quarkus) "@Dependent" else "@Configuration"
        val methodAnnotation = if (quarkus) "@Produces" else "@Bean"
        // Unique factory-method name so Spring @Bean names don't collide across the per-aggregate factories.
        val methodName = repositoryName.toFirstLower
        val String src = '''
            /**
             * Creates a «repositoryName».
             */
            «classAnnotation»
            public class «className» {

                /**
                 * Produces a «repositoryName».
                 *
                 * @param eventStore The event store to use for construction.
                 *
                 * @return The new repository instance.
                 */
                «methodAnnotation»
                public «repositoryName» «methodName»(final EventStore eventStore) {
                    return new «repositoryName»(eventStore);
                }

            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}

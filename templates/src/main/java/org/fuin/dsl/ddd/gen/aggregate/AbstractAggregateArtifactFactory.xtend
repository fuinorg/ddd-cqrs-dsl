package org.fuin.dsl.ddd.gen.aggregate

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAbstractChildEntityLocatorMethods
import org.fuin.dsl.ddd.gen.base.SrcAbstractHandleEventMethods
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcGetters
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcSetters
import org.fuin.dsl.ddd.gen.base.SrcVarsDecl
import org.fuin.dsl.ddd.gen.service.SrcServices
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractEntityExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAggregateExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import java.util.List
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.dsl.ddd.gen.base.SrcMethods

/**
 * Generates an abstract aggregate Java class.
 */
class AbstractAggregateArtifactFactory extends AbstractSource<Aggregate> {

    override getModelType() {
        return typeof(Aggregate)
    }

    override getTypeKey() {
        TypeKeys.JAVA_AGGREGATE_ABSTRACT
    }

    override create(Aggregate aggregate, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = aggregate.abstractName
        val pkg = aggregate.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(aggregate, TypeKeys.JAVA_AGGREGATE_ABSTRACT), fqn)
        // A service declared inline in a constructor or method is generated as a nested interface of
        // this class (see SrcServices below), and ServiceArtifactFactory deliberately creates no
        // top-level file for it. An operation referencing such a service takes it as a parameter, and
        // the only classes that do so are this one and the final subclass - both have the nested type
        // in scope by its simple name. It is therefore registered unqualified: no import is possible
        // (nor needed), and SrcImports drops a reference without a package.
        for (service : aggregate.services.nullSafe) {
            refReg.putReference(TypeKeys.refKey(service), service.name)
        }

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports
        ctx.addReferences(aggregate)

        return List.of(newArtifact(filename,
            create(ctx, aggregate, pkg, className).toString().getBytes("UTF-8"), aggregate));
    }

    def addImports(CodeSnippetContext ctx) {
        ctx.requiresImport("org.fuin.ddd4j.core.AbstractAggregateRoot")
        ctx.requiresImport("org.fuin.ddd4j.core.EntityType")
        ctx.requiresImport("org.fuin.objects4j.common.Contract")
    }

    def addReferences(CodeSnippetContext ctx, Aggregate aggregate) {
        ctx.requiresReference(TypeKeys.refKey(aggregate.idTypeNullsafe))
    }

    /**
     * Creates the actual source code.
     */
    def create(SimpleCodeSnippetContext ctx, Aggregate aggregate, String pkg, String className) {
        val String src = ''' 
            «new SrcJavaDocType(aggregate)»
            // Everything the model declares is left unset by the constructors on purpose: an
            // aggregate's state comes from the event that created it, applied a moment later and again
            // on every replay, so there is nothing for a constructor to put there.
            @SuppressWarnings("NullAway.Init")
            public abstract class «className» extends AbstractAggregateRoot<«aggregate.idTypeNullsafe.name»> {

                @SuppressWarnings("NullAway.Init")
                private «aggregate.idTypeNullsafe.name» id;

                «new SrcVarsDecl(ctx, "private", GenerateOptions.empty(), aggregate.attributes.nullSafe.toList)»
                /**
                 * Default constructor for loading the aggregate from its history. The identity comes
                 * from the event that created it (see setId below).
                 */
                @SuppressWarnings("NullAway.Init")
                protected «className»() {
                    super();
                }

                /**
                 * Constructor with the identity, used when the aggregate is created. Having it up front
                 * means every operation of the final class can rely on getId(), including the
                 * constructor that is still applying the event which brings the aggregate into being.
                 *
                 * @param id Unique aggregate identifier.
                 */
                protected «className»(final «aggregate.idTypeNullsafe.name» id) {
                    super();
                    // Checked here because a "super(id)" has to be the first statement of the creating
                    // constructor, leaving it no place to check the identity it passes on.
                    Contract.requireArgNotNull("id", id);
                    this.id = id;
                }

                @Override
                public final EntityType getType() {
                    return «aggregate.idTypeNullsafe.name».TYPE;
                }

                @Override
                public final «aggregate.idTypeNullsafe.name» getId() {
                    return id;
                }

                /**
                 * Sets the aggregate identifier. Called from the event handler that brings the
                 * aggregate into existence, which also runs when it is replayed from past events,
                 * so this must never throw.
                 *
                 * @param id Unique aggregate identifier.
                 */
                protected final void setId(final «aggregate.idTypeNullsafe.name» id) {
                    this.id = id;
                }

                «new SrcAbstractChildEntityLocatorMethods(ctx, GenerateOptions.empty(), aggregate)»
                «new SrcGetters(ctx, GenerateOptions.empty(), "public final", aggregate.attributes.nullSafe.toList)»
                «new SrcSetters(ctx, GenerateOptions.empty(), "protected final", aggregate.attributes.nullSafe.toList)»
                «new SrcAbstractHandleEventMethods(ctx, aggregate.allEvents)»
                «new SrcServices(ctx, aggregate.services)»
                «new SrcMethods(ctx, GenerateOptions.empty(), aggregate, true)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}

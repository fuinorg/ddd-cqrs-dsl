package org.fuin.dsl.ddd.gen.aggregateid

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAggregateIdExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import java.util.List

class AggregateIdStreamFactoryArtifactFactory extends AbstractSource<AggregateId> {

    override getModelType() {
        typeof(AggregateId)
    }

    override getTypeKey() {
        TypeKeys.JAVA_AGGREGATE_ID_STREAM_FACTORY
    }

    override create(AggregateId aggregateId, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (aggregateId.aggregateNullsafe === null) {
            return null;
        }

        val className = aggregateId.getName() + "StreamFactory"
        val pkg = aggregateId.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(aggregateId, TypeKeys.JAVA_AGGREGATE_ID_STREAM_FACTORY), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports
        ctx.addReferences(aggregateId)

        return List.of(newArtifact(filename,
            create(ctx, aggregateId, pkg, className).toString().getBytes("UTF-8"), aggregateId));
    }

    def addImports(CodeSnippetContext ctx) {
        ctx.requiresImport("org.fuin.ddd4j.eventstore.jpa.IdStreamFactory")
        ctx.requiresImport("org.fuin.ddd4j.eventstore.jpa.Stream")
        ctx.requiresImport("org.fuin.ddd4j.eventstore.intf.StreamId")
    }

    def addReferences(CodeSnippetContext ctx, AggregateId entityId) {
        ctx.requiresReference(TypeKeys.refKey(entityId))
        ctx.requiresReference(TypeKeys.refKey(entityId.aggregateNullsafe, TypeKeys.JAVA_AGGREGATE_JPA_STREAM))
    }

    def create(SimpleCodeSnippetContext ctx, AggregateId id, String pkg, String className) {
        val src = ''' 
            /**
             * Creates a «id.aggregateNullsafe.name»Stream based on a AggregateStreamId.
             */
            public class «id.name»StreamFactory implements IdStreamFactory {
            
                @Override
                public final Stream createStream(final StreamId streamId) {
                    final String id = streamId.getSingleParamValue();
                    return new «id.aggregateNullsafe.name»Stream(«id.name».valueOf(id));
              }
            
                @Override
                public boolean containsType(final StreamId streamId) {
                    return streamId.getName().equals(«id.name».TYPE.asString());
              }
            
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
        
    }

}

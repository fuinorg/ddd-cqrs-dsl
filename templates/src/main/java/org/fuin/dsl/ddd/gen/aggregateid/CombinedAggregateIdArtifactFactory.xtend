package org.fuin.dsl.ddd.gen.aggregateid

import java.util.ArrayList
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GeneratedArtifact

/**
 * Generates an aggregate id with a single delegate factory: the {@link SimpleAggregateIdArtifactFactory}
 * creates the complete self-contained class for a "base UUID" id. In all other cases the
 * {@link AbstractAggregateIdArtifactFactory} and the {@link FinalAggregateIdArtifactFactory} together
 * create the abstract base class and the final class that extends it.
 * <p>
 * Do not combine this factory with a separately configured {@link SimpleAggregateIdArtifactFactory},
 * {@link AbstractAggregateIdArtifactFactory} or {@link FinalAggregateIdArtifactFactory} - it already
 * creates those artifacts, so the id would be generated twice. Configure either this factory alone or
 * the delegates directly.
 */
class CombinedAggregateIdArtifactFactory extends AbstractSource<AggregateId> {

    val SimpleAggregateIdArtifactFactory simple;

    val AbstractAggregateIdArtifactFactory normalAbstract;

    val FinalAggregateIdArtifactFactory normalFinal;

    new() {
        simple = new SimpleAggregateIdArtifactFactory
        normalAbstract = new AbstractAggregateIdArtifactFactory
        normalFinal = new FinalAggregateIdArtifactFactory
    }

    override getModelType() {
        typeof(AggregateId)
    }

    override getTypeKey() {
        TypeKeys.JAVA_AGGREGATE_ID
    }

    override init(ArtifactFactoryConfig config) {
        super.init(config);
        // Every delegate resolves the hint with its own class name to write to its own target folder:
        // the abstract class is regenerated, while the final class goes to the non-generated sources.
        simple.initFrom(config, SimpleAggregateIdArtifactFactory.name)
        normalAbstract.initFrom(config, AbstractAggregateIdArtifactFactory.name)
        normalFinal.initFrom(config, FinalAggregateIdArtifactFactory.name)
    }

    override create(AggregateId aggregateId, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (aggregateId.base !== null && aggregateId.base.name == "UUID") {
            return simple.create(aggregateId, context, preparationRun)
        }

        // Both delegates only register their code references and return null during the preparation run
        val abstractArtifacts = normalAbstract.create(aggregateId, context, preparationRun)
        val finalArtifacts = normalFinal.create(aggregateId, context, preparationRun)
        if (preparationRun) {
            return null
        }

        val artifacts = new ArrayList<GeneratedArtifact>()
        artifacts.addAll(abstractArtifacts)
        artifacts.addAll(finalArtifacts)
        return artifacts

    }

}

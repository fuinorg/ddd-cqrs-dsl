package org.fuin.dsl.ddd.gen.entityid

import java.util.ArrayList
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GeneratedArtifact

/**
 * Generates an entity id with a single delegate factory: the {@link SimpleEntityIdArtifactFactory}
 * creates the complete self-contained class for a "base Integer" id. In all other cases the
 * {@link AbstractEntityIdArtifactFactory} and the {@link FinalEntityIdArtifactFactory} together
 * create the abstract base class and the final class that extends it.
 * <p>
 * Do not combine this factory with a separately configured {@link SimpleEntityIdArtifactFactory},
 * {@link AbstractEntityIdArtifactFactory} or {@link FinalEntityIdArtifactFactory} - it already
 * creates those artifacts, so the id would be generated twice. Configure either this factory alone or
 * the delegates directly.
 */
class CombinedEntityIdArtifactFactory extends AbstractSource<EntityId> {

    val SimpleEntityIdArtifactFactory simple;

    val AbstractEntityIdArtifactFactory normalAbstract;

    val FinalEntityIdArtifactFactory normalFinal;

    new() {
        simple = new SimpleEntityIdArtifactFactory
        normalAbstract = new AbstractEntityIdArtifactFactory
        normalFinal = new FinalEntityIdArtifactFactory
    }

    override getModelType() {
        typeof(EntityId)
    }

    override getTypeKey() {
        TypeKeys.JAVA_ENTITY_ID
    }

    override init(ArtifactFactoryConfig config) {
        super.init(config);
        // Every delegate resolves the hint with its own class name to write to its own target folder:
        // the abstract class is regenerated, while the final class goes to the non-generated sources.
        simple.initFrom(config, SimpleEntityIdArtifactFactory.name)
        normalAbstract.initFrom(config, AbstractEntityIdArtifactFactory.name)
        normalFinal.initFrom(config, FinalEntityIdArtifactFactory.name)
    }

    override create(EntityId entityId, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (entityId.base !== null && entityId.base.name == "Integer") {
            return simple.create(entityId, context, preparationRun)
        }

        // Both delegates only register their code references and return null during the preparation run
        val abstractArtifacts = normalAbstract.create(entityId, context, preparationRun)
        val finalArtifacts = normalFinal.create(entityId, context, preparationRun)
        if (preparationRun) {
            return null
        }

        val artifacts = new ArrayList<GeneratedArtifact>()
        artifacts.addAll(abstractArtifacts)
        artifacts.addAll(finalArtifacts)
        return artifacts

    }

}

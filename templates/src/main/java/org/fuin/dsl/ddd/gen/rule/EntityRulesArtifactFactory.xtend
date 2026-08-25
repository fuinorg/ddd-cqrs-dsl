package org.fuin.dsl.ddd.gen.rule

import org.fuin.dsl.cqrs.cqrsDsl.Entity

/** Creates the class that verifies everything one entity declares. */
class EntityRulesArtifactFactory extends AbstractRulesArtifactFactory<Entity> {

    override getModelType() {
        typeof(Entity)
    }

}

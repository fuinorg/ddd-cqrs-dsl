package org.fuin.dsl.ddd.gen.rule

import org.fuin.dsl.cqrs.cqrsDsl.Aggregate

/** Creates the class that verifies everything one aggregate declares. */
class AggregateRulesArtifactFactory extends AbstractRulesArtifactFactory<Aggregate> {

    override getModelType() {
        typeof(Aggregate)
    }

}

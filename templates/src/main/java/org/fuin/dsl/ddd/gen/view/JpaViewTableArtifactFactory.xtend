package org.fuin.dsl.ddd.gen.view

import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.dsl.ddd.gen.base.AbstractJpaTableArtifactFactory

/**
 * Renders JPA entity classes from the "JpaHint" hints declared inside a {@code view} body. Because it
 * generates per view, the entities end up in the same package as the view's other artifacts. The
 * actual rendering lives in {@link AbstractJpaTableArtifactFactory}; this class only binds it to the
 * {@code View} model type and points it at the view's own hints.
 */
class JpaViewTableArtifactFactory extends AbstractJpaTableArtifactFactory<View> {

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.JAVA_VIEW_JPA_TABLE
    }

    override protected jpaHints(View view) {
        view.hints.filter[name == "JpaHint"]
    }

}

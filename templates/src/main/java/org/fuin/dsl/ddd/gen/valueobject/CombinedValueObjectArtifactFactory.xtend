package org.fuin.dsl.ddd.gen.valueobject

import java.util.ArrayList
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

/**
 * Generates a value object with a single delegate factory: the {@link SimpleStringValueObjectArtifactFactory}
 * creates the complete class if possible. In all other cases the {@link AbstractValueObjectArtifactFactory} and
 * the {@link FinalValueObjectArtifactFactory} together create the abstract base class and the final class that
 * extends it.
 * <p>
 * Do not combine this factory with a separately configured {@link AbstractValueObjectArtifactFactory} or
 * {@link FinalValueObjectArtifactFactory} - it already creates both artifacts, so the value object would be
 * generated twice. Configure either this factory alone or the delegates directly.
 */
class CombinedValueObjectArtifactFactory extends AbstractSource<ValueObject> {

    val SimpleStringValueObjectArtifactFactory simple;

    val AbstractValueObjectArtifactFactory normalAbstract;

    val FinalValueObjectArtifactFactory normalFinal;

    new() {
        simple = new SimpleStringValueObjectArtifactFactory
        normalAbstract = new AbstractValueObjectArtifactFactory
        normalFinal = new FinalValueObjectArtifactFactory
    }

    override getModelType() {
        typeof(ValueObject)
    }

    override init(ArtifactFactoryConfig config) {
        super.init(config);
        // Every delegate resolves the hint with its own class name to write to its own target folder:
        // the abstract class is regenerated, while the final class goes to the non-generated sources.
        simple.initFrom(config, SimpleStringValueObjectArtifactFactory.name)
        normalAbstract.initFrom(config, AbstractValueObjectArtifactFactory.name)
        normalFinal.initFrom(config, FinalValueObjectArtifactFactory.name)
    }

    override create(ValueObject valueObject, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (valueObject.base !== null && valueObject.base.name == "String" && valueObject.attributes.size == 1) {
            return simple.create(valueObject, context, preparationRun)
        }

        // Both delegates only register their code references and return null during the preparation run
        val abstractArtifacts = normalAbstract.create(valueObject, context, preparationRun)
        val finalArtifacts = normalFinal.create(valueObject, context, preparationRun)
        if (preparationRun) {
            return null
        }

        val artifacts = new ArrayList<GeneratedArtifact>()
        artifacts.addAll(abstractArtifacts)
        artifacts.addAll(finalArtifacts)
        return artifacts

    }

}

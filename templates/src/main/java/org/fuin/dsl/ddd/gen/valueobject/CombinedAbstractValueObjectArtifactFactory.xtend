package org.fuin.dsl.ddd.gen.valueobject

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig

/**
 * Combines the {@link SimpleStringValueObjectArtifactFactory} and the {@link AbstractValueObjectArtifactFactory}.
 * The {@link SimpleStringValueObjectArtifactFactory} will be used for generation if possible. 
 * The {@link AbstractValueObjectArtifactFactory} will be used in all other cases.  
 */
class CombinedAbstractValueObjectArtifactFactory extends AbstractSource<ValueObject> {

    public static val ACTIVE = CombinedAbstractValueObjectArtifactFactory.name

    val SimpleStringValueObjectArtifactFactory simple;

    val AbstractValueObjectArtifactFactory normalAbstract;

    new() {
        simple = new SimpleStringValueObjectArtifactFactory
        normalAbstract = new AbstractValueObjectArtifactFactory
    }

    override getModelType() {
        typeof(ValueObject)
    }

    override init(ArtifactFactoryConfig config) {
        super.init(config);
        simple.init(config)
        normalAbstract.init(config)
    }


    override create(ValueObject valueObject, Map<String, Object> context, boolean preparationRun) throws GenerateException {
        
        if (preparationRun) {

            // Marker for FinalValueObjectArtifactFactory this is activated 
            context.put(ACTIVE, true)

            // No code generation during preparation phase
            return null
        }
        
        if (valueObject.base !== null && valueObject.base.name == "String" && valueObject.attributes.size > 0) {            
            return simple.create(valueObject, context, preparationRun)
        }
        
        return normalAbstract.create(valueObject, context, preparationRun)
        
    }

}

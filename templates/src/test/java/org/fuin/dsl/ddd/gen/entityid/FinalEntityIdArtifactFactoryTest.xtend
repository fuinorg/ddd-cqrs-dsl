package org.fuin.dsl.ddd.gen.entityid

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class FinalEntityIdArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreateMyEntityId() {

        // PREPARE
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.entityid.MyEntityIdConverter", "p.x.entityid.MyEntityIdConverter")
        refReg.putReference("p.x.entityid.AbstractMyEntityId", "p.x.entityid.AbstractMyEntityId")

        val FinalEntityIdArtifactFactory testee = createTestee()
        val EntityId entityId = model.find(typeof(EntityId), "MyEntityId")

        // TEST
        val result = new String(testee.create(entityId, context, false).iterator().next().data)

        // VERIFY
        assertThat(result).isEqualTo("x/entityid/MyEntityId.java".loadAbstractExample)

    }
    @Test
    def void testCreateMyEntity2Id() {
        
        // PREPARE
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.entityid.AbstractMyEntity2Id", "p.x.entityid.AbstractMy2EntityId")

        val FinalEntityIdArtifactFactory testee = createTestee()
        val EntityId entityId = model.find(typeof(EntityId), "MyEntity2Id")

        // TEST
        val result = new String(testee.create(entityId, context, false).iterator().next().data)

        // VERIFY
        assertThat(result).isEqualTo("x/entityid/MyEntity2Id.java".loadAbstractExample)
        
    }    

    @Test
    def void testCreateMyEntity3Id() {

        // PREPARE
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.entityid.MyEntity3IdConverter", "p.x.entityid.MyEntity3IdConverter")
        refReg.putReference("p.x.entityid.AbstractMyEntity3Id", "p.x.entityid.AbstractMyEntity3Id")

        val FinalEntityIdArtifactFactory testee = createTestee()
        val EntityId entityId = model.find(typeof(EntityId), "MyEntity3Id")

        // TEST
        val result = new String(testee.create(entityId, context, false).iterator().next().data)

        // VERIFY
        assertThat(result).isEqualTo("x/entityid/MyEntity3Id.java".loadAbstractExample)

    }
    @Test
    def void testCreateMyEntity4Id() {
        
        // PREPARE
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.entityid.AbstractMyEntity4Id", "p.x.entityid.AbstractMy4EntityId")

        val FinalEntityIdArtifactFactory testee = createTestee()
        val EntityId entityId = model.find(typeof(EntityId), "MyEntity4Id")

        // TEST
        val result = new String(testee.create(entityId, context, false).iterator().next().data)

        // VERIFY
        assertThat(result).isEqualTo("x/entityid/MyEntity4Id.java".loadAbstractExample)
        
    }
        
    private def createTestee() {
        val factory = new FinalEntityIdArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("entityId", FinalEntityIdArtifactFactory.name, "project", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_ABSTRACT))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/entityid.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

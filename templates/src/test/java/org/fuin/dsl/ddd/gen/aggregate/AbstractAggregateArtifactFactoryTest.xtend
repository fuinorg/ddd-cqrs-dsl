package org.fuin.dsl.ddd.gen.aggregate

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import org.junit.jupiter.api.^extension.ExtendWith
import org.eclipse.xtext.testing.extensions.InjectionExtension

@InjectWith(CqrsDslInjectorProvider)
@ExtendWith(InjectionExtension) 
class AbstractAggregateArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testAbstractAggregateA() {
        testAggregate("AggregateA")
    }

    @Test
    def void testAbstractAggregateB() {
        testAggregate("AggregateB")
    }
    
    @Test
    def void testAbstractAggregateC() {
        testAggregate("AggregateC")
    }

    @Test
    def void testAbstractAggregateD() {
        testAggregate("AggregateD")
    }

    /** An operation referencing a service declared inline: nested interface, so no import. */
    @Test
    def void testAbstractAggregateE() {
        testAggregate("AggregateE")
    }

    /** An operation referencing a service declared outside it: top-level interface, so imported. */
    @Test
    def void testAbstractAggregateF() {
        testAggregate("AggregateF")
    }

    private def testAggregate(String aggregateName) {

        // PREPARE
        val abstractName = "Abstract" + aggregateName
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.types.Integer", "java.lang.Integer")
        // A service declared outside the operation is a top-level interface, registered by
        // ServiceArtifactFactory and imported like any other type. The inline ones need no entry here:
        // the factory under test registers those itself, unqualified, because they end up nested in the
        // class it generates.

        val AbstractAggregateArtifactFactory testee = createTestee()
        val Aggregate aggregate = model.find(typeof(Aggregate), aggregateName)

        // TEST
        val result = new String(testee.create(aggregate, context, false).iterator().next().data)

        // VERIFY
        // Mirror what was produced to target/ so a golden can be created/diffed without relying
        // on the (truncated) assertion message.
        val actualFile = new java.io.File("target/actual-java/aggregates/" + abstractName + ".java")
        actualFile.parentFile.mkdirs
        java.nio.file.Files.write(actualFile.toPath, result.getBytes("UTF-8"))
        assertThat(result).isEqualTo(("x/aggregates/" + abstractName + ".java").loadAbstractExample)

    }

    private def createTestee() {
        val factory = new AbstractAggregateArtifactFactory() {}
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("abstractAggregate", AbstractAggregateArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_ABSTRACT))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/aggregate.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

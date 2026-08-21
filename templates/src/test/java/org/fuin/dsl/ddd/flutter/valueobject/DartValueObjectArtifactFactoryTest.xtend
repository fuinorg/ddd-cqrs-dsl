package org.fuin.dsl.ddd.flutter.valueobject

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*

/** Tests the Dart class generated from a single-value value object. */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartValueObjectArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testSingleValuedWithInvariants() {

        // PREPARE
        val testee = createTestee()
        val vo = model.find(typeof(ValueObject), "CategoryName")

        // TEST
        val result = testee.create(vo, new HashMap<String, Object>(), false)

        // VERIFY
        val artifact = result.iterator.next
        assertThat(artifact.pathAndName).isEqualTo("categories/category_name.dart")
        assertThat(new String(artifact.data, "UTF-8"))
            .isEqualTo("categories/category_name.dart".loadDartExample)
    }

    @Test
    def void testNothingIsGeneratedDuringPreparation() {
        assertThat(createTestee().create(model.find(typeof(ValueObject), "CategoryName"),
            new HashMap<String, Object>(), true)).isNull
    }

    private def createTestee() {
        val factory = new DartValueObjectArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartValueObject",
            DartValueObjectArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

package org.fuin.dsl.ddd.flutter.entityid

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
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

/** Tests the Dart class generated from an aggregate id or an entity id. */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartIdArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testAggregateId() {
        val artifact = generate()
        assertThat(artifact.pathAndName).isEqualTo("categories/category_id.dart")
        assertThat(new String(artifact.data, "UTF-8"))
            .isEqualTo("categories/category_id.dart".loadDartExample)
    }

    @Test
    def void testTheTypeConstantIsDerivedTheWayTheJvmDerivesIt() {
        // Upper snake case of the aggregate it identifies. That constant is the event stream's name and
        // half of every stored entity-id-path, so the two targets must agree about it by construction:
        // change the convention on one side only and the streams of the other stop resolving.
        assertThat(source).contains("static const String type = 'CATEGORY';")
    }

    @Test
    def void testItReadsEitherFormAndWritesOnlyTheTypedOne() {
        // A bare identifier is refused by the write side with a 400 that does not say which field was
        // wrong, so nothing here ever produces one.
        assertThat(source).contains("factory CategoryId.fromWire(String wire)")
        assertThat(source).contains("String get typed => '$type $value';")
        assertThat(source).contains("String toString() => typed;")
    }

    private def String source() {
        new String(generate().data, "UTF-8")
    }

    private def generate() {
        val factory = new DartIdArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartId",
            DartIdArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        val model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return factory.create(model.find(typeof(AggregateId), "CategoryId"),
            new HashMap<String, Object>(), false).iterator.next
    }

}

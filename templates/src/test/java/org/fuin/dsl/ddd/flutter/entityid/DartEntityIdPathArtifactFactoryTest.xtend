package org.fuin.dsl.ddd.flutter.entityid

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType
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

/**
 * Tests the client type generated for a declared entity identifier path.
 *
 * <p>A path travels as typed segments separated by a slash, and a client holding it as a bare string can
 * say nothing about what it addresses. These pin what the generated type adds: the shape, a refusal of a
 * path that does not have it, and a <code>last</code> typed to what the path points at.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartEntityIdPathArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testTheShapeTravelsAsAConstant() {
        val generated = generate("AccountTransactionPath")

        assertThat(generated).contains("static const EntityIdPathSpec shape = EntityIdPathSpec(<EntityIdPathStep>[")
        assertThat(generated).contains("EntityIdPathStep('ANNUAL_TRANSACTIONS'),")
        assertThat(generated).contains("EntityIdPathStep('TRANSACTION'),")
    }

    @Test
    def void testAStepStatesItsRangeOnlyWhenItIsNotExactlyOne() {
        // Unbounded is 'null' on this side, which is how Dart spells "no ceiling".
        assertThat(generate("NestedTransactionPath"))
            .contains("EntityIdPathStep('TRANSACTION', min: 1, max: null),")
    }

    @Test
    def void testAPathOfTheWrongShapeIsRefusedAsItIsRead() {
        // A path of the wrong shape addresses something other than the caller believes.
        val generated = generate("AccountTransactionPath")

        assertThat(generated).contains("if (!shape.matchesSegments(segments)) {")
        assertThat(generated).contains("throw ArgumentError.value(wire, 'wire',")
    }

    @Test
    def void testWhatThePathAddressesIsTypedAndNamed() {
        val generated = generate("AccountTransactionPath")

        assertThat(generated).contains("AccountTransactionId get last => AccountTransactionId.fromWire(segments.last);")
        // The wire type of the leaf, which is what a row's identity is matched on.
        assertThat(generated).contains("static const String type = 'TRANSACTION';")
    }

    private def String generate(String pathName) {
        val context = new HashMap<String, Object>()
        val path = model.find(typeof(EntityIdPathType), pathName)
        return new String(createTestee.create(path, context, false).iterator.next.data, "UTF-8")
    }

    private def createTestee() {
        val factory = new DartEntityIdPathArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartEntityIdPath",
            DartEntityIdPathArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/entity-id-path.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

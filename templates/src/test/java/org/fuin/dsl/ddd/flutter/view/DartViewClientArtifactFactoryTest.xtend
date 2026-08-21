package org.fuin.dsl.ddd.flutter.view

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.View
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

/** Tests the typed client generated for a view. */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartViewClientArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testClient() {
        val artifact = generate()
        assertThat(artifact.pathAndName).isEqualTo("categories/categoryview/category_view_client.dart")
        assertThat(new String(artifact.data, "UTF-8"))
            .isEqualTo("categories/categoryview/category_view_client.dart".loadDartExample)
    }

    @Test
    def void testAMethodNameBecomesAPathSegmentTheSameWayEverywhere() {
        // camel case to kebab case, as the generated Spring and JAX-RS contracts do it - so the three
        // cannot drift apart.
        val src = new String(generate().data, "UTF-8")
        assertThat(src).contains("'$basePath/list-categories'")
        assertThat(src).contains("'$basePath/list-by-type'")
    }

    @Test
    def void testAParameterTravelsAsTheWireFormOfItsType() {
        // The method takes the typed value; what goes into the query is what the server reads.
        assertThat(new String(generate().data, "UTF-8"))
            .contains("query: <String, Object?>{'kind': kind.wireName}")
    }

    @Test
    def void testItOwnsNoTransport() {
        // The package stays pure Dart: the client knows the paths and the types, and nothing about
        // bearer tokens, retries or which HTTP library is in use.
        val src = new String(generate().data, "UTF-8")
        assertThat(src).contains("final ViewTransport transport;")
        assertThat(src).doesNotContain("dio")
    }

    private def generate() {
        val factory = new DartViewClientArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartViewClient",
            DartViewClientArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        val model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return factory.create(model.find(typeof(View), "CategoryView"),
            new HashMap<String, Object>(), false).iterator.next
    }

}

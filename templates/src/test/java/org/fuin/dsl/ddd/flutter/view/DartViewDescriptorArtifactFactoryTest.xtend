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

/** Tests the const descriptor a generic renderer draws a view from. */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartViewDescriptorArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testDescriptor() {
        val artifact = generate()
        assertThat(artifact.pathAndName)
            .isEqualTo("categories/categoryview/category_view_descriptor.dart")
        assertThat(new String(artifact.data, "UTF-8"))
            .isEqualTo("categories/categoryview/category_view_descriptor.dart".loadDartExample)
    }

    @Test
    def void testTheMethodIdIsThePermissionId() {
        // One string answers both "may I call this" and "what do I call it", so a client that has just
        // been told what it may do can caption it without a second lookup.
        assertThat(source).contains("id: 'CategoryView.listCategories'")
        assertThat(source).contains("id: 'CategoryView.listByType'")
    }

    @Test
    def void testWhatItReturnsDecidesWhatShapeOfScreenItIs() {
        // Several rows is a list; the row's own descriptor is what the renderer draws each one from.
        assertThat(source).contains("kind: MethodKind.list")
        assertThat(source).contains("returns: CategoryDetails.descriptor")
    }

    @Test
    def void testAParameterBecomesAFilterWithItsOwnWording() {
        assertThat(source).contains("params: <AttributeDescriptor>[")
        assertThat(source).contains("values: CategoryType.descriptors")
        assertThat(source).contains("tooltip: 'The side of the ledger to list categories for'")
    }

    @Test
    def void testItNamesTheModuleEnablementIsDecidedOn() {
        // A view is offered when its own module is switched on, not the group it belongs to.
        assertThat(source).contains("module: 'categories.categoryview'")
    }

    private def String source() {
        new String(generate().data, "UTF-8")
    }

    private def generate() {
        val factory = new DartViewDescriptorArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartViewDescriptor",
            DartViewDescriptorArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        val model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return factory.create(model.find(typeof(View), "CategoryView"),
            new HashMap<String, Object>(), false).iterator.next
    }

}

package org.fuin.dsl.ddd.flutter.resourceset

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*

/** Tests the two catalogues that exist once for the whole model: the permission ids and the ARB. */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartResourceSetArtifactFactoriesTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testPermissionIds() {
        val artifact = generate(new DartPermissionIdsArtifactFactory())
        assertThat(artifact.pathAndName).isEqualTo("permission_ids.dart")
        assertThat(new String(artifact.data, "UTF-8")).isEqualTo("permission_ids.dart".loadDartExample)
    }

    @Test
    def void testEveryOperationHasAnId() {
        // An operation cannot ship without one, which on the read side would mean an unchecked read.
        val src = new String(generate(new DartPermissionIdsArtifactFactory()).data, "UTF-8")
        assertThat(src).contains("createCategoryCommand = 'CreateCategoryCommand'")
        assertThat(src).contains("categoryViewListCategories = 'CategoryView.listCategories'")
        // The whole-view form is an assigning shorthand, expanded server-side.
        assertThat(src).contains("categoryViewAll = 'CategoryView.*'")
        assertThat(src).contains("'CategoryView': <String>{categoryViewListCategories, categoryViewListByType}")
        assertThat(src).contains("createCategoryCommand: 'Category'")
    }

    @Test
    def void testArb() {
        val artifact = generate(new DartArbArtifactFactory())
        assertThat(artifact.pathAndName).isEqualTo("l10n/melkheftken_en.arb")
        assertThat(new String(artifact.data, "UTF-8"))
            .isEqualTo("l10n/melkheftken_en.arb".loadDartExample)
    }

    @Test
    def void testTheArbIsKeyedByBundleBecauseFlutterHasOneFileForEverything() {
        // The JVM side has one properties file per module; Flutter has one per locale for the whole
        // application, and two modules may each caption something "name".
        val src = new String(generate(new DartArbArtifactFactory()).data, "UTF-8")
        assertThat(src).contains('"Categories.name.label": "Name"')
        assertThat(src).contains('"Categoryview.CategoryView.listCategories.label"')
    }

    @Test
    def void testWordingTheModelNeverGaveHasNoEntryInAnyLanguage() {
        // A bundle translates what the model states; it does not add to it.
        assertThat(new String(generate(new DartArbArtifactFactory()).data, "UTF-8"))
            .doesNotContain("CategoryDetails.label")
    }

    private def generate(ArtifactFactory<ResourceSet> factory) {
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dart",
            factory.class.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        val ResourceSet rs = model.eResource.resourceSet
        return factory.create(rs, new HashMap<String, Object>(), false).iterator.next
    }

}

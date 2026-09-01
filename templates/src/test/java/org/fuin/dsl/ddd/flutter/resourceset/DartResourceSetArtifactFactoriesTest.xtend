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

    /** Every generated Dart file of the example model that can carry wording. */
    static val DART_EXAMPLES = #[
        "module_catalogue.dart",
        "categories/category_details.dart",
        "categories/category_id.dart",
        "categories/category_name.dart",
        "categories/category_type.dart",
        "categories/create_category_command.dart",
        "categories/remove_category_command.dart",
        "categories/rename_category_command.dart",
        "categories/categoryview/category_view_descriptor.dart"
    ]

    /** The bundle and key of one <code>ModelText</code> in a generated file. */
    static val MODEL_TEXT = java.util.regex.Pattern.compile(
        "bundle: '([^']+)',\\s*\\n\\s*key: '([^']+)'")

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
        assertThat(src).contains('"Categories.CategoryName.label": "Name"')
        assertThat(src).contains('"Categoryview.CategoryView.listCategories.label"')
    }

    @Test
    def void testAKeyNamesWhereTheWordingCameFromRatherThanWhereItIsUsed() {
        // The bundle alone does not make a key unique. A bundle holding a person and a role has two
        // attributes called "id" and two called "name", and keying them by the bare name collapses them
        // onto one entry with one of the captions arbitrarily winning - which is what a TreeMap does
        // silently. So wording taken from a type is keyed by that type, and wording an attribute
        // overrides is keyed by the attribute and its owner.
        val src = new String(generate(new DartArbArtifactFactory()).data, "UTF-8")
        // Taken from the value object, so one entry serves the row and every command form that sets it.
        assertThat(src).contains('"Categories.CategoryName.label"')
        // Overridden on the row, so it is keyed under the row rather than under a bare "name".
        assertThat(src).contains('"Categories.CategoryDetails.name.label"')
        assertThat(src).doesNotContain('"Categories.name.label"')
        // A parameter is keyed by the method it belongs to, for the same reason.
        assertThat(src).contains('"Categoryview.CategoryView.listByType.kind.label"')
        assertThat(src).doesNotContain('"Categoryview.kind.label"')
        // An enum instance is keyed by its enum: two enums may each have an ACTIVE.
        assertThat(src).contains('"Categories.CategoryType.INCOME.label"')
        assertThat(src).doesNotContain('"Categories.INCOME.label"')
        // An id states wording like anything else, and leaving ids out left those keys pointing at
        // nothing at all.
        assertThat(src).contains('"Categories.CategoryId.label": "Category ID"')
    }

    @Test
    def void testAModuleIsWordedOncePerGroupTheUserNavigatesBy() {
        // A context is split over a top-level module and the view modules under it, but the hub has one
        // entry per group, so only one of them is ever looked up. Writing an entry for each is what put
        // wording in the bundle that no descriptor could ask for.
        val src = new String(generate(new DartArbArtifactFactory()).data, "UTF-8")
        assertThat(src).contains('"Categories.categories.label": "Categories"')
        assertThat(src).doesNotContain("categories.categoryview.label")
    }

    @Test
    def void testEveryKeyADescriptorLooksUpIsInTheBundleAndNothingElseIs() {
        // The assertion whose absence let the two sides drift. The descriptors and the ARB are written
        // by different factories, and nothing ever compared them - so an attribute was keyed by its own
        // name in one and by its type's name in the other, and a bundle of 787 entries had 242 nobody
        // could reach and 242 reaching for nothing.
        val arb = "l10n/melkheftken_en.arb".loadDartExample
        val used = newTreeSet(null)
        for (example : DART_EXAMPLES) {
            val src = example.loadDartExample
            val matcher = MODEL_TEXT.matcher(src)
            while (matcher.find) {
                for (suffix : #["slabel", "label", "tooltip", "prompt", "message"]) {
                    val key = '"' + matcher.group(1) + "." + matcher.group(2) + "." + suffix + '"'
                    if (arb.contains(key)) {
                        used.add(key)
                    }
                }
                assertThat(arb)
                    .describedAs("%s looks up %s.%s, which the bundle does not carry",
                        example, matcher.group(1), matcher.group(2))
                    .contains('"' + matcher.group(1) + "." + matcher.group(2) + ".")
            }
        }
        for (line : arb.split("\\n").filter[contains('": "')].filter[!contains("@@locale")]) {
            val key = line.trim.substring(0, line.trim.indexOf(':')).trim
            // A refusal is the exception, in both senses. It is written for the *server*, which composes
            // the sentence it refuses with, so no Dart descriptor has to name it for it to be needed -
            // and the descriptors that do name one (a rule shown under a disabled action) are a subset.
            if (!key.contains("Exception.message")) {
                assertThat(used)
                    .describedAs("the bundle carries %s, which no descriptor looks up", key)
                    .contains(key)
            }
        }
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

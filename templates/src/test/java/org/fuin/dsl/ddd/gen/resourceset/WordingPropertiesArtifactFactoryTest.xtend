package org.fuin.dsl.ddd.gen.resourceset

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

/**
 * Tests the JVM half of the wording catalogue.
 *
 * <p>The Flutter half reads one file for the whole application and carries the bundle in every key; the
 * JVM reads one file per bundle and carries it in the file name. Both come from one walk, so what these
 * tests are really pinning is that the split says the same thing the ARB does.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class WordingPropertiesArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testOneFilePerBundleNamedTheWayAnAnnotationAsksForIt() {
        // The name is the contract: @Label(bundle = "Categories") resolves ResourceBundle base name
        // "Categories", so the file has to be Categories.properties and nothing else.
        assertThat(generate.map[pathAndName]).contains("Categories.properties")
    }

    @Test
    def void testTheBundleBecomesTheFileNameAndLeavesTheKey() {
        // Flutter: "Categories.CategoryName.label". The JVM: "CategoryName.label" in Categories.properties.
        val src = contentOf("Categories.properties")

        assertThat(src).contains("CategoryName.label=Name")
        assertThat(src).doesNotContain("Categories.CategoryName")
    }

    @Test
    def void testAKeyWithDotsInItKeepsThemAllButTheFirst() {
        // A view method is keyed "CategoryView.listCategories", and a module by its dotted name - only
        // the leading segment is the bundle.
        assertThat(contentOf("Categoryview.properties")).contains("CategoryView.listCategories.label=")
    }

    @Test
    def void testAnEmptyEnglishBundleEndsTheSearchBeforeTheMachinesOwnLanguage() {
        // ResourceBundle searches the default locale's candidates before the base file, so on a German
        // machine asking for English answers German when only a _de bundle exists. A present _en file
        // stops that, and inherits every value from the base through the parent chain.
        assertThat(generate.map[pathAndName]).contains("Categories_en.properties")
        assertThat(contentOf("Categories_en.properties"))
            .contains("Deliberately empty")
            .doesNotContain("CategoryName.label=")
    }

    @Test
    def void testItSaysWhereItCameFromAndWhereATranslationGoes() {
        // A generated file nobody can place is a file somebody edits by hand.
        assertThat(contentOf("Categories.properties"))
            .startsWith("# Categories - what the model states")
            .contains("Categories_de.properties")
    }

    private def contentOf(String name) {
        new String(generate.findFirst[pathAndName.endsWith(name)].data, "UTF-8")
    }

    private def Iterable<GeneratedArtifact> generate() {
        val factory = new WordingPropertiesArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("wordingProperties",
            WordingPropertiesArtifactFactory.name, "shared", "genMainRes")
        config.init(new DefaultContext(), null)
        factory.init(config)
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return factory.create(model.eResource.resourceSet, new HashMap<String, Object>(), false)
    }

}

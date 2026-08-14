package org.fuin.dsl.ddd.gen.resourceset

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

/**
 * Tests the generated permission catalogue. The property under test is <b>exhaustiveness</b>: every
 * command and every view method has to appear, because an operation that ships without an entry is
 * exactly the failure the generated catalogue exists to make impossible.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class PermissionCatalogueArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testEveryViewMethodIsOneReadPermission() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/view.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val doc = artifact(result, "PERMISSIONS.md")
        assertThat(doc.module).isEqualTo("shared")
        assertThat(doc.folder).isEqualTo("genMainRes")

        val md = new String(doc.data, "UTF-8")
        assertThat(md).contains("## p / x.m")
        assertThat(md).contains("### View `PersonListView` (`/persons`)")

        // One entry per method - the method, not the view.
        assertThat(md).contains("| `PersonListView.listPersons` | List<PersonListItem> |")
        assertThat(md).contains("| `PersonListView.findPerson` | optional PersonListItem |")
        assertThat(md).contains("| `PersonListView.countPersons` | Integer |")

        // The doc comment travels with the entry, so a role editor can show what it means.
        assertThat(md).contains("Returns the persons matching the optional filter.")

        // The whole-view id exists for assigning, and is called out as not being an enforcement unit.
        assertThat(md).contains("Whole-view id: `PersonListView.*`")
    }

    @Test
    def void testViewMethodsBecomeJavaConstants() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/view.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val java = artifact(result, "p/shared/domain/PermissionIds.java")
        assertThat(java.module).isEqualTo("shared")
        assertThat(java.folder).isEqualTo("genMainJava")

        val src = new String(java.data, "UTF-8")
        assertThat(src).contains("package p.shared.domain;")
        assertThat(src).contains(
            'public static final String PERSON_LIST_VIEW_LIST_PERSONS = "PersonListView.listPersons";')
        assertThat(src).contains(
            'public static final String PERSON_LIST_VIEW_FIND_PERSON = "PersonListView.findPerson";')
        assertThat(src).contains(
            'public static final String PERSON_LIST_VIEW_COUNT_PERSONS = "PersonListView.countPersons";')
        assertThat(src).contains('public static final String PERSON_LIST_VIEW_ALL = "PersonListView.*";')

        // ALL is what a stored role definition is validated against, so it holds the single operations
        // and NOT the group - a group is not itself an operation.
        assertThat(src).contains("Set<String> ALL = Set.of(")
        assertThat(src).contains("PERSON_LIST_VIEW_LIST_PERSONS")
        assertThat(src).doesNotContain("PERSON_LIST_VIEW_ALL,")

        // VIEW_METHODS is what a whole-view grant expands through, so it lists every method of the view.
        assertThat(src).contains(
            'Map.entry("PersonListView", Set.of(PERSON_LIST_VIEW_LIST_PERSONS, PERSON_LIST_VIEW_FIND_PERSON, PERSON_LIST_VIEW_COUNT_PERSONS))')
    }

    @Test
    def void testEveryCommandIsOneWritePermission() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/command.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val md = new String(artifact(result, "PERMISSIONS.md").data, "UTF-8")
        assertThat(md).contains("### Commands (write permissions)")

        // All six, including the two that target an aggregate method.
        assertThat(md).contains("| `CommandA` |")
        assertThat(md).contains("| `CommandB` |")
        assertThat(md).contains("| `CommandC` |")
        assertThat(md).contains("| `CommandD` |")
        assertThat(md).contains("| `CommandE` | Customer.rename |")
        assertThat(md).contains("| `CommandF` | Customer.rename |")

        val src = new String(artifact(result, "p/shared/domain/PermissionIds.java").data, "UTF-8")
        assertThat(src).contains('public static final String COMMAND_A = "CommandA";')
        assertThat(src).contains('public static final String COMMAND_F = "CommandF";')

        // No view in this model, so the expansion map is empty rather than absent.
        assertThat(src).contains("Map<String, Set<String>> VIEW_METHODS = Map.ofEntries(")
    }

    @Test
    def void testNothingIsGeneratedDuringPreparation() {
        assertThat(new PermissionCatalogueArtifactFactory().create(model("/view.cqrs"),
            new HashMap<String, Object>(), true)).isNull
    }

    @Test
    def void testModelWithoutAnyOperationGetsNoCatalogue() {
        // Value objects alone are no operation surface. Emitting an empty catalogue would name a target
        // module the project may have no reason to own.
        assertThat(createTestee().create(model("/valueobject.cqrs"), new HashMap<String, Object>(), false)).isNull
    }

    private def artifact(Iterable<org.fuin.srcgen4j.commons.GeneratedArtifact> result, String pathAndName) {
        val found = result.findFirst[it.pathAndName == pathAndName]
        assertThat(found).describedAs("Artifact '" + pathAndName + "' in " + result.map[it.pathAndName].toList).
            isNotNull
        return found
    }

    private def createTestee() {
        val factory = new PermissionCatalogueArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("permissionCatalogue",
            PermissionCatalogueArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def ResourceSet model(String resource) {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource(resource)))
        validationTester.assertNoErrors(model)
        return model.eResource.resourceSet
    }

}

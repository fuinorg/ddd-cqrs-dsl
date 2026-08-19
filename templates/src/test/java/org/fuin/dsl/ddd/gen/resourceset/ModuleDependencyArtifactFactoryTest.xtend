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
 * Tests the generated module dependency graph.
 *
 * <p>The fixture reproduces the shape this exists for: a sub-module of one group reaches into
 * another, while the other group depends back on the first. Neither <em>module</em> is in a cycle,
 * but the two <em>groups</em> cannot be switched independently - and that is exactly what has to
 * survive into the JSON.</p>
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class ModuleDependencyArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testEdgesComeFromCrossReferencesNotImports() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/moduledeps.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val artifact = artifact(result, "MODULES.json")
        assertThat(artifact.module).isEqualTo("shared")
        assertThat(artifact.folder).isEqualTo("genMainRes")

        val json = new String(artifact.data, "UTF-8")
        assertThat(json).contains('"context": "p"')

        // "journal" imports bankaccounts and refers to nothing in it. An import-based graph would put
        // it in a cycle; only categories (and the external types) are real edges.
        assertThat(json).describedAs("journal must not depend on bankaccounts - it only imports it")
            .containsPattern('"name": "journal",\\s*"group": "journal",\\s*"dependsOn": \\["categories", "types"\\]')

        // The seam does refer to it, so that edge is real.
        assertThat(json).containsPattern('"name": "journal.sync",\\s*"group": "journal",\\s*"dependsOn": \\["bankaccounts"\\]')
    }

    @Test
    def void testGroupsRollUpAndFlagMutualDependency() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/moduledeps.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val json = new String(artifact(result, "MODULES.json").data, "UTF-8")

        // The seam: journal.sync belongs to the journal group and reaches into bankaccounts.
        assertThat(json).contains('"name": "journal.sync"')
        assertThat(json).contains('"group": "journal"')

        // Neither group can be switched without the other, and the file says so.
        assertThat(json).contains('"mutuallyDependent": ["bankaccounts"]')
        assertThat(json).contains('"mutuallyDependent": ["journal"]')

        // A group that nothing depends back on is mutually dependent on nothing.
        assertThat(json).containsPattern('"name": "categories",\\s*"modules": \\["categories"\\],\\s*"dependsOn": \\["types"\\],\\s*"mutuallyDependent": \\[\\]')

        // And the two halves of the seam name each other.
        assertThat(json).containsPattern('"name": "journal",\\s*"modules": \\["journal", "journal.sync"\\]')
    }

    @Test
    def void testOrderIsTopological() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/moduledeps.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val json = new String(artifact(result, "MODULES.json").data, "UTF-8")
        val order = json.substring(json.indexOf('"order": [') + 10)
        val listed = order.substring(0, order.indexOf(']'))

        // Everything a module needs comes before it, so a consumer can enable from the leaves inward.
        assertThat(listed.indexOf("\"types\"")).isLessThan(listed.indexOf("\"categories\""))
        assertThat(listed.indexOf("\"categories\"")).isLessThan(listed.indexOf("\"journal\""))
        assertThat(listed.indexOf("\"journal\"")).isLessThan(listed.indexOf("\"bankaccounts\""))
        assertThat(listed.indexOf("\"bankaccounts\"")).isLessThan(listed.indexOf("\"journal.sync\""))
    }

    @Test
    def void testNothingIsGeneratedDuringPreparation() {
        assertThat(new ModuleDependencyArtifactFactory().create(model("/moduledeps.cqrs"),
            new HashMap<String, Object>(), true)).isNull
    }

    private def artifact(Iterable<org.fuin.srcgen4j.commons.GeneratedArtifact> result, String pathAndName) {
        val found = result.findFirst[it.pathAndName == pathAndName]
        assertThat(found).describedAs("Artifact '" + pathAndName + "' in " + result.map[it.pathAndName].toList).
            isNotNull
        return found
    }

    private def createTestee() {
        val factory = new ModuleDependencyArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("moduleDependencies",
            ModuleDependencyArtifactFactory.name, "module", "folder")
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

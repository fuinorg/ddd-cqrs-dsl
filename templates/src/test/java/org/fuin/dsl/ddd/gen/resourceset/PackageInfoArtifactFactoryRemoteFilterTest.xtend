package org.fuin.dsl.ddd.gen.resourceset

import java.util.HashMap
import java.util.Set
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.fuin.srcgen4j.core.emf.PrimaryResources
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

/**
 * Verifies that a {@link ResourceSet} based factory only generates artifacts for models that
 * originate from the source directory (primary resources) and ignores remotely resolved
 * dependency models.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class PackageInfoArtifactFactoryRemoteFilterTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Test
    def void testOnlyPrimaryNamespacesGenerate() {

        // PREPARE - Two models loaded into the same resource set. The first one represents a model
        // from the source directory, the second one a remotely resolved dependency.
        val primaryModel = parser.parse(Utils.readAsString(class.getResource("/valueobject.cqrs")))
        val resourceSet = primaryModel.eResource.resourceSet
        val remoteModel = parser.parse(Utils.readAsString(class.getResource("/enumobject.cqrs")), resourceSet)

        // Mark only the first resource as primary
        PrimaryResources.install(resourceSet, Set.of(primaryModel.eResource.URI))

        val testee = createTestee()
        val primaryPackages = primaryModel.eAllContents.filter(typeof(Module)).map[testee.asPackage(it)].toSet
        val remotePackages = remoteModel.eAllContents.filter(typeof(Module)).map[testee.asPackage(it)].toSet

        // TEST
        val artifacts = testee.create(resourceSet, new HashMap<String, Object>(), false)

        // VERIFY - At least one artifact and every artifact belongs to a primary (non-remote) package
        assertThat(artifacts).isNotEmpty
        val generatedPackages = artifacts.map[pathAndName.substring(0, pathAndName.lastIndexOf('/')).replace('/', '.')].toSet
        assertThat(generatedPackages).isSubsetOf(primaryPackages)
        assertThat(generatedPackages).doesNotContainAnyElementsOf(remotePackages.filter[!primaryPackages.contains(it)].toList)
        assertThat(generatedPackages.exists[contains(".valueobject")]).isTrue
        assertThat(generatedPackages.exists[contains(".enumobject")]).isFalse
    }

    private def createTestee() {
        val factory = new PackageInfoArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("packageInfo", PackageInfoArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, "tst"))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

}

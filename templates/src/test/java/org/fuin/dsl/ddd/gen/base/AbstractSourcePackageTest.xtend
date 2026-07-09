package org.fuin.dsl.ddd.gen.base

import java.util.Set
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Namespace
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.resourceset.PackageInfoArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.fuin.srcgen4j.core.emf.PrimaryResources
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

/**
 * Tests the package name construction in {@link AbstractSource}: it is derived from the
 * "srcgen4j-default.json" preset (project.module.group.context.namespace), the same for primary
 * (local) and remotely resolved elements.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class AbstractSourcePackageTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Test
    def void testAsPackageUsesProjectContextNamespace() {
        // A local model (primary) and a remote model (non-primary) in the SAME resource set.
        val localModel = parser.parse(Utils.readAsString(class.getResource("/valueobject.cqrs")))
        val resourceSet = localModel.eResource.resourceSet
        val remoteModel = parser.parse(Utils.readAsString(class.getResource("/enumobject.cqrs")), resourceSet)
        PrimaryResources.install(resourceSet, Set.of(localModel.eResource.URI))

        val testee = createTestee()
        val localNs = localModel.eAllContents.filter(typeof(Namespace)).findFirst[name == "valueobject"]
        val remoteNs = remoteModel.eAllContents.filter(typeof(Namespace)).findFirst[name == "enumobject"]

        // Both local and remote elements use the preset package (PackageInfoArtifactFactory targets
        // the ResourceSet type: module "shared", group "domain"), with no primary/remote distinction.
        assertThat(testee.asPackage(localNs)).isEqualTo("p.shared.domain.x.valueobject")
        assertThat(testee.asPackage(remoteNs)).isEqualTo("p.shared.domain.x.enumobject")
    }

    private def createTestee() {
        val factory = new PackageInfoArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("packageInfo", PackageInfoArtifactFactory.name, "module", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

}

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
 * Tests the package name construction in {@link AbstractSource}, especially the behavior when the
 * base package is not set.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class AbstractSourcePackageTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Test
    def void testContextPkgWithoutBasePkg() {
        // An unset base package must not produce a "null." prefix
        assertThat(createTestee(null).contextPkg("household")).isEqualTo("household")
    }

    @Test
    def void testContextPkgWithBasePkg() {
        assertThat(createTestee("a.b").contextPkg("household")).isEqualTo("a.b.household")
    }

    @Test
    def void testAsPackageWithoutBasePkg() {
        val ns = valueObjectNamespace
        assertThat(createTestee(null).asPackage(ns)).isEqualTo("x.valueobject")
    }

    @Test
    def void testAsPackageWithBasePkg() {
        val ns = valueObjectNamespace
        assertThat(createTestee("a.b").asPackage(ns)).isEqualTo("a.b.x.valueobject")
    }

    @Test
    def void testAsPackageForRemoteElementUsesOnlyContextNamespace() {
        // A local model (primary) and a remote model (non-primary) in the SAME resource set.
        val localModel = parser.parse(Utils.readAsString(class.getResource("/valueobject.cqrs")))
        val resourceSet = localModel.eResource.resourceSet
        val remoteModel = parser.parse(Utils.readAsString(class.getResource("/enumobject.cqrs")), resourceSet)
        PrimaryResources.install(resourceSet, Set.of(localModel.eResource.URI))

        val testee = createTestee("a.b")
        val localNs = localModel.eAllContents.filter(typeof(Namespace)).findFirst[name == "valueobject"]
        val remoteNs = remoteModel.eAllContents.filter(typeof(Namespace)).findFirst[name == "enumobject"]

        // Local element keeps the base package; the external element uses only its context.namespace.
        assertThat(testee.asPackage(localNs)).isEqualTo("a.b.x.valueobject")
        assertThat(testee.asPackage(remoteNs)).isEqualTo("x.enumobject")
    }

    private def Namespace valueObjectNamespace() {
        val model = parser.parse(Utils.readAsString(class.getResource("/valueobject.cqrs")))
        return model.eAllContents.filter(typeof(Namespace)).findFirst[name == "valueobject"]
    }

    private def createTestee(String basePkg) {
        val factory = new PackageInfoArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("packageInfo", PackageInfoArtifactFactory.name)
        if (basePkg !== null) {
            config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, basePkg))
        }
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

}

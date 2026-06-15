package org.fuin.dsl.ddd.gen.base

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

package org.fuin.dsl.ddd.gen.resourceset

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class PackageInfoArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testCreate() {

        // PREPARE
        val context = new HashMap<String, Object>()
        val PackageInfoArtifactFactory testee = createTestee()
        val ResourceSet resourceSet = model()

        // TEST
        val result = testee.create(resourceSet, context, false)

        // VERIFY
        // Only the "resourceset" namespace generates code; the "types" namespace is skipped
        assertThat(result).hasSize(1)
        val content = new String(result.get(0).data)
        assertThat(content).isEqualTo("x/resourceset/package-info.java".loadConcreteExample)

    }

    def createTestee() {
        val factory = new PackageInfoArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("packageInfo", PackageInfoArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_CONCRETE))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/resourceset.cqrs")))
        validationTester.assertNoIssues(model)
        return model.eResource.resourceSet
    }

}

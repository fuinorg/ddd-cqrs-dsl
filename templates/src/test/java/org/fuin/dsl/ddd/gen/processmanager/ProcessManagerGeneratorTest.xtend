package org.fuin.dsl.ddd.gen.processmanager

import java.io.File
import java.nio.^file.Files
import java.util.HashMap
import java.util.List
import java.util.Map
import jakarta.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ProcessManager
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.TestExtensions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Round-trip tests for the {@code process-manager} generator (analogous to {@code ViewGeneratorTest}).
 * Every generated file is compared against a golden under
 * {@code src/test/expected-java/tst/x/processmanager/<runtime>/}. Both runtimes are exercised.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class ProcessManagerGeneratorTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testProcessManagerSpring() {
        val pm = model.find(typeof(ProcessManager), "OrderPaymentProcess")
        assertGolden("processmanager", "spring",
            generate(new AbstractProcessManagerArtifactFactory(), AbstractProcessManagerArtifactFactory.name, "spring", pm))
        assertGolden("processmanager", "spring",
            generate(new ProcessManagerArtifactFactory(), ProcessManagerArtifactFactory.name, "spring", pm))
    }

    @Test
    def void testProcessManagerQuarkus() {
        val pm = model.find(typeof(ProcessManager), "OrderPaymentProcess")
        assertGolden("processmanager", "quarkus",
            generate(new AbstractProcessManagerArtifactFactory(), AbstractProcessManagerArtifactFactory.name, "quarkus", pm))
        assertGolden("processmanager", "quarkus",
            generate(new ProcessManagerArtifactFactory(), ProcessManagerArtifactFactory.name, "quarkus", pm))
    }

    private def List<GeneratedArtifact> generate(AbstractSource<?> factory, String factoryClassName, String runtime, EObject el) {
        val Map<String, Object> context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.m.UserCreatedEvent", "com.example.UserCreatedEvent")

        val config = new ArtifactFactoryConfig("artifact", factoryClassName, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.addVariable(new Variable("runtime", runtime))
        config.init(new DefaultContext(), null)
        factory.init(config)

        val result = (factory as AbstractSource<EObject>).create(el, context, false)
        if (result === null) {
            return newArrayList
        }
        return result
    }

    /**
     * Compares each generated artifact against a golden at
     * {@code src/test/expected-java/tst/x/<category>/<runtime>/<ClassName>.java}.
     */
    private def void assertGolden(String category, String runtime, List<GeneratedArtifact> artifacts) {
        for (GeneratedArtifact a : artifacts) {
            val className = a.pathAndName.substring(a.pathAndName.lastIndexOf('/') + 1)
            val actual = new String(a.data, "UTF-8")
            val file = new File("src/test/expected-java/" + TestExtensions.EXAMPLES_ABSTRACT
                + "/x/" + category + "/" + runtime + "/" + className)
            assertThat(actual)
                .describedAs(className)
                .isEqualTo(new String(Files.readAllBytes(file.toPath), "UTF-8"))
        }
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/process-manager.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

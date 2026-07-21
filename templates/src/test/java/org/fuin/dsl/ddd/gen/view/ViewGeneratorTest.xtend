package org.fuin.dsl.ddd.gen.view

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
import org.fuin.dsl.cqrs.cqrsDsl.View
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
 * Round-trip tests for the {@code view} generator. All view factories run over one shared context (a
 * preparation pass then the real pass, mirroring the real generation) so the cross-module reference from
 * the concrete controller to the {@code query.api} contract interface resolves. Every generated file is
 * compared against a golden under {@code src/test/expected-java/tst/x/view/<runtime>/}. Both the Spring
 * and Quarkus runtimes are exercised.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class ViewGeneratorTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testViewSpring() {
        val view = model.find(typeof(View), "PersonListView")
        assertGolden("view", "spring", generateView("spring", view))
    }

    @Test
    def void testViewQuarkus() {
        val view = model.find(typeof(View), "PersonListView")
        assertGolden("view", "quarkus", generateView("quarkus", view))
    }

    private def List<GeneratedArtifact> generateView(String runtime, View view) {
        val Map<String, Object> context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.m.UserCreatedEvent", "com.example.UserCreatedEvent")

        val factories = <AbstractSource<EObject>>newArrayList
        factories.add(configured(new ViewArtifactFactory(), ViewArtifactFactory.name, runtime))
        factories.add(configured(new ViewApiArtifactFactory(), ViewApiArtifactFactory.name, runtime))
        factories.add(configured(new FinalViewArtifactFactory(), FinalViewArtifactFactory.name, runtime))

        // Preparation pass registers cross references (e.g. the contract interface FQN).
        factories.forEach[create(view, context, true)]
        // Real pass collects the artifacts.
        val List<GeneratedArtifact> result = newArrayList
        factories.forEach [ f |
            val artifacts = f.create(view, context, false)
            if (artifacts !== null) {
                result.addAll(artifacts)
            }
        ]
        return result
    }

    private def AbstractSource<EObject> configured(AbstractSource<?> factory, String factoryClassName, String runtime) {
        val config = new ArtifactFactoryConfig("artifact", factoryClassName, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.addVariable(new Variable("runtime", runtime))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory as AbstractSource<EObject>
    }

    /**
     * Compares each generated artifact against a golden at
     * {@code src/test/expected-java/tst/x/<category>/<runtime>/<ClassName>.java}.
     */
    private def void assertGolden(String category, String runtime, List<GeneratedArtifact> artifacts) {
        // Mirror everything that was produced to target/ first, so a failing golden can be diffed
        // and updated from a complete set - the assertion below aborts on the first mismatch.
        for (GeneratedArtifact a : artifacts) {
            val actualFile = new File("target/actual-java/" + category + "/" + runtime + "/" + fileName(a))
            actualFile.parentFile.mkdirs
            Files.write(actualFile.toPath, a.data)
        }
        for (GeneratedArtifact a : artifacts) {
            val file = new File("src/test/expected-java/" + TestExtensions.EXAMPLES_ABSTRACT
                + "/x/" + category + "/" + runtime + "/" + fileName(a))
            assertThat(new String(a.data, "UTF-8"))
                .describedAs(fileName(a))
                .isEqualTo(new String(Files.readAllBytes(file.toPath), "UTF-8"))
        }
    }

    private def String fileName(GeneratedArtifact a) {
        a.pathAndName.substring(a.pathAndName.lastIndexOf('/') + 1)
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/view.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

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
        // Only the "resourceset" namespace generates code; the "types" namespace is skipped. It feeds two
        // modules: its aggregates go to "command.core" and its aggregate/entity ids to "shared", so each of
        // those packages gets its own "package-info.java".
        assertThat(result).hasSize(2)

        val shared = result.findFirst[module == "shared"]
        assertThat(shared).isNotNull
        assertThat(shared.folder).isEqualTo("genMainJava")
        assertThat(shared.pathAndName).isEqualTo("p/shared/domain/x/resourceset/package-info.java")
        assertThat(new String(shared.data)).isEqualTo("x/resourceset/package-info.java".loadConcreteExample)

        val core = result.findFirst[module == "command.core"]
        assertThat(core).isNotNull
        assertThat(core.folder).isEqualTo("genMainJava")
        assertThat(core.pathAndName).isEqualTo("p/command/core/domain/x/resourceset/package-info.java")
        assertThat(new String(core.data)).isEqualTo("x/resourceset/package-info-core.java".loadConcreteExample)

    }

    @Test
    def void testCreateWithModelHintOverride() {

        // PREPARE - the model overrides the module of the "Aggregate" type, exactly like a real project does.
        // The aggregate then lands in "command" instead of the preset's "core", and the "package-info.java"
        // has to follow it.
        val DomainModel model = parser.parse('''
            project p {
                hint SrcGen4J {
                    "types": [
                        { "name": "org.fuin.dsl.cqrs.cqrsDsl.Aggregate", "module": "command", "group": "core.domain" }
                    ]
                }
                context x {
                    namespace types {
                        type String
                    }
                    namespace resourceset {
                        import p.x.types.*
                        aggregate AggregateA identifier AggregateAId {}
                        aggregate-id AggregateAId identifies AggregateA base String {
                            String value
                        }
                    }
                }
            }
        ''')
        validationTester.assertNoIssues(model)
        val context = new HashMap<String, Object>()
        val PackageInfoArtifactFactory testee = createTestee()

        // TEST
        val result = testee.create(model.eResource.resourceSet, context, false)

        // VERIFY - the override wins for the aggregate, the preset still applies to the aggregate id
        assertThat(result).hasSize(2)
        assertThat(result.map[module]).containsExactlyInAnyOrder("command", "shared")
        assertThat(result.findFirst[module == "command"].pathAndName)
            .isEqualTo("p/command/core/domain/x/resourceset/package-info.java")
        assertThat(result.findFirst[module == "shared"].pathAndName)
            .isEqualTo("p/shared/domain/x/resourceset/package-info.java")

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

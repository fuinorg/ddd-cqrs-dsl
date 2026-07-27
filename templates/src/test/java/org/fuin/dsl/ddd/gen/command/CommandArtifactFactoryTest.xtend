package org.fuin.dsl.ddd.gen.command

import java.util.HashMap
import java.util.Map
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Command
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

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Golden tests for the generated command classes.
 * <p>
 * Commands had no coverage here at all, which is how a change to how their attributes are declared
 * could pass unnoticed. The interesting cases are a mandatory attribute against an optional one -
 * the first carries {@code @NotNull} so that "this has to be there" can be read off the type at
 * runtime, the second {@code @Nullable} and nothing else - and a command whose attributes come from
 * the aggregate method it targets rather than from itself.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class CommandArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testCreateCommandA() {

        val context = new HashMap<String, Object>()
        context.codeReferenceRegistry

        testCreate(context, "CommandA")

    }

    @Test
    def void testCreateCommandB() {

        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")

        testCreate(context, "CommandB")

    }

    @Test
    def void testCreateCommandC() {

        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.types.Integer", "java.lang.Integer")

        testCreate(context, "CommandC")

    }

    @Test
    def void testCreateCommandD() {

        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.cmd.MyString", "x.cmd.MyString")

        testCreate(context, "CommandD")

    }

    @Test
    def void testCreateCommandE() {

        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.cmd.CustomerId", "p.x.cmd.CustomerId")

        testCreate(context, "CommandE")

    }

    @Test
    def void testCreateCommandF() {

        // Targets a method AND declares an attribute of its own - both have to be generated.
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.cmd.CustomerId", "p.x.cmd.CustomerId")

        testCreate(context, "CommandF")

    }

    private def testCreate(Map<String, Object> context, String commandName) {

        // PREPARE
        val CommandArtifactFactory testee = createTestee(GenerateOptions.builder.withJaxb()
            .withJaxbElements(false).withJsonb().create()
        )
        val Command command = model.find(typeof(Command), commandName)

        // TEST
        val result = new String(testee.create(command, context, false).iterator().next().data)

        // VERIFY
        // Mirror what was produced to target/ so a golden can be created/diffed without relying
        // on the (truncated) assertion message.
        val actualFile = new java.io.File("target/actual-java/cmd/" + commandName + ".java")
        actualFile.parentFile.mkdirs
        java.nio.file.Files.write(actualFile.toPath, result.getBytes("UTF-8"))
        assertThat(result).isEqualTo(("x/cmd/" + commandName + ".java").loadConcreteExample)

    }

    private def createTestee(GenerateOptions options) {
        val factory = new CommandArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("command", CommandArtifactFactory.name, "module",
            "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_CONCRETE))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.addVariable(new Variable(GenerateOptions.KEY_JSONB, options.jsonb.toString));
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/command.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

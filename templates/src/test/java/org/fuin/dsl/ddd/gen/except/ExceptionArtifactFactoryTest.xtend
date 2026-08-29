package org.fuin.dsl.ddd.gen.except

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Exception
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

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class ExceptionArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreateEventA() {
        testCreate("ExceptionA")
    }

    @Test
    def void testCreateEventB() {
        testCreate("ExceptionB")
    }

    @Test
    def void testCreateEventC() {
        testCreate("ExceptionC")
    }

    @Test
    def void testCreateEventD() {
        testCreate("ExceptionD")
    }

    @Test
    def void testCreateEventE() {
        testCreate("ExceptionE")
    }

    @Test
    def void testCreateEventF() {
        testCreate("ExceptionF")
    }

    private def testCreate(String name) {

        // PREPARE
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.types.Integer", "java.lang.Integer")

        val ExceptionArtifactFactory testee = createTestee()
        val Exception ex = model.find(typeof(Exception), name)

        // TEST
        val result = new String(testee.create(ex, context, false).iterator().next().data)

        // VERIFY
        assertThat(result).isEqualTo(("x/except/" + name + ".java").loadConcreteExample)

    }

    /**
     * A model that names a prefix gets a short identifier: what a support desk quotes, the way the
     * library writes "DDD4J-AGGREGATE_NOT_FOUND". It is what the result's "code" then carries.
     */
    @Test
    def void testCreateWithShortId() {
        val ex = model.find(typeof(Exception), "ExceptionD")

        val result = new String(createTestee("MELK").create(ex, new HashMap<String, Object>(), false)
            .iterator.next.data)

        assertThat(result).contains("public final class ExceptionD extends Exception implements ExceptionShortIdentifable {")
        assertThat(result).contains("import org.fuin.objects4j.common.ExceptionShortIdentifable;")
        assertThat(result).contains("public static final String SHORT_ID = \"MELK-EXCEPTION_D\";")
        assertThat(result).contains("public static final String ELEMENT_NAME = \"exception-d\";")
        assertThat(result).contains("public final String getShortId() {")
    }

    /** A model that names no prefix keeps the exception it had, identified by its class name. */
    @Test
    def void testCreateWithoutShortIdPrefix() {
        val ex = model.find(typeof(Exception), "ExceptionD")

        val result = new String(createTestee.create(ex, new HashMap<String, Object>(), false)
            .iterator.next.data)

        assertThat(result).doesNotContain("SHORT_ID")
        assertThat(result).doesNotContain("ExceptionShortIdentifable")
    }

    private def createTestee(String shortIdPrefix) {
        val factory = new ExceptionArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("exception", ExceptionArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_CONCRETE))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.addVariable(new Variable(GenerateOptions.KEY_SHORT_ID_PREFIX, shortIdPrefix))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def createTestee() {
        val factory = new ExceptionArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("exception", ExceptionArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_CONCRETE))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/exception.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

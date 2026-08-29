package org.fuin.dsl.ddd.gen.except

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Exception
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*

/**
 * Tests the class that carries an exception's data to a client.
 *
 * <p>What pairs it with its exception at runtime is the {@code ExceptionData} type argument, which is
 * what the registry on the server reads off the classpath - so the declaration matters as much as the
 * fields do.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class ExceptionDataArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testItNamesTheExceptionItCarries() {
        val generated = generate("ExceptionD")

        assertThat(generated).contains("public final class ExceptionDData implements ExceptionData<ExceptionD> {")
        assertThat(generated).contains("import org.fuin.ddd4j.core.ExceptionData;")
        assertThat(generated).contains("@ImmutableAfterUnmarshal")
        assertThat(generated).contains("@JsonIgnoreProperties(ignoreUnknown = true)")
    }

    /** The exception's own attributes, so which field a refusal is about is never decided twice. */
    @Test
    def void testItCarriesTheExceptionsAttributes() {
        val generated = generate("ExceptionD")

        assertThat(generated).contains("@JsonProperty(\"a\")")
        assertThat(generated).contains("@JsonProperty(\"b\")")
        assertThat(generated).contains("public ExceptionDData(final ExceptionD ex) {")
        assertThat(generated).contains("this.a = ex.getA();")
        assertThat(generated).contains("this.b = ex.getB();")
    }

    /**
     * A generated exception builds its message from its attributes, so recreating it recreates the
     * message - and no second copy of that string can disagree with the values beside it.
     */
    @Test
    def void testItRecreatesTheException() {
        val generated = generate("ExceptionD")

        assertThat(generated).contains("public ExceptionD toException() {")
        assertThat(generated).contains("return new ExceptionD(a, b);")
        assertThat(generated).doesNotContain("private String message;")
    }

    /** The name it travels under is the exception's own, so the two cannot drift apart. */
    @Test
    def void testItIsTransportedUnderTheExceptionsElementName() {
        assertThat(generate("ExceptionD")).contains("return ExceptionD.ELEMENT_NAME;")
    }

    /** An exception with nothing to say still carries the fact that it happened. */
    @Test
    def void testAnExceptionWithoutAttributes() {
        val generated = generate("ExceptionA")

        assertThat(generated).contains("public final class ExceptionAData implements ExceptionData<ExceptionA> {")
        assertThat(generated).contains("return new ExceptionA();")
    }

    /** Nothing is generated for a flavour the model does not ask for. */
    @Test
    def void testNoDataClassWithoutJackson() {
        val ex = model.find(typeof(Exception), "ExceptionD")

        assertThat(createTestee(false).create(ex, new HashMap<String, Object>(), false)).isNull
    }

    private def String generate(String name) {
        return new String(createTestee(true).create(model.find(typeof(Exception), name),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    private def createTestee(boolean jackson) {
        val factory = new ExceptionDataArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("exceptionData",
            ExceptionDataArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_CONCRETE))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.addVariable(new Variable(GenerateOptions.KEY_JACKSON, String.valueOf(jackson)))
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

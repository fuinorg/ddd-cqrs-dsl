package org.fuin.dsl.ddd.gen.rule

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Key
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.eclipse.xtext.EcoreUtil2.getAllContentsOfType

/**
 * Tests the class a business key derives: the uniqueness rule it stands for.
 *
 * <p>The names are not incidental. A key called <code>Iban</code> derives
 * <code>IbanMustBeUnique</code> over a <code>boolean ibanTaken</code>, which is what the same rule was
 * called when it was written by hand - so migrating a model to the key is a deletion rather than a
 * rename of everything that catches the refusal.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class KeyArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testAKeyDerivesAUniquenessRuleOverTheKeyItself() {
        val generated = generate()

        assertThat(generated).contains("public final class NamePerKindMustBeUnique implements BusinessRule {")
        assertThat(generated).contains("import org.fuin.dsl.cqrs.common.rules.BusinessRule;")
    }

    /** The answer first, then the key - because the refusal names what it refused. */
    @Test
    def void testItDecidesFromTheAnswerAndTheKeysOwnValues() {
        val generated = generate()

        assertThat(generated).contains(
            "public NamePerKindMustBeUnique(final boolean namePerKindTaken, final CategoryName name, final CategoryKind kind) {")
        assertThat(generated).contains("public boolean getNamePerKindTaken() {")
    }

    /**
     * The condition is what the construct means rather than something the model states: a key is
     * satisfied exactly when nothing else holds it.
     */
    @Test
    def void testItRefusesWhenTheKeyIsAlreadyHeld() {
        val generated = generate()

        assertThat(generated).contains("if (!(!namePerKindTaken)) {")
        assertThat(generated).contains("throw new DuplicateNameException(name, kind);")
    }

    /** The key attributes are the type's own, so the refusal is constructed from them by name. */
    @Test
    def void testTheRefusalIsConstructedFromTheKey() {
        assertThat(generate()).contains("public void verify() throws DuplicateNameException {")
    }

    private def String generate() {
        new String(createTestee.create(key, new HashMap<String, Object>(), false)
            .iterator.next.data, "UTF-8")
    }

    private def Key key() {
        model.getAllContentsOfType(Aggregate).findFirst[name == "Category"].keys.head
    }

    private def createTestee() {
        val factory = new KeyArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("key",
            KeyArtifactFactory.name, "command.core", "genMainJava")
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/business-keys.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

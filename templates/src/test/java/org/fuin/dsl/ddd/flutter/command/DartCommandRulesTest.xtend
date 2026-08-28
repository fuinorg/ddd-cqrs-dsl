package org.fuin.dsl.ddd.flutter.command

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

/**
 * Tests the rules a command carries for a client to answer.
 *
 * <p>Advisory and deliberately incomplete: the server verifies everything the model declares, and what
 * travels here is the subset a screen could decide for itself, so it can avoid offering an action that
 * is certain to be refused. Which rules those are is a question the model answers - it used to be one
 * somebody answered by reading Java.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartCommandRulesTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testARuleOverTheRowsOwnStateTravels() {
        val generated = generate("UnassignReceiptCommand")

        assertThat(generated).contains("rules: <RuleDescriptor>[")
        assertThat(generated).contains("rule: 'MustBeAssigned',")
        assertThat(generated).contains(
            "predicate: RuleComparison('assignedEntry', CompareOp.ne, RuleNullOperand()),")
    }

    @Test
    def void testTheRefusalsOwnWordingTravelsWithTheRule() {
        // A greyed-out action and the same action pressed anyway have to say one thing. The template
        // stays a template: it names the thing it refused, and only the client knows which thing.
        assertThat(generate("UnassignReceiptCommand"))
            .contains("reason: r'Receipt ${receipt} backs no journal entry',")
    }

    @Test
    def void testTheRuleAndTheRowMayCallOneValueDifferentThings() {
        // The actuals bind where the rule is used, so the mapping travels rather than being assumed.
        assertThat(generate("UnassignReceiptCommand"))
            .contains("fromAttribute: <String, String>{'assignedEntry': 'assignedEntry'},")
    }

    @Test
    def void testTheIdentityArrivesApartFromTheAttributes() {
        // An aggregate states its identity as 'identifier' and never as an attribute, so it cannot be
        // looked up by name off a row.
        assertThat(generate("UnassignReceiptCommand")).contains("fromIdentity: <String>['receipt'],")
    }

    @Test
    def void testTwoRulesOnOneOperationBothTravel() {
        val generated = generate("IgnoreReceiptCommand")

        assertThat(generated).contains("rule: 'MustNotBeIgnored',")
        assertThat(generated).contains("rule: 'MustNotBeAssigned',")
        assertThat(generated).contains(
            "predicate: RuleComparison('status', CompareOp.ne, RuleValueOperand('IGNORED')),")
    }

    @Test
    def void testARuleNeedingAServiceIsLeftOutEntirely() {
        // A service call is a question only the server can ask, and half a rule is worse than none:
        // the client would decide from what it happened to have.
        val generated = generate("RecordReceiptCommand")

        assertThat(generated).doesNotContain("rules: <RuleDescriptor>[")
        assertThat(generated).doesNotContain("NameMustBeUniqueForType")
    }

    @Test
    def void testACommandWithNoRulesCarriesNoField() {
        // An empty list would read as "nothing guards this", which is never true.
        assertThat(generate("RecordReceiptCommand")).doesNotContain("rules:")
    }

    private def String generate(String commandName) {
        new String(createTestee.create(model.find(typeof(Command), commandName),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    private def createTestee() {
        val factory = new DartCommandArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartCommand",
            DartCommandArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/business-rules.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

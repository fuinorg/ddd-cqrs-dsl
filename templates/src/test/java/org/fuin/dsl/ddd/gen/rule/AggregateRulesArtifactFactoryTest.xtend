package org.fuin.dsl.ddd.gen.rule

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

/**
 * Tests the class that verifies everything one aggregate declares.
 *
 * <p>What it is for is that nothing can be skipped: the write-once operation names its operation here
 * and nothing else, every rule it verifies is inside a generated method, and no method takes a rule
 * from outside - so the model is the complete list of what is enforced.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class AggregateRulesArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testOneMethodPerOperationThatDeclaresRules() {
        val generated = generate("Receipt")

        assertThat(generated).contains("public final class ReceiptRules {")
        assertThat(generated).contains("private final Receipt self;")
        assertThat(generated).contains("void unassignEntry() throws ReceiptNotAssignedException {")

        // "ignore" declares two rules, so it verifies both and declares both refusals.
        assertThat(generated).contains(
            "void ignore() throws WrongStatusException, ReceiptAlreadyAssignedException {")
    }

    @Test
    def void testStateTheCarrierHoldsIsReadOffIt() {
        // The accessors on the generated abstract are what this needs, which is why they exist. The
        // identity comes from 'own-id': it is never a declared attribute, so nothing could name it as a
        // reference - and without it the refusal has no way to say which receipt it refused.
        assertThat(generate("Receipt"))
            .contains("new MustBeAssigned(self.getAssignedEntry(), self.getId()).verify();")
    }

    @Test
    def void testACreatingOperationIsStaticAndTakesOnlyItsArguments() {
        // A constructor cannot hand over a fully initialised instance and has no prior state to read,
        // so its rules can only concern the arguments and whatever the caller looked up.
        val generated = generate("Receipt")

        assertThat(generated).contains("static void record(")
        assertThat(generated).contains("new NameMustBeUniqueForType(nameService.existsForType(receiptNumber)).verify();")
    }

    @Test
    def void testTheServiceArrivesTheWayTheOperationTakesIt() {
        // A service reaches an operation as a parameter named after it; the validator asks for the
        // same thing rather than inventing a way of its own to reach it.
        assertThat(generate("Receipt")).contains("final NameService nameService")
    }

    @Test
    def void testAnAggregateThatDeclaresNoRulesGetsNoClass() {
        // An empty validator would be a file to explain rather than a thing to use.
        assertThat(createTestee().create(model.find(typeof(Aggregate), "JournalEntry"),
            new HashMap<String, Object>(), false)).isNull
    }

    private def String generate(String aggregateName) {
        new String(createTestee.create(model.find(typeof(Aggregate), aggregateName),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    private def createTestee() {
        val factory = new AggregateRulesArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("aggregateRules",
            AggregateRulesArtifactFactory.name, "command.core", "genMainJava")
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

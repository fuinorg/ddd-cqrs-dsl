package org.fuin.dsl.ddd.gen.rule

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
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
 * Tests the class generated for one business rule.
 *
 * <p>The conditions here mirror the shared conformance vectors in <code>ddd-cqrs-contexts</code>, so
 * the Java a rule generates can be read against the Dart evaluator that answers the same condition.
 * They cannot be <em>run</em> against each other from here - this repository knows nothing of
 * cqrs-common, and on the server the semantics are the generated code itself - so the executable half
 * of that comparison belongs downstream, where both exist.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class BusinessRuleArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testARuleIsAClassOverTheValuesItWasHanded() {
        // The whole design in one file: it implements the interface, holds what it decides from, and
        // never reaches for anything else.
        val generated = generate("MustBeAssigned")

        assertThat(generated).contains("public final class MustBeAssigned implements BusinessRule {")
        assertThat(generated).contains("import org.fuin.dsl.cqrs.common.rules.BusinessRule;")
        assertThat(generated).contains(
            "public MustBeAssigned(@Nullable final JournalEntryId assignedEntry, final ReceiptId receipt) {")

        // Mandatoriness governs construction, not the verdict: handing the rule a null where the model
        // says there is always a value is a programming error, and "assignedEntry" has to stay optional
        // precisely because its absence is the case the rule exists to report.
        assertThat(generated).contains("Contract.requireArgNotNull(\"receipt\", receipt);")
        assertThat(generated).doesNotContain("Contract.requireArgNotNull(\"assignedEntry\"")
    }

    @Test
    def void testTheRefusalIsThrownWhenTheConditionDoesNotHold() {
        // "requires" says what must be true, so the generated check is its negation - and the specific
        // exception is what "verify" declares, not the common base.
        val generated = generate("MustBeAssigned")

        assertThat(generated).contains("public void verify() throws ReceiptNotAssignedException {")
        assertThat(generated).contains("if (!(assignedEntry != null)) {")
        assertThat(generated).contains("throw new ReceiptNotAssignedException(receipt);")
    }

    @Test
    def void testTheRefusalIsNamedFromWhatTheRuleHolds() {
        // A rule commonly carries an attribute that plays no part in its condition: "receipt" is there
        // so the exception can name the receipt, which is why the two lists overlap by name.
        assertThat(generate("MustBeAssigned")).contains("throw new ReceiptNotAssignedException(receipt);")

        // And a refusal that needs nothing takes nothing.
        assertThat(generate("MustNotBeAssigned")).contains("throw new ReceiptAlreadyAssignedException();")
    }

    @Test
    def void testEqualityIsValueEqualityAndNeverJavasOwn() {
        // Java's "==" is identity and every attribute here is a value object. Only a comparison
        // against absence is written as "== null", where the two questions are the same one.
        assertThat(generate("MustBeIgnored")).contains("if (!(Objects.equals(status, ReceiptStatus.IGNORED))) {")
        assertThat(generate("MustNotBeIgnored")).contains("if (!(!Objects.equals(status, ReceiptStatus.IGNORED))) {")
        assertThat(generate("MustNotBeAssigned")).contains("if (!(assignedEntry == null)) {")
    }

    @Test
    def void testOrderingADateIsCompareTo() {
        // Nothing but a date is ordered in this language, and a date is a value object rather than a
        // primitive - so there is no ">=" to write.
        assertThat(generate("DueDateMustNotBeBeforeReceiptDate"))
            .contains("if (!(dueDate.compareTo(receiptDate) >= 0)) {")
    }

    @Test
    def void testTheOneBuiltInQuestionAboutACollection() {
        assertThat(generate("MustHaveNoLinks")).contains("if (!(linkedEntries.isEmpty())) {")
        assertThat(generate("MustHaveLinks")).contains("if (!(!(linkedEntries.isEmpty()))) {")
    }

    @Test
    def void testABareBooleanIsTheWholeCondition() {
        // The rule is handed the answer rather than going to look for it, which is what makes a rule
        // needing outside data an ordinary one.
        assertThat(generate("NameMustBeUniqueForType")).contains("if (!(!(nameTaken))) {")
    }

    @Test
    def void testACompoundConditionKeepsTheShapeTheModelWrote() {
        // Every compound node is parenthesised rather than relying on a precedence table, so the Java
        // parses back to the tree the model declared.
        assertThat(generate("MustNotBeLinkedForFinancialChange")).contains(
            "if (!((accountTransactionId == null || (Objects.equals(date, newDate) "
                + "&& Objects.equals(sourceCurrency, newSourceCurrency))))) {")
    }

    @Test
    def void testARuleWithNoConditionIsNotGeneratedAtAll() {
        // Some conditions the language deliberately cannot express, and those classes are written by
        // hand. A stub would be worse than nothing: srcgen4j's "override=false" stops a file being
        // overwritten but not being created, so a stub for a newly declared rule would appear on its
        // own and the build would stay green with the rule unenforced. A missing class does not
        // compile, which is the point.
        assertThat(createTestee().create(rule("StatementMustBeValid"), new HashMap<String, Object>(),
            false)).isNull
    }

    @Test
    def void testNothingIsGeneratedDuringPreparation() {
        assertThat(createTestee().create(rule("MustBeAssigned"), new HashMap<String, Object>(), true))
            .isNull
    }

    private def String generate(String ruleName) {
        new String(createTestee.create(rule(ruleName), new HashMap<String, Object>(), false)
            .iterator.next.data, "UTF-8")
    }

    private def BusinessRule rule(String ruleName) {
        model.find(typeof(BusinessRule), ruleName)
    }

    private def createTestee() {
        val factory = new BusinessRuleArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("businessRule",
            BusinessRuleArtifactFactory.name, "command.core", "genMainJava")
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

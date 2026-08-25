package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.validation.CqrsDslValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Verifies what a business rule and its usage have to agree on.
 *
 * <p>Both of these used to be found by the generator, or not at all: one as a failure at the far end
 * of a release chain, the other never - the actuals bind positionally and everything in them is a
 * reference, so a miscount usually still resolves.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsBusinessRuleValidationTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void testARuleHoldsWhatItsRefusalHasToName() {
		parseHelper.parse(model('''
			/** The thing being acted on. */
			ThingId thing
			consistency strong
			requires thing != null
		''', "MustBeOpen(own-id)")).assertNoIssues
	}

	@Test
	def void testARefusalNamingSomethingTheRuleDoesNotHold() {
		// Without this it is a generator failure in whichever project regenerates next, or Java with
		// an argument missing.
		parseHelper.parse(model('''
			/** Something else entirely. */
			ThingId other
			consistency strong
			requires other != null
		''', "MustBeOpen(own-id)")).assertError(CqrsDslPackage.Literals.BUSINESS_RULE,
			CqrsDslValidator.RULE_EXCEPTION_NOT_SUPPLIED)
	}

	@Test
	def void testARuleThatDeclaresNoConditionIsNotHeldToIt() {
		// It is written by hand and constructs its refusal however it likes; and a rule that has not
		// been given its inputs yet is unfinished rather than wrong.
		parseHelper.parse(model('''
			consistency strong
		''', "MustBeOpen")).assertNoIssues
	}

	@Test
	def void testAUsageThatHandsOverTooFew() {
		// The actuals bind positionally, so one too few shifts every value after it.
		parseHelper.parse(model('''
			/** The thing being acted on. */
			ThingId thing
			/** Whether it is open. */
			Boolean open
			consistency strong
			requires open
		''', "MustBeOpen(own-id)")).assertError(CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE,
			CqrsDslValidator.RULE_ACTUALS_MISMATCH)
	}

	private def String model(String ruleBody, String usage) '''
		context p {
			module c.n {
				type String

				type Boolean

				aggregate-id ThingId identifies Thing base String { }

				/** Refused because the thing is closed. */
				exception ClosedException {
					/** The thing that is closed. */
					ThingId thing
					message "${thing} is closed"
				}

				/** Makes sure the thing is open. */
				business-rule MustBeOpen exception ClosedException {
					«ruleBody»
				}

				aggregate Thing identifier ThingId {
					method close business-rules «usage» fires ClosedEvent {
						event ClosedEvent { message "Closed" }
					}
				}
			}
		}
	'''

}

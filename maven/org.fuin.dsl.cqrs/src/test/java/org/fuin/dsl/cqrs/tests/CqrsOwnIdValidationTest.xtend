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
 * Verifies where the carrier's own identity may be handed to a rule.
 *
 * <p>'own-id' exists because a refusal commonly has to name the thing it refused, and the identity is
 * the one value an aggregate holds without declaring it as an attribute - so there is nothing for a
 * cross reference to point at.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsOwnIdValidationTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void testAnOperationOnAnExistingThingMayNameItsIdentity() {
		parseHelper.parse(aggregateWith('''
			method close business-rules MustBeOpen(own-id) fires ClosedEvent {
				event ClosedEvent { message "Closed" }
			}
		''')).assertNoIssues
	}

	@Test
	def void testACreatingOperationMayNot() {
		// A constructor is what brings the identity into being, so there is nothing to read it from -
		// which is also why the generated validator's method for a create is static.
		parseHelper.parse(aggregateWith('''
			constructor open business-rules MustBeOpen(own-id) fires OpenedEvent {
				event OpenedEvent { message "Opened" }
			}
		''')).assertError(CqrsDslPackage.Literals.IDENTITY_ARGUMENT,
			CqrsDslValidator.RULE_OWN_ID_IN_CONSTRUCTOR)
	}

	private def String aggregateWith(String operation) '''
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
					/** The thing being acted on. */
					ThingId thing
					consistency strong
					requires thing != null
				}

				aggregate Thing identifier ThingId {
					«operation»
				}
			}
		}
	'''

}

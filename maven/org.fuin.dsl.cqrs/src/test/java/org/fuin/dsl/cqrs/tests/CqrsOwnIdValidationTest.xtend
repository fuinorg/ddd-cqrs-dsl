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
 * Verifies where the carrier's own identity and its own prior state may be handed to a rule.
 *
 * <p>'own-id' exists because a refusal commonly has to name the thing it refused, and the identity is
 * the one value an aggregate holds without declaring it as an attribute - so there is nothing for a
 * cross reference to point at. 'own' exists for the other half: what the carrier holds now, which a
 * bare name cannot reach when the operation's parameter is named after the field it overwrites.
 *
 * <p>Neither may be used by a constructor, and for the same reason: it is what brings the identity and
 * the state into being, so there is no "now" to read.
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

	@Test
	def void testAnOperationOnAnExistingThingMayNameItsPriorState() {
		parseHelper.parse(aggregateWith('''
			method rename business-rules MustHaveName(own name) fires RenamedEvent {
				/** The new name, named after the field it overwrites. */
				String name
				event RenamedEvent { message "Renamed" }
			}
		''')).assertNoIssues
	}

	@Test
	def void testACreatingOperationHasNoPriorState() {
		parseHelper.parse(aggregateWith('''
			constructor open business-rules MustHaveName(own name) fires OpenedEvent {
				event OpenedEvent { message "Opened" }
			}
		''')).assertError(CqrsDslPackage.Literals.CARRIER_ATTRIBUTE_ARGUMENT,
			CqrsDslValidator.RULE_OWN_ATTRIBUTE_IN_CONSTRUCTOR)
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

				/** Refused because the thing has no name. */
				exception NamelessException {
					/** The name it carries now. */
					String name
					message "'${name}' is not a name"
				}

				/** Makes sure the thing is named. */
				business-rule MustHaveName exception NamelessException {
					/** The name it carries now. */
					String name
					consistency strong
					requires name != null
				}

				aggregate Thing identifier ThingId {

					/** What it is called. */
					String name

					«operation»
				}
			}
		}
	'''

}

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
 * Verifies that a row offering a command can answer the rules guarding it.
 *
 * <p>A menu is drawn on a row, and a command gated by a rule over the aggregate's own state can be left
 * out of it rather than offered and refused. The client decides that from what the row publishes, so a
 * row that offers the command and omits what the gate reads makes the gate work on one screen and
 * quietly do nothing on another - and a gate that silently does not work is indistinguishable from one
 * nobody wrote.
 *
 * <p>A warning rather than an error on purpose: whether a row publishes what a rule reads is a
 * modelling decision with costs on the other side. What this removes is the silence, not the choice.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsRowGateValidationTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void testARowPublishingWhatItsGateReadsIsFine() {
		parseHelper.parse(model('''
			/** The thing's identifier. */
			ThingId id

			/** Whether it is open. */
			Boolean open
		''')).assertNoIssues
	}

	@Test
	def void testARowThatCannotAnswerTheGateItOffers() {
		// The screen offers "close" on every row, including the ones already closed, and nothing in
		// either build says so.
		parseHelper.parse(model('''
			/** The thing's identifier. */
			ThingId id
		''')).assertWarning(CqrsDslPackage.Literals.VALUE_OBJECT,
			CqrsDslValidator.ROW_CANNOT_ANSWER_GATE)
	}

	@Test
	def void testAValueObjectNoViewHandsBackIsNotARow() {
		// It is identified in exactly the same way - one attribute typed as an id - and no menu is ever
		// drawn on it, so holding it to a gate would report a screen that does not exist.
		parseHelper.parse('''
			context p {
				module c.n {
					type String
					type Boolean
					type List generics 1

					aggregate-id ThingId identifies Thing base String { }

					/** Refused because the thing is closed. */
					exception ClosedException {
						/** The thing that is closed. */
						ThingId thing
						message "${thing} is closed"
					}

					/** Makes sure the thing is open. */
					business-rule MustBeOpen exception ClosedException {
						/** The thing that is closed. */
						ThingId thing
						/** Whether it is open now. */
						Boolean open
						consistency strong
						requires open
					}

					aggregate Thing identifier ThingId {
						/** Whether it is open. */
						Boolean open
						method close business-rules MustBeOpen(own-id, own open) fires ClosedEvent {
							event ClosedEvent { message "Closed" }
						}
					}

					/** Points at a thing without being one. */
					value-object ThingRef {
						/** The thing pointed at. */
						ThingId id
					}

					/** Closes it. */
					command CloseThing target Thing.close {
						message "Close it"
					}
				}
			}
		''').assertNoIssues
	}

	@Test
	def void testAGateNoClientCouldAnswerIsNotHeldAgainstTheRow() {
		// The rule asks a service a question only the server can ask, so the row could not answer it
		// however much it published - and holding it to that would ask for a column nothing can fill.
		parseHelper.parse('''
			context p {
				module c.n {
					type String
					type Boolean
					type List generics 1

					aggregate-id ThingId identifies Thing base String { }

					/** Refused because the thing is closed. */
					exception ClosedException {
						/** The thing that is closed. */
						ThingId thing
						message "${thing} is closed"
					}

					/** Makes sure the thing is open. */
					business-rule MustBeOpen exception ClosedException {
						/** The thing that is closed. */
						ThingId thing
						/** Whether it is open now. */
						Boolean open
						consistency strong
						requires open
					}

					/** Answers what an operation cannot answer itself. */
					service ThingService {
						/** Whether the thing is still open. */
						method stillOpen {
							ThingId thing
							returns Boolean
						}
					}

					projection Things

					view ThingView uses Things {
						method listThings {
							returns List<ThingRow>
						}
					}

					/** One thing on screen. */
					value-object ThingRow {
						/** The thing's identifier. */
						ThingId id
					}

					aggregate Thing identifier ThingId {
						method close business-rules MustBeOpen(own-id, stillOpen(own-id)) fires ClosedEvent {
							operation-context ThingService
							event ClosedEvent { message "Closed" }
						}
					}

					/** Closes it. */
					command CloseThing target Thing.close {
						message "Close it"
					}
				}
			}
		''').assertNoIssues
	}

	/**
	 * A row a view hands back, an aggregate with a gated operation, and a command addressing it.
	 *
	 * @param rowBody Attributes the row publishes.
	 *
	 * @return Model source.
	 */
	private def String model(String rowBody) '''
		context p {
			module c.n {
				type String

				type Boolean

				type List generics 1

				aggregate-id ThingId identifies Thing base String { }

				/** Refused because the thing is closed. */
				exception ClosedException {
					/** The thing that is closed. */
					ThingId thing
					message "${thing} is closed"
				}

				/** Makes sure the thing is open. */
				business-rule MustBeOpen exception ClosedException {
					/** The thing that is closed. */
					ThingId thing
					/** Whether it is open now. */
					Boolean open
					consistency strong
					requires open
				}

				projection Things

				view ThingView uses Things {
					method listThings {
						returns List<ThingRow>
					}
				}

				/** One thing on screen. */
				value-object ThingRow {
					«rowBody»
				}

				aggregate Thing identifier ThingId {

					/** Whether it is open. */
					Boolean open

					method close business-rules MustBeOpen(own-id, own open) fires ClosedEvent {
						event ClosedEvent { message "Closed" }
					}
				}

				/** Closes it. */
				command CloseThing target Thing.close {
					message "Close it"
				}
			}
		}
	'''

}

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
 * Verifies where 'own-path' may be used.
 *
 * <p>It exists because an entity's identifier does not address it: the same one is assigned inside every
 * root, so a service asked to look the entity up somewhere else cannot be handed 'own-id'. What it reads
 * as is the declared 'entity-id-path' for the chain - so the three ways it can be wrong are a carrier
 * with no root, a carrier that does not exist yet, and a chain the model never declared a path for.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsOwnPathValidationTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void testAnEntityWithADeclaredPathMayNameIt() {
		parseHelper.parse(model(PATH, '''
			method close business-rules MustBeOpen(own-path) fires ClosedEvent {
				event ClosedEvent { message "Closed" }
			}
		''')).assertNoIssues
	}

	@Test
	def void testWithoutADeclaredPathThereIsNoTypeToReadItAs() {
		// Falling back to a bare path would put back the untyped reference the declaration removes, so
		// this says which declaration is missing instead.
		parseHelper.parse(model("", '''
			method close business-rules MustBeOpen(own-path) fires ClosedEvent {
				event ClosedEvent { message "Closed" }
			}
		''')).assertError(CqrsDslPackage.Literals.ENTITY_PATH_ARGUMENT,
			CqrsDslValidator.RULE_OWN_PATH_HAS_NO_TYPE)
	}

	@Test
	def void testACreatingOperationHasNoCarrierToAddress() {
		parseHelper.parse(model(PATH, '''
			constructor open business-rules MustBeOpen(own-path) fires OpenedEvent {
				event OpenedEvent { message "Opened" }
			}
		''')).assertError(CqrsDslPackage.Literals.ENTITY_PATH_ARGUMENT,
			CqrsDslValidator.RULE_OWN_PATH_IN_CONSTRUCTOR)
	}

	@Test
	def void testAnAggregateIsAddressedByItsOwnId() {
		// Nothing to prepend: an aggregate id addresses the aggregate on its own, which is the whole
		// difference between it and an entity id.
		parseHelper.parse('''
			context p {
				module c.n {
					type String

					/** Refused because the thing is closed. */
					exception ClosedException {
						/** The thing that is closed. */
						BoxId thing
						message "${thing} is closed"
					}

					/** Makes sure the box is open. */
					business-rule MustBeOpen exception ClosedException {
						/** The box being acted on. */
						BoxId thing
						consistency strong
						requires thing != null
					}

					aggregate-id BoxId identifies Box base String { }

					aggregate Box identifier BoxId {
						method close business-rules MustBeOpen(own-path) fires ClosedEvent {
							event ClosedEvent { message "Closed" }
						}
					}
				}
			}
		''').assertError(CqrsDslPackage.Literals.ENTITY_PATH_ARGUMENT,
			CqrsDslValidator.RULE_OWN_PATH_NOT_ON_ENTITY)
	}

	private static val PATH = '''
		/** How a thing inside a box is addressed. */
		entity-id-path ThingPath {
			BoxId / ThingId
		}
	'''

	private def String model(String path, String operation) '''
		context p {
			module c.n {
				type String

				aggregate-id BoxId identifies Box base String { }

				entity-id ThingId identifies Thing base String { }

				«path»

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

				aggregate Box identifier BoxId {

					/** What it is called. */
					String name

				}

				entity Thing identifier ThingId root Box {

					/** What it is called. */
					String name

					«operation»
				}
			}
		}
	'''

}

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
 * Verifies what a command's message may say.
 *
 * <p>An event's message is rendered by the JVM alone, so the event check waves complex Jakarta EL
 * through. A command's message is a confirmation prompt the client shows before sending, so it is
 * rendered on both sides - and Dart has no EL engine. These tests pin the intersection: a plain
 * identifier or a dotted path over something the command actually carries.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsCommandMessageValidationTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void testAVariableTheCommandCarriesIsFine() {
		parseHelper.parse(commandWith('''
			String newName
			message "Rename to '${newName}'"
		''')).assertNoIssues
	}

	@Test
	def void testTheImplicitEntityIdPathIsAlwaysAvailable() {
		// Never declared and always there, exactly as in an event's message.
		parseHelper.parse(commandWith('''
			message "Removed ${entityIdPath}"
		''')).assertNoIssues
	}

	@Test
	def void testADottedPathIsFine() {
		// The one case the model actually has: "Import exchange rates from ${provider.id}". Only the
		// root has to be a variable - what hangs off it is resolved when the message is rendered.
		parseHelper.parse(commandWith('''
			String provider
			message "Import from ${provider.id}"
		''')).assertNoIssues
	}

	@Test
	def void testAVariableTheCommandDoesNotCarryIsAnError() {
		// The row a client happens to show the command on is not in scope: no model link ties the two,
		// so there would be nothing to check it against.
		parseHelper.parse(commandWith('''
			String newName
			message "Remove '${name}'"
		''')).assertError(CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_UNKNOWN_VAR)
	}

	@Test
	def void testAMethodCallIsAnError() {
		// Legal in an event, because only the JVM renders that one.
		parseHelper.parse(commandWith('''
			String newName
			message "Rename to ${newName.toUpperCase()}"
		''')).assertError(CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_NOT_A_PATH)
	}

	@Test
	def void testAnOperatorIsAnError() {
		parseHelper.parse(commandWith('''
			Integer quantity
			Integer price
			message "Total ${quantity * price}"
		''')).assertError(CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_NOT_A_PATH)
	}

	@Test
	def void testAnUnclosedVariableIsAnError() {
		// Renders nowhere, and silently: the JVM leaves it as it stands and so would the client.
		parseHelper.parse(commandWith('''
			String newName
			message "Rename to ${newName"
		''')).assertError(CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_UNCLOSED_VAR)
	}

	@Test
	def void testAVariableOfTheTargetOperationIsFine() {
		// Most commands declare no attributes at all and take every one of them from the operation
		// they target, so checking only the command's own attributes would reject nearly all of them.
		parseHelper.parse('''
			context p {
				module c.n {
					type String

					aggregate-id AId identifies A base String { }

					aggregate A identifier AId {
						method rename fires RenamedEvent {
							String newName
							event RenamedEvent { message "Renamed" }
						}
					}

					command RenameCommand target A.rename {
						message "Rename to '${newName}'"
					}
				}
			}
		''').assertNoIssues
	}

	private def String commandWith(String body) '''
		context p {
			module c.n {
				type String

				type Integer

				command C {
					«body»
				}
			}
		}
	'''

}

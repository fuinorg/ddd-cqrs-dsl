package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.cqrs.validation.CqrsDslValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.junit.jupiter.api.Assertions.*

/**
 * Verifies the optional 'rest-path' of a method: it is parsed for view methods, rejected everywhere
 * else, and its "{name}" placeholders must be backed by declared parameters.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslRestPathTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	/** Model with one projection and one view whose methods carry the given body. */
	private def String model(String methods) '''
		context p {
			module c.n {
				type String
				type Integer
				value-object ItemId {
					String value
				}
				projection ItemProjection

				view ItemView uses ItemProjection {
					«methods»
				}
			}
		}
	'''

	@Test
	def void restPathIsOptionalAndParsedWhenPresent() {

		val m = parseHelper.parse(model('''
			method findItem rest-path "/{id}" {
				ItemId id
				returns optional ItemId
			}
			method countItems {
				returns Integer
			}
		'''))

		assertNotNull(m)
		EcoreUtil.resolveAll(m.eResource)
		assertTrue(m.eResource.errors.empty, '''Unexpected errors: «m.eResource.errors.join(", ")»''')

		val view = m.eAllContents.filter(View).head
		assertEquals("/{id}", view.methods.get(0).restPath)
		// Absent clause stays NULL so the generator can fall back to the method name
		assertNull(view.methods.get(1).restPath)
	}

	@Test
	def void unknownPathVariableIsRejected() {

		val m = parseHelper.parse(model('''
			method findItem rest-path "/{unknown}" {
				ItemId id
				returns optional ItemId
			}
		'''))

		m.assertError(
			CqrsDslPackage.Literals::METHOD,
			CqrsDslValidator.REST_PATH_UNKNOWN_VAR
		)
	}

	@Test
	def void restPathOutsideAViewIsRejected() {

		val m = parseHelper.parse('''
			context p {
				module c.n {
					type String
					service ItemService {
						method doSomething rest-path "/nope" {
						}
					}
				}
			}
		''')

		m.assertError(
			CqrsDslPackage.Literals::METHOD,
			CqrsDslValidator.REST_PATH_ONLY_ON_VIEW_METHODS
		)
	}
}

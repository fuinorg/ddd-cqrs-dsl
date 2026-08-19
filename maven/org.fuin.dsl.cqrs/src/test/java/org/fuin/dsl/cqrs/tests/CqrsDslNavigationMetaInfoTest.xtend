package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.junit.jupiter.api.Assertions.*

/**
 * Verifies the UI meta info a client needs to render navigation: on a 'module', on a 'view' and on a
 * 'method'.
 *
 * <p>A module name is a single lowercase identifier and a view name is a type name, so neither can be
 * turned into a caption by a rule a client could write - "businesspartners" is not "Business partners".
 * The wording therefore belongs in the model, beside the wording every attribute already carries.
 *
 * <p>All of it is optional: the block is an all-optional {TypeMetaInfo}, so every model written before
 * it existed keeps parsing unchanged. That is what the last test pins.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslNavigationMetaInfoTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Test
	def void moduleCarriesMetaInfo() {

		val model = parse('''
			context p {
				module businesspartners {

					slabel "Partners"
					label "Business partners"
					tooltip "Customers and suppliers this installation trades with"

					type String
				}
			}
		''')

		val module = model.contexts.get(0).modules.get(0)
		assertEquals("businesspartners", module.name)
		assertNotNull(module.metaInfo, "a module block must carry its meta info")
		assertEquals("Partners", module.metaInfo.slabel)
		assertEquals("Business partners", module.metaInfo.label)
		assertEquals("Customers and suppliers this installation trades with", module.metaInfo.tooltip)
	}

	@Test
	def void viewAndMethodCarryMetaInfo() {

		val model = parse('''
			context p {
				module c.n {
					type String
					type Integer
					projection ItemProjection

					view ItemView uses ItemProjection {

						slabel "Items"
						label "Item list"
						tooltip "Everything currently on the shelf"

						method listItems {

							slabel "All"
							label "All items"
							tooltip "Every item, newest first"

							returns Integer
						}
					}
				}
			}
		''')

		val view = model.contexts.get(0).modules.get(0).elements.filter(View).get(0)
		assertNotNull(view.metaInfo, "a view block must carry its meta info")
		assertEquals("Items", view.metaInfo.slabel)
		assertEquals("Item list", view.metaInfo.label)
		assertEquals("Everything currently on the shelf", view.metaInfo.tooltip)

		val method = view.methods.get(0)
		assertNotNull(method.metaInfo, "a method block must carry its meta info")
		assertEquals("All", method.metaInfo.slabel)
		assertEquals("All items", method.metaInfo.label)
		assertEquals("Every item, newest first", method.metaInfo.tooltip)
	}

	@Test
	def void metaInfoIsOptionalEverywhere() {

		// The same three blocks without any wording - what every model written before this looks like.
		val model = parse('''
			context p {
				module c.n {
					type Integer
					projection ItemProjection

					view ItemView uses ItemProjection {
						method listItems {
							returns Integer
						}
					}
				}
			}
		''')

		val module = model.contexts.get(0).modules.get(0)
		val view = module.elements.filter(View).get(0)

		// The rule has an explicit {TypeMetaInfo} action, so the object exists with nothing in it.
		assertNull(module.metaInfo?.label, "a module with no wording must report none")
		assertNull(view.metaInfo?.label, "a view with no wording must report none")
		assertNull(view.methods.get(0).metaInfo?.label, "a method with no wording must report none")
	}

	/** Parses the model and fails on any syntax or linking error. */
	private def DomainModel parse(CharSequence source) {
		val model = parseHelper.parse(source)
		assertNotNull(model)
		EcoreUtil.resolveAll(model.eResource)
		val errors = model.eResource.errors
		assertTrue(errors.empty, '''Unexpected errors: «errors.join(", ")»''')
		return model
	}

}

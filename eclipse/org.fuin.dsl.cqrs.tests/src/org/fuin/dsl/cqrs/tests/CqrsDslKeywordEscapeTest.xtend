package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.junit.jupiter.api.Assertions.*

/**
 * Verifies that a caret ('^') escapes a keyword so it can be used as a plain identifier - both as a
 * simple name (ID) and inside a qualified name / cross reference (FQN).
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslKeywordEscapeTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Test
	def void caretEscapedKeywordsAreUsableAsIdentifiers() {

		// 'module', 'import', 'type', 'event', 'dependency' and 'local' are all keywords - here used as
		// names via the '^' escape.
		val model = parseHelper.parse('''
			context ^module {
				module ^type.^local {
					type String
					value-object ^event {
						String value
					}
					value-object ^dependency {
						^event data
					}
				}
			}
		''')

		assertNotNull(model)
		// Resolve all cross references so any unresolved link surfaces as a resource error.
		EcoreUtil.resolveAll(model.eResource)
		val errors = model.eResource.errors
		assertTrue(errors.empty, '''Unexpected errors: «errors.join(", ")»''')

		// The caret is stripped from the stored names, per dot separated segment.
		val ctx = model.contexts.get(0)
		assertEquals("module", ctx.name)
		val ns = ctx.modules.get(0)
		assertEquals("type.local", ns.name)

		val event = ns.elements.filter(ValueObject).findFirst[name == "event"]
		assertNotNull(event, "value-object written as '^event' must be named 'event'")

		// The cross reference written as '^event' resolves to that value object.
		val dep = ns.elements.filter(ValueObject).findFirst[name == "dependency"]
		assertNotNull(dep)
		assertSame(event, dep.attributes.get(0).type)
	}

	@Test
	def void plainKeywordsStillParseAsKeywords() {
		// Without the escape the words remain keywords, so this ordinary model parses cleanly.
		val model = parseHelper.parse('''
			context plain {
				module m {
					type String
					value-object Money {
						String amount
					}
				}
			}
		''')
		assertNotNull(model)
		EcoreUtil.resolveAll(model.eResource)
		assertTrue(model.eResource.errors.empty, '''Unexpected errors: «model.eResource.errors.join(", ")»''')
	}
}

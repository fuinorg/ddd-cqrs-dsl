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
 * Verifies the hint-JSON validation: a "JpaHint" / "SrcGen4J" hint's JSON is checked against its JSON
 * schema (error), and a "JpaHint" declared outside a view produces a placement warning.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsHintJsonValidationTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void testValidJpaHintInViewHasNoIssues() {
		val model = parseHelper.parse(viewWith('''
			hint JpaHint {
				"tables": [
					{ "className": "Customer", "columns": [ { "fieldName": "id", "javaType": "java.util.UUID" } ] }
				]
			}
		'''))
		model.assertNoIssues
	}

	@Test
	def void testInvalidJpaHintReportsSchemaError() {
		// "columns" is required and "bogus" is not an allowed property.
		val model = parseHelper.parse(viewWith('''
			hint JpaHint {
				"tables": [ { "className": "Customer", "bogus": true } ]
			}
		'''))
		model.assertError(CqrsDslPackage.Literals.HINT, CqrsDslValidator.HINT_JSON_SCHEMA_VIOLATION)
	}

	@Test
	def void testJpaHintOutsideViewWarns() {
		val model = parseHelper.parse('''
			project p {
				hint JpaHint { "tables": [] }
				context c { }
			}
		''')
		model.assertWarning(CqrsDslPackage.Literals.HINT, CqrsDslValidator.JPA_HINT_OUTSIDE_VIEW)
	}

	@Test
	def void testInvalidSrcGen4JReportsSchemaError() {
		// A "types" entry must have a "name".
		val model = parseHelper.parse('''
			project p {
				hint SrcGen4J { "types": [ { "module": "x" } ] }
				context c { }
			}
		''')
		model.assertError(CqrsDslPackage.Literals.HINT, CqrsDslValidator.HINT_JSON_SCHEMA_VIOLATION)
	}

	private def String viewWith(String hint) '''
		project p {
			context c {
				namespace n {
					projection Pj
					view V uses Pj {
						«hint»
					}
				}
			}
		}
	'''

}

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

/** Verifies the three rules that guard an {@code import} statement. */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslImportValidationTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject ValidationTestHelper validationHelper

	/** An import that matches neither a context, nor a module, nor a type is an error. */
	@Test
	def void unresolvedImportIsAnError() {
		val model = parseHelper.parse('''
			context shop {
				module catalog {
					import nowhere.at.all.*

					type String
				}
			}
		''')
		validationHelper.assertError(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_UNRESOLVED)
	}

	/** The same import twice in one block is an error. */
	@Test
	def void duplicateImportIsAnError() {
		val model = parseHelper.parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					import shop.types.*
					import shop.types.*

					value-object ProductName base String {
						String value
					}
				}
			}
		''')
		validationHelper.assertError(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_DUPLICATE)
	}

	/** Repeating an import the context already declares is only a warning. */
	@Test
	def void importAlreadyDeclaredByContextIsAWarning() {
		val model = parseHelper.parse('''
			context shop {
				import shop.types.*

				module types {
					type String
				}
				module catalog {
					import shop.types.*

					value-object ProductName base String {
						String value
					}
				}
			}
		''')
		validationHelper.assertWarning(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_DUPLICATE)
	}

	/** An import nothing refers to is a warning. */
	@Test
	def void unusedImportIsAWarning() {
		val model = parseHelper.parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					import shop.types.*

					type Own

					value-object ProductName base Own {
						Own value
					}
				}
			}
		''')
		validationHelper.assertWarning(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_UNUSED)
	}

	/** An import that is actually used raises nothing. */
	@Test
	def void usedImportIsNotReported() {
		val model = parseHelper.parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					import shop.types.*

					value-object ProductName base String {
						String value
					}
				}
			}
		''')
		validationHelper.assertNoIssues(model)
	}
}

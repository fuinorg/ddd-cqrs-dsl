package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.emf.common.util.BasicDiagnostic
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.validation.CqrsDslValidator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/** Verifies the three rules that guard an {@code import} statement. */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslImportValidationTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject ValidationTestHelper validationHelper
	@Inject CqrsDslValidator validator

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

	/**
	 * "Unused" is a claim about what the block refers to, so it may only be made when everything in the
	 * block could be looked at. A name that did not resolve belongs to no import in particular - it may
	 * well be the one this import was written for - and calling the import dead weight because of it
	 * puts a second marker on somebody else's problem.
	 *
	 * <p>This is not a corner case in an IDE: the moment anything leaves a reference unresolved, every
	 * import whose only use was that reference turns yellow, which is how a single unresolved type
	 * turns into a screenful of warnings.</p>
	 */
	@Test
	def void unusedIsNotReportedWhenSomethingInTheBlockDoesNotResolve() {
		val model = parseHelper.parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					import shop.types.*

					value-object ProductName {
						NoSuchType value
					}
				}
			}
		''')
		val issues = validationHelper.validate(model)
		Assertions.assertTrue(issues.forall[code != CqrsDslValidator.IMPORT_UNUSED],
			'''Expected no unused-import warning, but got: «issues.join(", ")»''')
	}

	/**
	 * Xtext links lazily: a cross reference holds a proxy until somebody asks for its target. An import
	 * whose only use has not been asked for yet is still used, so the check has to ask - otherwise
	 * whether a name happens to have been resolved already, by a hover or a Ctrl-click or the order the
	 * editor validated in, decides whether its import is reported.
	 */
	@Test
	def void unusedIsNotReportedForAUseThatIsStillUnresolved() {
		assertUnusedReported(false, '''
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
	}

	/** ... and asking is what keeps the warning working there, rather than silencing it. */
	@Test
	def void unusedIsStillReportedWhenNothingHasBeenResolvedYet() {
		assertUnusedReported(true, '''
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
	}

	/**
	 * Runs the check on the second module's import <em>without</em> resolving anything first - the state
	 * an editor may well validate in, and the one a test using {@code ValidationTestHelper} never sees,
	 * because that resolves every proxy before it validates.
	 *
	 * @param expected Whether the import must be reported as unused.
	 * @param model Model to check.
	 */
	private def void assertUnusedReported(boolean expected, CharSequence model) {
		val imp = parseHelper.parse(model).contexts.head.modules.get(1).imports.head
		val diagnostics = new BasicDiagnostic
		validator.validate(imp.eClass, imp, diagnostics, newHashMap)

		val reported = diagnostics.children.exists[message.contains("is not used")]
		Assertions.assertEquals(expected, reported,
			'''Diagnostics were: «diagnostics.children.map[message].join(", ")»''')
	}
}

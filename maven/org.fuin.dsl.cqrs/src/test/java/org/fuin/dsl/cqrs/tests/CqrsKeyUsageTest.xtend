package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Key
import org.fuin.dsl.cqrs.validation.CqrsDslValidator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static extension org.eclipse.xtext.EcoreUtil2.getAllContentsOfType

/**
 * Verifies that an operation can say it checks a business key, and what it may say about it.
 *
 * <p>Before this, a key could be declared and nothing could use one: 'Key' sat outside the vocabulary
 * a 'business-rules' clause references, so naming one did not resolve. A key derives a uniqueness rule,
 * and the model keeps saying <em>which</em> operations check it rather than letting the generator guess
 * - so the usage is written, and it is written where a rule usage is written.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsKeyUsageTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	/** The usual case: the operation names the key and says nothing else about it. */
	@Test
	def void testAnOperationNamesAKey() {
		val model = parseHelper.parse(aggregate("business-rules NamePerKind", '''
			String name
			Kind kind
			operation-context CreateService
			/** Answers whether the key is free. */
			service CreateService {
			}
		'''))
		model.assertNoErrors
		val instance = model.getAllContentsOfType(Aggregate).head.constructors.head.businessRules
			.businessRuleInstances.head
		Assertions.assertFalse(instance.businessRule.eIsProxy, "the key did not resolve")
		Assertions.assertTrue(instance.businessRule instanceof Key, "did not resolve to a key")
		Assertions.assertEquals("NamePerKind", instance.businessRule.name)
	}

	/**
	 * A model that spells the actuals out spells all of them out: the answer, then every attribute the
	 * key is made of, because the refusal names what it refused.
	 */
	@Test
	def void testTheActualsMayBeWrittenOut() {
		parseHelper.parse(aggregate(
			"business-rules NamePerKind(taken(name, kind), name, kind)",
			'''
				String name
				operation-context CreateService
				/** Answers whether the key is free. */
				service CreateService {
					/** Returns true when something already holds it. */
					method taken {
						/** The name asked for. */
						String name
						/** The kind it must be unique within. */
						String kind
						/** True when it is taken. */
						returns Boolean
					}
				}
			'''
		)).assertNoErrors
	}

	/** Half of them is the failure this refuses: positional actuals shift silently. */
	@Test
	def void testSomeOfTheActualsIsRefused() {
		parseHelper.parse(aggregate("business-rules NamePerKind(name)", "String name"))
			.assertError(CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE,
				CqrsDslValidator.RULE_ACTUALS_MISMATCH)
	}

	/** A key at module level has no type to name attributes of, so it is refused where it parses. */
	@Test
	def void testAKeyOutsideATypeIsRefused() {
		parseHelper.parse('''
			context foo {
				module bar {

					type String

					/** Reported when the name is taken. */
					exception DuplicateNameException {
						message "Name is already taken"
					}

					/** Nothing here has attributes. */
					key Loose exception DuplicateNameException {
						attributes name
						on-collision refuse
						consistency strong
					}

				}
			}
		''').assertError(CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_OUTSIDE_TYPE)
	}

	/** A key that overwrites refuses nobody, so an exception beside it would never be thrown. */
	@Test
	def void testACollisionThatDoesNotRefuseHasNothingToThrow() {
		parseHelper.parse(aggregate(null, "String name", '''
			/** No two things of the same kind share a name. */
			key NamePerKind exception DuplicateNameException {
				attributes name, kind
				on-collision overwrite
				consistency strong
			}
		''')).assertError(CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_EXCEPTION_MISMATCH)
	}

	/** And one that refuses needs the exception that says so. */
	@Test
	def void testARefusingKeyNeedsItsException() {
		parseHelper.parse(aggregate(null, "String name", '''
			/** No two things of the same kind share a name. */
			key NamePerKind {
				attributes name, kind
				on-collision refuse
				consistency strong
			}
		''')).assertError(CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_EXCEPTION_MISMATCH)
	}

	/**
	 * 'display-as' may name an attribute the key is not made of. What a person recognises a thing by
	 * is not always what makes it unique - an account is keyed by its IBAN and read by its name.
	 */
	@Test
	def void testDisplayAsReachesTheWholeType() {
		parseHelper.parse(aggregate(null, "String name", '''
			/** No two things of the same kind share a name. */
			key KindOnly exception DuplicateNameException {
				attributes kind
				on-collision refuse
				consistency strong
				display-as "${name} (${kind})"
			}
		''')).assertNoErrors
	}

	/** But not something the type does not have. */
	@Test
	def void testDisplayAsCannotNameWhatIsNotThere() {
		parseHelper.parse(aggregate(null, "String name", '''
			/** No two things of the same kind share a name. */
			key KindOnly exception DuplicateNameException {
				attributes kind
				on-collision refuse
				consistency strong
				display-as "${nickname}"
			}
		''')).assertError(CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_DISPLAY_UNKNOWN_VAR)
	}

	/** Two formats are two answers to "what does a picker show", with nothing to choose between them. */
	@Test
	def void testATypeIsDisplayedByOneKey() {
		parseHelper.parse(aggregate(null, "String name", '''
			/** No two things of the same kind share a name. */
			key NamePerKind exception DuplicateNameException {
				attributes name, kind
				on-collision refuse
				consistency strong
				display-as "${name}"
			}

			/** No two things share a kind. */
			key KindOnly exception DuplicateNameException {
				attributes kind
				on-collision refuse
				consistency strong
				display-as "${kind}"
			}
		''')).assertError(CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_SEVERAL_DISPLAY_KEYS)
	}

	/**
	 * A key derives a rule named after it, and that is usually what the rule it replaces is already
	 * called. Both would generate a class of that name into one package.
	 */
	@Test
	def void testTheDerivedRuleNameCollidesWithADeclaredRule() {
		parseHelper.parse(aggregate(null, "String name", '''
			/** Makes sure the name is free. */
			business-rule NamePerKindMustBeUnique exception DuplicateNameException {

				/** Whether it is taken. */
				Boolean taken

				consistency strong

				requires !taken
			}

			/** No two things of the same kind share a name. */
			key NamePerKind exception DuplicateNameException {
				attributes name, kind
				on-collision refuse
				consistency strong
			}
		''')).assertError(CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_RULE_NAME_TAKEN)
	}

	/** A collision the model answers by overwriting is not something an operation is refused for. */
	@Test
	def void testAnOperationCannotBeGuardedByAKeyThatDoesNotRefuse() {
		parseHelper.parse(aggregate("business-rules NamePerKind", "String name", '''
			/** A later one replaces the earlier. */
			key NamePerKind {
				attributes name, kind
				on-collision overwrite
				consistency strong
			}
		''')).assertError(CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE,
				CqrsDslValidator.KEY_USAGE_DOES_NOT_REFUSE)
	}

	/**
	 * Two key attributes of one type and a parameter of it: each attribute matches that parameter, so
	 * both would be read off it and half the composite key would be checked against the wrong value.
	 */
	@Test
	def void testTwoKeyAttributesSharingATypeCannotBePairedUp() {
		parseHelper.parse(aggregate("business-rules SameType", '''
			String name
			operation-context CreateService
			/** Answers whether the key is free. */
			service CreateService {
			}
		''', '''
			/** Both halves are strings, and the operation offers one. */
			key SameType exception DuplicateNameException {
				attributes name, alias
				on-collision refuse
				consistency strong
			}
		''')).assertError(CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE,
				CqrsDslValidator.KEY_ACTUAL_AMBIGUOUS)
	}

	/** The answer has to be declared somewhere, and that somewhere is the operation's own service. */
	@Test
	def void testADerivedUsageNeedsAnOperationContext() {
		parseHelper.parse(aggregate("business-rules NamePerKind", "String name"))
			.assertError(CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE,
				CqrsDslValidator.KEY_USAGE_NEEDS_INLINE_CONTEXT)
	}

	/** A creating operation has no prior state, so every key attribute has to arrive as an argument. */
	@Test
	def void testACreateThatCannotReachAKeyAttribute() {
		parseHelper.parse(aggregate("business-rules NamePerKind", '''
			String name
			operation-context CreateService
			/** Answers whether the key is free. */
			service CreateService {
			}
		''')).assertError(CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE,
				CqrsDslValidator.KEY_ACTUAL_UNREACHABLE)
	}

	private def CharSequence aggregate(String usage, CharSequence body) {
		return aggregate(usage, body, '''
			/** No two things of the same kind share a name. */
			key NamePerKind exception DuplicateNameException {
				attributes name, kind
				on-collision refuse
				consistency strong
			}
		''')
	}

	private def CharSequence aggregate(String usage, CharSequence body, CharSequence keys) '''
		context foo {
			module bar {

				type String
				type Boolean
				type Kind

				/** Reported when the name is taken. */
				exception DuplicateNameException {
					message "Name is already taken"
				}

				aggregate-id ThingId identifies Thing {}

				aggregate Thing identifier ThingId {

					String name
					Kind kind

					/** A second name, so a key can be made of two of one type. */
					String alias

					«keys»

					/** Creates one. */
					constructor create «IF usage !== null»«usage» «ENDIF»{
						«body»
					}

				}

			}
		}
	'''

}

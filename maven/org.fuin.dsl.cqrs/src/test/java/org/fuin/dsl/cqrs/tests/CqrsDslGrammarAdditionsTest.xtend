/*
 * Covers the constructs added for business keys, generated business rules and the read model's
 * declared identity. Parsing is only half of what is asserted: every one of them introduces a cross
 * reference that must resolve inside the element it is written in, so each test also checks that the
 * reference found its target - and, where it matters, that a same named element elsewhere did not.
 */
package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance
import org.fuin.dsl.cqrs.cqrsDsl.CarrierAttributeArgument
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.IdentityArgument
import org.fuin.dsl.cqrs.cqrsDsl.RuleAttrRef
import org.fuin.dsl.cqrs.cqrsDsl.RuleComparison
import org.fuin.dsl.cqrs.cqrsDsl.RuleIsEmpty
import org.fuin.dsl.cqrs.cqrsDsl.RuleNot
import org.fuin.dsl.cqrs.cqrsDsl.RuleOr
import org.fuin.dsl.cqrs.cqrsDsl.RuleRefOperand
import org.fuin.dsl.cqrs.cqrsDsl.ServiceCallArgument
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.cqrsDsl.VariableArgument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static extension org.eclipse.xtext.EcoreUtil2.*

@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslGrammarAdditionsTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	ValidationTestHelper validationHelper

	/** A read model row names the attribute that identifies it. */
	@Test
	def void rowDeclaresItsIdentity() {
		val model = '''
			context foo {
				module bar {

					type String

					value-object Row {
						String id
						String name
						identified-by id
					}

				}
			}
		'''.parsed
		val row = model.getAllContentsOfType(ValueObject).head
		Assertions.assertNotNull(row.identifiedBy, "identified-by did not link")
		Assertions.assertEquals("id", row.identifiedBy.name)
	}

	/**
	 * And only its own. A row is identified by one of its attributes, so an attribute of the same name
	 * on another row is not a candidate - which is the whole reason this is a cross reference.
	 */
	@Test
	def void identityDoesNotReachAnotherRow() {
		val result = parseHelper.parse('''
			context foo {
				module bar {

					type String

					value-object Other {
						String elsewhere
					}

					value-object Row {
						String id
						identified-by elsewhere
					}

				}
			}
		''')
		val row = result.getAllContentsOfType(ValueObject).findFirst[name == "Row"]
		Assertions.assertTrue(row.identifiedBy.eIsProxy,
			"'elsewhere' belongs to another row and should not have resolved")
	}

	/** A business key names attributes of the type declaring it, and says what a collision does. */
	@Test
	def void aggregateDeclaresABusinessKey() {
		val model = '''
			context foo {
				module bar {

					type String

					/** Reported when the name is taken. */
					exception DuplicateNameException {
						message "Name is already taken"
					}

					aggregate-id ThingId identifies Thing {}

					aggregate Thing identifier ThingId {

						String name
						String kind

						/** No two things of the same kind share a name. */
						key NamePerKind exception DuplicateNameException {
							attributes name, kind
							on-collision refuse
							consistency strong
							display-as "$-name ($-kind)"
						}

					}

				}
			}
		'''.parsed
		val thing = model.getAllContentsOfType(Aggregate).head
		val key = thing.keys.head
		Assertions.assertEquals(2, key.keyAttributes.size)
		Assertions.assertEquals(#["name", "kind"], key.keyAttributes.map[it.name])
		Assertions.assertEquals("refuse", key.onCollision.literal)
	}

	/** Having no business key is stated rather than left out, and the reason is mandatory. */
	@Test
	def void aggregateDeclaresThatItHasNoKey() {
		val model = '''
			context foo {
				module bar {

					type String

					aggregate-id ThingId identifies Thing {}

					aggregate Thing identifier ThingId {

						String name

						/** Near duplicates are expected here; merging is the answer, not refusal. */
						no-key

					}

				}
			}
		'''.parsed
		Assertions.assertNotNull(model.getAllContentsOfType(Aggregate).head.noKey)
	}



	/** A rule declares the values it is handed and the condition it verifies over them. */
	@Test
	def void ruleComparesAnAttributeToNull() {
		val rule = '''
			/** Makes sure the thing is assigned. */
			business-rule MustBeAssigned exception SomeException {
				/** The entry it backs, if any. */
				optional String assignedEntry
				consistency strong
				requires assignedEntry != null
			}
		'''.ruleOf
		val comparison = rule.requires as RuleComparison
		Assertions.assertEquals("assignedEntry", comparison.left.attribute.name)
		Assertions.assertEquals("ne", comparison.op.getName())
	}

	/** A named value of an enumeration is a cross reference, scoped by the type on the left. */
	@Test
	def void ruleComparesAnAttributeToAnEnumValue() {
		val rule = '''
			/** Makes sure the transaction is ignored. */
			business-rule MustBeIgnored exception SomeException {
				/** The reconciliation status. */
				Status status
				consistency strong
				requires status == IGNORED
			}
		'''.ruleOf
		val comparison = rule.requires as RuleComparison
		val right = comparison.right as RuleRefOperand
		Assertions.assertEquals("IGNORED", right.target.eGet(right.target.eClass.getEStructuralFeature("name")))
	}

	/** Two attributes of the same enumeration compare too - the right hand side is not enum values only. */
	@Test
	def void ruleComparesTwoAttributes() {
		val rule = '''
			/** Makes sure both sides agree. */
			business-rule TypeMustMatch exception SomeException {
				/** One side. */
				Status status
				/** The other. */
				Status otherStatus
				consistency strong
				requires status == otherStatus
			}
		'''.ruleOf
		val right = (rule.requires as RuleComparison).right as RuleRefOperand
		Assertions.assertEquals("otherStatus", right.target.eGet(right.target.eClass.getEStructuralFeature("name")))
	}

	/** The one built-in operator over a collection. */
	@Test
	def void ruleAsksAboutACollection() {
		val empty = '''
			/** Makes sure nothing is linked. */
			business-rule MustHaveNoLinks exception SomeException {
				/** What is linked. */
				String links
				consistency strong
				requires links.is-empty()
			}
		'''.ruleOf
		Assertions.assertInstanceOf(RuleIsEmpty, empty.requires)
	}

	/** Negation, conjunction, disjunction and parentheses - the one compound shape the corpus needs. */
	@Test
	def void ruleCombinesConditions() {
		val rule = '''
			/** Makes sure a financial change does not conflict with a link. */
			business-rule MustNotBeLinkedForFinancialChange exception SomeException {
				/** The transaction it is linked to, if any. */
				optional String accountTransactionId
				/** The date as it stands. */
				String date
				/** The date as it would become. */
				String newDate
				consistency strong
				requires accountTransactionId == null || (date == newDate && !accountTransactionId.is-empty())
			}
		'''.ruleOf
		val or = rule.requires as RuleOr
		Assertions.assertInstanceOf(RuleComparison, or.left)
		Assertions.assertNotNull(or.right)
	}

	/** A bare Boolean attribute is a condition on its own. */
	@Test
	def void ruleUsesABareBoolean() {
		val rule = '''
			/** Makes sure the name is free. */
			business-rule NameMustBeUnique exception SomeException {
				/** Whether the name is taken. */
				Boolean nameTaken
				consistency strong
				requires !nameTaken
			}
		'''.ruleOf
		val not = rule.requires as RuleNot
		Assertions.assertEquals("nameTaken", (not.expr as RuleAttrRef).attribute.name)
	}

	/** The usage site binds the rule's attributes to what the carrying operation actually holds. */
	@Test
	def void usageBindsActuals() {
		val model = '''
			context foo {
				module bar {

					type String
					type Boolean

					/** Something went wrong. */
					exception SomeException {
						message "Nope"
					}

					aggregate-id ThingId identifies Thing {}

					aggregate Thing identifier ThingId {

						String name

						/** Makes sure the name is free. */
						business-rule NameMustBeUnique exception SomeException {
							/** Whether the name is taken. */
							Boolean nameTaken
							consistency strong
							requires !nameTaken
						}

						/** Renames it. */
						method rename business-rules NameMustBeUnique(exists(newName)) {
							/** The new name. */
							String newName
							operation-context RenameService
							service RenameService {
								/** Whether the name is taken. */
								method exists {
									/** The name to check. */
									String candidate
									returns Boolean
								}
							}
						}

					}

				}
			}
		'''.parsed
		val call = model.getAllContentsOfType(ServiceCallArgument).head
		Assertions.assertEquals("exists", call.method.name)
		Assertions.assertEquals(#["newName"], call.args.map[(it as VariableArgument).variable.name])
	}

	/**
	 * A service asked about the thing being acted on is handed its identity, which is not a declared
	 * attribute and therefore cannot be a reference - the same reason a rule takes 'own-id' directly.
	 */
	@Test
	def void serviceCallTakesOwnId() {
		val model = '''
			context foo {
				module bar {

					type String
					type Boolean

					/** Something went wrong. */
					exception SomeException {
						message "Nope"
					}

					aggregate-id ThingId identifies Thing {}

					aggregate Thing identifier ThingId {

						String name

						/** Makes sure nothing else points at it. */
						business-rule MustNotBeReferenced exception SomeException {
							/** Whether anything still points at it. */
							Boolean referenced
							consistency strong
							requires !referenced
						}

						/** Removes it. */
						method remove business-rules MustNotBeReferenced(referenced(own-id)) {
							operation-context RemoveService
							service RemoveService {
								/** Whether anything still points at the thing. */
								method referenced {
									/** The thing to count references for. */
									ThingId thing
									returns Boolean
								}
							}
						}

					}

				}
			}
		'''.parsed
		val call = model.getAllContentsOfType(ServiceCallArgument).head
		Assertions.assertEquals("referenced", call.method.name)
		Assertions.assertEquals(1, call.args.size)
		Assertions.assertTrue(call.args.head instanceof IdentityArgument)
	}

	/**
	 * A rule guarding an edit needs the value the carrier holds now as well as the one the operation
	 * would give it, and an edit's parameter is commonly named after the very field it overwrites.
	 * 'own' reaches past the parameters, so the two are told apart by the model rather than by naming.
	 */
	@Test
	def void usageBindsThePriorValuePastAShadowingParameter() {
		val model = '''
			context foo {
				module bar {

					type String
					type Boolean

					/** Something went wrong. */
					exception SomeException {
						message "Nope"
					}

					aggregate-id ThingId identifies Thing {}

					aggregate Thing identifier ThingId {

						String name

						/** Makes sure a locked thing keeps the name it has. */
						business-rule LockedNameMustNotChange exception SomeException {
							/** The name it carries now. */
							String name
							/** The name the edit would give it. */
							String newName
							consistency strong
							requires name == newName
						}

						/** Renames it, under a parameter named after the field it overwrites. */
						method rename business-rules LockedNameMustNotChange(own name, name) {
							/** The new name. */
							String name
						}

					}

				}
			}
		'''.parsed
		val aggregate = model.getAllContentsOfType(Aggregate).head
		val actuals = model.getAllContentsOfType(BusinessRuleInstance).head.params
		// The first actual is the field the parameter shadows, the second the parameter itself.
		Assertions.assertSame(aggregate.attributes.head, (actuals.get(0) as CarrierAttributeArgument).attribute)
		Assertions.assertSame(aggregate.methods.head.parameters.head,
			(actuals.get(1) as VariableArgument).variable)
	}

	/** A plain value of the operation is an actual too, and so is a literal. */
	@Test
	def void usageBindsAVariableAndALiteral() {
		val model = '''
			context foo {
				module bar {

					type String
					type Integer

					/** Something went wrong. */
					exception SomeException {
						message "Nope"
					}

					aggregate-id ThingId identifies Thing {}

					aggregate Thing identifier ThingId {

						String name

						/** Makes sure the name is short enough. */
						business-rule NameMustBeShort exception SomeException {
							/** The name being checked. */
							String candidate
							/** How long it may be. */
							Integer limit
							consistency strong
						}

						/** Renames it. */
						method rename business-rules NameMustBeShort(name, 10) {
							/** The new name. */
							String newName
						}

					}

				}
			}
		'''.parsed
		val variable = model.getAllContentsOfType(VariableArgument).head
		Assertions.assertEquals("name", variable.variable.name)
	}

	/** A hint may now sit wherever wording may, not only on a context or a view. */
	@Test
	def void hintsSitWhereverMetaInfoDoes() {
		val model = '''
			context foo {
				module bar {

					type String

					value-object Row {
						hint org.fuin.Whatever { "a" : true }
						String id {
							hint org.fuin.Field { "b" : 1 }
						}
					}

					command DoIt {
						slabel "Do it"
						label "Do it now"
						tooltip "Does the thing"
						hint org.fuin.Action { "c" : "yes" }
					}

				}
			}
		'''.parsed
		val row = model.getAllContentsOfType(ValueObject).head
		Assertions.assertEquals(1, row.hints.size)
		Assertions.assertEquals(1, row.attributes.head.overridden.hints.size)
	}

	/** A command carries its own wording, like every other named element except an event. */
	@Test
	def void commandCarriesWording() {
		val model = '''
			context foo {
				module bar {
					command DoIt {
						slabel "Do it"
						label "Do it now"
						tooltip "Does the thing"
					}
				}
			}
		'''.parsed
		val command = model.getAllContentsOfType(org.fuin.dsl.cqrs.cqrsDsl.Command).head
		Assertions.assertEquals("Do it", command.metaInfo.slabel)
		Assertions.assertEquals("Do it now", command.metaInfo.label)
		Assertions.assertEquals("Does the thing", command.metaInfo.tooltip)
	}

	/** Parses the model and fails the test on any syntax or linking error. */
	private def DomainModel parsed(CharSequence source) {
		val result = parseHelper.parse(source)
		Assertions.assertNotNull(result)
		val errors = result.eResource.errors
		Assertions.assertTrue(errors.isEmpty, '''Unexpected errors: «errors.join(", ")»''')
		validationHelper.assertNoErrors(result)
		result
	}

	/** Wraps a business rule in the smallest model that can hold one and returns the rule. */
	private def BusinessRule ruleOf(CharSequence rule) {
		val model = '''
			context foo {
				module bar {

					type String
					type Boolean

					/** Something went wrong. */
					exception SomeException {
						message "Nope"
					}

					/** How far a transaction got. */
					enum Status {
						instances {
							/** Still open. */
							OPEN
							/** Deliberately skipped. */
							IGNORED
						}
					}

					aggregate-id ThingId identifies Thing {}

					aggregate Thing identifier ThingId {
						String name
						«rule»
					}

				}
			}
		'''.parsed
		model.getAllContentsOfType(BusinessRule).head
	}
}

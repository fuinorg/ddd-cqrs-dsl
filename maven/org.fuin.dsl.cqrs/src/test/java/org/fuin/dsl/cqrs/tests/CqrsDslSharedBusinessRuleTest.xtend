package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.nio.file.Files
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Verifies that a {@code business-rule} may be declared at module level and referenced from an
 * aggregate in another module or context.
 *
 * <p>A rule such as "the entity must not be deleted" is the same rule in every aggregate that has a
 * soft delete. Declaring it once and importing it is what keeps the name, the exception and the
 * consistency classification from drifting apart across contexts.</p>
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslSharedBusinessRuleTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider
	@Inject ValidationTestHelper validationHelper

	/** A rule outside an aggregate is a module element like any other. */
	@Test
	def void moduleLevelRuleParses() {
		val model = parse('''
			context common {
				module rules {
					type String

					/** An operation was attempted on a deleted entity. */
					exception EntityInStateDeletedException {
						String entityIdPath
						message "Deleted: ${entityIdPath}"
					}

					/** Makes sure the entity was not (soft) deleted yet. */
					business-rule EntityMustNotBeDeletedRule exception EntityInStateDeletedException {
						consistency strong
					}
				}
			}
		''')
		assertNoErrors(model)

		val rules = model.contexts.head.modules.head
		Assertions.assertNotNull(rules.elements.filter(BusinessRule).findFirst[name == "EntityMustNotBeDeletedRule"],
			"the rule must be a module element, not something the parser dropped")
	}

	/** An aggregate of another context references the shared rule through a wildcard import. */
	@Test
	def void importedRuleIsReferencedByAnAggregateOfAnotherContext() {
		val model = parse('''
			«sharedRules»
			context shop {
				module orders {
					import common.rules.*

					aggregate-id OrderId identifies Order base String {
						slabel "OID"
						label "Order ID"
						tooltip "Unique identifier of the order"
						examples "4711"
					}

					/** An order. */
					aggregate Order identifier OrderId {

						/** Cancels the order. */
						method cancel business-rules EntityMustNotBeDeletedRule {
						}

					}
				}
			}
		''')
		assertNoErrors(model)

		val rule = referencedRule(model)
		Assertions.assertFalse(rule.eIsProxy, "the imported rule must resolve")
		Assertions.assertEquals("rules", (rule.eContainer as Module).name,
			"the reference must resolve to the shared module level rule")
		Assertions.assertEquals("EntityInStateDeletedException", rule.exception.name,
			"the shared rule must carry its exception across the import - that is what reaches the generated code")
	}

	/** Fully qualified, so no import is involved. */
	@Test
	def void fullyQualifiedRuleNeedsNoImport() {
		assertNoErrors(parse('''
			«sharedRules»
			context shop {
				module orders {
					aggregate-id OrderId identifies Order base common.rules.String {
						slabel "OID"
						label "Order ID"
						tooltip "Unique identifier of the order"
						examples "4711"
					}

					/** An order. */
					aggregate Order identifier OrderId {

						/** Cancels the order. */
						method cancel business-rules common.rules.EntityMustNotBeDeletedRule {
						}

					}
				}
			}
		'''))
	}

	/**
	 * Without an import the rule stays out of reach - a shared rule must not become visible
	 * everywhere just because it sits at module level.
	 */
	@Test
	def void ruleOfAnotherModuleIsNotVisibleWithoutImport() {
		assertErrors(parse('''
			«sharedRules»
			context shop {
				module orders {
					import common.rules.*

					aggregate-id OrderId identifies Order base String {
						slabel "OID"
						label "Order ID"
						tooltip "Unique identifier of the order"
						examples "4711"
					}

					/** An order. */
					aggregate Order identifier OrderId {

						/** Cancels the order. */
						method cancel business-rules UnknownRule {
						}

					}
				}
			}
		'''))
	}

	/**
	 * A rule declared inside the aggregate still works and still wins over an imported one of the
	 * same name - the aggregate's own declaration is the more specific statement.
	 */
	@Test
	def void ownRuleShadowsTheImportedOne() {
		val model = parse('''
			«sharedRules»
			context shop {
				module orders {
					import common.rules.*

					aggregate-id OrderId identifies Order base String {
						slabel "OID"
						label "Order ID"
						tooltip "Unique identifier of the order"
						examples "4711"
					}

					/** An order. */
					aggregate Order identifier OrderId {

						/** The aggregate's own take on it. */
						business-rule EntityMustNotBeDeletedRule exception EntityInStateDeletedException {
							consistency strong
						}

						/** Cancels the order. */
						method cancel business-rules EntityMustNotBeDeletedRule {
						}

					}
				}
			}
		''')
		assertNoErrors(model)

		val rule = referencedRule(model)
		Assertions.assertTrue(rule.eContainer instanceof Aggregate,
			"the aggregate's own rule must win over the imported one of the same name")

		// Where it lands matters as much as that it parses: a rule nested in an aggregate belongs to
		// its 'businessRules', not to the nested 'elements' that a module level rule is part of.
		// Allowing BusinessRule in both made the parser choose the wrong one - silently, because the
		// reference still resolved.
		val aggregate = rule.eContainer as Aggregate
		Assertions.assertEquals(1, aggregate.businessRules.size,
			"the aggregate's own rule must be one of its businessRules")
		Assertions.assertSame(rule, aggregate.businessRules.head,
			"the reference must point at the rule the aggregate declared")
		Assertions.assertTrue(aggregate.elements.filter(BusinessRule).empty,
			"a rule declared in an aggregate must not end up among its nested elements")
	}

	/**
	 * A rule an aggregate declares itself must not be reported as an illegal nested element.
	 *
	 * <p>Making BusinessRule a module element made it an AbstractElement, and the check that restricts
	 * what an aggregate may nest looks at every AbstractElement it contains - which now included the
	 * aggregate's own rules. Nothing about the parse tree was wrong, so only validation catches it.</p>
	 */
	@Test
	def void anAggregatesOwnRuleIsNoIllegalNestedElement() {
		val model = parse('''
			«sharedRules»
			context shop {
				module orders {
					import common.rules.*

					aggregate-id OrderId identifies Order base String {
						slabel "OID"
						label "Order ID"
						tooltip "Unique identifier of the order"
						examples "4711"
					}

					/** An order. */
					aggregate Order identifier OrderId {

						/** A rule of its own. */
						business-rule MustNotBeShipped exception EntityInStateDeletedException {
							consistency strong
						}

						/** Cancels the order. */
						method cancel business-rules MustNotBeShipped, EntityMustNotBeDeletedRule {
						}

					}
				}
			}
		''')
		assertNoErrors(model)
		validationHelper.assertNoIssues(model)
	}

	/** The shared module every test above imports from. */
	private def CharSequence sharedRules() '''
		context common {
			module rules {
				type String

				/** An operation was attempted on a deleted entity. */
				exception EntityInStateDeletedException {
					String entityIdPath
					message "Deleted: ${entityIdPath}"
				}

				/** Makes sure the entity was not (soft) deleted yet. */
				business-rule EntityMustNotBeDeletedRule exception EntityInStateDeletedException {
					consistency strong
				}
			}
		}
	'''

	/** The rule the aggregate's only method refers to. */
	private def BusinessRule referencedRule(DomainModel model) {
		val orders = model.contexts.findFirst[name == "shop"].modules.head
		val order = orders.elements.filter(Aggregate).head
		return order.methods.head.businessRules.businessRuleInstances.head.businessRule
	}

	private def DomainModel parse(CharSequence text) {
		val root = Files.createTempDirectory("shared-business-rule")
		val uri = URI.createFileURI(root.resolve("model.cqrs").toString)
		return parseHelper.parse(text, uri, resourceSetProvider.get)
	}

	private def void assertNoErrors(DomainModel model) {
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertTrue(model.eResource.errors.empty,
			'''Unexpected errors: «model.eResource.errors.join(", ")»''')
	}

	private def void assertErrors(DomainModel model) {
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertFalse(model.eResource.errors.empty,
			"Expected the reference to fail because nothing declares it")
	}
}

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

import static org.junit.jupiter.api.Assertions.*

/**
 * Verifies the typed path from an aggregate root down to the entity it addresses.
 *
 * <p>A child of a root there are many of cannot be addressed by its own identifier - 'TRANSACTION 45'
 * exists in every account-year - so it travels as a path. Untyped, that path says nothing about what it
 * addresses, which is why this model could hold fourteen of them all pointing at a transaction and state
 * it nowhere.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsEntityIdPathTypeTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void testAPathNamesTheIdentifiersItIsMadeOf() {
		val model = parseHelper.parse(this.model('''
			/** How a transaction is addressed. */
			entity-id-path AccountTransactionPath {
				AnnualTransactionsId / AccountTransactionId
			}
		'''))
		model.assertNoErrors

		val path = model.pathType
		assertEquals("AccountTransactionPath", path.name)
		assertEquals(#["AnnualTransactionsId", "AccountTransactionId"],
			path.segments.map[type.name].toList)
		// No range written means exactly one, and it has to be tellable from a range starting at zero.
		assertTrue(path.segments.forall[range === null])
	}

	@Test
	def void testASegmentMayStateHowManyItTakes() {
		// An entity that may contain another of its own kind - a role inside a role. Written out rather
		// than marked, because "may repeat" does not say whether the step may also be absent.
		val model = parseHelper.parse(this.model('''
			/** How a nested transaction is addressed. */
			entity-id-path NestedPath {
				AnnualTransactionsId / AccountTransactionId[1..*]
			}
		'''))
		model.assertNoErrors

		val segments = model.pathType.segments
		assertNull(segments.get(0).range, "a step without a range takes exactly one")
		assertEquals(1, segments.get(1).range.min)
		assertTrue(segments.get(1).range.unbounded, "'*' is unbounded")
	}

	@Test
	def void testARangeCanAlsoSayZeroOrCapped() {
		// Neither is what a role path says - that is '[1..*]', one or more, so the path always addresses
		// the thing it names. These only pin that the range can express what a repeat marker could not,
		// which is why it replaced one.
		val model = parseHelper.parse(this.model('''
			/** Skippable. */
			entity-id-path SkippablePath {
				AnnualTransactionsId / AccountTransactionId[0..*]
			}

			/** Capped. */
			entity-id-path CappedPath {
				AnnualTransactionsId / AccountTransactionId[1..2]
			}
		'''))
		model.assertNoErrors

		val paths = model.eAllContents.filter(org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType).toList
		assertEquals(0, paths.get(0).segments.get(1).range.min)
		assertTrue(paths.get(0).segments.get(1).range.unbounded)
		assertEquals(1, paths.get(1).segments.get(1).range.min)
		assertEquals(2, paths.get(1).segments.get(1).range.max)
		assertFalse(paths.get(1).segments.get(1).range.unbounded)
	}

	@Test
	def void testAPathIsATypeAnAttributeCanUse() {
		// Which is the whole point: one declaration types every attribute that holds such a path.
		parseHelper.parse(this.model('''
			/** How a transaction is addressed. */
			entity-id-path AccountTransactionPath {
				AnnualTransactionsId / AccountTransactionId
			}

			/** One transaction on screen. */
			value-object TransactionRow {
				/** What this row addresses. */
				AccountTransactionPath id
			}
		''')).assertNoErrors
	}

	@Test
	def void testAPathOfOneStepIsTheIdentifierItself() {
		// 'ANNUAL_TRANSACTIONS 2026-a' is an aggregate id, not a path, and calling it one buys nothing.
		parseHelper.parse(this.model('''
			/** Pointless. */
			entity-id-path RootOnlyPath {
				AnnualTransactionsId
			}
		''')).assertError(CqrsDslPackage.Literals.ENTITY_ID_PATH_TYPE,
			CqrsDslValidator.PATH_NEEDS_MORE_THAN_A_ROOT)
	}

	@Test
	def void testAPathHasToStartAtARoot() {
		// The mistake the model's own prose invites: 'AnnualTransactionsId' is made of an account and a
		// year, described as "the natural composite key (account, year)" - so writing the account as a
		// first step reads right and is wrong. The path still begins at 'ANNUAL_TRANSACTIONS'.
		parseHelper.parse(this.model('''
			/** Starts one level too high. */
			entity-id-path TooHighPath {
				AccountTransactionId / AnnualTransactionsId
			}
		''')).assertError(CqrsDslPackage.Literals.PATH_SEGMENT,
			CqrsDslValidator.PATH_MUST_START_AT_A_ROOT)
	}

	@Test
	def void testALaterStepHasToBeAnEntityOfThatRoot() {
		parseHelper.parse(this.model('''
			/** Second step is another aggregate. */
			entity-id-path TwoRootsPath {
				AnnualTransactionsId / AccountId
			}
		''')).assertError(CqrsDslPackage.Literals.PATH_SEGMENT,
			CqrsDslValidator.PATH_SEGMENT_NOT_OF_ROOT)
	}

	@Test
	def void testAnEntityOfAnotherRootIsNotAStep() {
		// It resolves, it is an entity id, and it belongs to a different aggregate - which no amount of
		// reading the path would tell you.
		parseHelper.parse(this.model('''
			/** Wrong owner. */
			entity-id-path ForeignPath {
				AccountId / AccountTransactionId
			}
		''')).assertError(CqrsDslPackage.Literals.PATH_SEGMENT,
			CqrsDslValidator.PATH_SEGMENT_NOT_OF_ROOT)
	}

	@Test
	def void testAnEmptyRangeIsRefused() {
		// An impossible range silently rejects every path it is given, so it is caught where it is written.
		parseHelper.parse(this.model('''
			/** Contradictory. */
			entity-id-path ImpossiblePath {
				AnnualTransactionsId / AccountTransactionId[2..1]
			}
		''')).assertError(CqrsDslPackage.Literals.SEGMENT_RANGE, CqrsDslValidator.PATH_RANGE_INVALID)
	}

	@Test
	def void testAStepThatAcceptsNothingIsRefused() {
		parseHelper.parse(this.model('''
			/** Accepts nothing. */
			entity-id-path ZeroPath {
				AnnualTransactionsId / AccountTransactionId[0..0]
			}
		''')).assertError(CqrsDslPackage.Literals.SEGMENT_RANGE, CqrsDslValidator.PATH_RANGE_INVALID)
	}

	private def pathType(DomainModel model) {
		model.eAllContents.filter(org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType).head
	}

	private def String model(String declarations) '''
		context p {
			module c.n {
				type String

				type UUID

				aggregate-id AccountId identifies Account base UUID { }

				aggregate-id AnnualTransactionsId identifies AnnualTransactions base UUID { }

				entity-id AccountTransactionId identifies Transaction base String { }

				«declarations»

				aggregate Account identifier AccountId { }

				aggregate AnnualTransactions identifier AnnualTransactionsId { }

				entity Transaction identifier AccountTransactionId root AnnualTransactions { }
			}
		}
	'''

}

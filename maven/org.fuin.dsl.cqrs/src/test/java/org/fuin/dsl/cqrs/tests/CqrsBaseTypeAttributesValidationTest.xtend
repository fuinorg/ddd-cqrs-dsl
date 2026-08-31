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
 * Verifies which attributes a 'base' allows.
 *
 * <p>A 'base' says the type wraps a single value, and the generator writes it as exactly that: one
 * declared attribute holds the value and the constructor taking it is what the generated valueOf()
 * and the converters call. With no attribute there is neither a constructor nor anything for
 * asBaseType() to return; with several and a base the generator instantiates from, the emitted
 * one-argument call matches no constructor. Both used to reach javac as a broken write-once file.
 *
 * <p>An entity id on Integer or UUID and an aggregate id on UUID are exempt: for those the generator
 * emits the complete class from the base type alone, so there is nothing to declare.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsBaseTypeAttributesValidationTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Inject
	extension ValidationTestHelper

	@Test
	def void aBaseWithTheOneAttributeHoldingItIsFine() {
		parseHelper.parse(model('''
			value-object Email base String {
				String value
			}
		''')).assertNoIssues
	}

	@Test
	def void anInstantiatedBaseWithNoAttributeAtAllIsRefused() {
		parseHelper.parse(model('''
			value-object Key base UUID {
			}
		''')).assertError(CqrsDslPackage.Literals.VALUE_OBJECT, CqrsDslValidator.BASE_TYPE_NEEDS_ONE_ATTRIBUTE)
	}

	@Test
	def void aStringBaseWithNoAttributeIsLeftToItsWriteOnceClass() {
		// The generator never instantiates a String-backed type - its valueOf() is emitted commented
		// out - so a model may leave the value to the hand-written final class. jtenman does.
		parseHelper.parse(model('''
			value-object Email base String {
			}
		''')).assertNoIssues
	}

	@Test
	def void aStringBaseMayPackSeveralAttributes() {
		// Nothing instantiates a String-backed type from a single value - its valueOf() is generated
		// commented out - so the several-attribute shape (a phone number, say) stays legal.
		parseHelper.parse(model('''
			value-object PhoneNumber base String {
				String country
				String number
			}
		''')).assertNoIssues
	}

	@Test
	def void anInstantiatedBaseMayNot() {
		parseHelper.parse(model('''
			value-object Pair base UUID {
				UUID a
				UUID b
			}
		''')).assertError(CqrsDslPackage.Literals.VALUE_OBJECT, CqrsDslValidator.BASE_TYPE_NEEDS_ONE_ATTRIBUTE)
	}

	@Test
	def void anEntityIdOnIntegerOrUuidIsWrittenWholeFromItsBase() {
		parseHelper.parse(model('''
			aggregate-id ThingId identifies Thing base UUID {
			}

			aggregate Thing identifier ThingId {
			}

			entity-id PartId identifies Part base Integer {
			}

			entity Part identifier PartId root Thing {
			}

			entity-id SlotId identifies Slot base UUID {
			}

			entity Slot identifier SlotId root Thing {
			}
		''')).assertNoIssues
	}

	@Test
	def void anAggregateIdOnIntegerNeedsIt() {
		// Only the UUID shortcut exists for an aggregate id - there is no IntegerAggregateRootId.
		parseHelper.parse(model('''
			aggregate-id ThingId identifies Thing base Integer {
			}

			aggregate Thing identifier ThingId {
			}
		''')).assertError(CqrsDslPackage.Literals.AGGREGATE_ID, CqrsDslValidator.BASE_TYPE_NEEDS_ONE_ATTRIBUTE)
	}

	private def String model(CharSequence body) {
		'''
			context p {
				module c.n {
					type String
					type Integer
					type UUID

					«body»
				}
			}
		'''.toString
	}

}

package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.junit.jupiter.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*

/**
 * Turning an attribute into a parameter must leave the attribute as it was.
 *
 * <p>These read like tests of nothing - convert something, then look at what was converted - and that
 * is the point. EMF gives an object one container, so assigning an attribute's <code>overridden</code>
 * wording to a freshly built parameter <em>moves</em> it: the parameter gains the wording and the
 * attribute in the parsed model silently loses it. Nothing fails, nothing is logged, and the damage is
 * not visible to the generator that caused it - only to whatever reads the same model afterwards, which
 * in a build with two targets is the second one.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class CqrsAttributeExtensionsTest {

    @Inject ParseHelper<DomainModel> parser
    @Inject ValidationTestHelper validationTester

    static val MODEL = '''
        context a.b {
            module m {

                type String

                type Integer

                /** The text length must lie between the two bounds, inclusive. */
                constraint Length input String {
                    Integer min
                    Integer max
                    message "must be between ${min} and ${max} characters"
                }

                value-object Row {
                    String name invariants Length(1, 100) {
                        slabel "N"
                        label "Name"
                        tooltip "What it is called"
                    }
                }

            }
        }
    '''

    @Test
    def void testAsParameterLeavesTheAttributesWordingWhereItWas() {
        val attribute = attribute()

        attribute.asParameter

        assertNotNull(attribute.overridden, "the attribute kept its wording")
        assertEquals("Name", attribute.overridden.metaInfo.label)
    }

    @Test
    def void testAsParameterLeavesTheAttributesInvariantsWhereTheyWere() {
        val attribute = attribute()

        attribute.asParameter

        assertNotNull(attribute.invariants, "the attribute kept its invariants")
        assertEquals(1, attribute.invariants.constraintInstances.size)
    }

    @Test
    def void testAsParameterStillCarriesTheWordingOver() {
        assertEquals("Name", attribute().asParameter.overridden.metaInfo.label)
    }

    @Test
    def void testCopyWithNewNameLeavesTheOriginalIntact() {
        val attribute = attribute()

        val copy = attribute.copyWithNewName("other")

        assertEquals("Name", copy.overridden.metaInfo.label)
        assertNotNull(attribute.overridden, "the original kept its wording")
        assertNotNull(attribute.invariants, "the original kept its invariants")
    }

    private def attribute() {
        val model = parser.parse(MODEL)
        validationTester.assertNoErrors(model)
        val row = model.eAllContents.filter(typeof(ValueObject)).findFirst[name == "Row"]
        return row.attributes.get(0)
    }

}

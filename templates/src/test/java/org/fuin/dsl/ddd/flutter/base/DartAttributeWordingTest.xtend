package org.fuin.dsl.ddd.flutter.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.Utils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

/**
 * Tests the rule the whole descriptor design rests on: either the type or the field states the wording,
 * and where both do, the field wins.
 *
 * <p>Worth its own test because the two are easy to confuse in the output. A model that captions a type
 * and every use of it identically - which is what a careful model tends to do - cannot tell you which
 * of the two the generator actually read. Everything here therefore words the field differently from
 * the type on purpose.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartAttributeWordingTest {

    @Inject ParseHelper<DomainModel> parser
    @Inject ValidationTestHelper validationTester

    @Test
    def void testFieldWordingWinsOverTheType() {
        assertThat(attribute("nickname").meta?.label).isEqualTo("Nickname")
    }

    @Test
    def void testAnOptionalFieldStatesItsWordingToo() {
        assertThat(attribute("alias").meta?.label).isEqualTo("Alias")
    }

    @Test
    def void testAFieldWithoutWordingInheritsTheTypes() {
        assertThat(attribute("plain").meta?.label).isEqualTo("Product name")
    }

    @Test
    def void testAFieldWhoseTypeCanHoldNoWordingCarriesItsOwn() {
        assertThat(attribute("listedOn").meta?.label).isEqualTo("Listed on")
    }

    @Test
    def void testFieldWordingSurvivesOnAProtectedRow() {
        // A row of personal data is still a row. Whether the model declares it protected says what may
        // be done with the values, and nothing about what to call them.
        assertThat(attribute("ProtectedDetails", "nickname").meta?.label).isEqualTo("Nickname")
        assertThat(attribute("ProtectedDetails", "alias").meta?.label).isEqualTo("Alias")
    }

    @Test
    def void testAListIsTypedAndReadByItsElementType() {
        // The declared type of `List<X> xs` is `List`, which says nothing worth knowing. Everything a
        // caller wants - what it holds, how to read it, what to call it - is a question about X.
        val aliases = attribute("aliases")
        assertThat(aliases.type).isEqualTo("List<ProductName>")
        assertThat(aliases.multiple).isTrue
        assertThat(aliases.fromJson("json"))
            .isEqualTo("requiredList(json, 'aliases', ProductName.fromWire)")
        assertThat(aliases.toJson()).isEqualTo("aliases.map((e) => e.value).toList(growable: false)")
        assertThat(aliases.meta?.label).isEqualTo("Product name")
    }

    @Test
    def void testAListOfEnumeratedValuesKeepsTheElementsKind() {
        val colours = attribute("colours")
        assertThat(colours.type).isEqualTo("List<Colour>")
        assertThat(colours.valueKind).isEqualTo("ValueKind.enumeration")
        assertThat(colours.values).isEqualTo("Colour.descriptors")
        assertThat(colours.fromJson("json"))
            .isEqualTo("requiredList(json, 'colours', (e) => Colour.fromWire(e as String))")
    }

    @Test
    def void testAListOfPlainValuesTravelsAsItStands() {
        val tags = attribute("tags")
        assertThat(tags.type).isEqualTo("List<String>")
        assertThat(tags.fromJson("json")).isEqualTo("requiredList(json, 'tags', (e) => e as String)")
        assertThat(tags.toJson()).isEqualTo("tags")
    }

    @Test
    def void testAnAbsentListIsNotTheSameAsAnEmptyOne() {
        val nicknames = attribute("nicknames")
        assertThat(nicknames.type).isEqualTo("List<ProductName>?")
        assertThat(nicknames.fromJson("json"))
            .isEqualTo("optionalList(json, 'nicknames', ProductName.fromWire)")
    }

    @Test
    def void testASingleValueIsNotMultiple() {
        assertThat(attribute("plain").multiple).isFalse
        assertThat(attribute("plain").type).isEqualTo("ProductName")
    }

    @Test
    def void testAKeyNamesTheTypeTheWordingWasTakenFrom() {
        // One entry serves every use of the value object, which is the point of captioning it once on
        // the type: a row and the form of every command that sets it read the same caption, so they
        // read the same translation of it too.
        assertThat(attribute("plain").metaKey("ProductDetails")).isEqualTo("ProductName")
        assertThat(attribute("aliases").metaKey("ProductDetails")).isEqualTo("ProductName")
    }

    @Test
    def void testAnOverrideIsKeyedByItsOwnerBecauseABareNameIsNotUnique() {
        // A bundle holding a person and a role has two attributes called "id" and two called "name".
        // Keying an override by the bare name puts them on one entry, and one of the captions wins
        // silently - the bug this whole scheme exists to make impossible.
        assertThat(attribute("nickname").metaKey("ProductDetails")).isEqualTo("ProductDetails.nickname")
        assertThat(attribute("alias").metaKey("ProductDetails")).isEqualTo("ProductDetails.alias")
        assertThat(attribute("ProtectedDetails", "nickname").metaKey("ProtectedDetails"))
            .isEqualTo("ProtectedDetails.nickname")
    }

    @Test
    def void testAFieldWhoseTypeCanHoldNoWordingIsKeyedByItself() {
        assertThat(attribute("listedOn").metaKey("ProductDetails")).isEqualTo("ProductDetails.listedOn")
    }

    @Test
    def void testTheKeyAndTheWordingAlwaysComeFromTheSamePlace() {
        // The two are decided by one function on purpose. Where they are decided twice they drift, and
        // a key pointing at wording that is not the wording it stands for is a caption that silently
        // becomes another field's the moment a second language is installed.
        for (name : #["nickname", "alias", "plain", "listedOn", "aliases"]) {
            val a = attribute(name)
            val fromType = a.metaKey("ProductDetails").indexOf('.') < 0
            assertThat(fromType)
                .describedAs("%s: keyed by its type but worded by itself, or the other way round", name)
                .isEqualTo(a.attribute.overridden?.metaInfo === null
                    || a.attribute.overridden.metaInfo.label === null)
        }
    }

    private def DartAttribute attribute(String name) {
        attribute("ProductDetails", name)
    }

    private def DartAttribute attribute(String row, String name) {
        val model = parser.parse(Utils.readAsString(class.getResource("/dart-wording.cqrs")))
        validationTester.assertNoErrors(model)
        val found = model.find(typeof(ValueObject), row)
        return new DartAttribute(found.attributes.findFirst[it.name == name])
    }

}

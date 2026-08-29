package org.fuin.dsl.ddd.flutter.valueobject

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*

/**
 * Tests the read-model row: its JSON, and the descriptor a generic renderer draws it from.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartRowArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testRow() {

        // PREPARE
        val testee = createTestee()
        val vo = model.find(typeof(ValueObject), "CategoryDetails")

        // TEST
        val result = testee.create(vo, new HashMap<String, Object>(), false)

        // VERIFY
        val artifact = result.iterator.next
        assertThat(artifact.pathAndName).isEqualTo("categories/category_details.dart")
        assertThat(new String(artifact.data, "UTF-8"))
            .isEqualTo("categories/category_details.dart".loadDartExample)
    }

    @Test
    def void testASingleValuedValueObjectIsNotThisFactorysBusiness() {
        // It is a wrapper around one value, and a different factory's artifact. Saying nothing beats
        // emitting a row with one column in it.
        assertThat(createTestee().create(model.find(typeof(ValueObject), "CategoryName"),
            new HashMap<String, Object>(), false)).isNull
    }

    @Test
    def void testARowCanNameTheAttributeThatIdentifiesIt() {
        // Two things the attribute's type cannot say. A natural key is an ordinary value object, so the
        // type-derived rule makes it plain data and the row has no identity at all. And a row holding a
        // second id - a reference, not its identity - has that id hidden from the screen although the
        // model gives it wording. Declaring the identity settles both, and because 'identified-by' is a
        // cross-reference, naming an attribute the row does not have cannot get as far as a generator.
        val generated = generateFrom("/dart-child-entity.cqrs", "BookRow")

        assertThat(generated).contains("modelType: 'Isbn',\n        role: AttributeRole.key,")

        // The aggregate's own id is a reference here, so it is shown rather than taken for the identity.
        assertThat(generated).doesNotContain("role: AttributeRole.identifier")

        // And the projection's bookkeeping is still recognised by its type, key or no key.
        assertThat(generated).contains("role: AttributeRole.source")
    }

    @Test
    def void testNamingTheKeyDoesNotMakeASurrogateAColumn() {
        // Which attribute identifies the row and whether a user should read it are two questions. A
        // journal entry's UUID is its key and is of no interest on screen; the reference beside it is.
        val generated = generateFrom("/dart-child-entity.cqrs", "ShelfRow")

        assertThat(generated).contains("modelType: 'BookId',\n        role: AttributeRole.identifier,")
        assertThat(generated).doesNotContain("modelType: 'BookId',\n        role: AttributeRole.key,")

        // The second id is a reference, so it stays a column - it is what the model gives wording to.
        assertThat(generated).contains("kind: ValueKind.identifier,\n        modelType: 'ChapterId',\n        text:")
    }

    @Test
    def void testAPathIsAnIdentityRatherThanSomethingToRead() {
        // A child of a root there are many of cannot be addressed by its own id, so the row's identity
        // is the whole path. It is an external type, so without recognising it by name the row would
        // show a column of raw paths and - having no attribute marked identifier - offer no action at all.
        val generated = generateFrom("/dart-child-entity.cqrs", "ChapterRow")

        assertThat(generated).contains("name: 'id',\n        kind: ValueKind.text,\n        role: AttributeRole.identifier,")

        // And only the one it declares. The other path points at a different chapter, so it is a
        // reference and stays a column - otherwise every row that records what it was matched to would
        // lose that column and gain a second identity.
        assertThat(generated).contains("name: 'continues',\n        kind: ValueKind.text,\n        optional: true,")
    }

    @Test
    def void testARowWithoutAKeyStillRecognisesAnIdentifierByItsType() {
        // The counterpart: nothing in dart-categories.cqrs declares a key, and CategoryDetails must
        // still identify itself. Otherwise "name the key" would quietly become "name it everywhere".
        assertThat(generate("CategoryDetails")).contains("role: AttributeRole.identifier")
    }

    @Test
    def void testACompositeAttributePointsAtItsOwnDescriptor() {
        // Declared like any other type, but it arrives as a JSON object - so a cell handed one has
        // nothing printable and renders the map. Its own descriptor is what gives a cell the
        // sub-attributes and their wording, and a form the fields to draw.
        val generated = generateFrom("/dart-child-entity.cqrs", "BookRow")

        assertThat(generated).contains("nested: Imprint.descriptor,")

        // A wrapper around a single value has no descriptor to point at, so it must not claim one.
        assertThat(generated).doesNotContain("nested: Isbn.descriptor")

        // Nor does the projection's bookkeeping, which is a composite no screen ever draws.
        assertThat(generated).doesNotContain("nested: VersionedEntityIdPath.descriptor")
    }

    private def String generate(String valueObject) {
        new String(createTestee.create(model.find(typeof(ValueObject), valueObject),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    /**
     * A row is named to a person by the business key of the type it projects, which it reaches through
     * its own identity attribute.
     */
    @Test
    def void testARowIsNamedByTheKeyOfWhatItProjects() {
        val generated = generateFrom("/business-keys.cqrs", "CategoryDetails")

        // Raw, because "${" in an ordinary Dart literal is interpolation rather than text.
        assertThat(generated).contains("displayFormat: r'${name} (${kind})',")
    }

    /**
     * A key is declared over the write model and a row is a projection of it, so the two need not
     * agree. Where the row cannot answer the whole format it says nothing, and the client falls back
     * to the first displayed attribute visibly rather than rendering a label with a gap in it.
     */
    @Test
    def void testARowCarryingHalfTheKeyIsNotNamedByIt() {
        assertThat(generateFrom("/business-keys.cqrs", "CategorySummary")).doesNotContain("displayFormat")
    }

    /**
     * A row carrying another type's id refers to it rather than being it, so it is not named after
     * what that id identifies - which is the distinction the identity role draws.
     */
    @Test
    def void testARowIsNotNamedByATypeItMerelyReferences() {
        assertThat(generateFrom("/business-keys.cqrs", "EntryDetails")).doesNotContain("displayFormat")
    }

    private def String generateFrom(String resource, String valueObject) {
        val DomainModel other = parser.parse(Utils.readAsString(class.getResource(resource)))
        validationTester.assertNoErrors(other)
        return new String(createTestee.create(other.find(typeof(ValueObject), valueObject),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    private def createTestee() {
        val factory = new DartRowArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartRow",
            DartRowArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

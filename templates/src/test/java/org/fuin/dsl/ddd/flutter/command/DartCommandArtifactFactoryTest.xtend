package org.fuin.dsl.ddd.flutter.command

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
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

/** Tests the Dart command: the wire body, and what a form has to collect before it can be sent. */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartCommandArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testCreate() {
        assertGenerates("CreateCategoryCommand", "categories/create_category_command.dart")
    }

    @Test
    def void testModify() {
        assertGenerates("RenameCategoryCommand", "categories/rename_category_command.dart")
    }

    @Test
    def void testRemove() {
        assertGenerates("RemoveCategoryCommand", "categories/remove_category_command.dart")
    }

    @Test
    def void testTheKindComesFromWhatTheOperationDoes() {
        // A constructor creates, an operation firing an exodus event removes, anything else modifies.
        // That is what decides whether a screen puts it on a button, behind a row, or behind a
        // confirmation - and whether the command carries an aggregate version at all.
        assertThat(generate("CreateCategoryCommand")).contains("kind: CommandKind.create")
        assertThat(generate("RenameCategoryCommand")).contains("kind: CommandKind.modify")
        assertThat(generate("RemoveCategoryCommand")).contains("kind: CommandKind.remove")

        assertThat(generate("CreateCategoryCommand")).doesNotContain("aggregateVersion")
        assertThat(generate("RenameCategoryCommand")).contains("aggregateVersion")
    }

    @Test
    def void testAttributesComeFromTheTargetOperation() {
        // A command declares none of its own - it names an operation and takes that operation's
        // parameters, which is what keeps a form and the aggregate in step.
        assertThat(generate("CreateCategoryCommand")).contains("final CategoryName name;")
        assertThat(generate("CreateCategoryCommand")).contains("final CategoryType kind;")
        assertThat(generate("RenameCategoryCommand")).contains("final CategoryName newName;")
    }

    @Test
    def void testWordingFallsBackToTheType() {
        // The parameters of an operation carry no wording of their own, so a form built from a command
        // would be captioned "name" and "kind" without this: the value object and the enum are
        // captioned once, and every use of them inherits it.
        assertThat(generate("CreateCategoryCommand")).contains("label: 'Name'")
        assertThat(generate("CreateCategoryCommand")).contains("label: 'Type'")
    }

    @Test
    def void testARefusalTheModelCannotPlaceIsLeftOut() {
        // DuplicateCategoryNameException carries BOTH a name and a kind, and the create command has an
        // attribute of each - so the model does not say which the rule is about. Guessing from the
        // exception's class name would put it on "name" and would be a guess; a refusal shown on the
        // wrong field is worse than one shown above the form. See todo.md.
        assertThat(generate("CreateCategoryCommand")).doesNotContain("rejections")

        // A rename has exactly one field, so any field-level refusal is about it. That the model does
        // state.
        assertThat(generate("RenameCategoryCommand"))
            .contains("'DuplicateCategoryNameException': 'newName'")
    }

    @Test
    def void testAnAttributeSaysWhichTypeItHolds() {
        // A rename's newName and a row's name are both a CategoryName. That is the only statement in
        // the model saying a form may open with the value the row already holds, because the two are
        // not called the same thing - matching on the attribute name alone finds nothing here.
        assertThat(generate("RenameCategoryCommand")).contains("modelType: 'CategoryName'")
        assertThat(generate("CreateCategoryCommand")).contains("modelType: 'CategoryType'")
    }

    @Test
    def void testACommandAddressingAChildEntityCarriesBothIds() {
        // The wire carries the path from the root down to the entity. A command that knows only the
        // root cannot say which chapter it means, so the generated class would be unusable for every
        // child entity in the model - which is what it was until this test existed.
        val generated = generateFrom("/dart-child-entity.cqrs", "RetitleChapterCommand")

        assertThat(generated).contains("final BookId aggregateId;")
        assertThat(generated).contains("final ChapterId entityId;")
        assertThat(generated).contains(
            "String get entityIdPath => '${aggregateId.typed}/${entityId.typed}';")
        assertThat(generated).contains("'entity-id-path': entityIdPath,")

        // Both ids have to be asked for, or one of them is silently absent from the request.
        assertThat(generated).contains("required this.aggregateId,")
        assertThat(generated).contains("required this.entityId,")

        // And both types have to be imported, or the file does not compile where it lands.
        assertThat(generated).contains("books/book_id.dart';")
        assertThat(generated).contains("books/chapter_id.dart';")
    }

    @Test
    def void testACommandSaysWhereItsIdentifierComesFrom() {
        // Four different things are asked of a screen here, and picking the wrong one does not draw
        // badly - it addresses the write at the wrong aggregate. None of it is derivable from the rest
        // of the descriptor: `target` names the aggregate, not what a client is supposed to do about it.
        assertThat(generate("CreateCategoryCommand"))
            .contains("targetOrigin: CommandTargetOrigin.clientGenerated")
        assertThat(generate("RenameCategoryCommand"))
            .contains("targetOrigin: CommandTargetOrigin.row")
        assertThat(generateFrom("/dart-child-entity.cqrs", "AddChapterCommand"))
            .contains("targetOrigin: CommandTargetOrigin.parentOfRow")
        assertThat(generateFrom("/dart-child-entity.cqrs", "PrintEditionCommand"))
            .contains("targetOrigin: CommandTargetOrigin.derived")
    }

    @Test
    def void testTheTargetTypeIsTheModelsAndNotGuessedFromAName() {
        // The wire type of an entity is its own name in upper snake case, and that is not recoverable
        // from the id class or the aggregate: melkheftken has an AccountTransactionId whose type is
        // TRANSACTION. So the model states it rather than a screen inferring it.
        assertThat(generate("CreateCategoryCommand")).contains("targetType: 'CATEGORY'")
        assertThat(generateFrom("/dart-child-entity.cqrs", "AddChapterCommand"))
            .contains("targetType: 'CHAPTER'")
    }

    @Test
    def void testADateTravelsAsTheWireCarriesIt() {
        // A bare DateTime cannot be sent at all: a JSON encoder refuses it outright, and a query string
        // renders it '2026-08-21 00:00:00.000', which the server does not parse back. The model says it
        // is a calendar day, so it travels as one.
        val generated = generateFrom("/dart-child-entity.cqrs", "PublishBookCommand")

        assertThat(generated).contains("'publishOn': wireDate(publishOn),")
        assertThat(generated).doesNotContain("'publishOn': publishOn,")

        // And the helper has to be reachable from where the file lands.
        assertThat(generated).contains("src/json/json.dart';")
    }

    @Test
    def void testACommandAddressingTheRootCarriesOneId() {
        // The counterpart, so the child case cannot be "fixed" by giving every command two ids.
        val generated = generate("RenameCategoryCommand")

        assertThat(generated).contains("final CategoryId aggregateId;")
        assertThat(generated).contains("String get entityIdPath => aggregateId.typed;")
        assertThat(generated).doesNotContain("entityId;")
    }

    private def void assertGenerates(String command, String expected) {
        val artifact = createTestee.create(model.find(typeof(Command), command),
            new HashMap<String, Object>(), false).iterator.next
        assertThat(artifact.pathAndName).isEqualTo(expected)
        assertThat(new String(artifact.data, "UTF-8")).isEqualTo(expected.loadDartExample)
    }

    private def String generate(String command) {
        new String(createTestee.create(model.find(typeof(Command), command),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    private def createTestee() {
        val factory = new DartCommandArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartCommand",
            DartCommandArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def String generateFrom(String resource, String command) {
        new String(createTestee.create(modelOf(resource).find(typeof(Command), command),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    private def model() {
        modelOf("/dart-categories.cqrs")
    }

    private def modelOf(String resource) {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource(resource)))
        validationTester.assertNoErrors(model)
        return model
    }

}

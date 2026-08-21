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

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

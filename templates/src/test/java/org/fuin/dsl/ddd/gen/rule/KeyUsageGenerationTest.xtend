package org.fuin.dsl.ddd.gen.rule

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.aggregate.AbstractAggregateArtifactFactory
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

/**
 * Tests what an operation that names a business key, and says nothing else, generates.
 *
 * <p>Three things follow from the key and the operation, and none of them is written in the model: the
 * call that verifies the rule, the values handed to it, and the service method that answers whether
 * the key is taken. What binds a key attribute to a value is its <em>type</em> - an operation that
 * edits names its parameter after the change rather than the field, so a name match would bind nothing
 * on exactly the operations a uniqueness check matters most for.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class KeyUsageGenerationTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    /** A create hands over its own arguments: everything the key is made of is one of them. */
    @Test
    def void testACreateBindsEveryKeyAttributeToItsArgument() {
        assertThat(rules).contains(
            "new NamePerKindMustBeUnique(createService.existsNamePerKind(name, kind), name, kind).verify();")
    }

    /**
     * An edit binds what it changes to its parameter and reads the rest off the carrier - by type,
     * because the parameter is called "newName" and the key attribute is called "name".
     */
    @Test
    def void testAnEditBindsByTypeAndReadsTheRestOffTheCarrier() {
        assertThat(rules).contains(
            "new NamePerKindMustBeUnique(renameService.existsNamePerKind(newName, self.getKind(), "
                + "self.getId()), newName, self.getKind()).verify();")
    }

    /** Both operations refuse with the key's exception, declared once on each generated method. */
    @Test
    def void testTheOperationDeclaresTheKeysRefusal() {
        val generated = rules
        assertThat(generated).contains("static void create(")
        assertThat(generated).contains("throws DuplicateNameException {")
    }

    /**
     * The service is asked for the answer, so the method is declared on it. An operation that edits
     * also hands over the carrier's identity: renaming something to the name it already holds is a
     * repetition rather than a collision, and there would otherwise be nothing to exclude it by.
     */
    @Test
    def void testTheAnswerIsDeclaredOnTheOperationsOwnService() {
        val generated = abstractAggregate

        assertThat(generated).contains(
            "boolean existsNamePerKind(final CategoryName name, final CategoryKind kind);")
        assertThat(generated).contains(
            "boolean existsNamePerKind(final CategoryName name, final CategoryKind kind, final CategoryId categoryId);")
    }

    private def String rules() {
        return generate(new AggregateRulesArtifactFactory(), "aggregateRules")
    }

    private def String abstractAggregate() {
        return generate(new AbstractAggregateArtifactFactory(), "abstractAggregate")
    }

    private def String generate(ArtifactFactory<Aggregate> factory, String name) {
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig(name, factory.class.name,
            "command.core", "genMainJava")
        config.init(new DefaultContext(), null)
        factory.init(config)
        return new String(factory.create(model.find(typeof(Aggregate), "Category"),
            new HashMap<String, Object>(), false).iterator.next.data, "UTF-8")
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/business-keys.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}

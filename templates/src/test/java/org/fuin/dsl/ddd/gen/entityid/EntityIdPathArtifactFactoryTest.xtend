package org.fuin.dsl.ddd.gen.entityid

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Tests the type generated for a declared entity identifier path.
 *
 * <p>A path travels as a string of typed segments and, untyped, says nothing about what it addresses.
 * These pin the three things the generated type adds: the shape as a constant, a constructor that refuses
 * a path of the wrong shape, and a <code>last()</code> typed to what the path points at.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class EntityIdPathArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testTheShapeTravelsAsAConstant() {
        val generated = generate("AccountTransactionPath")

        assertThat(generated).contains("public static final EntityIdPathSpec SPEC = EntityIdPathSpec.builder()")
        assertThat(generated).contains(".step(AnnualTransactionsId.class)")
        assertThat(generated).contains(".step(AccountTransactionId.class)")
        assertThat(generated).contains(".build();")
    }

    @Test
    def void testAStepStatesItsRangeOnlyWhenItIsNotExactlyOne() {
        // The common step is written plainly; a range is only there when it says something.
        assertThat(generate("NestedTransactionPath"))
            .contains(".step(AccountTransactionId.class, 1, Integer.MAX_VALUE)")
    }

    @Test
    def void testAPathOfTheWrongShapeIsRefusedWhereItIsBuilt() {
        // Rather than wherever it is later read, which is what an untyped path leaves you with.
        assertThat(generate("AccountTransactionPath")).contains('SPEC.requireArgValid("path", path);')
    }

    @Test
    def void testWhatThePathAddressesIsTyped() {
        // The whole point: the leaf segment is the thing addressed, so 'last()' says so.
        assertThat(generate("AccountTransactionPath")).contains("public AccountTransactionId last() {")
    }

    private def String generate(String pathName) {
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        val model = this.model
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.types.UUID", "java.util.UUID")
        for (id : #["AnnualTransactionsId", "AccountTransactionId"]) {
            val element = model.find(typeof(org.fuin.dsl.cqrs.cqrsDsl.AbstractElement), id)
            refReg.putReference(TypeKeys.refKey(element), "p.x.entityid." + id)
        }
        val path = model.find(typeof(EntityIdPathType), pathName)
        return new String(createTestee.create(path, context, false).iterator.next.data, "UTF-8")
    }

    private def createTestee() {
        val factory = new EntityIdPathArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("entityIdPath",
            EntityIdPathArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, "p"))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER,
            Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/entity-id-path.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

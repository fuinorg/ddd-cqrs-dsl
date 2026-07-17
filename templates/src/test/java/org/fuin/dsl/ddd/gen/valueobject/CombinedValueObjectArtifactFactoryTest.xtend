package org.fuin.dsl.ddd.gen.valueobject

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Verifies the delegation of {@link CombinedValueObjectArtifactFactory}. The generated code itself is
 * verified by the tests of the delegates; this test only covers which delegates are used and that every
 * one of them writes to its own target folder.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class CombinedValueObjectArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testCreateAbstractAndFinal() {

        // PREPARE - a value object that is not a simple String based one.
        val context = newContext()
        val testee = createTestee()
        val ValueObject vo = model.find(typeof(ValueObject), "MyValueObject2")

        // TEST
        val result = testee.create(vo, context, false)

        // VERIFY the abstract base class and the final class are both created.
        assertThat(result).hasSize(2)

        // VERIFY the abstract class goes to the regenerated folder of the AbstractValueObjectArtifactFactory
        // and not to the one of the delegating factory.
        val abstractArtifact = result.get(0)
        assertThat(abstractArtifact.pathAndName).isEqualTo("p/shared/domain/x/valueobject/AbstractMyValueObject2.java")
        assertThat(abstractArtifact.module).isEqualTo("shared")
        assertThat(abstractArtifact.folder).isEqualTo("genMainJava")
        assertThat(new String(abstractArtifact.data)).contains("public abstract class AbstractMyValueObject2")

        // VERIFY the final class goes to the non-generated folder of the FinalValueObjectArtifactFactory,
        // so it is created once and never overwritten.
        val finalArtifact = result.get(1)
        assertThat(finalArtifact.pathAndName).isEqualTo("p/shared/domain/x/valueobject/MyValueObject2.java")
        assertThat(finalArtifact.module).isEqualTo("shared")
        assertThat(finalArtifact.folder).isEqualTo("mainJava")
        assertThat(new String(finalArtifact.data)).contains(
            "public final class MyValueObject2 extends AbstractMyValueObject2")

    }

    @Test
    def void testCreateSimpleStringValueObject() {

        // PREPARE - a value object with a "String" base and a single attribute.
        val context = newContext()
        val testee = createTestee()
        val ValueObject vo = model.find(typeof(ValueObject), "MySimpleStringValueObject")

        // TEST
        val result = testee.create(vo, context, false)

        // VERIFY only the complete class of the SimpleStringValueObjectArtifactFactory is created - no
        // abstract base class and no final class.
        assertThat(result).hasSize(1)
        val artifact = result.get(0)
        assertThat(artifact.pathAndName).isEqualTo("p/shared/domain/x/valueobject/MySimpleStringValueObject.java")
        assertThat(artifact.folder).isEqualTo("genMainJava")
        assertThat(new String(artifact.data)).contains("public final class MySimpleStringValueObject")

    }

    @Test
    def void testPreparationRunRegistersReferencesOfBothDelegates() {

        // PREPARE
        val context = newContext()
        val testee = createTestee()
        val ValueObject vo = model.find(typeof(ValueObject), "MyValueObject2")

        // TEST
        val result = testee.create(vo, context, true)

        // VERIFY nothing is created during the preparation run, but both delegates registered their code
        // reference - the final class can only be created later on if the abstract one is known.
        assertThat(result).isNull
        val refReg = context.codeReferenceRegistry
        assertThat(refReg.getReference("p.x.valueobject.AbstractMyValueObject2")).
            isEqualTo("p.shared.domain.x.valueobject.AbstractMyValueObject2")
        assertThat(refReg.getReference("p.x.valueobject.MyValueObject2")).
            isEqualTo("p.shared.domain.x.valueobject.MyValueObject2")

    }

    private def newContext() {
        val context = new HashMap<String, Object>()
        val refReg = context.codeReferenceRegistry
        refReg.putReference("p.x.types.String", "java.lang.String")
        refReg.putReference("p.x.valueobject.MySimpleStringValueObjectConverter",
            "p.x.valueobject.MySimpleStringValueObjectConverter")
        return context
    }

    private def createTestee() {
        val factory = new CombinedValueObjectArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("vo", CombinedValueObjectArtifactFactory.name,
            "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_BASE_PKG, EXAMPLES_ABSTRACT))
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/valueobject.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

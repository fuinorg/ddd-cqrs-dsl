package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Entity
import org.fuin.dsl.ddd.gen.base.ComputingCodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.fuin.dsl.ddd.gen.base.TypeKeys

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class SrcConstructorWithParamsAssignmentTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreate() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference(TypeKeys.refKey("p.x.a.MyEntityId", TypeKeys.JAVA_ENTITY_ID), "a.b.c.MyEntityId")
        refReg.putReference(TypeKeys.refKey("p.x.a.MyValueObject", TypeKeys.JAVA_VALUE_OBJECT), "a.b.c.MyValueObject")
        refReg.putReference(TypeKeys.refKey("p.x.a.ConstraintViolatedException", TypeKeys.JAVA_EXCEPTION), "a.b.c.ConstraintViolatedException")
        val ctx = new SimpleCodeSnippetContext(refReg)
        val Entity entity = model().find(Entity, "MyEntity")
        val constructor = entity.constructors.get(0)
        val SrcConstructorWithParamsAssignment testee = new SrcConstructorWithParamsAssignment(ctx, "public",
            entity.getName(), GenerateOptions.empty(), constructor)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                /**
                 * Creates the entity.
                 *
                 * @param id Unique entity identifier.
                 * @param vo Example value object.
                 *
                 * @throws ConstraintViolatedException The constraint was violated.
                 */
                public MyEntity(final MyEntityId id, @Nullable final MyValueObject vo) throws ConstraintViolatedException {
                    super();
                    Contract.requireArgNotNull("id", id);
                    
                    this.id = id;
                    this.vo = vo;
                }
            '''.toString)
        assertThat(ctx.imports).containsOnly("a.b.c.MyEntityId",
            "a.b.c.MyValueObject", "a.b.c.ConstraintViolatedException", "org.fuin.objects4j.common.Contract", "org.jspecify.annotations.Nullable")

    }

    def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/example1.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }

}

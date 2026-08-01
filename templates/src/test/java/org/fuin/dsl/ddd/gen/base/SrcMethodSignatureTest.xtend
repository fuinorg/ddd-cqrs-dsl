package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.ddd.gen.base.ComputingCodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.fuin.dsl.ddd.gen.base.TypeKeys

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class SrcMethodSignatureTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreate() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference(TypeKeys.refKey("p.x.a.MyAggregateId", TypeKeys.JAVA_AGGREGATE_ID), "a.b.c.MyAggregateId")
        refReg.putReference(TypeKeys.refKey("p.x.a.MyValueObject", TypeKeys.JAVA_VALUE_OBJECT), "a.b.c.MyValueObject")
        refReg.putReference(TypeKeys.refKey("p.x.a.ConstraintViolatedException", TypeKeys.JAVA_EXCEPTION), "a.b.c.ConstraintViolatedException")
        val ctx = new SimpleCodeSnippetContext(refReg)
        val Aggregate aggregate = model().find(Aggregate, "MyAggregate")
        val method = aggregate.methods.get(0)
        val SrcMethodSignature testee = new SrcMethodSignature(ctx, "public", false, GenerateOptions.empty(), method)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''public void doSomething(final MyAggregateId id, @Nullable final MyValueObject vo) throws ConstraintViolatedException'''.toString)
        assertThat(ctx.imports).containsOnly("a.b.c.MyAggregateId",
            "a.b.c.MyValueObject", "a.b.c.ConstraintViolatedException", "org.jspecify.annotations.Nullable")

    }

    @Test
    def void testCreateAbstract() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference(TypeKeys.refKey("p.x.a.MyAggregateId", TypeKeys.JAVA_AGGREGATE_ID), "a.b.c.MyAggregateId")
        refReg.putReference(TypeKeys.refKey("p.x.a.MyValueObject", TypeKeys.JAVA_VALUE_OBJECT), "a.b.c.MyValueObject")
        refReg.putReference(TypeKeys.refKey("p.x.a.ConstraintViolatedException", TypeKeys.JAVA_EXCEPTION), "a.b.c.ConstraintViolatedException")
        val ctx = new SimpleCodeSnippetContext(refReg)
        val Aggregate aggregate = model().find(Aggregate, "MyAggregate")
        val method = aggregate.methods.get(0)
        val SrcMethodSignature testee = new SrcMethodSignature(ctx, "public", true, GenerateOptions.empty(), method)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''public abstract void doSomething(final MyAggregateId id, @Nullable final MyValueObject vo) throws ConstraintViolatedException'''.toString)
        assertThat(ctx.imports).containsOnly("a.b.c.MyAggregateId",
            "a.b.c.MyValueObject", "a.b.c.ConstraintViolatedException", "org.jspecify.annotations.Nullable")

    }

    def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/example1.cqrs")))
        validationTester.assertNoIssues(model)
        return model
    }


}

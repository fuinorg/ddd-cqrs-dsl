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

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class SrcXmlAttributeOrElementTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreateAggregateIdAttribute() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.x.types.String", "java.lang.String")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val Aggregate aggregate = createModel().find(Aggregate, "MyAggregate")
        val idVar = aggregate.attributes.get(0)
        val SrcXmlAttributeOrElement testeeId = new SrcXmlAttributeOrElement(ctx, idVar, false)

        // TEST
        val resultId = testeeId.toString


        // VERIFY
        assertThat(resultId).isEqualTo('''@XmlAttribute(name = "id")'''.toString)
        assertThat(ctx.imports).containsOnly("jakarta.xml.bind.annotation.XmlAttribute")

    }

    @Test
    def void testCreateAggregateIdElement() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.x.types.String", "java.lang.String")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val Aggregate aggregate = createModel().find(Aggregate, "MyAggregate")
        val idVar = aggregate.attributes.get(0)
        val SrcXmlAttributeOrElement testeeId = new SrcXmlAttributeOrElement(ctx, idVar, true)

        // TEST
        val resultId = testeeId.toString


        // VERIFY
        assertThat(resultId).isEqualTo('''@XmlElement(name = "id")'''.toString)
        assertThat(ctx.imports).containsOnly("jakarta.xml.bind.annotation.XmlElement")

    }
    
    @Test
    def void testCreateValueObject() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.x.types.String", "java.lang.String")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val Aggregate aggregate = createModel().find(Aggregate, "MyAggregate")
        val voVar = aggregate.attributes.get(1)
        val SrcXmlAttributeOrElement testeeVo = new SrcXmlAttributeOrElement(ctx, voVar, false)

        // TEST
        val resultVo = testeeVo.toString

        // VERIFY
        assertThat(resultVo).isEqualTo('''@XmlElement(name = "vo")'''.toString)
        assertThat(ctx.imports).containsOnly("jakarta.xml.bind.annotation.XmlElement")

    }

    def DomainModel createModel() {
        val DomainModel model =parser.parse(
            '''
				context p {

				    module x.a {
				        import p.x.types.*


				        value-object MyValueObject {}

				        aggregate MyAggregate identifier MyAggregateId {
				            MyAggregateId id
				            MyValueObject vo
				        }

				        aggregate-id MyAggregateId identifies MyAggregate base String {}
				    }

				    module x.types {
				        type String
				    }
				}
			'''
        )
        validationTester.assertNoIssues(model)
        return model
    }

}

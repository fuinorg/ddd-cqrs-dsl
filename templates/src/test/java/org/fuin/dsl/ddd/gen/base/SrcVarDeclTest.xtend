package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.ComputingCodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.fuin.dsl.ddd.gen.base.TypeKeys

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class SrcVarDeclTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreateWithConstraint() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.a.types.String", "java.lang.String")
        refReg.putReference(TypeKeys.refKey("p.a.b.AnyConstraint", TypeKeys.JAVA_CONSTRAINT), "x.y.z.AnyConstraint")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val ValueObject valueObject = createModel().find(ValueObject, "MyValueObject")
        val attr = valueObject.attributes.get(0)
        val SrcVarDecl testee = new SrcVarDecl(ctx, "private", GenerateOptions.empty(), attr)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                @AnyConstraint
                private String str;
            '''.toString)
        assertThat(ctx.imports).containsOnly("x.y.z.AnyConstraint",
            "java.lang.String")

    }

    @Test
    def void testCreateWithoutConstraint() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.a.types.String", "java.lang.String")
        refReg.putReference(TypeKeys.refKey("p.a.b.AnyConstraint", TypeKeys.JAVA_CONSTRAINT), "x.y.z.AnyConstraint")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val ValueObject valueObject = createModel().find(ValueObject, "MyValueObject")
        val attr = valueObject.attributes.get(1)
        val SrcVarDecl testee = new SrcVarDecl(ctx, "private", GenerateOptions.empty(), attr)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                private String str2;
            '''.toString)
        assertThat(ctx.imports).containsOnly("java.lang.String")

    }

    @Test
    def void testCreateWithoutConstraintNullable() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.a.types.String", "java.lang.String")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val ValueObject valueObject = createModel().find(ValueObject, "MyValueObject")
        val attr = valueObject.attributes.get(2)
        val SrcVarDecl testee = new SrcVarDecl(ctx, "private", GenerateOptions.empty(), attr)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''@Nullable
private String str3;
            '''.toString)
        assertThat(ctx.imports).containsOnly("java.lang.String", "org.jspecify.annotations.Nullable")

    }

    @Test
    def void testCreateWithXmlAttribute() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.a.types.String", "java.lang.String")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val ValueObject valueObject = createModel().find(ValueObject, "MyValueObject")
        val attr = valueObject.attributes.get(3)
        val GenerateOptions options = new GenerateOptions.Builder().withJaxb().withJsonb().create();
        val SrcVarDecl testee = new SrcVarDecl(ctx, "private", options, attr)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                @XmlAttribute(name = "abc-def-ghi")
                @JsonbProperty("abc-def-ghi")
                private String abcDefGhi;
            '''.toString)
        assertThat(ctx.imports).containsOnly(
            "jakarta.xml.bind.annotation.XmlAttribute", "jakarta.json.bind.annotation.JsonbProperty", 
            "java.lang.String")

    }

    @Test
    def void testCreateWithXmlElement() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.a.types.String", "java.lang.String")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val ValueObject valueObject = createModel().find(ValueObject, "MyValueObject")
        val attr = valueObject.attributes.get(3)
        val GenerateOptions options = new GenerateOptions.Builder().withJaxb().withJaxbElements().withJsonb().create();
        val SrcVarDecl testee = new SrcVarDecl(ctx, "private", options, attr)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                @XmlElement(name = "abc-def-ghi")
                @JsonbProperty("abc-def-ghi")
                private String abcDefGhi;
            '''.toString)
        assertThat(ctx.imports).containsOnly(
            "jakarta.xml.bind.annotation.XmlElement", "jakarta.json.bind.annotation.JsonbProperty", 
            "java.lang.String")

    }

    def DomainModel createModel() {
        val DomainModel model = parser.parse(
            '''
				context p {

				    module a.b {
				        import p.a.types.*


				        constraint AnyConstraint input String {
				            message "message"
				        }

				        value-object MyValueObject {
				            String str invariants AnyConstraint
				            String str2
				            optional String str3
				            String abcDefGhi
				        }
				    }

				    module a.types {
				        type String
				    }
				}
			'''
        )
        validationTester.assertNoIssues(model)
        return model
    }

}

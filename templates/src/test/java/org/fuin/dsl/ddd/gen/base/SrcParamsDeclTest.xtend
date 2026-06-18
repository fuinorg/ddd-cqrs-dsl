package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.srcgen4j.core.emf.SimpleCodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class SrcParamsDeclTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreate() {

        // PREPARE
        val refReg = new SimpleCodeReferenceRegistry()
        refReg.putReference("y.types.String", "java.lang.String")
        refReg.putReference("y.a.NoArgConstraint", "a.b.c.NoArgConstraint")
        val ctx = new SimpleCodeSnippetContext(refReg)

        val ValueObject valueObject = createModel().find(ValueObject, "MyValueObject")
        val SrcParamsDecl testee = new SrcParamsDecl(ctx, GenerateOptions.empty(), valueObject.attributes.asParameters)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            "@NoArgConstraint final String a, @NoArgConstraint @Nullable final String b, final String c, @Nullable final String d")
        assertThat(ctx.imports).containsOnly("a.b.c.NoArgConstraint", "java.lang.String", "org.jspecify.annotations.Nullable")

    }

    def DomainModel createModel() {
        val DomainModel model = parser.parse(
            '''
                context y {
                    
                    namespace a {
                        
                        import y.types.*
                
                        constraint NoArgConstraint input String {
                            message "NoArgConstraint message"
                        }
                
                        value-object MyValueObject {
                            String a invariants NoArgConstraint
                            optional String b invariants NoArgConstraint
                            String c
                            optional String d                        
                        }
                
                    }
                
                    namespace types {
                        type String
                    }
                        
                }
            '''
        )
        validationTester.assertNoIssues(model)
        return model
    }


}

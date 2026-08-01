package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.dsl.ddd.gen.base.ComputingCodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class SrcGetterTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreateNoMultiplicity() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.ctx.types.String", "java.lang.String")
        val ctx = new SimpleCodeSnippetContext(refReg)
        val SrcGetter testee = createTesteeNoMultiplicity(ctx)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                /**
                 * Returns: Human readable name.
                 *
                 * @return Current value.
                 */
                public String getName() {
                    return name;
                }
            '''.toString)
        assertThat(ctx.imports).containsOnly("java.lang.String")

    }

    @Test
    def void testCreateWithMultiplicity() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        refReg.putReference("p.ctx.types.String", "java.lang.String")
        refReg.putReference("p.ctx.types.List", "java.util.List")
        val ctx = new SimpleCodeSnippetContext(refReg)
        val SrcGetter testee = createTesteeWithMultiplicity(ctx)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                /**
                 * Returns: List of human readable names.
                 *
                 * @return Current value.
                 */
                public List<String> getNames() {
                    return names;
                }
            '''.toString)
        assertThat(ctx.imports).containsOnly("java.util.List", "java.lang.String")

    }

    private def SrcGetter createTesteeNoMultiplicity(CodeSnippetContext codeSnippetContext) {
        val model = parser.parse(
            '''
				context p {

				    module ctx.a.b {
				        import p.ctx.types.*


				        value-object MyValueObject {

				            /** Human readable name. */
				            String name

				        }
				    }

				    module ctx.types {
				        type String
				    }
				}
			'''
        )
        val ValueObject valueObject = model.contexts.get(0).modules.get(0).elements.get(0) as ValueObject
        return new SrcGetter(codeSnippetContext, GenerateOptions.empty(), "public", valueObject.attributes.first)
    }

    private def SrcGetter createTesteeWithMultiplicity(CodeSnippetContext codeSnippetContext) {
        val model = parser.parse(
            '''
				context p {

				    module ctx.a.b {
				        import p.ctx.types.*


				        value-object MyValueObject {

				            /** List of human readable names. */
				            List<String> names

				        }
				    }

				    module ctx.types {
				        type String
				        type List generics 1
				    }
				}
			'''
        )
        validationTester.assertNoIssues(model)
        val ValueObject valueObject = model.contexts.get(0).modules.get(0).elements.get(0) as ValueObject
        return new SrcGetter(codeSnippetContext, GenerateOptions.empty(), "public", valueObject.attributes.first)
    }

}

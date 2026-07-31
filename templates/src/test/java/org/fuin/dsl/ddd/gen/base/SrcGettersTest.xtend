package org.fuin.dsl.ddd.gen.base

import java.util.List
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension) 
class SrcGettersTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testCreate() {

        // PREPARE
        val refReg = new SimpleCodeReferenceRegistry()
        refReg.putReference("p.ctx.types.String", "java.lang.String")
        refReg.putReference("p.ctx.types.Locale", "java.util.Locale")
        val ctx = new SimpleCodeSnippetContext(refReg);
        val SrcGetters testee = createTestee(ctx)

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
                
                /**
                 * Returns: Language the name is in.
                 *
                 * @return Current value.
                 */
                @Nullable
                public Locale getLocale() {
                    return locale;
                }
                
            '''.toString)
        assertThat(ctx.imports).containsOnly("java.lang.String", "java.util.Locale", "org.jspecify.annotations.Nullable")

    }

    private def SrcGetters createTestee(CodeSnippetContext codeSnippetContext) {
        val model = parser.parse(
            '''
				context p {

				    module ctx.a.b {
				        import p.ctx.types.*


				        value-object MyValueObject {

				            /** Human readable name. */
				            String name

				            /** Language the name is in. */
				            optional Locale locale

				        }
				    }

				    module ctx.types {
				        type String
				        type Locale
				    }
				}
			'''
        )
        validationTester.assertNoIssues(model)
        val ValueObject valueObject = model.contexts.get(0).modules.get(0).elements.get(0) as ValueObject
        val List<Attribute> attributes = valueObject.attributes
        return new SrcGetters(codeSnippetContext, GenerateOptions.empty(), "public", attributes)
    }

}

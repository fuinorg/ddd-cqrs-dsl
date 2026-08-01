package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
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
class SrcVoBaseMethodsUUIDTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject 
    ValidationTestHelper validationTester

    @Test
    def void testString() {

        // PREPARE
        val refReg = new ComputingCodeReferenceRegistry()
        val ctx = new SimpleCodeSnippetContext(refReg)
        refReg.putReference("p.y.types.UUID", "java.util.UUID")
        refReg.putReference(TypeKeys.refKey("p.y.a.MyAggregateId", TypeKeys.JAVA_AGGREGATE_ID), "a.b.c.MyAggregateId")
        val AggregateId aggregateId = createModel().find(AggregateId, "MyAggregateId")

        val testee = new SrcVoBaseMethodsUUID(ctx, aggregateId)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
        '''    
            /**
             * Returns the information if a given string can be converted into
             * an instance of MyAggregateId. A <code>null</code> value returns <code>true</code>.
             * 
             * @param value
             *            Value to check.
             * 
             * @return TRUE if it's a valid string, else FALSE.
             */
            public static boolean isValid(@Nullable final String value) {
                return UUIDStrValidator.isValid(value);
            }
            
            /**
             * Parses a given string and returns a new instance of MyAggregateId.
             * 
             * @param value
             *            Value to convert. A <code>null</code> value returns
             *            <code>null</code>.
             * 
             * @return Converted value.
             */
            @Nullable
            public static MyAggregateId valueOf(@Nullable final String value) {
                if (value == null) {
                    return null;
                }
                return new MyAggregateId(UUID.fromString(value));
            }
            
        '''.toString
        )
        assertThat(ctx.imports).containsOnly("a.b.c.MyAggregateId", "java.util.UUID", "org.fuin.objects4j.core.UUIDStrValidator", "org.jspecify.annotations.Nullable")

    }

    def DomainModel createModel() {
        val DomainModel model = parser.parse(
            '''
				context p {

				    module y.types {
				        type UUID
				    }

				    module y.a {
				        import p.y.types.*


				        aggregate-id MyAggregateId identifies MyAggregate base UUID {}

				        aggregate MyAggregate identifier MyAggregateId {}
				    }
				}
			'''
        )
        validationTester.assertNoIssues(model)
        return model
    }

}

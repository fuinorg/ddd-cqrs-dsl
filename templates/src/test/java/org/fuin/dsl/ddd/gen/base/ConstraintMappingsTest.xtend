package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsInvariantsExtensions.*
import org.eclipse.emf.common.util.EList

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class ConstraintMappingsTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testParseNull() {

        // TEST + VERIFY a missing variable means "no constraint is mapped".
        val testee = ConstraintMappings.parse(null)

        assertThat(testee.mapped(constraintInstance("strLength").constraint)).isFalse

    }

    @Test
    def void testMappingWithoutParameters() {

        // PREPARE
        val testee = ConstraintMappings.parse("p.y.a.NoArgConstraint=jakarta.validation.constraints.NotEmpty")
        val ci = constraintInstance("strNoArgConstraint")

        // TEST + VERIFY
        assertThat(testee.mapped(ci.constraint)).isTrue
        assertThat(testee.imports(ci.constraint)).containsOnly("jakarta.validation.constraints.NotEmpty")
        assertThat(testee.annotations(ci)).isEqualTo("@NotEmpty")

    }

    @Test
    def void testMappingRenamesTheParameter() {

        // PREPARE - the DSL parameter "expected" becomes the Java parameter "min".
        val testee = ConstraintMappings.parse(
            "p.y.a.OneArgConstraint(expected)=jakarta.validation.constraints.Size(min=expected)")
        val ci = constraintInstance("strOneArgConstraint")

        // TEST + VERIFY
        assertThat(testee.annotations(ci)).isEqualTo("@Size(min=50)")

    }

    @Test
    def void testMappingUsesOneParameterTwice() {

        // PREPARE
        val testee = ConstraintMappings.parse(
            "p.y.a.OneArgConstraint(expected)=jakarta.validation.constraints.Size(min=expected,max=expected)")
        val ci = constraintInstance("strOneArgConstraint")

        // TEST + VERIFY
        assertThat(testee.annotations(ci)).isEqualTo("@Size(min=50, max=50)")

    }

    @Test
    def void testMappingToTwoAnnotations() {

        // PREPARE - the comma between the two annotations must not be confused with the ones inside a
        // parameter list.
        val testee = ConstraintMappings.parse(
            "p.y.a.TwoArgsConstraint(min,max)=jakarta.validation.constraints.Min(value=min),jakarta.validation.constraints.Max(value=max)")
        val ci = constraintInstance("strTwoArgsConstraint")

        // TEST + VERIFY
        assertThat(testee.imports(ci.constraint)).containsExactly("jakarta.validation.constraints.Min",
            "jakarta.validation.constraints.Max")
        assertThat(testee.annotations(ci)).isEqualTo("@Min(value=1)\n@Max(value=100)")

    }

    @Test
    def void testMappingWithSeveralParametersIsOneAnnotation() {

        // PREPARE - the comma inside the parameter list must not split the annotation.
        val testee = ConstraintMappings.parse(
            "p.y.a.TwoArgsConstraint(min,max)=jakarta.validation.constraints.Size(min=min,max=max)")
        val ci = constraintInstance("strTwoArgsConstraint")

        // TEST + VERIFY
        assertThat(testee.imports(ci.constraint)).containsOnly("jakarta.validation.constraints.Size")
        assertThat(testee.annotations(ci)).isEqualTo("@Size(min=1, max=100)")

    }

    @Test
    def void testStringValueKeepsItsQuotes() {

        // PREPARE
        val testee = ConstraintMappings.parse(
            "org.fuin.constr.Pattern(expression)=jakarta.validation.constraints.Pattern(regexp=expression)")
        val ci = constraintInstance("strPattern")

        // TEST + VERIFY
        assertThat(testee.annotations(ci)).isEqualTo('''@Pattern(regexp="\d")'''.toString)

    }

    @Test
    def void testSeveralMappingsSeparatedByWhitespace() {

        // PREPARE - a multiline XML attribute arrives with the line breaks replaced by spaces, so both must
        // work the same way.
        val String mappings = '''
            p.y.a.NoArgConstraint=jakarta.validation.constraints.NotEmpty
            p.y.a.TwoArgsConstraint(min,max)=jakarta.validation.constraints.Size(min=min,max=max)
        '''
        val testee = ConstraintMappings.parse(mappings)
        val testee2 = ConstraintMappings.parse(mappings.replaceAll("\\s+", " "))

        // TEST + VERIFY
        for (t : #[testee, testee2]) {
            assertThat(t.annotations(constraintInstance("strNoArgConstraint"))).isEqualTo("@NotEmpty")
            assertThat(t.annotations(constraintInstance("strTwoArgsConstraint"))).isEqualTo("@Size(min=1, max=100)")
        }

    }

    @Test
    def void testUnmappedConstraint() {

        // PREPARE - only another constraint is mapped.
        val testee = ConstraintMappings.parse("p.y.a.NoArgConstraint=jakarta.validation.constraints.NotEmpty")

        // TEST + VERIFY
        assertThat(testee.mapped(constraintInstance("strTwoArgsConstraint").constraint)).isFalse

    }

    @Test
    def void testMappingWithoutEqualsSign() {

        // TEST + VERIFY
        assertThatThrownBy[ConstraintMappings.parse("p.y.a.NoArgConstraint")].isInstanceOf(
            IllegalArgumentException).hasMessageContaining("DSL=JAVA")

    }

    @Test
    def void testMappingWithUnknownDslParameter() {

        // PREPARE - the Java parameter refers to a DSL parameter that the mapping does not declare.
        val testee = ConstraintMappings.parse(
            "p.y.a.TwoArgsConstraint(min,max)=jakarta.validation.constraints.Size(min=wrong)")
        val ci = constraintInstance("strTwoArgsConstraint")

        // TEST + VERIFY
        assertThatThrownBy[testee.annotations(ci)].isInstanceOf(IllegalStateException).hasMessageContaining(
            "has no parameter 'wrong'")

    }

    private def ConstraintInstance constraintInstance(String attributeName) {
        val ValueObject valueObject = createModel().find(ValueObject, "MyValueObject")
        return valueObject.attributes.find(attributeName).invariants.nullSafe.get(0)
    }

    private def Attribute find(EList<Attribute> attrs, String nameToFind) {
        for (Attribute attr : attrs) {
            if (attr.name.equals(nameToFind)) {
                return attr
            }
        }
        return null
    }

    private def DomainModel createModel() {
        val DomainModel model = parser.parse(ConstraintModel.text)
        validationTester.assertNoIssues(model)
        return model
    }

}

package org.fuin.dsl.ddd.gen.base

import java.net.URL
import org.apache.commons.io.IOUtils

/**
 * Model with constraints and a value object that uses them as invariants. Shared by the tests around
 * {@link ConstraintMappings} and {@link SrcValidationAnnotation}.
 */
class ConstraintModel {

    /**
     * Returns the model text: the built-in constraints of "org.fuin.constr" plus a project of its own that
     * declares three constraints and uses all of them.
     *
     * @return Text to parse.
     */
    def static String text() {

        val URL url = ConstraintModel.classLoader.getResource("org/fuin/dsl/ddd/Basics.cqrs")
        val basics = IOUtils.toString(url, "utf-8")

        return basics + '''
			project p {
            context y {

                namespace a {

                    import org.fuin.types.*
                    import org.fuin.constr.*

                    constraint NoArgConstraint input String {
                        message "NoArgConstraint message"
                    }

                    constraint OneArgConstraint input String {
                        Integer expected
                        message "OneArgConstraint message"
                    }

                    constraint TwoArgsConstraint input String {
                        Integer min
                        Integer max
                        message "TwoArgsConstraint message"
                    }

                    value-object MyValueObject {

                        String strNoArgConstraint invariants NoArgConstraint
                        String strOneArgConstraint invariants OneArgConstraint(50)
                        String strTwoArgsConstraint invariants TwoArgsConstraint(1, 100)

                        String strNotNull invariants NotNull
                        String strNull invariants Null
                        Boolean booleanAssertTrue invariants AssertTrue
                        Boolean booleanAssertFalse invariants AssertFalse

                        BigDecimal minValueBigDecimal invariants MinValue("123.45")
                        BigInteger minValueBigInteger invariants MinValue("123")
                        Integer minValueInteger invariants MinValue("234")
                        Long minValueLong invariants MinValue("-345")

                        BigDecimal maxValueBigDecimal invariants MaxValue("123.45")
                        BigInteger maxValueBigInteger invariants MaxValue("123")
                        Integer maxValueInteger invariants MaxValue("234")
                        Long maxValueLong invariants MaxValue("-345")

                        BigDecimal valueRangeBigDecimal invariants ValueRange("0", "100")
                        BigInteger valueRangeBigInteger invariants ValueRange("1", "99")
                        Integer valueRangeInteger invariants ValueRange("-2", "2")
                        Long valueRangeLong invariants ValueRange("-1", "1")

                        BigDecimal negativeBigDecimal invariants Negative
                        BigInteger negativeBigInteger invariants Negative
                        Integer negativeInteger invariants Negative
                        Long negativeLong invariants Negative
                        Float negativeFloat invariants Negative
                        Double negativeDouble invariants Negative

                        BigDecimal negativeOrZeroBigDecimal invariants NegativeOrZero
                        BigInteger negativeOrZeroBigInteger invariants NegativeOrZero
                        Integer negativeOrZeroInteger invariants NegativeOrZero
                        Long negativeOrZeroLong invariants NegativeOrZero
                        Float negativeOrZeroFloat invariants NegativeOrZero
                        Double negativeOrZeroDouble invariants NegativeOrZero

                        BigDecimal positiveBigDecimal invariants Positive
                        BigInteger positiveBigInteger invariants Positive
                        Integer positiveInteger invariants Positive
                        Long positiveLong invariants Positive
                        Float positiveFloat invariants Positive
                        Double positiveDouble invariants Positive

                        BigDecimal positiveOrZeroBigDecimal invariants PositiveOrZero
                        BigInteger positiveOrZeroBigInteger invariants PositiveOrZero
                        Integer positiveOrZeroInteger invariants PositiveOrZero
                        Long positiveOrZeroLong invariants PositiveOrZero
                        Float positiveOrZeroFloat invariants PositiveOrZero
                        Double positiveOrZeroDouble invariants PositiveOrZero

                        String strMinLength invariants MinLength(1)
                        String strMaxLength invariants MaxLength(2)
                        String strExactLength invariants ExactLength(3)
                        String strLength invariants Length(1, 100)

                        String strNotEmpty invariants NotEmpty
                        List<String> listNotEmpty invariants NotEmpty
                        String strNotBlank invariants NotBlank
                        String strPattern invariants Pattern("\\d")

                    }

                }

            }
            }
		'''

    }



}

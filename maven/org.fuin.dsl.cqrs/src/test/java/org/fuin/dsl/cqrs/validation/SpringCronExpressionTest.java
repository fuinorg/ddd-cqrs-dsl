package org.fuin.dsl.cqrs.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SpringCronExpression}. The identical copy in the IntelliJ plugin
 * ({@code org.fuin.dsl.cqrs.intellij.SpringCronExpression}) is exercised through
 * {@code CqrsValidationAnnotatorTest}.
 */
public class SpringCronExpressionTest {

    @Test
    public void testValidExpressions() {
        assertTrue(SpringCronExpression.isValid("* * * * * *"));
        assertTrue(SpringCronExpression.isValid("0 0 12 * * MON-FRI"));
        assertTrue(SpringCronExpression.isValid("*/5 * * * * *"));
        assertTrue(SpringCronExpression.isValid("0 15 10 ? * *"));
        assertTrue(SpringCronExpression.isValid("0 0/30 8-10 * * *"));
        assertTrue(SpringCronExpression.isValid("0 0 0 1 JAN-DEC *"));
        assertTrue(SpringCronExpression.isValid("0 0 0 L * *"));
        assertTrue(SpringCronExpression.isValid("0 0 0 LW * *"));
        assertTrue(SpringCronExpression.isValid("0 0 0 15W * *"));
        assertTrue(SpringCronExpression.isValid("0 0 0 L-3 * *"));
        assertTrue(SpringCronExpression.isValid("0 0 0 ? * FRI#3"));
        assertTrue(SpringCronExpression.isValid("0 0 0 ? * 5L"));
    }

    @Test
    public void testValidMacros() {
        assertTrue(SpringCronExpression.isValid("@yearly"));
        assertTrue(SpringCronExpression.isValid("@annually"));
        assertTrue(SpringCronExpression.isValid("@monthly"));
        assertTrue(SpringCronExpression.isValid("@weekly"));
        assertTrue(SpringCronExpression.isValid("@daily"));
        assertTrue(SpringCronExpression.isValid("@midnight"));
        assertTrue(SpringCronExpression.isValid("@hourly"));
    }

    @Test
    public void testInvalidExpressions() {
        assertFalse(SpringCronExpression.isValid(null));
        assertFalse(SpringCronExpression.isValid(""));
        assertFalse(SpringCronExpression.isValid("   "));
        assertFalse(SpringCronExpression.isValid("* * * * *")); // only five fields (Unix cron)
        assertFalse(SpringCronExpression.isValid("* * * * * * *")); // seven fields
        assertFalse(SpringCronExpression.isValid("60 * * * * *")); // seconds > 59
        assertFalse(SpringCronExpression.isValid("* 60 * * * *")); // minutes > 59
        assertFalse(SpringCronExpression.isValid("* * 24 * * *")); // hours > 23
        assertFalse(SpringCronExpression.isValid("* * * 32 * *")); // day-of-month > 31
        assertFalse(SpringCronExpression.isValid("* * * * 13 *")); // month > 12
        assertFalse(SpringCronExpression.isValid("* * * * * 8")); // day-of-week > 7
        assertFalse(SpringCronExpression.isValid("* * * * FOO *")); // unknown month name
        assertFalse(SpringCronExpression.isValid("*/0 * * * * *")); // zero step
        assertFalse(SpringCronExpression.isValid("not a cron"));
        assertFalse(SpringCronExpression.isValid("@reboot")); // not supported by Spring
        assertFalse(SpringCronExpression.isValid("@weekly extra")); // trailing garbage after macro
    }
}

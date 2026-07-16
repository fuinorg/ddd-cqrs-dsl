package org.fuin.dsl.cqrs.intellij;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for the pure-string helper {@link CqrsValidationUtil#findUnknownVar(List, String)}.
 */
public class CqrsValidationUtilTest {

    @Test
    public void simpleUnknownVariableIsReported() {
        assertEquals("name", CqrsValidationUtil.findUnknownVar(Collections.emptyList(), "Hello ${name}"));
    }

    @Test
    public void simpleKnownVariableIsAccepted() {
        assertNull(CqrsValidationUtil.findUnknownVar(asList("name"), "Hello ${name}"));
    }

    @Test
    public void elMethodCallIsSkipped() {
        assertNull(CqrsValidationUtil.findUnknownVar(Collections.emptyList(), "${name.toUpperCase()}"));
    }

    @Test
    public void elPropertyAccessIsSkipped() {
        assertNull(CqrsValidationUtil.findUnknownVar(Collections.emptyList(), "${order.id}"));
    }

    @Test
    public void elArithmeticExpressionIsSkipped() {
        assertNull(CqrsValidationUtil.findUnknownVar(Collections.emptyList(), "${quantity * price}"));
    }

    @Test
    public void implicitEntityIdPathIsAccepted() {
        assertNull(CqrsValidationUtil.findUnknownVar(Collections.emptyList(), "Event A [${entityIdPath}]"));
    }

    @Test
    public void unknownSimpleVariableAmongElExpressionsIsReported() {
        assertEquals("nam",
                CqrsValidationUtil.findUnknownVar(asList("name"), "${name.toUpperCase()} and ${nam}"));
    }

    @Test
    public void nullMessageIsAccepted() {
        assertNull(CqrsValidationUtil.findUnknownVar(Collections.emptyList(), null));
    }
}

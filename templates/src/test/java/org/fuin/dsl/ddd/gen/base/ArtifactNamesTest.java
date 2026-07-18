package org.fuin.dsl.ddd.gen.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArtifactNames}.
 */
public class ArtifactNamesTest {

    @Test
    public void testViewBaseName() {
        // Trailing "View" is stripped, so both spellings collapse to the same base.
        assertEquals("PersonList", ArtifactNames.viewBaseName("PersonListView"));
        assertEquals("PersonList", ArtifactNames.viewBaseName("PersonList"));
        // Only a trailing occurrence is stripped, and never down to empty.
        assertEquals("ViewList", ArtifactNames.viewBaseName("ViewList"));
        assertEquals("View", ArtifactNames.viewBaseName("View"));
    }

    @Test
    public void testProcessManagerBaseName() {
        // "ProcessManager" (longer) is preferred over "Process".
        assertEquals("OrderPayment", ArtifactNames.processManagerBaseName("OrderPaymentProcessManager"));
        assertEquals("OrderPayment", ArtifactNames.processManagerBaseName("OrderPaymentProcess"));
        assertEquals("OrderPayment", ArtifactNames.processManagerBaseName("OrderPayment"));
        // Never stripped down to empty.
        assertEquals("Process", ArtifactNames.processManagerBaseName("Process"));
        assertEquals("ProcessManager", ArtifactNames.processManagerBaseName("ProcessManager"));
    }

    @Test
    public void testStripSuffixOrder() {
        assertEquals("Foo", ArtifactNames.stripSuffix("FooBar", "Bar"));
        assertEquals("FooBar", ArtifactNames.stripSuffix("FooBar", "Baz"));
        // First matching suffix in argument order wins.
        assertEquals("Foo", ArtifactNames.stripSuffix("FooManager", "Manager", "erManager"));
    }
}

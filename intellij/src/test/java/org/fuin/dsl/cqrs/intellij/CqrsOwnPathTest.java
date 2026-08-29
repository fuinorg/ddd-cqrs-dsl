package org.fuin.dsl.cqrs.intellij;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.List;

/**
 * Verifies the 'own-path' checks - the port of the Xtext validator's {@code checkOwnPath}, so the editor
 * refuses what the SrcGen4J build refuses.
 *
 * <p>'own-path' exists because an entity's identifier does not address it: the same one is assigned inside
 * every root, so a service asked to look the entity up somewhere else cannot be handed 'own-id'. What it
 * reads as is the declared {@code entity-id-path} for the chain, which is the three ways it can be
 * wrong - a carrier with no root, a carrier that does not exist yet, and an undeclared chain.</p>
 */
public class CqrsOwnPathTest extends BasePlatformTestCase {

    /** The 'own-path' errors reported for the given model. */
    private List<HighlightInfo> ownPathErrors(String body) {
        myFixture.configureByText("test.cqrs", body);
        return myFixture.doHighlighting().stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .filter(info -> info.getDescription() != null)
                .filter(info -> info.getDescription().contains("own-path"))
                .toList();
    }

    /** An entity whose chain the model declares a path for may name it. */
    public void testAnEntityWithADeclaredPath() {
        assertEmpty(ownPathErrors(model("entity-id-path PartPath { ThingId / PartId }",
                "method close business-rules MustBeOpen(own-path) fires ClosedEvent {"
                        + " event ClosedEvent { message \"Closed\" } }")));
    }

    /** Without a declaration there is no type to read it as, and no anonymous path to fall back to. */
    public void testWithoutADeclaredPath() {
        List<HighlightInfo> errors = ownPathErrors(model("",
                "method close business-rules MustBeOpen(own-path) fires ClosedEvent {"
                        + " event ClosedEvent { message \"Closed\" } }"));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("declare one"));
    }

    /** A constructor is what brings the carrier into being, so there is nothing to address. */
    public void testACreatingOperation() {
        List<HighlightInfo> errors = ownPathErrors(model("entity-id-path PartPath { ThingId / PartId }",
                "constructor open business-rules MustBeOpen(own-path) fires OpenedEvent {"
                        + " event OpenedEvent { message \"Opened\" } }"));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("nothing to read in a constructor"));
    }

    private static String model(String path, String operation) {
        return """
                context p {
                  module c.n {
                    type String
                    aggregate-id ThingId identifies Thing base String {}
                    entity-id PartId identifies Part base String {}
                %s
                    /** Refused because the part is closed. */
                    exception ClosedException {
                      /** The part that is closed. */
                      PartId part
                      message "${part} is closed"
                    }
                    /** Makes sure the part is open. */
                    business-rule MustBeOpen exception ClosedException {
                      /** The part being acted on. */
                      PartId part
                      consistency strong
                      requires part != null
                    }
                    aggregate Thing identifier ThingId {}
                    entity Part identifier PartId root Thing {
                      %s
                    }
                  }
                }
                """.formatted("    " + path, operation);
    }

}

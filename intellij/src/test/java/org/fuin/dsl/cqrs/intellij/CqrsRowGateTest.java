package org.fuin.dsl.cqrs.intellij;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.List;

/**
 * Verifies the row gate check - the port of the Xtext validator's
 * {@code checkRowAnswersTheGatesItOffers}, so the editor says what the SrcGen4J build says.
 *
 * <p>A menu is drawn on a row, and a command gated by a rule over the aggregate's own state can be left
 * out of it rather than offered and refused. The client decides that from what the row publishes, so a
 * row that offers the command and omits what the gate reads makes the gate work on one screen and
 * quietly do nothing on another.</p>
 *
 * <p>A warning rather than an error: whether the row should publish it is a modelling decision with real
 * costs on the other side, and what this removes is the silence rather than the choice.</p>
 */
public class CqrsRowGateTest extends BasePlatformTestCase {

    /** The gate warnings reported for the given model. */
    private List<HighlightInfo> gateWarnings(String body) {
        myFixture.configureByText("test.cqrs", body);
        return myFixture.doHighlighting().stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.WARNING)
                .filter(info -> info.getDescription() != null && info.getDescription().contains("which is gated by"))
                .toList();
    }

    /** A row publishing what its gate reads answers it, so nothing is reported. */
    public void testARowPublishingWhatItsGateReadsIsFine() {
        assertEmpty(gateWarnings(model("""
                    /** The thing's identifier. */
                    ThingId id
                    /** Whether it is open. */
                    Boolean open
                """)));
    }

    /** A row omitting it cannot decide, and the screen offers "close" on rows already closed. */
    public void testARowThatCannotAnswerTheGateItOffers() {
        List<HighlightInfo> warnings = gateWarnings(model("""
                    /** The thing's identifier. */
                    ThingId id
                """));
        assertEquals("one gate, one warning", 1, warnings.size());
        assertTrue("the message names the row, the command, the rule and what is missing",
                warnings.get(0).getDescription().contains("'ThingRow' offers 'CloseThing', which is gated by "
                        + "'MustBeOpen', but does not publish 'open'"));
    }

    /**
     * A value object no view hands back is not a row.
     *
     * <p>It is identified in exactly the same way - one attribute typed as an id - and no menu is ever
     * drawn on it, so holding it to a gate would report a screen that does not exist.
     */
    public void testAValueObjectNoViewHandsBackIsNotARow() {
        assertEmpty(gateWarnings("""
                context p {
                  module c.n {
                    type String
                    type Boolean
                    type List generics 1
                    aggregate-id ThingId identifies Thing base String { }
                    /** Refused because the thing is closed. */
                    exception ClosedException {
                      /** The thing that is closed. */
                      ThingId thing
                      message "${thing} is closed"
                    }
                    /** Makes sure the thing is open. */
                    business-rule MustBeOpen exception ClosedException {
                      /** The thing that is closed. */
                      ThingId thing
                      /** Whether it is open now. */
                      Boolean open
                      consistency strong
                      requires open
                    }
                    /** Points at a thing without being one. */
                    value-object ThingRef {
                      /** The thing pointed at. */
                      ThingId id
                    }
                    aggregate Thing identifier ThingId {
                      /** Whether it is open. */
                      Boolean open
                      method close business-rules MustBeOpen(own-id, own open) fires ClosedEvent {
                        event ClosedEvent { message "Closed" }
                      }
                    }
                    /** Closes it. */
                    command CloseThing target Thing.close {
                      message "Close it"
                    }
                  }
                }
                """));
    }

    /**
     * A row a view hands back, an aggregate with a gated operation, and a command addressing it.
     *
     * @param rowBody Attributes the row publishes.
     *
     * @return Model source.
     */
    private static String model(String rowBody) {
        return """
                context p {
                  module c.n {
                    type String
                    type Boolean
                    type List generics 1
                    aggregate-id ThingId identifies Thing base String { }
                    /** Refused because the thing is closed. */
                    exception ClosedException {
                      /** The thing that is closed. */
                      ThingId thing
                      message "${thing} is closed"
                    }
                    /** Makes sure the thing is open. */
                    business-rule MustBeOpen exception ClosedException {
                      /** The thing that is closed. */
                      ThingId thing
                      /** Whether it is open now. */
                      Boolean open
                      consistency strong
                      requires open
                    }
                    projection Things
                    view ThingView uses Things {
                      method listThings {
                        returns List<ThingRow>
                      }
                    }
                    /** One thing on screen. */
                    value-object ThingRow {
                %s
                    }
                    aggregate Thing identifier ThingId {
                      /** Whether it is open. */
                      Boolean open
                      method close business-rules MustBeOpen(own-id, own open) fires ClosedEvent {
                        event ClosedEvent { message "Closed" }
                      }
                    }
                    /** Closes it. */
                    command CloseThing target Thing.close {
                      message "Close it"
                    }
                  }
                }
                """.formatted(rowBody);
    }

}

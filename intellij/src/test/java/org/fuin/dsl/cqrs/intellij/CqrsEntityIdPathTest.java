package org.fuin.dsl.cqrs.intellij;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.List;

/**
 * Verifies the typed entity id path checks - the port of the Xtext validator's
 * {@code checkEntityIdPathShape} and {@code checkSegmentRange}, so the editor refuses what the SrcGen4J
 * build refuses.
 *
 * <p>A path begins at an aggregate root and names the chain of children down to the thing it addresses:
 * {@code ANNUAL_TRANSACTIONS 2026-a/TRANSACTION 45}. An aggregate reached <em>inside</em> a composite
 * identifier is not a step, which is the mistake the model's own prose invites.</p>
 */
public class CqrsEntityIdPathTest extends BasePlatformTestCase {

    /** The path errors reported for the given model. */
    private List<HighlightInfo> pathErrors(String body) {
        myFixture.configureByText("test.cqrs", body);
        return myFixture.doHighlighting().stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .filter(info -> info.getDescription() != null)
                .filter(info -> info.getDescription().contains("path")
                        || info.getDescription().contains("step")
                        || info.getDescription().contains("range is empty"))
                .toList();
    }

    /** A root followed by one of its entities is what a path is. */
    public void testARootAndOneOfItsEntitiesIsFine() {
        assertEmpty(pathErrors(model("""
                    entity-id-path PartPath { ThingId / PartId }
                """)));
    }

    /** A range is fine too, and says how many of the step the path takes. */
    public void testARangeIsFine() {
        assertEmpty(pathErrors(model("""
                    entity-id-path NestedPath { ThingId / PartId[1..*] }
                    entity-id-path CappedPath { ThingId / PartId[0..2] }
                """)));
    }

    /** One step is the identifier itself, and calling it a path buys nothing. */
    public void testAPathOfOneStep() {
        List<HighlightInfo> errors = pathErrors(model("""
                    entity-id-path RootOnly { ThingId }
                """));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("A path of one step is the identifier itself"));
    }

    /** A path that starts at an entity has no root to hang from. */
    public void testAPathThatStartsAtAnEntity() {
        List<HighlightInfo> errors = pathErrors(model("""
                    entity-id-path TooLow { PartId / ThingId }
                """));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("A path starts at an aggregate root"));
    }

    /** Only the first step is an aggregate. */
    public void testALaterStepThatIsAnotherAggregate() {
        List<HighlightInfo> errors = pathErrors(model("""
                    entity-id-path TwoRoots { ThingId / OtherId }
                """));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("Only the first step is an aggregate"));
    }

    /** It resolves, it is an entity id, and it belongs to a different aggregate. */
    public void testAnEntityOfAnotherRoot() {
        List<HighlightInfo> errors = pathErrors(model("""
                    entity-id-path Foreign { OtherId / PartId }
                """));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("belongs to 'Thing', not to 'Other'"));
    }

    /** An impossible range silently rejects every path, so it is caught where it is written. */
    public void testAnEmptyRange() {
        List<HighlightInfo> errors = pathErrors(model("""
                    entity-id-path Impossible { ThingId / PartId[2..1] }
                """));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("The range is empty"));
    }

    /** A step that takes nothing at all is a step that should not be written. */
    public void testAStepThatAcceptsNothing() {
        List<HighlightInfo> errors = pathErrors(model("""
                    entity-id-path Zero { ThingId / PartId[0..0] }
                """));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).getDescription().contains("accepts no identifier at all"));
    }

    private static String model(String declarations) {
        return """
                context p {
                  module c.n {
                    aggregate-id ThingId identifies Thing {}
                    aggregate-id OtherId identifies Other {}
                    entity-id PartId identifies Part {}
                %s
                    aggregate Thing identifier ThingId {}
                    aggregate Other identifier OtherId {}
                    entity Part identifier PartId root Thing {}
                  }
                }
                """.formatted(declarations);
    }

}

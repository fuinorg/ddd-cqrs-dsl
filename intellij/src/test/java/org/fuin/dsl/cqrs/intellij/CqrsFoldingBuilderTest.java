package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.util.TextRange;
import com.intellij.testFramework.EditorTestUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verifies the fold regions produced for {@code { ... }} blocks and multi-line comments, including
 * the {@code enum} body/{@code instances} pair that a per-rule (first-brace-to-last-brace) builder
 * would merge into a single region.
 */
public class CqrsFoldingBuilderTest extends BasePlatformTestCase {

    private List<FoldingDescriptor> foldRegions(String source) {
        myFixture.configureByText("test.cqrs", source);
        FoldingDescriptor[] descriptors = new CqrsFoldingBuilder()
                .buildFoldRegions(myFixture.getFile(), myFixture.getEditor().getDocument(), false);
        return Arrays.asList(descriptors);
    }

    /** The source text each region hides, so assertions read like the model they came from. */
    private List<String> foldedTexts(String source) {
        List<String> texts = new ArrayList<>();
        for (FoldingDescriptor descriptor : foldRegions(source)) {
            texts.add(descriptor.getRange().substring(source));
        }
        return texts;
    }

    private List<String> placeholders(String source) {
        List<String> texts = new ArrayList<>();
        for (FoldingDescriptor descriptor : foldRegions(source)) {
            texts.add(descriptor.getPlaceholderText());
        }
        return texts;
    }

    public void testNestedBlocksEachFoldSeparately() {
        List<FoldingDescriptor> regions = foldRegions("""
                project p {

                    context orders {

                        namespace m {

                            type String

                        }

                    }

                }
                """);

        assertEquals(3, regions.size());
        for (FoldingDescriptor region : regions) {
            assertEquals("{...}", region.getPlaceholderText());
        }
        // Stack matching pops the innermost pair first, so the regions come out inside-out and each
        // one is strictly contained in the next.
        for (int i = 0; i < regions.size() - 1; i++) {
            TextRange inner = regions.get(i).getRange();
            TextRange outer = regions.get(i + 1).getRange();
            assertTrue("region " + i + " must nest inside region " + (i + 1),
                    outer.contains(inner) && !outer.equals(inner));
        }
    }

    public void testEnumBodyAndInstancesFoldIndependently() {
        // 'enum_object' holds two brace pairs as direct children of one PSI node.
        List<String> folded = foldedTexts("""
                project p {
                    context c {
                        namespace m {
                            enum Color {
                                instances {
                                    RED
                                    GREEN
                                }
                            }
                        }
                    }
                }
                """);

        assertEquals(5, folded.size());
        assertTrue("the instances block must fold on its own",
                folded.get(0).startsWith("{") && folded.get(0).contains("RED") && !folded.get(0).contains("enum"));
        assertTrue("the enum body must fold on its own",
                folded.get(1).contains("instances") && !folded.get(1).contains("namespace"));
    }

    public void testSingleLineBlockIsNotFoldable() {
        assertEmpty(foldRegions("project p { context c { namespace m { type String } } }"));
    }

    public void testJsonObjectIsFoldable() {
        List<String> folded = foldedTexts("""
                project p {
                    hint SrcGen4J {
                        "package": "a.b.c",
                        "inline": { "x": 1 }
                    }
                }
                """);

        // The hint body and the project body fold; the one-line "inline" object does not.
        assertEquals(2, folded.size());
        assertTrue(folded.get(0).contains("\"package\""));
        assertTrue(folded.get(1).contains("hint SrcGen4J"));
    }

    public void testMultiLineCommentsFoldButSingleLineOnesDoNot() {
        assertEquals(List.of("/**...*/", "/*...*/"), placeholders("""
                /**
                 * Doc comment.
                 */
                /*
                 * Block comment.
                 */
                /* one-liner */
                // line comment
                """));
    }

    /** Drives the platform itself, so a descriptor the folding model rejects fails here. */
    public void testPlatformBuildsTheRegionsInTheEditor() {
        myFixture.configureByText("test.cqrs", """
                project p {

                    context orders {

                        namespace m {

                            type String

                        }

                    }

                }
                """);
        EditorTestUtil.buildInitialFoldingsInBackground(myFixture.getEditor());

        FoldRegion[] regions = myFixture.getEditor().getFoldingModel().getAllFoldRegions();
        assertEquals(3, regions.length);
        for (FoldRegion region : regions) {
            assertEquals("{...}", region.getPlaceholderText());
            assertTrue("nothing may start collapsed", region.isExpanded());
        }
    }

    public void testUnbalancedBracesProduceNoRegion() {
        // An opening brace with no partner is dropped ...
        assertEmpty(foldRegions("""
                project p {
                    context c {
                """));

        // ... and a stray closing brace is ignored rather than throwing.
        assertEmpty(foldRegions("""
                }
                }
                """));
    }
}

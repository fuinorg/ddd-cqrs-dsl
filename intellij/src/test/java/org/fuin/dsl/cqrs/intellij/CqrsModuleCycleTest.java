package org.fuin.dsl.cqrs.intellij;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.List;

/**
 * Verifies the module dependency cycle check — the port of the Xtext validator's
 * {@code checkModuleDependencyCycle}, so the editor refuses what the SrcGen4J build refuses.
 *
 * <p>The edges are resolved references, not {@code import} lines, and the fixtures below are written
 * to pull those apart: one has two modules importing each other while only one direction is actually
 * referenced, and it must stay clean.</p>
 */
public class CqrsModuleCycleTest extends BasePlatformTestCase {

    /** The cycle errors reported for the given model. */
    private List<HighlightInfo> cycleErrors(String body) {
        myFixture.configureByText("test.cqrs", body);
        return myFixture.doHighlighting().stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .filter(info -> info.getDescription() != null && info.getDescription().contains("dependency cycle"))
                .toList();
    }

    /** Two modules whose value objects reference each other close a cycle - both are reported. */
    public void testMutualReferenceIsReported() {
        List<HighlightInfo> errors = cycleErrors("""
                context p {
                  module types {
                    type String
                  }
                  module a {
                    import p.types.*
                    import p.b.*
                    value-object AThing {
                      BThing other
                    }
                  }
                  module b {
                    import p.types.*
                    import p.a.*
                    value-object BThing {
                      AThing other
                    }
                  }
                }
                """);
        assertEquals("both modules of the cycle are reported", 2, errors.size());
        assertTrue("the message names the path",
                errors.get(0).getDescription().matches(".*p\\.[ab] -> p\\.[ab] -> p\\.[ab].*"));
    }

    /** One direction only is a dependency, not a cycle - even though both modules import each other. */
    public void testOneDirectionIsNotACycle() {
        // "b" imports "a" and refers to nothing in it. Reading imports as dependencies would call this
        // a cycle; reading resolved references does not.
        assertEmpty(cycleErrors("""
                context p {
                  module types {
                    type String
                  }
                  module a {
                    import p.types.*
                    import p.b.*
                    value-object AThing {
                      BThing other
                    }
                  }
                  module b {
                    import p.types.*
                    import p.a.*
                    value-object BThing {
                      String value
                    }
                  }
                }
                """));
    }

    /** A module referring to its own types is not a cycle - an edge to itself is not an edge. */
    public void testSelfReferenceIsNotACycle() {
        assertEmpty(cycleErrors("""
                context p {
                  module solo {
                    type String
                    value-object Inner {
                      String value
                    }
                    value-object Outer {
                      Inner inner
                    }
                  }
                }
                """));
    }

    /** A three module ring is reported too, not only the two module case. */
    public void testLongerRingIsReported() {
        assertFalse("a three module ring must be reported", cycleErrors("""
                context p {
                  module types {
                    type String
                  }
                  module a {
                    import p.types.*
                    import p.c.*
                    value-object AThing {
                      CThing other
                    }
                  }
                  module b {
                    import p.types.*
                    import p.a.*
                    value-object BThing {
                      AThing other
                    }
                  }
                  module c {
                    import p.types.*
                    import p.b.*
                    value-object CThing {
                      BThing other
                    }
                  }
                }
                """).isEmpty());
    }

}

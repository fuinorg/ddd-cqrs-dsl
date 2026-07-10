package org.fuin.dsl.cqrs.intellij;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

/** Verifies {@link CqrsAnnotator} — unresolved cross-references are reported as errors. */
public class CqrsAnnotatorTest extends BasePlatformTestCase {

    /** Asserts the highlighting of {@code body} matches its inline markup. */
    private void check(String body) {
        myFixture.configureByText("test.cqrs", body);
        myFixture.checkHighlighting(true, false, false);
    }

    public void testUnknownAttributeTypeIsError() {
        check("""
                project p {
                context c {
                  namespace n {
                    event E {
                      <error descr="Cannot resolve 'Unknown'">Unknown</error> value
                    }
                  }
                }
                }
                """);
    }

    public void testUnknownEventInFiresIsError() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    type UUID
                    aggregate-id OrderId identifies Order base UUID {
                      examples "6bc75dd5-be5b-4c57-977e-8ee404b21c74"
                    }
                    aggregate Order identifier OrderId {
                      method remove fires <error descr="Cannot resolve 'OrderFooEvent'">OrderFooEvent</error> {
                        event OrderRemovedEvent {
                        }
                      }
                    }
                  }
                }
                }
                """);
    }

    public void testKnownTypeIsNotFlagged() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    value-object X base String {
                      String value
                    }
                  }
                }
                }
                """);
    }
}

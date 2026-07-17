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

    // --- a command's 'target' is a method, not any named element --------------------------------

    /** The target names an attribute of the event, which can never be a method. */
    public void testCommandTargetPointingToAnAttributeIsError() {
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
                      method rename fires OrderRenamedEvent {
                        String newName
                        event OrderRenamedEvent {
                          String renamedTo
                        }
                      }
                      command RenameCommand target <error descr="Cannot resolve 'renamedTo'">renamedTo</error> {
                        String newName
                      }
                    }
                  }
                }
                }
                """);
    }

    public void testCommandTargetPointingToAMethodIsValid() {
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
                      method rename fires OrderRenamedEvent {
                        String newName
                        event OrderRenamedEvent {
                          String renamedTo
                        }
                      }
                      command RenameCommand target Order.rename {
                        String newName
                      }
                    }
                  }
                }
                }
                """);
    }

    // --- a name that exists but is not imported is out of scope ---------------------------------

    /** 'Money' is declared in another context and not imported, so it must not resolve. */
    public void testTypeFromAnotherContextWithoutImportIsError() {
        myFixture.configureByText("other.cqrs", """
                project p {
                  context other {
                    type String
                    value-object Money base String {
                      String value
                    }
                  }
                }
                """);
        check("""
                project p {
                context c {
                  namespace n {
                    event E {
                      <error descr="Cannot resolve 'Money'">Money</error> price
                    }
                  }
                }
                }
                """);
    }

    /** The same reference resolves once the declaring context is imported. */
    public void testTypeFromAnotherContextWithImportIsValid() {
        myFixture.configureByText("other.cqrs", """
                project p {
                  context other {
                    type String
                    value-object Money base String {
                      String value
                    }
                  }
                }
                """);
        check("""
                project p {
                context c {
                  namespace n {
                    import p.other.*
                    event E {
                      Money price
                    }
                  }
                }
                }
                """);
    }
}

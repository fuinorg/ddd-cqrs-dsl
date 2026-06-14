package org.fuin.dsl.cqrs.intellij;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

/**
 * Verifies {@link CqrsValidationAnnotator} — the semantic validation ported from the Eclipse/Xtext
 * {@code CqrsDslValidator}. Expected errors/warnings are marked inline with {@code <error>}/
 * {@code <warning>} ranges; weak warnings (the unresolved-reference annotator) are ignored so that
 * only the semantic diagnostics are asserted.
 */
public class CqrsValidationAnnotatorTest extends BasePlatformTestCase {

    /** Asserts the highlighting of {@code body} matches its inline markup (errors + warnings). */
    private void check(String body) {
        myFixture.configureByText("test.cqrs", body);
        myFixture.checkHighlighting(true, false, false);
    }

    // --- value object 'base': exactly one attribute with the base type --------------------------

    public void testValueObjectBaseSingleMatchingAttributeIsValid() {
        check("""
                context c {
                  namespace n {
                    type String
                    value-object Email base String {
                      String value
                    }
                  }
                }
                """);
    }

    public void testValueObjectBaseWithTwoAttributesIsFlagged() {
        check("""
                context c {
                  namespace n {
                    type String
                    value-object X base <error>String</error> {
                      String a
                      String b
                    }
                  }
                }
                """);
    }

    public void testValueObjectBaseWithMismatchingAttributeTypeIsFlagged() {
        check("""
                context c {
                  namespace n {
                    type String
                    type Integer
                    value-object X base <error>Integer</error> {
                      String value
                    }
                  }
                }
                """);
    }

    public void testValueObjectWithoutBaseAllowsSeveralAttributes() {
        check("""
                context c {
                  namespace n {
                    type String
                    type Integer
                    value-object Money {
                      Integer amount
                      String currency
                    }
                  }
                }
                """);
    }

    // --- value object 'base': no constructors / methods -----------------------------------------

    public void testValueObjectBaseWithConstructorIsFlagged() {
        check("""
                context c {
                  namespace n {
                    type String
                    value-object X base String {
                      String value
                      constructor <error>create</error> {
                      }
                    }
                  }
                }
                """);
    }

    public void testValueObjectBaseWithMethodIsFlagged() {
        check("""
                context c {
                  namespace n {
                    type String
                    value-object X base String {
                      String value
                      method <error>doIt</error> {
                        returns String
                      }
                    }
                  }
                }
                """);
    }

    // --- variable naming ------------------------------------------------------------------------

    public void testVariableNameStartingUpperCaseIsWarned() {
        check("""
                context c {
                  namespace n {
                    type String
                    value-object X {
                      String <warning>Value</warning>
                    }
                  }
                }
                """);
    }
}

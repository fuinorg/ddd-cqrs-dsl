package org.fuin.dsl.cqrs.intellij;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

/**
 * Verifies {@link CqrsValidationAnnotator} — the semantic validation ported from the Eclipse/Xtext
 * {@code CqrsDslValidator}. Expected errors/warnings are marked inline with {@code <error>}/
 * {@code <warning>} ranges. Unresolved references are reported separately by
 * {@link CqrsAnnotator}, so the bodies below only use types they declare.
 */
public class CqrsValidationAnnotatorTest extends BasePlatformTestCase {

    /** Asserts the highlighting of {@code body} matches its inline markup (errors + warnings). */
    private void check(String body) {
        myFixture.configureByText("test.cqrs", body);
        myFixture.checkHighlighting(true, false, false);
    }

    // --- value object 'base' ---------------------------------------------------------------------
    // A 'base' does not restrict the attributes: only "base String + exactly one attribute" is
    // generated as a complete class (SimpleStringValueObjectArtifactFactory); every other shape
    // gets an abstract base class plus a hand-written final class supplying asBaseType(). See
    // CombinedValueObjectArtifactFactory.

    public void testValueObjectBaseSingleMatchingAttributeIsValid() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    value-object Email base String {
                      String value
                    }
                  }
                }
                }
                """);
    }

    /** The PhoneNumber shape: a String-backed VO whose base representation packs several attributes. */
    public void testValueObjectBaseWithSeveralAttributesIsValid() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    enum PhoneType {
                      instances {
                        MOBILE
                        LANDLINE
                      }
                    }
                    value-object PhoneNumber base String {
                      PhoneType typ
                      String value
                    }
                  }
                }
                }
                """);
    }

    public void testValueObjectBaseWithMismatchingAttributeTypeIsValid() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    type Integer
                    value-object X base Integer {
                      String value
                    }
                  }
                }
                }
                """);
    }

    public void testValueObjectWithoutBaseAllowsSeveralAttributes() {
        check("""
                project p {
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
                }
                """);
    }

    // --- value object 'base': no constructors / methods -----------------------------------------

    public void testValueObjectBaseWithConstructorIsFlagged() {
        check("""
                project p {
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
                }
                """);
    }

    public void testValueObjectBaseWithMethodIsFlagged() {
        check("""
                project p {
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
                }
                """);
    }

    // --- variable naming ------------------------------------------------------------------------

    public void testVariableNameStartingUpperCaseIsWarned() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    value-object X {
                      String <warning>Value</warning>
                    }
                  }
                }
                }
                """);
    }
}

package org.fuin.dsl.cqrs.intellij;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.util.List;

/**
 * Verifies that basic code completion offers the visible type declarations (not only keywords) when
 * starting an attribute inside the various type-declaring blocks — in particular {@code value-object}
 * and {@code constraint} bodies, where the half-typed type identifier is not yet parsed as a
 * {@code type_ref}.
 */
public class CqrsCompletionContributorTest extends BasePlatformTestCase {

    private List<String> lookups(String body) {
        myFixture.configureByText("test.cqrs", body);
        myFixture.complete(CompletionType.BASIC);
        List<String> strings = myFixture.getLookupElementStrings();
        return strings == null ? List.of() : strings;
    }

    public void testValueObjectAttributeStartOffersTypes() {
        assertContainsType(lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    value-object Foo {
                      <caret>
                    }
                  }
                }
                }
                """));
    }

    public void testConstraintAttributeStartOffersTypes() {
        assertContainsType(lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    constraint NotBlank input String {
                      <caret>
                    }
                  }
                }
                }
                """));
    }

    public void testAggregateAttributeStartOffersTypes() {
        assertContainsType(lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      <caret>
                    }
                  }
                }
                }
                """));
    }

    public void testNamespaceLevelDoesNotOfferTypesButOffersElementKeywords() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    <caret>
                  }
                }
                }
                """);
        assertFalse("namespace level must not offer types: " + lookups, lookups.contains("String"));
        assertTrue("namespace level must offer element keywords: " + lookups, lookups.contains("value-object"));
    }

    public void testContextWithoutNamespaceOffersElementKeywords() {
        // The namespace is optional: element keywords (and "import") are offered directly inside a
        // context, while "namespace" is still offered too.
        List<String> lookups = lookups("""
                project p {
                context c {
                  <caret>
                }
                }
                """);
        assertFalse("context level must not offer types: " + lookups, lookups.contains("String"));
        assertTrue("context without namespace must offer element keywords: " + lookups, lookups.contains("value-object"));
        assertTrue("context without namespace must still offer 'namespace': " + lookups, lookups.contains("namespace"));
    }

    public void testTopLevelDoesNotOfferTypes() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  <caret>
                }
                }
                """);
        assertFalse("top level must not offer types: " + lookups, lookups.contains("String"));
    }

    private static void assertContainsType(List<String> lookups) {
        assertTrue("expected the declared type 'String' among completions, but got: " + lookups,
                lookups.contains("String"));
    }

    // ---- data-protection block value completion -----------------------------------------

    public void testProtectionOffersProtectionLevels() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    data-protection P {
                      protection <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected protection levels, got: " + lookups,
                lookups.containsAll(List.of("none", "personal", "sensitive")));
    }

    public void testLawfulBasisOffersBases() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    data-protection P {
                      protection personal
                      lawful-basis <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected lawful bases, got: " + lookups,
                lookups.containsAll(List.of("contract", "explicit_consent", "legitimate_interests")));
    }

    public void testRetentionNumberOffersTimeUnits() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    data-protection P {
                      protection personal
                      retention 10 <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected time units, got: " + lookups,
                lookups.containsAll(List.of("days", "months", "years")));
    }

    public void testThenOffersErasureStrategies() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    data-protection P {
                      protection personal
                      retention 10 years then <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected erasure strategies, got: " + lookups,
                lookups.containsAll(List.of("delete", "anonymize", "pseudonymize")));
    }

    public void testDataProtectionClauseStartOffersClauseKeywords() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    data-protection P {
                      protection personal
                      <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected clause keywords, got: " + lookups,
                lookups.containsAll(List.of("category", "subject", "purpose", "lawful-basis", "retention")));
        assertFalse("must not offer namespace element keywords inside the block: " + lookups,
                lookups.contains("value-object"));
    }

    // ---- process-manager completion -----------------------------------------------------

    public void testNamespaceOffersProcessManagerElementKeyword() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    <caret>
                  }
                }
                }
                """);
        assertTrue("expected 'process-manager' among element keywords, got: " + lookups,
                lookups.contains("process-manager"));
    }

    public void testProcessManagerBodyOffersClauseKeywords() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    process-manager P {
                      <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected process-manager clauses, got: " + lookups,
                lookups.containsAll(List.of("cron-schedule", "correlation-id", "process-states", "reacts-to")));
        assertFalse("must not offer namespace element keywords inside the block: " + lookups,
                lookups.contains("value-object"));
    }

    public void testProcessReactionBodyOffersReactionKeywords() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    event E { message "e" }
                    process-manager P {
                      reacts-to E {
                        <caret>
                      }
                    }
                  }
                }
                }
                """);
        assertTrue("expected reaction clauses, got: " + lookups,
                lookups.containsAll(List.of("correlate-by", "issues-commands", "transition-to", "arm-timeout", "cancel-timeout")));
    }

    public void testArmTimeoutNumberOffersTimeUnits() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    event E { message "e" }
                    process-manager P {
                      reacts-to E {
                        arm-timeout 15 <caret>
                      }
                    }
                  }
                }
                }
                """);
        assertTrue("expected time units after the arm-timeout number, got: " + lookups,
                lookups.containsAll(List.of("seconds", "minutes", "hours")));
    }

    public void testViewBodyOffersBusinessRuleAndMethod() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    projection Pj
                    view V uses Pj {
                      <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected view-body keywords, got: " + lookups,
                lookups.containsAll(List.of("business-rule", "method")));
        assertFalse("must not offer namespace element keywords inside the view body: " + lookups,
                lookups.contains("value-object"));
    }
}

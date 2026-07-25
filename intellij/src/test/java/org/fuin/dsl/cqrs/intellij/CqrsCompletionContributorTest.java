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
                lookups.containsAll(List.of("cron-schedule", "instance-key", "process-states", "reacts-to")));
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
                lookups.containsAll(List.of("hint", "cron-schedule", "business-rule", "method")));
        assertFalse("'rest-path' is a header clause and must not be offered in the view body: " + lookups,
                lookups.contains("rest-path"));
        assertFalse("must not offer namespace element keywords inside the view body: " + lookups,
                lookups.contains("value-object"));
    }

    public void testViewHeaderOffersRestPath() {
        // 'rest-path' sits between the projection reference and the opening brace.
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    projection Pj
                    view V uses Pj <caret> {
                    }
                  }
                }
                }
                """);
        assertTrue("expected 'rest-path' in the view header, got: " + lookups,
                lookups.contains("rest-path"));
    }

    public void testViewMethodHeaderOffersRestPath() {
        // A view method is exposed as a REST operation, so its header may set its own sub path.
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    projection Pj
                    view V uses Pj {
                      method find <caret> {
                      }
                    }
                  }
                }
                }
                """);
        assertTrue("expected 'rest-path' in the view method header, got: " + lookups,
                lookups.contains("rest-path"));
    }

    public void testViewMethodBodyDoesNotOfferRestPath() {
        // Inside the body it would be invalid syntax - only 'returns' and friends belong there.
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    projection Pj
                    view V uses Pj {
                      method find {
                        <caret>
                      }
                    }
                  }
                }
                }
                """);
        assertFalse("'rest-path' is a header clause and must not be offered in a method body: " + lookups,
                lookups.contains("rest-path"));
        assertTrue("expected 'returns' inside a method, got: " + lookups, lookups.contains("returns"));
    }

    public void testServiceMethodDoesNotOfferRestPath() {
        // Only a view method is a REST operation - on any other method 'rest-path' is an error.
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    service S {
                      method doIt <caret> {
                      }
                    }
                  }
                }
                }
                """);
        assertFalse("'rest-path' must not be offered outside a view method: " + lookups,
                lookups.contains("rest-path"));
        assertTrue("expected 'returns' inside a method, got: " + lookups, lookups.contains("returns"));
    }

    // ---- business-rule consistency completion -------------------------------------------
    //
    // A business rule is nested in an aggregate, so without its own branch the caret was claimed by
    // the aggregate/entity branch: the consistency keywords were never offered, and every visible
    // type declaration was.

    public void testConsistencyOffersOnlyTheLevels() {
        assertOffersExactly(aggregateBusinessRule("consistency <caret>"), "weak", "strong");
    }

    public void testBusinessRuleBodyOffersConsistency() {
        List<String> lookups = aggregateBusinessRule("<caret>");
        assertTrue("expected 'consistency' in the rule body, got: " + lookups,
                lookups.contains("consistency"));
        assertNoTypesAndNoAggregateKeywords(lookups);
    }

    public void testWeakOffersAcceptable() {
        List<String> lookups = aggregateBusinessRule("consistency weak <caret>");
        assertTrue("expected 'acceptable', got: " + lookups, lookups.contains("acceptable"));
    }

    public void testWeakBlockStartOffersAcceptable() {
        List<String> lookups = aggregateBusinessRule("""
                consistency weak {
                          <caret>
                        }""");
        assertTrue("expected 'acceptable' at the start of the weak block, got: " + lookups,
                lookups.contains("acceptable"));
        assertFalse("'consistency' is already given at that point: " + lookups,
                lookups.contains("consistency"));
    }

    public void testAcceptableNumberOffersTimeUnits() {
        List<String> lookups = aggregateBusinessRule("""
                consistency weak {
                          acceptable 1 <caret>
                        }""");
        assertTrue("expected time units after the acceptable number, got: " + lookups,
                lookups.containsAll(List.of("seconds", "minutes", "days")));
        assertNoTypesAndNoAggregateKeywords(lookups);
    }

    public void testTimeUnitOffersDetection() {
        List<String> lookups = aggregateBusinessRule("""
                consistency weak {
                          acceptable 1 days <caret>
                        }""");
        assertTrue("expected 'detection' after the duration, got: " + lookups,
                lookups.contains("detection"));
    }

    public void testDetectionOffersItsOwnValues() {
        List<String> lookups = aggregateBusinessRule("""
                consistency weak {
                          acceptable 1 days
                          detection <caret>
                        }""");
        assertOffersExactly(lookups, "never", "manually", "automatic");
        assertFalse("'workflow' is a resolution value only: " + lookups, lookups.contains("workflow"));
    }

    public void testResolutionOffersItsOwnValues() {
        List<String> lookups = aggregateBusinessRule("""
                consistency weak {
                          acceptable 1 days
                          detection automatic
                          resolution <caret>
                        }""");
        assertOffersExactly(lookups, "never", "manually", "automatic", "workflow");
    }

    public void testDetectionValueOffersResolution() {
        List<String> lookups = aggregateBusinessRule("""
                consistency weak {
                          acceptable 1 days
                          detection automatic <caret>
                        }""");
        assertTrue("expected 'resolution' after the detection value, got: " + lookups,
                lookups.contains("resolution"));
    }

    public void testBusinessRuleExceptionPositionStillOffersTypes() {
        // The one type position inside a business rule - suppressing the type dump must not break it.
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    exception MyException { message "m" }
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      business-rule Rule exception <caret>
                    }
                  }
                }
                }
                """);
        assertTrue("expected the exception type among completions, got: " + lookups,
                lookups.contains("MyException"));
    }

    /**
     * Runs completion on the given business-rule body, placed in a minimal aggregate that also
     * declares a type and an exception - so a leaking type dump is visible as {@code String} among
     * the lookups.
     *
     * @param ruleBody Content of the rule's braces, containing the {@code <caret>} marker.
     *
     * @return Offered lookup strings.
     */
    private List<String> aggregateBusinessRule(String ruleBody) {
        return lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    exception MyException { message "m" }
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      business-rule Rule exception MyException {
                        %s
                      }
                    }
                  }
                }
                }
                """.formatted(ruleBody));
    }

    // ---- literal and external-type keyword completion ------------------------------------

    public void testExamplesOffersLiteralKeywords() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    value-object Foo {
                      String value
                      examples <caret>
                    }
                  }
                }
                }
                """);
        assertOffersExactly(lookups, "null", "true", "false");
    }

    public void testConstraintArgumentListOffersLiteralKeywords() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    type String
                    constraint Length input String { message "m" }
                    value-object Foo {
                      String value invariants Length(<caret>)
                    }
                  }
                }
                }
                """);
        assertOffersExactly(lookups, "null", "true", "false");
    }

    public void testTypeOffersElementKeyword() {
        List<String> lookups = lookups("""
                project p {
                context c {
                  namespace n {
                    type <caret>
                  }
                }
                }
                """);
        assertOffersExactly(lookups, "element");
    }

    /** Asserts the offered lookups are exactly the expected ones - the popup order is not fixed. */
    private static void assertOffersExactly(List<String> lookups, String... expected) {
        assertEquals("expected exactly " + List.of(expected) + ", got: " + lookups,
                new java.util.TreeSet<>(List.of(expected)), new java.util.TreeSet<>(lookups));
    }

    private static void assertNoTypesAndNoAggregateKeywords(List<String> lookups) {
        assertFalse("a business rule holds no attributes, so no type may be offered: " + lookups,
                lookups.contains("String"));
        assertFalse("must not offer the aggregate body keywords inside a business rule: " + lookups,
                lookups.contains("business-rule"));
        assertFalse("must not offer the aggregate body keywords inside a business rule: " + lookups,
                lookups.contains("method"));
    }
}

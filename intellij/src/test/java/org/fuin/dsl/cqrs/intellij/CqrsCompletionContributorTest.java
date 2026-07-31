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

    /**
     * The same scenario the Eclipse editor showed: an attribute inside a value object must offer the
     * module's own types only - not a sibling module's, not another file's, and not the module names
     * themselves.
     */
    public void testAttributeStartOffersOnlyReachableTypes() {
        myFixture.configureByText("other.cqrs", """
                context other_ctx {
                  module far.away {
                    type ShouldNotBeOffered
                  }
                }
                """);
        List<String> lookups = lookups("""
                context p_03_value_object {
                  module vo.m {
                    type String
                    type Integer

                    value-object Money {
                      <caret>
                    }
                  }
                  module vo.sibling {
                    type SiblingType
                  }
                }
                """);
        assertTrue("own module types must be offered: " + lookups, lookups.contains("String"));
        assertTrue("own module types must be offered: " + lookups, lookups.contains("Integer"));
        assertFalse("a sibling module needs an import: " + lookups, lookups.contains("SiblingType"));
        assertFalse("another file must not leak in: " + lookups, lookups.contains("ShouldNotBeOffered"));
        assertFalse("a module is not a type: " + lookups, lookups.contains("vo.sibling"));
        assertFalse("a module is not a type: " + lookups, lookups.contains("vo.m"));
        assertFalse("a context is not a type: " + lookups, lookups.contains("p_03_value_object"));
    }

    /** A context wide wildcard reaches the modules' types - but the module names are not types. */
    public void testContextWildcardDoesNotOfferModuleNames() {
        List<String> lookups = lookups("""
                context p {
                  module vo.m {
                    import p.*

                    type Integer

                    value-object Money {
                      <caret>
                    }
                  }
                  module vo.sibling {
                    type SiblingType
                  }
                }
                """);
        assertTrue("an imported type must be offered: " + lookups, lookups.contains("SiblingType"));
        assertFalse("a module is not a type: " + lookups, lookups.contains("vo.sibling"));
        assertFalse("a module is not a type: " + lookups, lookups.contains("vo.m"));
    }

    public void testValueObjectAttributeStartOffersTypes() {
        assertContainsType(lookups("""
                context p {
                  module c.n {
                    type String
                    value-object Foo {
                      <caret>
                    }
                  }
                }
                """));
    }

    public void testConstraintAttributeStartOffersTypes() {
        assertContainsType(lookups("""
                context p {
                  module c.n {
                    type String
                    constraint NotBlank input String {
                      <caret>
                    }
                  }
                }
                """));
    }

    public void testAggregateAttributeStartOffersTypes() {
        assertContainsType(lookups("""
                context p {
                  module c.n {
                    type String
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      <caret>
                    }
                  }
                }
                """));
    }

    public void testNamespaceLevelDoesNotOfferTypesButOffersElementKeywords() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    type String
                    <caret>
                  }
                }
                """);
        assertFalse("module level must not offer types: " + lookups, lookups.contains("String"));
        assertTrue("module level must offer element keywords: " + lookups, lookups.contains("value-object"));
    }

    /** After 'import' the reachable contexts, modules and single types are offered. */
    public void testImportOffersReachablePaths() {
        List<String> lookups = lookups("""
                context p {
                  module types {
                    type Money
                  }
                  module use {
                    import <caret>
                  }
                }
                """);
        assertTrue("must offer the module wildcard: " + lookups, lookups.contains("p.types.*"));
        assertTrue("must offer the context wildcard: " + lookups, lookups.contains("p.*"));
        assertTrue("must offer the single type: " + lookups, lookups.contains("p.types.Money"));
        assertFalse("must not offer the own module: " + lookups, lookups.contains("p.use.*"));
        assertFalse("must not offer element keywords: " + lookups, lookups.contains("value-object"));
    }

    /** A type of another module is offered only once that module is imported. */
    public void testTypeOfAnotherModuleIsOfferedOnlyWhenImported() {
        List<String> without = lookups("""
                context p {
                  module types {
                    type Money
                  }
                  module use {
                    value-object Price {
                      <caret>
                    }
                  }
                }
                """);
        assertFalse("a type that is not imported must not be offered: " + without,
                without.contains("Money"));

        List<String> with = lookups("""
                context p {
                  module types {
                    type Money
                  }
                  module use {
                    import p.types.*

                    value-object Price {
                      <caret>
                    }
                  }
                }
                """);
        assertTrue("an imported type must be offered: " + with, with.contains("Money"));
    }

    public void testContextLevelOffersDependencyImportHintAndModule() {
        // A context holds only its dependencies, its hints and its modules - never elements.
        List<String> lookups = lookups("""
                context p {
                  <caret>
                  module c { }
                }
                """);
        assertFalse("context level must not offer types: " + lookups, lookups.contains("String"));
        assertFalse("context level must not offer element keywords: " + lookups, lookups.contains("value-object"));
        assertTrue("context must offer 'module': " + lookups, lookups.contains("module"));
        assertTrue("context must offer 'dependency': " + lookups, lookups.contains("dependency"));
        assertTrue("context must offer 'hint': " + lookups, lookups.contains("hint"));
        assertTrue("context must offer 'import': " + lookups, lookups.contains("import"));
    }

    public void testTopLevelDoesNotOfferTypes() {
        List<String> lookups = lookups("""
                context p {
                module c {
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
                context p {
                  module c.n {
                    data-protection P {
                      protection <caret>
                    }
                  }
                }
                """);
        assertTrue("expected protection levels, got: " + lookups,
                lookups.containsAll(List.of("none", "personal", "sensitive")));
    }

    public void testLawfulBasisOffersBases() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    data-protection P {
                      protection personal
                      lawful-basis <caret>
                    }
                  }
                }
                """);
        assertTrue("expected lawful bases, got: " + lookups,
                lookups.containsAll(List.of("contract", "explicit_consent", "legitimate_interests")));
    }

    public void testRetentionNumberOffersTimeUnits() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    data-protection P {
                      protection personal
                      retention 10 <caret>
                    }
                  }
                }
                """);
        assertTrue("expected time units, got: " + lookups,
                lookups.containsAll(List.of("days", "months", "years")));
    }

    public void testThenOffersErasureStrategies() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    data-protection P {
                      protection personal
                      retention 10 years then <caret>
                    }
                  }
                }
                """);
        assertTrue("expected erasure strategies, got: " + lookups,
                lookups.containsAll(List.of("delete", "anonymize", "pseudonymize")));
    }

    public void testDataProtectionClauseStartOffersClauseKeywords() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    data-protection P {
                      protection personal
                      <caret>
                    }
                  }
                }
                """);
        assertTrue("expected clause keywords, got: " + lookups,
                lookups.containsAll(List.of("category", "subject", "purpose", "lawful-basis", "retention")));
        assertFalse("must not offer module element keywords inside the block: " + lookups,
                lookups.contains("value-object"));
    }

    // ---- process-manager completion -----------------------------------------------------

    public void testNamespaceOffersProcessManagerElementKeyword() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    <caret>
                  }
                }
                """);
        assertTrue("expected 'process-manager' among element keywords, got: " + lookups,
                lookups.contains("process-manager"));
    }

    public void testProcessManagerBodyOffersClauseKeywords() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    process-manager P {
                      <caret>
                    }
                  }
                }
                """);
        assertTrue("expected process-manager clauses, got: " + lookups,
                lookups.containsAll(List.of("cron-schedule", "instance-key", "process-states", "reacts-to")));
        assertFalse("must not offer module element keywords inside the block: " + lookups,
                lookups.contains("value-object"));
    }

    public void testProcessReactionBodyOffersReactionKeywords() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    event E { message "e" }
                    process-manager P {
                      reacts-to E {
                        <caret>
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
                context p {
                  module c.n {
                    event E { message "e" }
                    process-manager P {
                      reacts-to E {
                        arm-timeout 15 <caret>
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
                context p {
                  module c.n {
                    projection Pj
                    view V uses Pj {
                      <caret>
                    }
                  }
                }
                """);
        assertTrue("expected view-body keywords, got: " + lookups,
                lookups.containsAll(List.of("hint", "cron-schedule", "business-rule", "method")));
        assertFalse("'rest-path' is a header clause and must not be offered in the view body: " + lookups,
                lookups.contains("rest-path"));
        assertFalse("must not offer module element keywords inside the view body: " + lookups,
                lookups.contains("value-object"));
    }

    public void testViewHeaderOffersRestPath() {
        // 'rest-path' sits between the projection reference and the opening brace.
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    projection Pj
                    view V uses Pj <caret> {
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
                context p {
                  module c.n {
                    projection Pj
                    view V uses Pj {
                      method find <caret> {
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
                context p {
                  module c.n {
                    projection Pj
                    view V uses Pj {
                      method find {
                        <caret>
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
                context p {
                  module c.n {
                    service S {
                      method doIt <caret> {
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
                context p {
                  module c.n {
                    type String
                    exception MyException { message "m" }
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      business-rule Rule exception <caret>
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
                context p {
                  module c.n {
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
                """.formatted(ruleBody));
    }

    // ---- operation context -----------------------------------------------------------------

    public void testMethodBodyOffersOperationContextAndService() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    type String
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      method doIt {
                        <caret>
                      }
                    }
                  }
                }
                """);
        assertTrue("expected the operation's own clauses, got: " + lookups,
                lookups.containsAll(List.of("operation-context", "service", "returns")));
    }

    public void testOperationContextOffersTheVisibleServices() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    type String
                    service DoItService { }
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      method doIt {
                        operation-context <caret>
                      }
                    }
                  }
                }
                """);
        assertTrue("expected the declared service as the operation context, got: " + lookups,
                lookups.contains("DoItService"));
        // The grammar types the reference as [Service|FQN], so nothing else may be offered there.
        assertFalse("an operation context is a service, not a type: " + lookups, lookups.contains("String"));
        assertFalse("an operation context is a service, not an aggregate: " + lookups, lookups.contains("Foo"));
        assertFalse("an operation context is a service, not an id: " + lookups, lookups.contains("FooId"));
    }

    public void testOperationContextOffersAnInlineServiceOnly() {
        // A service declared inside the operation itself is the usual case.
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    type String
                    type Boolean
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      method doIt {
                        operation-context <caret>
                        service DoItService {
                          method check {
                            returns Boolean
                          }
                        }
                      }
                    }
                  }
                }
                """);
        assertTrue("expected the inline service, got: " + lookups, lookups.contains("DoItService"));
        assertFalse("must not offer plain types: " + lookups, lookups.contains("String"));
        assertFalse("must not offer the enclosing aggregate: " + lookups, lookups.contains("Foo"));
    }

    // ---- literal and external-type keyword completion ------------------------------------

    public void testExamplesOffersLiteralKeywords() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    type String
                    value-object Foo {
                      String value
                      examples <caret>
                    }
                  }
                }
                """);
        assertOffersExactly(lookups, "null", "true", "false");
    }

    public void testConstraintArgumentListOffersLiteralKeywords() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    type String
                    constraint Length input String { message "m" }
                    value-object Foo {
                      String value invariants Length(<caret>)
                    }
                  }
                }
                """);
        assertOffersExactly(lookups, "null", "true", "false");
    }

    public void testTypeOffersElementKeyword() {
        List<String> lookups = lookups("""
                context p {
                  module c.n {
                    type <caret>
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
    /**
     * Typing a Maven coordinate must stay quiet. The dots in "org.fuin..." and in the version used to
     * pop up the keyword list, because a string is unterminated while it is being typed: the lexer
     * needs both quotes for a STRING, so the opening quote stayed a bad character and the content
     * lexed as ordinary identifiers and dots.
     */
    public void testNothingIsOfferedInsideADependencyCoordinate() {
        assertEmpty("a coordinate is free text", lookups("""
                context cp {
                  dependency "org.fuin.<caret>"
                  module m {
                    type Own
                  }
                }
                """));
        assertEmpty("the version is free text too", lookups("""
                context cp {
                  dependency "g:a:0.1.<caret>"
                  module m {
                    type Own
                  }
                }
                """));
        assertEmpty("a 'local' directory is free text too", lookups("""
                context cp {
                  dependency "g:a:1" local "some.<caret>"
                  module m {
                    type Own
                  }
                }
                """));
    }

    /** A label is free text as well - the same string rule protects it. */
    public void testNothingIsOfferedInsideALabel() {
        assertEmpty("a label is free text", lookups("""
                context cp {
                  module m {
                    value-object V {
                      label "some. <caret>"
                    }
                  }
                }
                """));
    }

    /**
     * Opening a quote closes it at once, so a string is never left unterminated while it is typed.
     * That is what keeps the lexer producing a STRING for it - see
     * {@link org.fuin.dsl.cqrs.intellij.CqrsQuoteHandler}.
     */
    public void testTypingAQuoteClosesIt() {
        myFixture.configureByText("test.cqrs", """
                context cp {
                  dependency <caret>
                }
                """);
        myFixture.type('"');
        myFixture.checkResult("""
                context cp {
                  dependency ""
                }
                """);
    }

    /** With the quote closed, typing the coordinate offers nothing at any dot. */
    public void testTypingACoordinateOffersNothing() {
        myFixture.configureByText("test.cqrs", """
                context cp {
                  dependency <caret>
                  module m {
                    type Own
                  }
                }
                """);
        myFixture.type("\"org.fuin.");
        myFixture.complete(CompletionType.BASIC);
        List<String> strings = myFixture.getLookupElementStrings();
        assertEmpty("typing a coordinate must stay quiet",
                strings == null ? List.of() : strings);
    }

    private static void assertEmpty(String message, List<String> lookups) {
        assertTrue(message + ", but got: " + lookups, lookups.isEmpty());
    }
}

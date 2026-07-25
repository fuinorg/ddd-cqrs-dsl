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
    // ---- typed cross-references ----------------------------------------------------------------
    // Almost every reference in the grammar names one kind of declaration. A name of the wrong kind
    // resolves to nothing - the same as an unknown name - so the editor reports what the build would.

    public void testOperationContextMustBeAService() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    service DoItService { }
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      method doIt {
                        operation-context DoItService
                      }
                      method doItWrong {
                        operation-context <error descr="Cannot resolve 'String'">String</error>
                      }
                    }
                  }
                }
                }
                """);
    }

    public void testViewUsesMustBeAProjection() {
        check("""
                project p {
                context c {
                  namespace n {
                    event E { }
                    projection Pj input E
                    view Ok uses Pj { }
                    view Wrong uses <error descr="Cannot resolve 'E'">E</error> { }
                  }
                }
                }
                """);
    }

    public void testFiresMustBeAnEvent() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    value-object Money base String { String value }
                    event OkEvent { }
                    aggregate-id FooId identifies Foo {}
                    aggregate Foo identifier FooId {
                      method a fires OkEvent { }
                      method b fires <error descr="Cannot resolve 'Money'">Money</error> { }
                    }
                  }
                }
                }
                """);
    }

    public void testAggregateIdentifierMustBeAnAggregateId() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    aggregate-id FooId identifies Foo {}
                    entity-id BarId {}
                    aggregate Foo identifier FooId { }
                    aggregate Baz identifier <error descr="Cannot resolve 'BarId'">BarId</error> { }
                  }
                }
                }
                """);
    }

    public void testInvariantsMustBeAConstraint() {
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    constraint NotBlank input String { message "Blank" }
                    event OkEvent { }
                    value-object Ok base String { String value invariants NotBlank }
                    value-object Wrong base String {
                      String value invariants <error descr="Cannot resolve 'OkEvent'">OkEvent</error>
                    }
                  }
                }
                }
                """);
    }

    public void testAnyTypeIsStillAllowedForAnAttribute() {
        // Not every reference is narrowed: an attribute, a 'returns' and a generic argument take any
        // type, so a value object, an enum or an external type are all valid there.
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    value-object Money base String { String value }
                    event E {
                      Money price
                      String note
                    }
                  }
                }
                }
                """);
    }
    public void testEveryNarrowedReferenceUsedCorrectlyIsValid() {
        // Guards against false positives: each keyword whose reference is narrowed to one kind of
        // declaration appears here with a valid target, so a wrong narrowing shows up as an error.
        check("""
                project p {
                context c {
                  namespace n {
                    type String
                    type UUID
                    type Boolean
                    annotation Marker { }
                    constraint NotBlank input String { message "Blank" }
                    data-protection Policy { protection none }
                    exception BoomException { message "Boom" }
                    value-object Money base String { String value invariants NotBlank }
                    service HelperService {
                      method check { returns Boolean }
                    }
                    @Marker
                    event CreatedEvent { }
                    event ChangedEvent copies-attributes-of Foo.change { }
                    projection Pj input CreatedEvent, ChangedEvent
                    view V uses Pj { }
                    aggregate-id FooId identifies Foo base UUID { }
                    entity-id BarId identifies Bar base UUID { }
                    aggregate Foo identifier FooId protected-by Policy {
                      business-rule Rule exception BoomException { consistency strong }
                      method change business-rules Rule fires ChangedEvent {
                        String newName preconditions NotBlank
                        operation-context HelperService
                      }
                      method forceChange ref change fires ChangedEvent { }
                    }
                    entity Bar identifier BarId root Foo { }
                    command DoIt target Foo.change { message "Do it" }
                    command-handler Handler handles DoIt uses Foo
                    process-manager PM {
                      process-states { Running }
                      reacts-to CreatedEvent in-state Running {
                        issues-commands DoIt
                        transition-to Running
                      }
                    }
                  }
                }
                }
                """);
    }
}
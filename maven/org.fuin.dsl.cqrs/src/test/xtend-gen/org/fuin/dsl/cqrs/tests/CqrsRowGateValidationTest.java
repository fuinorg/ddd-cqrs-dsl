package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that a row offering a command can answer the rules guarding it.
 * 
 * <p>A menu is drawn on a row, and a command gated by a rule over the aggregate's own state can be left
 * out of it rather than offered and refused. The client decides that from what the row publishes, so a
 * row that offers the command and omits what the gate reads makes the gate work on one screen and
 * quietly do nothing on another - and a gate that silently does not work is indistinguishable from one
 * nobody wrote.
 * 
 * <p>A warning rather than an error on purpose: whether a row publishes what a rule reads is a
 * modelling decision with costs on the other side. What this removes is the silence, not the choice.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsRowGateValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void testARowPublishingWhatItsGateReadsIsFine() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** The thing\'s identifier. */");
      _builder.newLine();
      _builder.append("ThingId id");
      _builder.newLine();
      _builder.newLine();
      _builder.append("/** Whether it is open. */");
      _builder.newLine();
      _builder.append("Boolean open");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(_builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testARowThatCannotAnswerTheGateItOffers() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** The thing\'s identifier. */");
      _builder.newLine();
      _builder.append("ThingId id");
      _builder.newLine();
      this._validationTestHelper.assertWarning(this.parseHelper.parse(this.model(_builder.toString())), CqrsDslPackage.Literals.VALUE_OBJECT, 
        CqrsDslValidator.ROW_CANNOT_ANSWER_GATE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAValueObjectNoViewHandsBackIsNotARow() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context p {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module c.n {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Boolean");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type List generics 1");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate-id ThingId identifies Thing base String { }");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Refused because the thing is closed. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("exception ClosedException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The thing that is closed. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("ThingId thing");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"${thing} is closed\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Makes sure the thing is open. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("business-rule MustBeOpen exception ClosedException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The thing that is closed. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("ThingId thing");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** Whether it is open now. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Boolean open");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("requires open");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate Thing identifier ThingId {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** Whether it is open. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Boolean open");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("method close business-rules MustBeOpen(own-id, own open) fires ClosedEvent {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("event ClosedEvent { message \"Closed\" }");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Points at a thing without being one. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ThingRef {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The thing pointed at. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("ThingId id");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Closes it. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("command CloseThing target Thing.close {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"Close it\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(_builder));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAGateNoClientCouldAnswerIsNotHeldAgainstTheRow() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context p {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module c.n {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Boolean");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type List generics 1");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate-id ThingId identifies Thing base String { }");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Refused because the thing is closed. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("exception ClosedException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The thing that is closed. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("ThingId thing");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"${thing} is closed\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Makes sure the thing is open. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("business-rule MustBeOpen exception ClosedException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The thing that is closed. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("ThingId thing");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** Whether it is open now. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Boolean open");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("requires open");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Answers what an operation cannot answer itself. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("service ThingService {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** Whether the thing is still open. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("method stillOpen {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("ThingId thing");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("returns Boolean");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("projection Things");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("view ThingView uses Things {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("method listThings {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("returns List<ThingRow>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** One thing on screen. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ThingRow {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The thing\'s identifier. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("ThingId id");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate Thing identifier ThingId {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("method close business-rules MustBeOpen(own-id, stillOpen(own-id)) fires ClosedEvent {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("operation-context ThingService");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("event ClosedEvent { message \"Closed\" }");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Closes it. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("command CloseThing target Thing.close {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"Close it\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(_builder));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A row a view hands back, an aggregate with a gated operation, and a command addressing it.
   * 
   * @param rowBody Attributes the row publishes.
   * 
   * @return Model source.
   */
  private String model(final String rowBody) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module c.n {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Boolean");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type List generics 1");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing base String { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Refused because the thing is closed. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception ClosedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** The thing that is closed. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("ThingId thing");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"${thing} is closed\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Makes sure the thing is open. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("business-rule MustBeOpen exception ClosedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** The thing that is closed. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("ThingId thing");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Whether it is open now. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("Boolean open");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("requires open");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("projection Things");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("view ThingView uses Things {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method listThings {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("returns List<ThingRow>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** One thing on screen. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object ThingRow {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append(rowBody, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Whether it is open. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("Boolean open");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method close business-rules MustBeOpen(own-id, own open) fires ClosedEvent {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("event ClosedEvent { message \"Closed\" }");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Closes it. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("command CloseThing target Thing.close {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"Close it\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}

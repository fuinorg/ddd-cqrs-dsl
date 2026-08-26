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
 * Verifies where the carrier's own identity and its own prior state may be handed to a rule.
 * 
 * <p>'own-id' exists because a refusal commonly has to name the thing it refused, and the identity is
 * the one value an aggregate holds without declaring it as an attribute - so there is nothing for a
 * cross reference to point at. 'own' exists for the other half: what the carrier holds now, which a
 * bare name cannot reach when the operation's parameter is named after the field it overwrites.
 * 
 * <p>Neither may be used by a constructor, and for the same reason: it is what brings the identity and
 * the state into being, so there is no "now" to read.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsOwnIdValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void testAnOperationOnAnExistingThingMayNameItsIdentity() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("method close business-rules MustBeOpen(own-id) fires ClosedEvent {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("event ClosedEvent { message \"Closed\" }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.aggregateWith(_builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testACreatingOperationMayNot() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("constructor open business-rules MustBeOpen(own-id) fires OpenedEvent {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("event OpenedEvent { message \"Opened\" }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.aggregateWith(_builder.toString())), CqrsDslPackage.Literals.IDENTITY_ARGUMENT, 
        CqrsDslValidator.RULE_OWN_ID_IN_CONSTRUCTOR);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAnOperationOnAnExistingThingMayNameItsPriorState() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("method rename business-rules MustHaveName(own name) fires RenamedEvent {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("/** The new name, named after the field it overwrites. */");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("String name");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("event RenamedEvent { message \"Renamed\" }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.aggregateWith(_builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testACreatingOperationHasNoPriorState() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("constructor open business-rules MustHaveName(own name) fires OpenedEvent {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("event OpenedEvent { message \"Opened\" }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.aggregateWith(_builder.toString())), CqrsDslPackage.Literals.CARRIER_ATTRIBUTE_ARGUMENT, 
        CqrsDslValidator.RULE_OWN_ATTRIBUTE_IN_CONSTRUCTOR);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private String aggregateWith(final String operation) {
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
    _builder.append("/** The thing being acted on. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("ThingId thing");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("requires thing != null");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Refused because the thing has no name. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception NamelessException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** The name it carries now. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"\'${name}\' is not a name\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Makes sure the thing is named. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("business-rule MustHaveName exception NamelessException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** The name it carries now. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("requires name != null");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** What it is called. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append(operation, "\t\t\t");
    _builder.newLineIfNotEmpty();
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

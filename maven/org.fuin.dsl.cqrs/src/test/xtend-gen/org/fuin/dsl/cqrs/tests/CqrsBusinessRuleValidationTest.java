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
 * Verifies what a business rule and its usage have to agree on.
 * 
 * <p>Both of these used to be found by the generator, or not at all: one as a failure at the far end
 * of a release chain, the other never - the actuals bind positionally and everything in them is a
 * reference, so a miscount usually still resolves.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsBusinessRuleValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void testARuleHoldsWhatItsRefusalHasToName() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** The thing being acted on. */");
      _builder.newLine();
      _builder.append("ThingId thing");
      _builder.newLine();
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("requires thing != null");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(_builder.toString(), "MustBeOpen(own-id)")));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testARefusalNamingSomethingTheRuleDoesNotHold() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Something else entirely. */");
      _builder.newLine();
      _builder.append("ThingId other");
      _builder.newLine();
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("requires other != null");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString(), "MustBeOpen(own-id)")), CqrsDslPackage.Literals.BUSINESS_RULE, 
        CqrsDslValidator.RULE_EXCEPTION_NOT_SUPPLIED);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testARuleThatDeclaresNoConditionIsNotHeldToIt() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("consistency strong");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(_builder.toString(), "MustBeOpen")));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAUsageThatHandsOverTooFew() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** The thing being acted on. */");
      _builder.newLine();
      _builder.append("ThingId thing");
      _builder.newLine();
      _builder.append("/** Whether it is open. */");
      _builder.newLine();
      _builder.append("Boolean open");
      _builder.newLine();
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("requires open");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString(), "MustBeOpen(own-id)")), CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE, 
        CqrsDslValidator.RULE_ACTUALS_MISMATCH);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private String model(final String ruleBody, final String usage) {
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
    _builder.append(ruleBody, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method close business-rules ");
    _builder.append(usage, "\t\t\t");
    _builder.append(" fires ClosedEvent {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t");
    _builder.append("event ClosedEvent { message \"Closed\" }");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
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

package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractBusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance;
import org.fuin.dsl.cqrs.cqrsDsl.Constructor;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Key;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that an operation can say it checks a business key, and what it may say about it.
 * 
 * <p>Before this, a key could be declared and nothing could use one: 'Key' sat outside the vocabulary
 * a 'business-rules' clause references, so naming one did not resolve. A key derives a uniqueness rule,
 * and the model keeps saying <em>which</em> operations check it rather than letting the generator guess
 * - so the usage is written, and it is written where a rule usage is written.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsKeyUsageTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  /**
   * The usual case: the operation names the key and says nothing else about it.
   */
  @Test
  public void testAnOperationNamesAKey() {
    try {
      final DomainModel model = this.parseHelper.parse(this.aggregate("business-rules NamePerKind", "String name"));
      this._validationTestHelper.assertNoErrors(model);
      final BusinessRuleInstance instance = IterableExtensions.<BusinessRuleInstance>head(IterableExtensions.<Constructor>head(IterableExtensions.<Aggregate>head(EcoreUtil2.<Aggregate>getAllContentsOfType(model, Aggregate.class)).getConstructors()).getBusinessRules().getBusinessRuleInstances());
      Assertions.assertFalse(instance.getBusinessRule().eIsProxy(), "the key did not resolve");
      AbstractBusinessRule _businessRule = instance.getBusinessRule();
      Assertions.assertTrue((_businessRule instanceof Key), "did not resolve to a key");
      Assertions.assertEquals("NamePerKind", instance.getBusinessRule().getName());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A model that spells the actuals out spells all of them out: the answer, then every attribute the
   * key is made of, because the refusal names what it refused.
   */
  @Test
  public void testTheActualsMayBeWrittenOut() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("String name");
      _builder.newLine();
      _builder.append("operation-context CreateService");
      _builder.newLine();
      _builder.append("/** Answers whether the key is free. */");
      _builder.newLine();
      _builder.append("service CreateService {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("/** Returns true when something already holds it. */");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("method taken {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** The name asked for. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("String name");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** The kind it must be unique within. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("String kind");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** True when it is taken. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("returns Boolean");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoErrors(this.parseHelper.parse(
        this.aggregate(
          "business-rules NamePerKind(taken(name, kind), name, kind)", _builder)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Half of them is the failure this refuses: positional actuals shift silently.
   */
  @Test
  public void testSomeOfTheActualsIsRefused() {
    try {
      this._validationTestHelper.assertError(this.parseHelper.parse(this.aggregate("business-rules NamePerKind(name)", "String name")), CqrsDslPackage.Literals.BUSINESS_RULE_INSTANCE, 
        CqrsDslValidator.RULE_ACTUALS_MISMATCH);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A key at module level has no type to name attributes of, so it is refused where it parses.
   */
  @Test
  public void testAKeyOutsideATypeIsRefused() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context foo {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module bar {");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Reported when the name is taken. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("exception DuplicateNameException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"Name is already taken\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Nothing here has attributes. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("key Loose exception DuplicateNameException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("attributes name");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("on-collision refuse");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(_builder), CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_OUTSIDE_TYPE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A key that overwrites refuses nobody, so an exception beside it would never be thrown.
   */
  @Test
  public void testACollisionThatDoesNotRefuseHasNothingToThrow() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** No two things of the same kind share a name. */");
      _builder.newLine();
      _builder.append("key NamePerKind exception DuplicateNameException {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("attributes name, kind");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("on-collision overwrite");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.aggregate(null, "String name", _builder)), CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_EXCEPTION_MISMATCH);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * And one that refuses needs the exception that says so.
   */
  @Test
  public void testARefusingKeyNeedsItsException() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** No two things of the same kind share a name. */");
      _builder.newLine();
      _builder.append("key NamePerKind {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("attributes name, kind");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("on-collision refuse");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.aggregate(null, "String name", _builder)), CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_EXCEPTION_MISMATCH);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * 'display-as' may name an attribute the key is not made of. What a person recognises a thing by
   * is not always what makes it unique - an account is keyed by its IBAN and read by its name.
   */
  @Test
  public void testDisplayAsReachesTheWholeType() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** No two things of the same kind share a name. */");
      _builder.newLine();
      _builder.append("key KindOnly exception DuplicateNameException {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("attributes kind");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("on-collision refuse");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("display-as \"${name} (${kind})\"");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoErrors(this.parseHelper.parse(this.aggregate(null, "String name", _builder)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * But not something the type does not have.
   */
  @Test
  public void testDisplayAsCannotNameWhatIsNotThere() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** No two things of the same kind share a name. */");
      _builder.newLine();
      _builder.append("key KindOnly exception DuplicateNameException {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("attributes kind");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("on-collision refuse");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("display-as \"${nickname}\"");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.aggregate(null, "String name", _builder)), CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_DISPLAY_UNKNOWN_VAR);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Two formats are two answers to "what does a picker show", with nothing to choose between them.
   */
  @Test
  public void testATypeIsDisplayedByOneKey() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** No two things of the same kind share a name. */");
      _builder.newLine();
      _builder.append("key NamePerKind exception DuplicateNameException {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("attributes name, kind");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("on-collision refuse");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("display-as \"${name}\"");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("/** No two things share a kind. */");
      _builder.newLine();
      _builder.append("key KindOnly exception DuplicateNameException {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("attributes kind");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("on-collision refuse");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("display-as \"${kind}\"");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.aggregate(null, "String name", _builder)), CqrsDslPackage.Literals.KEY, CqrsDslValidator.KEY_SEVERAL_DISPLAY_KEYS);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private CharSequence aggregate(final String usage, final CharSequence body) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/** No two things of the same kind share a name. */");
    _builder.newLine();
    _builder.append("key NamePerKind exception DuplicateNameException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("attributes name, kind");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("on-collision refuse");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return this.aggregate(usage, body, _builder);
  }

  private CharSequence aggregate(final String usage, final CharSequence body, final CharSequence keys) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Boolean");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Reported when the name is taken. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception DuplicateNameException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"Name is already taken\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String kind");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append(keys, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Creates one. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("constructor create ");
    {
      if ((usage != null)) {
        _builder.append(usage, "\t\t\t");
        _builder.append(" ");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t");
    _builder.append(body, "\t\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}

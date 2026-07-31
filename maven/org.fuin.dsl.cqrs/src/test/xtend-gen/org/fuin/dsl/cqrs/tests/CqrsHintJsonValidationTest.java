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
 * Verifies the hint-JSON validation: a "JpaHint" / "SrcGen4J" hint's JSON is checked against its JSON
 * schema (error), and a "JpaHint" declared outside a view produces a placement warning.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsHintJsonValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void testValidJpaHintInViewHasNoIssues() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("hint JpaHint {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("\"tables\": [");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("{ \"className\": \"Customer\", \"columns\": [ { \"fieldName\": \"id\", \"javaType\": \"java.util.UUID\" } ] }");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("]");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(this.viewWith(_builder.toString()));
      this._validationTestHelper.assertNoIssues(model);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testInvalidJpaHintReportsSchemaError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("hint JpaHint {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("\"tables\": [ { \"className\": \"Customer\", \"bogus\": true } ]");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(this.viewWith(_builder.toString()));
      this._validationTestHelper.assertError(model, CqrsDslPackage.Literals.HINT, CqrsDslValidator.HINT_JSON_SCHEMA_VIOLATION);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testJpaHintOutsideViewWarns() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context p {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("hint JpaHint { \"tables\": [] }");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module c { }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder);
      this._validationTestHelper.assertWarning(model, CqrsDslPackage.Literals.HINT, CqrsDslValidator.JPA_HINT_OUTSIDE_VIEW);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testInvalidSrcGen4JReportsSchemaError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context p {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("hint SrcGen4J { \"types\": [ { \"module\": \"x\" } ] }");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module c { }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder);
      this._validationTestHelper.assertError(model, CqrsDslPackage.Literals.HINT, CqrsDslValidator.HINT_JSON_SCHEMA_VIOLATION);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private String viewWith(final String hint) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module c.n {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("projection Pj");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("view V uses Pj {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append(hint, "\t\t\t");
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

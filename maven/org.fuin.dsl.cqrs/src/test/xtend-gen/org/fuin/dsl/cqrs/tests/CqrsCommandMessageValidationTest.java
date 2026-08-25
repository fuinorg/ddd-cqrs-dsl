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
 * Verifies what a command's message may say.
 * 
 * <p>An event's message is rendered by the JVM alone, so the event check waves complex Jakarta EL
 * through. A command's message is a confirmation prompt the client shows before sending, so it is
 * rendered on both sides - and Dart has no EL engine. These tests pin the intersection: a plain
 * identifier or a dotted path over something the command actually carries.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsCommandMessageValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void testAVariableTheCommandCarriesIsFine() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("String newName");
      _builder.newLine();
      _builder.append("message \"Rename to \'${newName}\'\"");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.commandWith(_builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testTheImplicitEntityIdPathIsAlwaysAvailable() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("message \"Removed ${entityIdPath}\"");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.commandWith(_builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testADottedPathIsFine() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("String provider");
      _builder.newLine();
      _builder.append("message \"Import from ${provider.id}\"");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.commandWith(_builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAVariableTheCommandDoesNotCarryIsAnError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("String newName");
      _builder.newLine();
      _builder.append("message \"Remove \'${name}\'\"");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.commandWith(_builder.toString())), CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_UNKNOWN_VAR);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAMethodCallIsAnError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("String newName");
      _builder.newLine();
      _builder.append("message \"Rename to ${newName.toUpperCase()}\"");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.commandWith(_builder.toString())), CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_NOT_A_PATH);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAnOperatorIsAnError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Integer quantity");
      _builder.newLine();
      _builder.append("Integer price");
      _builder.newLine();
      _builder.append("message \"Total ${quantity * price}\"");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.commandWith(_builder.toString())), CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_NOT_A_PATH);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAnUnclosedVariableIsAnError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("String newName");
      _builder.newLine();
      _builder.append("message \"Rename to ${newName\"");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.commandWith(_builder.toString())), CqrsDslPackage.Literals.COMMAND, CqrsDslValidator.COMMAND_MSG_UNCLOSED_VAR);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAVariableOfTheTargetOperationIsFine() {
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
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate-id AId identifies A base String { }");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate A identifier AId {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("method rename fires RenamedEvent {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("String newName");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("event RenamedEvent { message \"Renamed\" }");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("command RenameCommand target A.rename {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"Rename to \'${newName}\'\"");
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

  private String commandWith(final String body) {
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
    _builder.append("type Integer");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("command C {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append(body, "\t\t\t");
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

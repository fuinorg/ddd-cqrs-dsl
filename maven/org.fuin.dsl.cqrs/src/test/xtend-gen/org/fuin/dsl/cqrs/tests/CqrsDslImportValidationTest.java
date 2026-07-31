package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies the three rules that guard an {@code import} statement.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDslImportValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private ValidationTestHelper validationHelper;

  /**
   * An import that matches neither a context, nor a module, nor a type is an error.
   */
  @Test
  public void unresolvedImportIsAnError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context shop {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module catalog {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import nowhere.at.all.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder);
      this.validationHelper.assertError(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_UNRESOLVED);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * The same import twice in one block is an error.
   */
  @Test
  public void duplicateImportIsAnError() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context shop {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module types {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module catalog {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import shop.types.*");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import shop.types.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ProductName base String {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String value");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder);
      this.validationHelper.assertError(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_DUPLICATE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Repeating an import the context already declares is only a warning.
   */
  @Test
  public void importAlreadyDeclaredByContextIsAWarning() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context shop {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("import shop.types.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module types {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module catalog {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import shop.types.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ProductName base String {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String value");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder);
      this.validationHelper.assertWarning(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_DUPLICATE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * An import nothing refers to is a warning.
   */
  @Test
  public void unusedImportIsAWarning() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context shop {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module types {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module catalog {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import shop.types.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Own");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ProductName base Own {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Own value");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder);
      this.validationHelper.assertWarning(model, CqrsDslPackage.Literals.IMPORT, CqrsDslValidator.IMPORT_UNUSED);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * An import that is actually used raises nothing.
   */
  @Test
  public void usedImportIsNotReported() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context shop {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module types {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module catalog {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import shop.types.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ProductName base String {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String value");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder);
      this.validationHelper.assertNoIssues(model);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

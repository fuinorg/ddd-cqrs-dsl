package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import java.util.List;
import java.util.Objects;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Import;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Assertions;
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

  @Inject
  private CqrsDslValidator validator;

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

  /**
   * "Unused" is a claim about what the block refers to, so it may only be made when everything in the
   * block could be looked at. A name that did not resolve belongs to no import in particular - it may
   * well be the one this import was written for - and calling the import dead weight because of it
   * puts a second marker on somebody else's problem.
   * 
   * <p>This is not a corner case in an IDE: the moment anything leaves a reference unresolved, every
   * import whose only use was that reference turns yellow, which is how a single unresolved type
   * turns into a screenful of warnings.</p>
   */
  @Test
  public void unusedIsNotReportedWhenSomethingInTheBlockDoesNotResolve() {
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
      _builder.append("value-object ProductName {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("NoSuchType value");
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
      final List<Issue> issues = this.validationHelper.validate(model);
      final Function1<Issue, Boolean> _function = (Issue it) -> {
        String _code = it.getCode();
        return Boolean.valueOf((!Objects.equals(_code, CqrsDslValidator.IMPORT_UNUSED)));
      };
      boolean _forall = IterableExtensions.<Issue>forall(issues, _function);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Expected no unused-import warning, but got: ");
      String _join = IterableExtensions.join(issues, ", ");
      _builder_1.append(_join);
      Assertions.assertTrue(_forall, _builder_1.toString());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Xtext links lazily: a cross reference holds a proxy until somebody asks for its target. An import
   * whose only use has not been asked for yet is still used, so the check has to ask - otherwise
   * whether a name happens to have been resolved already, by a hover or a Ctrl-click or the order the
   * editor validated in, decides whether its import is reported.
   */
  @Test
  public void unusedIsNotReportedForAUseThatIsStillUnresolved() {
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
    this.assertUnusedReported(false, _builder);
  }

  /**
   * ... and asking is what keeps the warning working there, rather than silencing it.
   */
  @Test
  public void unusedIsStillReportedWhenNothingHasBeenResolvedYet() {
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
    this.assertUnusedReported(true, _builder);
  }

  /**
   * Runs the check on the second module's import <em>without</em> resolving anything first - the state
   * an editor may well validate in, and the one a test using {@code ValidationTestHelper} never sees,
   * because that resolves every proxy before it validates.
   * 
   * @param expected Whether the import must be reported as unused.
   * @param model Model to check.
   */
  private void assertUnusedReported(final boolean expected, final CharSequence model) {
    try {
      final Import imp = IterableExtensions.<Import>head(IterableExtensions.<Context>head(this.parseHelper.parse(model).getContexts()).getModules().get(1).getImports());
      final BasicDiagnostic diagnostics = new BasicDiagnostic();
      this.validator.validate(imp.eClass(), imp, diagnostics, CollectionLiterals.<Object, Object>newHashMap());
      final Function1<Diagnostic, Boolean> _function = (Diagnostic it) -> {
        return Boolean.valueOf(it.getMessage().contains("is not used"));
      };
      final boolean reported = IterableExtensions.<Diagnostic>exists(diagnostics.getChildren(), _function);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Diagnostics were: ");
      final Function1<Diagnostic, String> _function_1 = (Diagnostic it) -> {
        return it.getMessage();
      };
      String _join = IterableExtensions.join(ListExtensions.<Diagnostic, String>map(diagnostics.getChildren(), _function_1), ", ");
      _builder.append(_join);
      Assertions.assertEquals(Boolean.valueOf(expected), Boolean.valueOf(reported), _builder.toString());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.View;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies the optional 'rest-path' of a method: it is parsed for view methods, rejected everywhere
 * else, and its "{name}" placeholders must be backed by declared parameters.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDslRestPathTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  /**
   * Model with one projection and one view whose methods carry the given body.
   */
  private String model(final String methods) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("project p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("context c {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("namespace n {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("type Integer");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("value-object ItemId {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("String value");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("projection ItemProjection");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("view ItemView uses ItemProjection {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append(methods, "\t\t\t\t");
    _builder.newLineIfNotEmpty();
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

  @Test
  public void restPathIsOptionalAndParsedWhenPresent() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("method findItem rest-path \"/{id}\" {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("ItemId id");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("returns optional ItemId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("method countItems {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("returns Integer");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel m = this.parseHelper.parse(this.model(_builder.toString()));
      Assertions.assertNotNull(m);
      EcoreUtil.resolveAll(m.eResource());
      boolean _isEmpty = m.eResource().getErrors().isEmpty();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Unexpected errors: ");
      String _join = IterableExtensions.join(m.eResource().getErrors(), ", ");
      _builder_1.append(_join);
      Assertions.assertTrue(_isEmpty, _builder_1.toString());
      final View view = IteratorExtensions.<View>head(Iterators.<View>filter(m.eAllContents(), View.class));
      Assertions.assertEquals("/{id}", view.getMethods().get(0).getRestPath());
      Assertions.assertNull(view.getMethods().get(1).getRestPath());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void unknownPathVariableIsRejected() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("method findItem rest-path \"/{unknown}\" {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("ItemId id");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("returns optional ItemId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel m = this.parseHelper.parse(this.model(_builder.toString()));
      this._validationTestHelper.assertError(m, 
        CqrsDslPackage.Literals.METHOD, 
        CqrsDslValidator.REST_PATH_UNKNOWN_VAR);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void restPathOutsideAViewIsRejected() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("project p {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("context c {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("namespace n {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("service ItemService {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("method doSomething rest-path \"/nope\" {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("}");
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
      final DomainModel m = this.parseHelper.parse(_builder);
      this._validationTestHelper.assertError(m, 
        CqrsDslPackage.Literals.METHOD, 
        CqrsDslValidator.REST_PATH_ONLY_ON_VIEW_METHODS);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that a caret ('^') escapes a keyword so it can be used as a plain identifier - both as a
 * simple name (ID) and inside a qualified name / cross reference (FQN).
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDslKeywordEscapeTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Test
  public void caretEscapedKeywordsAreUsableAsIdentifiers() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context ^module {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module ^type.^local {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ^event {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String value");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object ^dependency {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("^event data");
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
      Assertions.assertNotNull(model);
      EcoreUtil.resolveAll(model.eResource());
      final EList<Resource.Diagnostic> errors = model.eResource().getErrors();
      boolean _isEmpty = errors.isEmpty();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Unexpected errors: ");
      String _join = IterableExtensions.join(errors, ", ");
      _builder_1.append(_join);
      Assertions.assertTrue(_isEmpty, _builder_1.toString());
      final Context ctx = model.getContexts().get(0);
      Assertions.assertEquals("module", ctx.getName());
      final org.fuin.dsl.cqrs.cqrsDsl.Module ns = ctx.getModules().get(0);
      Assertions.assertEquals("type.local", ns.getName());
      final Function1<ValueObject, Boolean> _function = (ValueObject it) -> {
        String _name = it.getName();
        return Boolean.valueOf(Objects.equals(_name, "event"));
      };
      final ValueObject event = IterableExtensions.<ValueObject>findFirst(Iterables.<ValueObject>filter(ns.getElements(), ValueObject.class), _function);
      Assertions.assertNotNull(event, "value-object written as \'^event\' must be named \'event\'");
      final Function1<ValueObject, Boolean> _function_1 = (ValueObject it) -> {
        String _name = it.getName();
        return Boolean.valueOf(Objects.equals(_name, "dependency"));
      };
      final ValueObject dep = IterableExtensions.<ValueObject>findFirst(Iterables.<ValueObject>filter(ns.getElements(), ValueObject.class), _function_1);
      Assertions.assertNotNull(dep);
      Assertions.assertSame(event, dep.getAttributes().get(0).getType());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void plainKeywordsStillParseAsKeywords() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context plain {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module m {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Money {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String amount");
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
      Assertions.assertNotNull(model);
      EcoreUtil.resolveAll(model.eResource());
      boolean _isEmpty = model.eResource().getErrors().isEmpty();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Unexpected errors: ");
      String _join = IterableExtensions.join(model.eResource().getErrors(), ", ");
      _builder_1.append(_join);
      Assertions.assertTrue(_isEmpty, _builder_1.toString());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

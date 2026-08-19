package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Method;
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo;
import org.fuin.dsl.cqrs.cqrsDsl.View;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies the UI meta info a client needs to render navigation: on a 'module', on a 'view' and on a
 * 'method'.
 * 
 * <p>A module name is a single lowercase identifier and a view name is a type name, so neither can be
 * turned into a caption by a rule a client could write - "businesspartners" is not "Business partners".
 * The wording therefore belongs in the model, beside the wording every attribute already carries.
 * 
 * <p>All of it is optional: the block is an all-optional {TypeMetaInfo}, so every model written before
 * it existed keeps parsing unchanged. That is what the last test pins.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDslNavigationMetaInfoTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Test
  public void moduleCarriesMetaInfo() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module businesspartners {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("slabel \"Partners\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("label \"Business partners\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("tooltip \"Customers and suppliers this installation trades with\"");
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
    final DomainModel model = this.parse(_builder);
    final org.fuin.dsl.cqrs.cqrsDsl.Module module = model.getContexts().get(0).getModules().get(0);
    Assertions.assertEquals("businesspartners", module.getName());
    Assertions.assertNotNull(module.getMetaInfo(), "a module block must carry its meta info");
    Assertions.assertEquals("Partners", module.getMetaInfo().getSlabel());
    Assertions.assertEquals("Business partners", module.getMetaInfo().getLabel());
    Assertions.assertEquals("Customers and suppliers this installation trades with", module.getMetaInfo().getTooltip());
  }

  @Test
  public void viewAndMethodCarryMetaInfo() {
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
    _builder.append("type Integer");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("projection ItemProjection");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("view ItemView uses ItemProjection {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"Items\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Item list\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Everything currently on the shelf\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method listItems {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("slabel \"All\"");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("label \"All items\"");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("tooltip \"Every item, newest first\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("returns Integer");
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
    final DomainModel model = this.parse(_builder);
    final View view = ((View[])Conversions.unwrapArray((Iterables.<View>filter(model.getContexts().get(0).getModules().get(0).getElements(), View.class)), View.class))[0];
    Assertions.assertNotNull(view.getMetaInfo(), "a view block must carry its meta info");
    Assertions.assertEquals("Items", view.getMetaInfo().getSlabel());
    Assertions.assertEquals("Item list", view.getMetaInfo().getLabel());
    Assertions.assertEquals("Everything currently on the shelf", view.getMetaInfo().getTooltip());
    final Method method = view.getMethods().get(0);
    Assertions.assertNotNull(method.getMetaInfo(), "a method block must carry its meta info");
    Assertions.assertEquals("All", method.getMetaInfo().getSlabel());
    Assertions.assertEquals("All items", method.getMetaInfo().getLabel());
    Assertions.assertEquals("Every item, newest first", method.getMetaInfo().getTooltip());
  }

  @Test
  public void metaInfoIsOptionalEverywhere() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module c.n {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Integer");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("projection ItemProjection");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("view ItemView uses ItemProjection {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method listItems {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("returns Integer");
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
    final DomainModel model = this.parse(_builder);
    final org.fuin.dsl.cqrs.cqrsDsl.Module module = model.getContexts().get(0).getModules().get(0);
    final View view = ((View[])Conversions.unwrapArray((Iterables.<View>filter(module.getElements(), View.class)), View.class))[0];
    TypeMetaInfo _metaInfo = module.getMetaInfo();
    String _label = null;
    if (_metaInfo!=null) {
      _label=_metaInfo.getLabel();
    }
    Assertions.assertNull(_label, "a module with no wording must report none");
    TypeMetaInfo _metaInfo_1 = view.getMetaInfo();
    String _label_1 = null;
    if (_metaInfo_1!=null) {
      _label_1=_metaInfo_1.getLabel();
    }
    Assertions.assertNull(_label_1, "a view with no wording must report none");
    TypeMetaInfo _metaInfo_2 = view.getMethods().get(0).getMetaInfo();
    String _label_2 = null;
    if (_metaInfo_2!=null) {
      _label_2=_metaInfo_2.getLabel();
    }
    Assertions.assertNull(_label_2, "a method with no wording must report none");
  }

  /**
   * Parses the model and fails on any syntax or linking error.
   */
  private DomainModel parse(final CharSequence source) {
    try {
      final DomainModel model = this.parseHelper.parse(source);
      Assertions.assertNotNull(model);
      EcoreUtil.resolveAll(model.eResource());
      final EList<Resource.Diagnostic> errors = model.eResource().getErrors();
      boolean _isEmpty = errors.isEmpty();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Unexpected errors: ");
      String _join = IterableExtensions.join(errors, ", ");
      _builder.append(_join);
      Assertions.assertTrue(_isEmpty, _builder.toString());
      return model;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

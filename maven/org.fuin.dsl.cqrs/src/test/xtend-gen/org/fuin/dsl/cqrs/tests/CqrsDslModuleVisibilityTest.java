package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Type;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that a {@code module} is the unit of visibility: its own elements are reachable by their
 * simple name, everything else needs an {@code import} - even a sibling module of the same context.
 * A fully qualified reference always works, with or without an import.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDslModuleVisibilityTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  /**
   * A module always sees its own elements.
   */
  @Test
  public void ownModuleIsVisible() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context shop {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module catalog {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
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
    this.assertNoErrors(this.parse(_builder));
  }

  /**
   * Without an import a sibling module of the very same context stays out of reach.
   */
  @Test
  public void siblingModuleIsNotVisibleWithoutImport() {
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
    this.assertErrors(this.parse(_builder));
  }

  /**
   * 'context.module.*' makes every type of one module visible.
   */
  @Test
  public void moduleWildcardImportMakesSiblingVisible() {
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
    this.assertNoErrors(this.parse(_builder));
  }

  /**
   * 'context.*' reaches every module of a context, including one with a dotted name.
   */
  @Test
  public void contextWildcardImportReachesDottedModule() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context shop {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module core.types {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module core.catalog {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import shop.*");
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
    this.assertNoErrors(this.parse(_builder));
  }

  /**
   * 'context.module.Type' imports a single type.
   */
  @Test
  public void singleTypeImportMakesOnlyThatTypeVisible() {
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
    _builder.append("import shop.types.String");
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
    this.assertNoErrors(this.parse(_builder));
  }

  /**
   * An import declared on the context applies to every module below it.
   */
  @Test
  public void contextImportIsInheritedByModules() {
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
    this.assertNoErrors(this.parse(_builder));
  }

  /**
   * A fully qualified reference resolves through the global scope, so it needs no import.
   */
  @Test
  public void fullyQualifiedReferenceNeedsNoImport() {
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
    _builder.append("value-object ProductName base shop.types.String {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("shop.types.String value");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    this.assertNoErrors(this.parse(_builder));
  }

  /**
   * The same simple name in two modules is not ambiguous: a module's own declaration wins over an
   * imported one. This is what models that reuse names such as {@code TaxRate} rely on.
   */
  @Test
  public void ownDeclarationShadowsImportedOne() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module journal {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object TaxRate base String {");
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
    _builder.append("\t");
    _builder.append("module receipts {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import p.journal.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object TaxRate base String {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String value");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object Uses {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("TaxRate rate");
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
    this.assertNoErrors(model);
    final Function1<org.fuin.dsl.cqrs.cqrsDsl.Module, Boolean> _function = (org.fuin.dsl.cqrs.cqrsDsl.Module it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equals(_name, "receipts"));
    };
    final org.fuin.dsl.cqrs.cqrsDsl.Module receipts = IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>findFirst(IterableExtensions.<Context>head(model.getContexts()).getModules(), _function);
    final Function1<ValueObject, Boolean> _function_1 = (ValueObject it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equals(_name, "Uses"));
    };
    final ValueObject uses = IterableExtensions.<ValueObject>findFirst(Iterables.<ValueObject>filter(receipts.getElements(), ValueObject.class), _function_1);
    final Type resolved = IterableExtensions.<Attribute>head(uses.getAttributes()).getType();
    Assertions.assertFalse(resolved.eIsProxy(), "TaxRate must resolve");
    Assertions.assertSame(receipts, resolved.eContainer(), 
      "an unqualified name must resolve to the declaration of its own module, not the imported one");
  }

  /**
   * The same, with the module spread over two files: its own declaration must still win over the
   * imported one of the same name.
   * 
   * <p>A module may be split across files - a model that publishes only part of itself has to be -
   * and the half that uses a name is not necessarily the half that declares it. Resolving the two
   * halves against each other only works if the module's own elements shadow its imports no matter
   * which file either of them sits in.</p>
   */
  @Test
  public void ownDeclarationShadowsImportedOneAcrossFiles() {
    try {
      final Path root = Files.createTempDirectory("module-visibility-split");
      final XtextResourceSet resourceSet = this.resourceSetProvider.get();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context p {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module journal {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object TaxRate base String {");
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
      _builder.append("\t");
      _builder.append("module receipts {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object TaxRate base String {");
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
      this.parseHelper.parse(_builder, URI.createFileURI(root.resolve("public.cqrs").toString()), resourceSet);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context p {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("module receipts {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("import p.journal.*");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("value-object Uses {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("TaxRate rate");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final DomainModel other = this.parseHelper.parse(_builder_1, URI.createFileURI(root.resolve("private.cqrs").toString()), resourceSet);
      EcoreUtil.resolveAll(resourceSet);
      final Function1<ValueObject, Boolean> _function = (ValueObject it) -> {
        String _name = it.getName();
        return Boolean.valueOf(Objects.equals(_name, "Uses"));
      };
      final ValueObject uses = IterableExtensions.<ValueObject>findFirst(Iterables.<ValueObject>filter(IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(other.getContexts()).getModules()).getElements(), ValueObject.class), _function);
      final Type resolved = IterableExtensions.<Attribute>head(uses.getAttributes()).getType();
      Assertions.assertFalse(resolved.eIsProxy(), 
        "TaxRate must resolve although the module is split over two files");
      EObject _eContainer = resolved.eContainer();
      Assertions.assertEquals("receipts", ((org.fuin.dsl.cqrs.cqrsDsl.Module) _eContainer).getName(), 
        "an unqualified name must resolve to the declaration of its own module, not the imported one");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private DomainModel parse(final CharSequence text) {
    try {
      final Path root = Files.createTempDirectory("module-visibility");
      final URI uri = URI.createFileURI(root.resolve("model.cqrs").toString());
      return this.parseHelper.parse(text, uri, this.resourceSetProvider.get());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void assertNoErrors(final DomainModel model) {
    EcoreUtil.resolveAll(model.eResource());
    boolean _isEmpty = model.eResource().getErrors().isEmpty();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Unexpected errors: ");
    String _join = IterableExtensions.join(model.eResource().getErrors(), ", ");
    _builder.append(_join);
    Assertions.assertTrue(_isEmpty, _builder.toString());
  }

  private void assertErrors(final DomainModel model) {
    EcoreUtil.resolveAll(model.eResource());
    Assertions.assertFalse(model.eResource().getErrors().isEmpty(), 
      "Expected the reference to fail because nothing imports it");
  }
}

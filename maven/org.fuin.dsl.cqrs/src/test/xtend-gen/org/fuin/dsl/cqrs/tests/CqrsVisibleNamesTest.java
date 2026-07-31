package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.Command;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.scoping.CqrsVisibleNames;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies what content assist may propose. The scope a reference resolves against is wider than
 * what a module reaches directly - it also carries container relative names and, through the global
 * scope, every element of the workspace by its fully qualified name - so proposing all of it would
 * dump the whole workspace into the completion list.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsVisibleNamesTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  @Inject
  private IScopeProvider scopeProvider;

  /**
   * Only the module's own types - a sibling module and another file stay out.
   */
  @Test
  public void offersOnlyOwnModuleWithoutImports() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module vo.m {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Integer");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object Money {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("Integer amount");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module vo.sibling {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type SiblingType");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parseWithNeighbour(_builder);
    Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("Integer", "Money", "String")), 
      this.proposals(this.firstAttribute(model), CqrsDslPackage.Literals.VARIABLE__TYPE));
  }

  /**
   * An imported module's types join the list, each exactly once.
   */
  @Test
  public void offersImportedTypesOnce() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module vo.m {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import ctx.vo.sibling.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Integer");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object Money {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("Integer amount");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module vo.sibling {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type SiblingType");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parseWithNeighbour(_builder);
    Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("Integer", "Money", "SiblingType")), 
      this.proposals(this.firstAttribute(model), CqrsDslPackage.Literals.VARIABLE__TYPE));
  }

  /**
   * A context wide wildcard reaches every module below it.
   */
  @Test
  public void contextWildcardOffersEveryModule() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module vo.m {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import ctx.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Integer");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object Money {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("Integer amount");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module vo.sibling {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type SiblingType");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parseWithNeighbour(_builder);
    Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("Integer", "Money", "SiblingType")), 
      this.proposals(this.firstAttribute(model), CqrsDslPackage.Literals.VARIABLE__TYPE));
  }

  /**
   * A name nested deeper than the module stays proposable - it is still relative to it.
   */
  @Test
  public void offersNamesNestedInsideTheModule() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module vo.m {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type UUID");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id OrderId identifies Order base UUID {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("examples \"6bc75dd5-be5b-4c57-977e-8ee404b21c74\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Order identifier OrderId {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method rename {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("String newName");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("command RenameCommand target Order.rename {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("String newName");
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
    final DomainModel model = this.parseWithNeighbour(_builder);
    final Command command = IteratorExtensions.<Command>head(Iterators.<Command>filter(model.eAllContents(), Command.class));
    Assertions.assertTrue(this.proposals(command, CqrsDslPackage.Literals.COMMAND__TARGET).contains("Order.rename"), 
      "a method nested in an aggregate of the same module must be proposable");
  }

  private EObject firstAttribute(final DomainModel model) {
    Attribute _head = IteratorExtensions.<Attribute>head(Iterators.<Attribute>filter(model.eAllContents(), Attribute.class));
    return ((EObject) _head);
  }

  /**
   * The names {@link CqrsVisibleNames} lets through, sorted.
   */
  private List<String> proposals(final EObject context, final EReference reference) {
    final Function1<IEObjectDescription, Boolean> _function = (IEObjectDescription it) -> {
      return Boolean.valueOf(CqrsVisibleNames.isAddressable(context, it));
    };
    final Function1<IEObjectDescription, String> _function_1 = (IEObjectDescription it) -> {
      return it.getName().toString();
    };
    return IterableExtensions.<String>toList(IterableExtensions.<String>sort(IterableExtensions.<IEObjectDescription, String>map(IterableExtensions.<IEObjectDescription>filter(this.scopeProvider.getScope(context, reference).getAllElements(), _function), _function_1)));
  }

  /**
   * Parses the model with an unrelated second file in the same resource set.
   */
  private DomainModel parseWithNeighbour(final CharSequence text) {
    try {
      final Path root = Files.createTempDirectory("visible-names");
      final XtextResourceSet rs = this.resourceSetProvider.get();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context other_ctx {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module far.away {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type ShouldNotBeOffered");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this.parseHelper.parse(_builder, URI.createFileURI(root.resolve("other.cqrs").toString()), rs);
      final DomainModel model = this.parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString()), rs);
      EcoreUtil.resolveAll(model.eResource());
      return model;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

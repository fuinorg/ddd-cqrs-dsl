package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.scoping.CqrsImportProposals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies what content assist offers after an {@code import}.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsImportProposalsTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  @Inject
  private CqrsImportProposals testee;

  /**
   * Contexts and modules as wildcards, single types by name - the own module left out.
   */
  @Test
  public void offersReachableContextsModulesAndTypes() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module types {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Money");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module use {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Own");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parse(_builder);
    final List<String> candidates = this.testee.candidates(this.moduleNamed(model, "use"));
    Assertions.assertTrue(candidates.contains("ctx.*"), ("context wildcard: " + candidates));
    Assertions.assertTrue(candidates.contains("ctx.types.*"), ("module wildcard: " + candidates));
    Assertions.assertTrue(candidates.contains("ctx.types.Money"), ("single type: " + candidates));
    Assertions.assertFalse(candidates.contains("ctx.use.*"), ("own module: " + candidates));
    Assertions.assertFalse(candidates.contains("ctx.use.Own"), ("own type: " + candidates));
  }

  /**
   * Another file - a dependency model lands in the index the same way - is reachable too.
   */
  @Test
  public void offersNamesOfAnotherFile() {
    try {
      final Path root = Files.createTempDirectory("import-proposals");
      final XtextResourceSet rs = this.resourceSetProvider.get();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context other {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module far {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type FarAway");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this.parseHelper.parse(_builder, URI.createFileURI(root.resolve("other.cqrs").toString()), rs);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context ctx {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("module use {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("type Own");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final DomainModel model = this.parseHelper.parse(_builder_1, URI.createFileURI(root.resolve("main.cqrs").toString()), rs);
      EcoreUtil.resolveAll(model.eResource());
      final List<String> candidates = this.testee.candidates(this.moduleNamed(model, "use"));
      Assertions.assertTrue(candidates.contains("other.*"), candidates.toString());
      Assertions.assertTrue(candidates.contains("other.far.*"), candidates.toString());
      Assertions.assertTrue(candidates.contains("other.far.FarAway"), candidates.toString());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * What is already imported is not offered again.
   */
  @Test
  public void doesNotRepeatWhatIsAlreadyImported() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module types {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Money");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module use {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import ctx.types.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Own");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parse(_builder);
    final List<String> candidates = this.testee.candidates(this.moduleNamed(model, "use"));
    Assertions.assertFalse(candidates.contains("ctx.types.*"), ("already imported: " + candidates));
    Assertions.assertTrue(candidates.contains("ctx.types.Money"), 
      ("a single type of it may still be narrowed to: " + candidates));
  }

  /**
   * A module named 'a.b' is not the owner of a module named 'a.b.c'.
   */
  @Test
  public void keepsDottedModuleNamesApart() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module a.b {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type InB");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module a.b.c {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type InC");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parse(_builder);
    final List<String> candidates = this.testee.candidates(this.moduleNamed(model, "a.b"));
    Assertions.assertFalse(candidates.contains("ctx.a.b.InB"), ("own type: " + candidates));
    Assertions.assertTrue(candidates.contains("ctx.a.b.c.InC"), 
      ("a type of the deeper module is not its own: " + candidates));
  }

  private org.fuin.dsl.cqrs.cqrsDsl.Module moduleNamed(final DomainModel model, final String name) {
    final Function1<org.fuin.dsl.cqrs.cqrsDsl.Module, Boolean> _function = (org.fuin.dsl.cqrs.cqrsDsl.Module it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equals(_name, name));
    };
    return IteratorExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>findFirst(Iterators.<org.fuin.dsl.cqrs.cqrsDsl.Module>filter(model.eAllContents(), org.fuin.dsl.cqrs.cqrsDsl.Module.class), _function);
  }

  private DomainModel parse(final CharSequence text) {
    try {
      final Path root = Files.createTempDirectory("import-proposals");
      final DomainModel model = this.parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString()), 
        this.resourceSetProvider.get());
      EcoreUtil.resolveAll(model.eResource());
      return model;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

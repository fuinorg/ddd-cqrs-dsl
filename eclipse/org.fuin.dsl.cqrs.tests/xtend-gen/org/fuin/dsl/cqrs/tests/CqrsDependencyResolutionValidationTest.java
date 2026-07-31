package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolver;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers;
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * A declared <code>dependency</code> that cannot be resolved is reported on its coordinate. Without
 * it the only symptom is every type the artifact provides failing to resolve, which points at the
 * models rather than at the declaration that is actually wrong.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDependencyResolutionValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  @Inject
  private ValidationTestHelper validationHelper;

  @Inject
  private CqrsModelArchives archives;

  @BeforeEach
  public void forgetPreviousResolutions() {
    this.archives.invalidate();
  }

  @AfterEach
  public void restoreResolver() {
    CqrsArtifactResolvers.set(null);
  }

  /**
   * A 'local' directory that is not there cannot provide anything.
   */
  @Test
  public void missingLocalDirectoryIsAnError() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("dependency \"org.acme:no-such-model:1.0.0\" local \"no-such-dir\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module m {");
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
    this.validationHelper.assertError(model, CqrsDslPackage.Literals.DEPENDENCY, 
      CqrsDslValidator.DEPENDENCY_UNRESOLVED);
  }

  /**
   * A 'local' directory holding models resolves, so nothing is reported.
   */
  @Test
  public void resolvableLocalDirectoryIsNotAnError() {
    try {
      final Path root = Files.createTempDirectory("dependency-resolution");
      final Path provider = Files.createDirectories(root.resolve("provider"));
      Files.writeString(provider.resolve("provided.cqrs"), "context remote { module r { type Money } }");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context ctx {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"org.acme:provided-model:1.0.0\" local \"provider\"");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module m {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import remote.r.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Price {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Money amount");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(_builder, URI.createFileURI(root.resolve("main.cqrs").toString()), this.resourceSetProvider.get());
      this.validationHelper.assertNoError(model, CqrsDslValidator.DEPENDENCY_UNRESOLVED);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * An artifact Maven cannot resolve is reported on the coordinate, not swallowed.
   */
  @Test
  public void unresolvableArtifactIsAnError() {
    final CqrsArtifactResolver _function = (String groupId, String artifactId, String version) -> {
      throw new IllegalStateException(("Could not find artifact " + artifactId));
    };
    CqrsArtifactResolvers.set(_function);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context ctx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("dependency \"org.acme:unresolvable-model:1.0.0\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module m {");
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
    this.validationHelper.assertError(model, CqrsDslPackage.Literals.DEPENDENCY, 
      CqrsDslValidator.DEPENDENCY_UNRESOLVED);
  }

  private DomainModel parse(final CharSequence text) {
    try {
      final Path root = Files.createTempDirectory("dependency-resolution");
      return this.parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString()), 
        this.resourceSetProvider.get());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

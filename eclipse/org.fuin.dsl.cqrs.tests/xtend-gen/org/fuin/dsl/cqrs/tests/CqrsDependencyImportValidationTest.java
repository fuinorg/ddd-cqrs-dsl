package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Type;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolver;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers;
import org.fuin.dsl.cqrs.scoping.CqrsImportProposals;
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * What a <code>dependency</code> provides must be visible to everything that answers "does this name
 * exist" - not only to the scope that resolves the reference.
 * 
 * <p>These tests run on an index that hides the models read out of an archive, which is what an IDE's
 * index does (see {@link WorkspaceOnlyIndexInjectorProvider}). Without that the default headless setup
 * has the dependency's models in the index as well, the two pools agree by accident, and the bug this
 * covers - an <code>import</code> marked as unresolvable while the very types it provides resolve -
 * cannot happen at all.</p>
 * 
 * <p>The artifact is a zip written by the test and handed over by a stub resolver, the same way
 * {@link RemoteScopeResolutionTest} does it.</p>
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(WorkspaceOnlyIndexInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDependencyImportValidationTest {
  private static final String COORDINATE = "org.fuin.test:cqrs-model:1.0.0";

  private static final String REMOTE_BILLING = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context remote {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.billing {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Money");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      return _builder.toString();
    }
  }.apply();

  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  @Inject
  private ValidationTestHelper validationHelper;

  @Inject
  private CqrsModelArchives archives;

  @Inject
  private CqrsImportProposals importProposals;

  @BeforeEach
  public void forgetPreviousResolutions() {
    this.archives.invalidate();
  }

  @AfterEach
  public void restoreResolver() {
    CqrsArtifactResolvers.set(null);
  }

  /**
   * The coordinate is declared on the context in one file, the import written in another - the way a
   * model that publishes only part of itself is laid out. Neither the import nor the type it provides
   * may be reported.
   */
  @Test
  public void moduleImportOfADependencyIsNotReported() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context consumer {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module com.acme.sales {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import remote.com.acme.billing.*");
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
    final DomainModel using = this.twoFileModel(_builder);
    this.assertTypeResolves(using, "Money");
    this.assertNoIssues(using);
  }

  /**
   * A context level import reaches the modules below it. Those have to be expanded into one resolver
   * each, and for a dependency they can only come from the artifact - so this fails where the module
   * level import above still works.
   */
  @Test
  public void contextImportOfADependencyResolvesAndIsNotReported() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context consumer {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module com.acme.sales {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import remote.*");
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
    final DomainModel using = this.twoFileModel(_builder);
    this.assertTypeResolves(using, "Money");
    this.assertNoIssues(using);
  }

  /**
   * An import that really matches nothing is still reported - the fallback must not swallow that.
   */
  @Test
  public void importMatchingNothingIsStillReported() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context consumer {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module com.acme.sales {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import remote.com.acme.shipping.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object Price {");
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
    final DomainModel using = this.twoFileModel(_builder);
    final List<Issue> issues = this.validationHelper.validate(using);
    final Function1<Issue, Boolean> _function = (Issue it) -> {
      String _code = it.getCode();
      return Boolean.valueOf(Objects.equals(_code, CqrsDslValidator.IMPORT_UNRESOLVED));
    };
    boolean _exists = IterableExtensions.<Issue>exists(issues, _function);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("Expected an ");
    _builder_1.append(CqrsDslValidator.IMPORT_UNRESOLVED);
    _builder_1.append(" issue, but got: ");
    String _join = IterableExtensions.join(issues, ", ");
    _builder_1.append(_join);
    Assertions.assertTrue(_exists, _builder_1.toString());
  }

  /**
   * What may be written after 'import' includes what the dependency provides.
   */
  @Test
  public void contentAssistProposesWhatTheDependencyProvides() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context consumer {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module com.acme.sales {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel using = this.twoFileModel(_builder);
    EcoreUtil.resolveAll(using.eResource().getResourceSet());
    final List<String> candidates = this.importProposals.candidates(IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(using.getContexts()).getModules()));
    Assertions.assertTrue(candidates.contains("remote.*"), 
      ("the dependency\'s context must be proposable, but was: " + candidates));
    Assertions.assertTrue(candidates.contains("remote.com.acme.billing.*"), 
      ("the dependency\'s module must be proposable, but was: " + candidates));
    Assertions.assertTrue(candidates.contains("remote.com.acme.billing.Money"), 
      ("the dependency\'s type must be proposable, but was: " + candidates));
  }

  /**
   * A model read out of an archive is not ours to report on: it belongs to another project, it is
   * opened read-only, and a marker on it would name a problem the reader cannot fix. The model used
   * here carries an import that matches nothing, so without the guard it would be reported.
   */
  @Test
  public void aModelReadOutOfAnArchiveIsNotReportedOn() {
    try {
      final Path root = Files.createTempDirectory("dependency-import-archive");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context remote {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.billing {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import nothing.of.the.sort.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Money");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      String _string = _builder.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("model/public/money.cqrs", _string);
      this.installResolver(root, Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo)));
      XtextResourceSet _get = this.resourceSetProvider.get();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context consumer {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("dependency \"");
      _builder_1.append(CqrsDependencyImportValidationTest.COORDINATE, "\t");
      _builder_1.append("\"");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("module com.acme.sales {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("import remote.com.acme.billing.*");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("value-object Price {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("Money amount");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final DomainModel using = this.parse(root, "main.cqrs", _get, _builder_1);
      EcoreUtil.resolveAll(using.eResource().getResourceSet());
      final Function1<Resource, Boolean> _function = (Resource it) -> {
        return Boolean.valueOf(CqrsModelArchives.isArchived(it.getURI()));
      };
      final Resource archived = IterableExtensions.<Resource>findFirst(using.eResource().getResourceSet().getResources(), _function);
      Assertions.assertNotNull(archived, "the dependency\'s model must have been read out of the archive");
      this.assertNoIssues(IterableExtensions.<EObject>head(archived.getContents()));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * The declaration of the dependency and the model using it, in two files of one context - the
   * split a model that publishes only part of itself has to make.
   * 
   * @param using Content of the second file.
   * 
   * @return Model of the second file.
   */
  private DomainModel twoFileModel(final CharSequence using) {
    try {
      final Path root = Files.createTempDirectory("dependency-import");
      String _string = CqrsDependencyImportValidationTest.REMOTE_BILLING.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("model/public/money.cqrs", _string);
      this.installResolver(root, Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo)));
      final XtextResourceSet resourceSet = this.resourceSetProvider.get();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(CqrsDependencyImportValidationTest.COORDINATE, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      this.parse(root, "declaration.cqrs", resourceSet, _builder);
      return this.parse(root, "usage.cqrs", resourceSet, using);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Nothing at all is reported for the given model. {@code ValidationTestHelper} would do this too,
   * but it fails through JUnit 4, which the headless test run does not put on the class path.
   */
  private void assertNoIssues(final EObject model) {
    final List<Issue> issues = this.validationHelper.validate(model);
    boolean _isEmpty = issues.isEmpty();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Unexpected issues: ");
    String _join = IterableExtensions.join(issues, ", ");
    _builder.append(_join);
    Assertions.assertTrue(_isEmpty, _builder.toString());
  }

  private void assertTypeResolves(final DomainModel model, final String typeName) {
    EcoreUtil.resolveAll(model.eResource().getResourceSet());
    final Type type = IterableExtensions.<Attribute>head(IterableExtensions.<ValueObject>head(Iterables.<ValueObject>filter(IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(model.getContexts()).getModules()).getElements(), ValueObject.class)).getAttributes()).getType();
    Assertions.assertFalse(type.eIsProxy(), (typeName + " must resolve against the dependency"));
    Assertions.assertEquals(typeName, type.getName());
  }

  /**
   * Writes a zip with the given entries and installs a resolver that answers with it.
   */
  private void installResolver(final Path root, final Map<String, String> entries) {
    try {
      final Path archive = root.resolve("cqrs-model-1.0.0.zip");
      Files.write(archive, this.zip(entries));
      final CqrsArtifactResolver _function = (String groupId, String artifactId, String version) -> {
        return archive;
      };
      CqrsArtifactResolvers.set(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private byte[] zip(final Map<String, String> entries) {
    try {
      final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      final ZipOutputStream zip = new ZipOutputStream(bytes);
      Set<Map.Entry<String, String>> _entrySet = entries.entrySet();
      for (final Map.Entry<String, String> entry : _entrySet) {
        {
          String _key = entry.getKey();
          ZipEntry _zipEntry = new ZipEntry(_key);
          zip.putNextEntry(_zipEntry);
          zip.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
          zip.closeEntry();
        }
      }
      zip.close();
      return bytes.toByteArray();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private DomainModel parse(final Path root, final String fileName, final XtextResourceSet resourceSet, final CharSequence text) {
    try {
      return this.parseHelper.parse(text, URI.createFileURI(root.resolve(fileName).toString()), resourceSet);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

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
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType;
import org.fuin.dsl.cqrs.cqrsDsl.Type;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolver;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers;
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that cross-references resolve against the models of a declared {@code dependency}: an
 * artifact resolved by Maven and read <em>inside</em> its jar in the local repository, and a
 * {@code local} directory read directly.
 * 
 * <p>The artifact is a jar written by the test and handed over by a stub resolver, so this covers the
 * reading half only - that {@code MimaArtifactResolverTest} really resolves through Maven is verified
 * separately.</p>
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class RemoteScopeResolutionTest {
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

  private static final String REMOTE_CATALOG = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context remote {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.catalog {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Sku");
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
  private CqrsModelArchives archives;

  /**
   * What an artifact resolved to is remembered for the session, and every test here publishes the
   * same coordinate into a temp repository of its own - so the memory has to be dropped in between,
   * or the second test would see the first one's jar.
   */
  @BeforeEach
  public void forgetPreviousResolutions() {
    this.archives.invalidate();
  }

  @AfterEach
  public void restoreResolver() {
    CqrsArtifactResolvers.set(null);
  }

  /**
   * The artifact is resolved by Maven and its models are read straight out of the jar - the URIs of
   * the resolved types point <em>inside</em> the archive, nothing is unpacked.
   */
  @Test
  public void resolvesFromInsideTheArtifactJar() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-maven");
      String _string = RemoteScopeResolutionTest.REMOTE_BILLING.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("model/money.cqrs", _string);
      String _string_1 = RemoteScopeResolutionTest.REMOTE_CATALOG.toString();
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("model/sub/sku.cqrs", _string_1);
      this.installResolver(root, Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1)));
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(RemoteScopeResolutionTest.COORDINATE, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.sales {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import remote.com.acme.billing.*");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import remote.com.acme.catalog.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Price {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Money amount");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Sku item");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parse(root, _builder);
      EcoreUtil.resolveAll(model.eResource());
      boolean _isEmpty = model.eResource().getErrors().isEmpty();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Unexpected errors: ");
      String _join = IterableExtensions.join(model.eResource().getErrors(), ", ");
      _builder_1.append(_join);
      Assertions.assertTrue(_isEmpty, _builder_1.toString());
      final Function1<Attribute, Type> _function = (Attribute it) -> {
        return it.getType();
      };
      final List<Type> attributeTypes = ListExtensions.<Attribute, Type>map(IterableExtensions.<ValueObject>head(Iterables.<ValueObject>filter(IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(model.getContexts()).getModules()).getElements(), ValueObject.class)).getAttributes(), _function);
      final Function1<Type, Boolean> _function_1 = (Type it) -> {
        return Boolean.valueOf(((it instanceof ExternalType) && (!it.eIsProxy())));
      };
      Assertions.assertTrue(IterableExtensions.<Type>forall(attributeTypes, _function_1), 
        "both types of the artifact must be resolved");
      final Function1<Type, String> _function_2 = (Type it) -> {
        return it.getName();
      };
      Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("Money", "Sku")), IterableExtensions.<String>sort(ListExtensions.<Type, String>map(attributeTypes, _function_2)));
      for (final Type type : attributeTypes) {
        {
          final String uri = type.eResource().getURI().toString();
          Assertions.assertTrue(uri.startsWith("archive:"), 
            ("a model of a dependency must be read from inside the jar, but was: " + uri));
          Assertions.assertTrue(uri.contains("!/model/"), 
            ("only models below \'model/\' are read, but was: " + uri));
        }
      }
      Assertions.assertFalse(Files.exists(root.resolve(".dependencies-cache")), 
        "nothing may be unpacked next to the model any more");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A model in a sub folder of the jar is found too - entries are read recursively.
   */
  @Test
  public void readsModelsFromSubFoldersOfTheJar() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-nested");
      String _string = RemoteScopeResolutionTest.REMOTE_CATALOG.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("model/sub/sku.cqrs", _string);
      this.installResolver(root, Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo)));
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(RemoteScopeResolutionTest.COORDINATE, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.sales {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import remote.com.acme.catalog.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Price {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Sku item");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this.assertResolves(this.parse(root, _builder), "Sku");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Entries outside 'model/' are not models and must be ignored.
   */
  @Test
  public void ignoresEntriesOutsideTheModelFolder() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-outside");
      String _string = RemoteScopeResolutionTest.REMOTE_BILLING.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("other/money.cqrs", _string);
      this.installResolver(root, Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo)));
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(RemoteScopeResolutionTest.COORDINATE, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.sales {");
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
      final DomainModel model = this.parse(root, _builder);
      EcoreUtil.resolveAll(model.eResource());
      Assertions.assertFalse(model.eResource().getErrors().isEmpty(), 
        "a \'.cqrs\' outside \'model/\' must not be picked up");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A {@code local} directory is read directly, with no resolution at all.
   */
  @Test
  public void readsLocalDirectoryDirectly() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-local");
      final Path localDir = Files.createDirectories(root.resolve("provider"));
      Files.writeString(localDir.resolve("billing.cqrs"), RemoteScopeResolutionTest.REMOTE_BILLING.toString());
      final CqrsArtifactResolver _function = (String groupId, String artifactId, String version) -> {
        throw new IllegalStateException("must not resolve when \'local\' is declared");
      };
      CqrsArtifactResolvers.set(_function);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(RemoteScopeResolutionTest.COORDINATE, "\t");
      _builder.append("\" local \"provider\"");
      _builder.newLineIfNotEmpty();
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
      this.assertResolves(this.parse(root, _builder), "Money");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A context may be split across files, and a dependency declared on it applies to all of them: the
   * 'dependency' sits in one file, the module importing the artifact's types in another.
   */
  @Test
  public void contextDependencyAppliesToTheOtherFilesOfTheContext() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-split");
      String _string = RemoteScopeResolutionTest.REMOTE_BILLING.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("model/money.cqrs", _string);
      this.installResolver(root, Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo)));
      final XtextResourceSet resourceSet = this.resourceSetProvider.get();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(RemoteScopeResolutionTest.COORDINATE, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      this.parseHelper.parse(_builder, URI.createFileURI(root.resolve("aaa.cqrs").toString()), resourceSet);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context consumer {");
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
      final DomainModel model = this.parseHelper.parse(_builder_1, URI.createFileURI(root.resolve("sales.cqrs").toString()), resourceSet);
      this.assertResolves(model, "Money");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Without a dependency the artifact's types stay unresolved.
   */
  @Test
  public void fallsBackWithoutDependency() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-none");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.sales {");
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
      final DomainModel model = this.parse(root, _builder);
      EcoreUtil.resolveAll(model.eResource());
      Assertions.assertFalse(model.eResource().getErrors().isEmpty(), 
        "reference to a type of another project must stay unresolved without a dependency");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Writes a jar with the given entries and installs a resolver that answers with it.
   * 
   * <p>Which Maven does the resolving is beside the point here - that is
   * {@code MimaArtifactResolverTest} - so a stub keeps this test free of any environment and lets the
   * Eclipse tests bundle, which resolves through m2e, run exactly the same code.</p>
   */
  private void installResolver(final Path root, final Map<String, String> entries) {
    try {
      final Path jar = root.resolve("cqrs-model-1.0.0.jar");
      Files.write(jar, this.jar(entries));
      final CqrsArtifactResolver _function = (String groupId, String artifactId, String version) -> {
        return jar;
      };
      CqrsArtifactResolvers.set(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A jar holding the given entries (path inside the archive to content).
   */
  private byte[] jar(final Map<String, String> entries) {
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

  private DomainModel parse(final Path root, final CharSequence text) {
    try {
      return this.parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString()), 
        this.resourceSetProvider.get());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void assertResolves(final DomainModel model, final String typeName) {
    EcoreUtil.resolveAll(model.eResource());
    boolean _isEmpty = model.eResource().getErrors().isEmpty();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Unexpected errors: ");
    String _join = IterableExtensions.join(model.eResource().getErrors(), ", ");
    _builder.append(_join);
    Assertions.assertTrue(_isEmpty, _builder.toString());
    final Type type = IterableExtensions.<Attribute>head(IterableExtensions.<ValueObject>head(Iterables.<ValueObject>filter(IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(model.getContexts()).getModules()).getElements(), ValueObject.class)).getAttributes()).getType();
    Assertions.assertFalse(type.eIsProxy(), (typeName + " must resolve"));
    Assertions.assertEquals(typeName, type.getName());
  }
}

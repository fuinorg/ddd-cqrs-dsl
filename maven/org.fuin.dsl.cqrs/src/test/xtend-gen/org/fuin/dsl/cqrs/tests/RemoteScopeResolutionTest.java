package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
import org.fuin.dsl.cqrs.cqrsDsl.Namespace;
import org.fuin.dsl.cqrs.cqrsDsl.Type;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that cross-references are resolved against remote {@code .cqrs} models declared in the
 * {@code dependencies.json} catalog: a {@code maven} artifact (tar.gz) cached once per GAV under the
 * {@code .dependencies-cache} directory, and a {@code local} directory that is read directly.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class RemoteScopeResolutionTest {
  private static final String REMOTE_BILLING = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context com.acme {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("namespace billing {");
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
      _builder.append("context com.acme {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("namespace catalog {");
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

  private static final String LOCAL_SALES = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context com.acme.sales {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("namespace sales {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import com.acme.billing.*");
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
      return _builder.toString();
    }
  }.apply();

  /**
   * Imports two namespaces that a single catalog entry provides from one source.
   */
  private static final String LOCAL_SALES_BOTH = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context com.acme.sales {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("namespace sales {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import com.acme.billing.*");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import com.acme.catalog.*");
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
      return _builder.toString();
    }
  }.apply();

  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  /**
   * Fetches a {@code maven} artifact (tar.gz) over HTTP, unpacks all models and resolves types from
   * <em>two</em> namespaces declared by a single catalog entry. The artifact is cached once per GAV,
   * so both namespaces share a single <code>&lt;artifactId&gt;-&lt;version&gt;-&lt;sha1&gt;</code> dir.
   */
  @Test
  public void resolvesMavenArtifactOverHttpAndCaches() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-maven");
      String _string = RemoteScopeResolutionTest.REMOTE_BILLING.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("money.cqrs", _string);
      String _string_1 = RemoteScopeResolutionTest.REMOTE_CATALOG.toString();
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("sku.cqrs", _string_1);
      final byte[] tarGz = TarGzTestSupport.tarGz(
        Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1)));
      final String dir = "/org/fuin/test/cqrs-model/0.1.0-SNAPSHOT/";
      byte[] _bytes = RemoteScopeResolutionTest.mavenMetadata().getBytes(StandardCharsets.UTF_8);
      Pair<String, byte[]> _mappedTo_2 = Pair.<String, byte[]>of((dir + "maven-metadata.xml"), _bytes);
      Pair<String, byte[]> _mappedTo_3 = Pair.<String, byte[]>of((dir + "cqrs-model-0.1.0-20240101.000000-1-cqrs.tar.gz"), tarGz);
      final HttpServer server = RemoteScopeResolutionTest.serve(
        Collections.<String, byte[]>unmodifiableMap(CollectionLiterals.<String, byte[]>newHashMap(_mappedTo_2, _mappedTo_3)));
      server.start();
      final Path emptyLocalRepo = Files.createTempDirectory("m2-empty");
      int _port = server.getAddress().getPort();
      String _plus = ("http://127.0.0.1:" + Integer.valueOf(_port));
      String _plus_1 = (_plus + "/");
      System.setProperty("cqrs.maven.repo.snapshots", _plus_1);
      System.setProperty("maven.repo.local", emptyLocalRepo.toString());
      try {
        Path _resolve = root.resolve("dependencies.json");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[ { \"type\": \"maven\", \"namespaces\": [\"com.acme.billing\", \"com.acme.catalog\"], \"data\": {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\"groupId\": \"org.fuin.test\", \"artifactId\": \"cqrs-model\", \"version\": \"0.1.0-SNAPSHOT\" } } ]");
        _builder.newLine();
        Files.writeString(_resolve, _builder);
        final DomainModel model = this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES_BOTH);
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
        final List<Type> attributeTypes = ListExtensions.<Attribute, Type>map(IterableExtensions.<ValueObject>head(Iterables.<ValueObject>filter(IterableExtensions.<Namespace>head(IterableExtensions.<Context>head(model.getContexts()).getNamespaces()).getElements(), ValueObject.class)).getAttributes(), _function);
        final Function1<Type, Boolean> _function_1 = (Type it) -> {
          return Boolean.valueOf(((it instanceof ExternalType) && (!it.eIsProxy())));
        };
        Assertions.assertTrue(IterableExtensions.<Type>forall(attributeTypes, _function_1), 
          "both remote types must be resolved");
        final Function1<Type, String> _function_2 = (Type it) -> {
          return it.getName();
        };
        Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("Money", "Sku")), IterableExtensions.<String>sort(ListExtensions.<Type, String>map(attributeTypes, _function_2)));
        final Predicate<Path> _function_3 = (Path it) -> {
          return Files.isDirectory(it);
        };
        final List<Path> cacheDirs = Files.list(root.resolve(".dependencies-cache")).filter(_function_3).collect(
          Collectors.<Path>toList());
        Assertions.assertEquals(1, cacheDirs.size(), 
          "the artifact must be cached once per GAV, shared by both namespaces (no duplication)");
        Assertions.assertTrue(IterableExtensions.<Path>head(cacheDirs).getFileName().toString().startsWith("cqrs-model-0.1.0-SNAPSHOT-"), 
          "cache dir name must be <artifactId>-<version>-<sha1>");
        final Predicate<Path> _function_4 = (Path it) -> {
          return it.toString().endsWith(".cqrs");
        };
        Assertions.assertEquals(2, Files.list(IterableExtensions.<Path>head(cacheDirs)).filter(_function_4).count(), 
          "both .cqrs files from the tar.gz must be unpacked once");
      } finally {
        System.clearProperty("cqrs.maven.repo.snapshots");
        System.clearProperty("maven.repo.local");
        server.stop(0);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A {@code maven} artifact already present in the local repository is used without any network.
   */
  @Test
  public void resolvesMavenArtifactFromLocalRepository() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-maven-local");
      final Path localRepo = Files.createTempDirectory("m2-local");
      final Path artifactDir = Files.createDirectories(
        localRepo.resolve("org/fuin/test/cqrs-model/0.1.0-SNAPSHOT"));
      String _string = RemoteScopeResolutionTest.REMOTE_BILLING.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("money.cqrs", _string);
      Files.write(artifactDir.resolve("cqrs-model-0.1.0-SNAPSHOT-cqrs.tar.gz"), 
        TarGzTestSupport.tarGz(Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo))));
      System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/");
      System.setProperty("maven.repo.local", localRepo.toString());
      try {
        Path _resolve = root.resolve("dependencies.json");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[ { \"type\": \"maven\", \"namespaces\": [\"com.acme.billing\"], \"data\": {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\"groupId\": \"org.fuin.test\", \"artifactId\": \"cqrs-model\", \"version\": \"0.1.0-SNAPSHOT\" } } ]");
        _builder.newLine();
        Files.writeString(_resolve, _builder);
        this.assertResolvesToMoney(this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES));
      } finally {
        System.clearProperty("cqrs.maven.repo.snapshots");
        System.clearProperty("maven.repo.local");
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A {@code local} directory is read directly: models resolve and no cache directory is created.
   */
  @Test
  public void resolvesFromLocalDirectory() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-local");
      final Path localDir = Files.createTempDirectory("local-models");
      Files.writeString(localDir.resolve("billing.cqrs"), RemoteScopeResolutionTest.REMOTE_BILLING.toString());
      System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/");
      try {
        Path _resolve = root.resolve("dependencies.json");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[ { \"type\": \"maven\", \"namespaces\": [\"com.acme.billing\"], \"data\": {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\"groupId\": \"org.fuin.test\", \"artifactId\": \"cqrs-model\", \"version\": \"0.1.0-SNAPSHOT\",");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\"local\": \"");
        String _string = localDir.toString();
        _builder.append(_string, "\t");
        _builder.append("\" } } ]");
        _builder.newLineIfNotEmpty();
        Files.writeString(_resolve, _builder);
        this.assertResolvesToMoney(this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES));
        Assertions.assertFalse(Files.exists(root.resolve(".dependencies-cache")), 
          "a local directory must be read directly without creating a cache");
      } finally {
        System.clearProperty("cqrs.maven.repo.snapshots");
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Without a catalog the standard mechanism applies and the remote reference stays unresolved.
   */
  @Test
  public void fallsBackWithoutCatalog() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-none");
      final DomainModel model = this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES);
      EcoreUtil.resolveAll(model.eResource());
      Assertions.assertFalse(model.eResource().getErrors().isEmpty(), 
        "reference to remote type must stay unresolved without a catalog");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * An empty (or otherwise unparseable) catalog must degrade gracefully instead of breaking editing.
   */
  @Test
  public void fallsBackWithEmptyCatalog() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-empty");
      Files.writeString(root.resolve("dependencies.json"), "");
      final DomainModel model = this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES);
      EcoreUtil.resolveAll(model.eResource());
      Assertions.assertFalse(model.eResource().getErrors().isEmpty(), 
        "reference to remote type must stay unresolved when the catalog cannot be parsed");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private static String mavenMetadata() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<metadata>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<groupId>org.fuin.test</groupId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<artifactId>cqrs-model</artifactId>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<version>0.1.0-SNAPSHOT</version>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<versioning>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<snapshotVersions>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<snapshotVersion>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<classifier>cqrs</classifier>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<extension>tar.gz</extension>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<value>0.1.0-20240101.000000-1</value>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</snapshotVersion>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</snapshotVersions>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</versioning>");
    _builder.newLine();
    _builder.append("</metadata>");
    _builder.newLine();
    return _builder.toString();
  }

  /**
   * Starts (but does not yet {@code start()}) a local HTTP server answering the given byte routes.
   */
  private static HttpServer serve(final Map<String, byte[]> routes) {
    try {
      InetSocketAddress _inetSocketAddress = new InetSocketAddress("127.0.0.1", 0);
      final HttpServer server = HttpServer.create(_inetSocketAddress, 0);
      Set<Map.Entry<String, byte[]>> _entrySet = routes.entrySet();
      for (final Map.Entry<String, byte[]> route : _entrySet) {
        {
          final byte[] body = route.getValue();
          final HttpHandler _function = (HttpExchange exchange) -> {
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
          };
          server.createContext(route.getKey(), _function);
        }
      }
      return server;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private DomainModel parse(final Path root, final CharSequence text) {
    try {
      DomainModel _xblockexpression = null;
      {
        final URI uri = URI.createFileURI(root.resolve("model.cqrs").toString());
        _xblockexpression = this.parseHelper.parse(text, uri, this.resourceSetProvider.get());
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void assertResolvesToMoney(final DomainModel model) {
    EcoreUtil.resolveAll(model.eResource());
    boolean _isEmpty = model.eResource().getErrors().isEmpty();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Unexpected errors: ");
    String _join = IterableExtensions.join(model.eResource().getErrors(), ", ");
    _builder.append(_join);
    Assertions.assertTrue(_isEmpty, _builder.toString());
    final ValueObject valueObject = IterableExtensions.<ValueObject>head(Iterables.<ValueObject>filter(IterableExtensions.<Namespace>head(IterableExtensions.<Context>head(model.getContexts()).getNamespaces()).getElements(), ValueObject.class));
    final Type type = IterableExtensions.<Attribute>head(valueObject.getAttributes()).getType();
    Assertions.assertFalse(type.eIsProxy(), "remote type reference must be resolved");
    Assertions.assertTrue((type instanceof ExternalType));
    Assertions.assertEquals("Money", type.getName());
  }
}

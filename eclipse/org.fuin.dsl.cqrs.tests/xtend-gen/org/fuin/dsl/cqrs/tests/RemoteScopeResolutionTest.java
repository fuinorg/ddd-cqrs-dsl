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
import java.util.function.Predicate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
 * Verifies that cross-references are resolved against remote (HTTP-only) {@code .cqrs} models
 * through the {@code .remote-scope.json} catalog and the {@code .remote-scope-cache} directory.
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

  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  /**
   * Fetches the remote model over HTTP, caches it on disk and resolves the cross-reference.
   */
  @Test
  public void resolvesRemoteTypeOverHttpAndCaches() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-http");
      InetSocketAddress _inetSocketAddress = new InetSocketAddress("127.0.0.1", 0);
      final HttpServer server = HttpServer.create(_inetSocketAddress, 0);
      final HttpHandler _function = (HttpExchange exchange) -> {
        final byte[] bytes = RemoteScopeResolutionTest.REMOTE_BILLING.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
      };
      server.createContext("/billing.cqrs", _function);
      server.start();
      try {
        final int port = server.getAddress().getPort();
        Path _resolve = root.resolve(".remote-scope.json");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[ { \"com.acme.billing\": \"http://127.0.0.1:");
        _builder.append(port);
        _builder.append("/billing.cqrs\" } ]");
        _builder.newLineIfNotEmpty();
        Files.writeString(_resolve, _builder);
        final DomainModel model = this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES);
        this.assertResolvesToMoney(model);
        final Path cacheDir = root.resolve(".remote-scope-cache");
        Assertions.assertTrue(Files.exists(cacheDir.resolve("index.json")), "cache index must be written");
        final Predicate<Path> _function_1 = (Path it) -> {
          return it.toString().endsWith(".cqrs");
        };
        Assertions.assertTrue(Files.list(cacheDir).anyMatch(_function_1), 
          "downloaded .cqrs must be cached");
      } finally {
        server.stop(0);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * With a pre-populated cache and no reachable server, resolution is served from disk (offline).
   */
  @Test
  public void resolvesFromCacheWhenOffline() {
    try {
      final Path root = Files.createTempDirectory("remote-scope-offline");
      Path _resolve = root.resolve(".remote-scope.json");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("[ { \"com.acme.billing\": \"http://127.0.0.1:1/billing.cqrs\" } ]");
      _builder.newLine();
      Files.writeString(_resolve, _builder);
      final Path cacheDir = Files.createDirectory(root.resolve(".remote-scope-cache"));
      Files.writeString(cacheDir.resolve("billing-cached.cqrs"), RemoteScopeResolutionTest.REMOTE_BILLING.toString());
      Path _resolve_1 = cacheDir.resolve("index.json");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("{ \"entries\": [");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("{ \"namespace\": \"com.acme.billing\",");
      _builder_1.newLine();
      _builder_1.append("\t  ");
      _builder_1.append("\"url\": \"http://127.0.0.1:1/billing.cqrs\", \"file\": \"billing-cached.cqrs\" }");
      _builder_1.newLine();
      _builder_1.append("] }");
      _builder_1.newLine();
      Files.writeString(_resolve_1, _builder_1);
      this.assertResolvesToMoney(this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES));
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
      Files.writeString(root.resolve(".remote-scope.json"), "");
      final DomainModel model = this.parse(root, RemoteScopeResolutionTest.LOCAL_SALES);
      EcoreUtil.resolveAll(model.eResource());
      Assertions.assertFalse(model.eResource().getErrors().isEmpty(), 
        "reference to remote type must stay unresolved when the catalog cannot be parsed");
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

package org.fuin.dsl.cqrs.scoping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * Reads the local <code>.remote-scope.json</code> catalog that maps a (context, namespace)
 * coordinate to the URL of a remote <code>.cqrs</code> model.
 * 
 * <p>The catalog file is discovered by walking up the directory hierarchy starting at the model
 * resource's URI. Its name defaults to <code>.remote-scope.json</code> and can be overridden with
 * the system property <code>cqrs.remote.scope.file</code>. The JSON structure is a nested object
 * <code>context &rarr; namespace &rarr; url</code>:</p>
 * 
 * <pre>
 * {
 *   "com.acme.sales": {
 *     "com.acme.billing": "http://models.acme.com/billing.cqrs"
 *   }
 * }
 * </pre>
 * 
 * <p>A coordinate that is not present in the catalog yields <code>null</code>, which lets the
 * caller fall back to the standard file-based scoping mechanism.</p>
 */
@Singleton
@SuppressWarnings("all")
public class RemoteScopeCatalog {
  private static final String DEFAULT_FILE_NAME = ".remote-scope.json";

  /**
   * Sentinel stored in the discovery cache to mark "no catalog found" (maps can't cache null).
   */
  private static final URI NONE = URI.createURI("cqrs:no-remote-scope-catalog");

  private static final Map<Object, Object> NO_OPTIONS = CollectionLiterals.<Object, Object>newHashMap();

  /**
   * Caches the discovered catalog URI per containing directory (value may be {@link #NONE}).
   */
  private final Map<URI, URI> configUriByDir = CollectionLiterals.<URI, URI>newHashMap();

  /**
   * Caches the parsed catalog per discovered catalog URI.
   */
  private final Map<URI, Map<String, Map<String, String>>> catalogByConfig = CollectionLiterals.<URI, Map<String, Map<String, String>>>newHashMap();

  public String fileName() {
    return System.getProperty("cqrs.remote.scope.file", RemoteScopeCatalog.DEFAULT_FILE_NAME);
  }

  /**
   * Directory that contains the catalog file for the given model, or <code>null</code> if none.
   */
  public URI rootDir(final ResourceSet rs, final URI modelUri) {
    final URI config = this.configUri(rs, modelUri);
    URI _xifexpression = null;
    if ((config == RemoteScopeCatalog.NONE)) {
      _xifexpression = null;
    } else {
      _xifexpression = config.trimSegments(1);
    }
    return _xifexpression;
  }

  /**
   * Resolves the remote model URL configured for the (context, namespace) pair, or
   * <code>null</code> when not configured. A trailing <code>.*</code> wildcard on the namespace
   * is ignored, so <code>import a.b</code> and <code>import a.b.*</code> resolve to the same entry.
   */
  public String lookupUrl(final ResourceSet rs, final URI modelUri, final String context, final String namespace) {
    final Map<String, Map<String, String>> catalog = this.catalog(rs, modelUri);
    Map<String, String> _get = null;
    if (catalog!=null) {
      _get=catalog.get(context);
    }
    String _get_1 = null;
    if (_get!=null) {
      _get_1=_get.get(RemoteScopeCatalog.stripWildcard(namespace));
    }
    return _get_1;
  }

  public static String stripWildcard(final String namespace) {
    String _xifexpression = null;
    if (((namespace != null) && namespace.endsWith(".*"))) {
      int _length = namespace.length();
      int _minus = (_length - 2);
      _xifexpression = namespace.substring(0, _minus);
    } else {
      _xifexpression = namespace;
    }
    return _xifexpression;
  }

  private Map<String, Map<String, String>> catalog(final ResourceSet rs, final URI modelUri) {
    final URI config = this.configUri(rs, modelUri);
    if ((config == RemoteScopeCatalog.NONE)) {
      return null;
    }
    final Function<URI, Map<String, Map<String, String>>> _function = (URI it) -> {
      return this.parse(rs, it);
    };
    return this.catalogByConfig.computeIfAbsent(config, _function);
  }

  private URI configUri(final ResourceSet rs, final URI modelUri) {
    URI _xblockexpression = null;
    {
      if ((modelUri == null)) {
        return RemoteScopeCatalog.NONE;
      }
      final Function<URI, URI> _function = (URI it) -> {
        return this.findConfigUri(rs, it);
      };
      _xblockexpression = this.configUriByDir.computeIfAbsent(modelUri.trimSegments(1), _function);
    }
    return _xblockexpression;
  }

  private URI findConfigUri(final ResourceSet rs, final URI startDir) {
    final URIConverter converter = rs.getURIConverter();
    final String name = this.fileName();
    URI dir = startDir;
    while (((dir != null) && (dir.segmentCount() > 0))) {
      {
        final URI candidate = dir.appendSegment(name);
        boolean _exists = converter.exists(candidate, RemoteScopeCatalog.NO_OPTIONS);
        if (_exists) {
          return candidate;
        }
        dir = dir.trimSegments(1);
      }
    }
    return RemoteScopeCatalog.NONE;
  }

  private Map<String, Map<String, String>> parse(final ResourceSet rs, final URI configUri) {
    try {
      final LinkedHashMap<String, Map<String, String>> result = CollectionLiterals.<String, Map<String, String>>newLinkedHashMap();
      InputStream _createInputStream = rs.getURIConverter().createInputStream(configUri, RemoteScopeCatalog.NO_OPTIONS);
      final InputStreamReader reader = new InputStreamReader(_createInputStream, 
        StandardCharsets.UTF_8);
      try {
        final JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> _entrySet = root.entrySet();
        for (final Map.Entry<String, JsonElement> contextEntry : _entrySet) {
          {
            final LinkedHashMap<String, String> namespaces = CollectionLiterals.<String, String>newLinkedHashMap();
            Set<Map.Entry<String, JsonElement>> _entrySet_1 = contextEntry.getValue().getAsJsonObject().entrySet();
            for (final Map.Entry<String, JsonElement> namespaceEntry : _entrySet_1) {
              namespaces.put(namespaceEntry.getKey(), namespaceEntry.getValue().getAsString());
            }
            result.put(contextEntry.getKey(), namespaces);
          }
        }
      } finally {
        reader.close();
      }
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

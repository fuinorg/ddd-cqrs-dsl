package org.fuin.dsl.cqrs.scoping;

import com.google.common.io.CharStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pair;

/**
 * Reads the local <code>dependencies.json</code> catalog that declares <em>where a namespace
 * lives</em>, mapping it to the Maven artifact that provides the remote <code>.cqrs</code> model(s).
 * 
 * <p>The catalog file is discovered by walking up the directory hierarchy starting at the model
 * resource's URI. Its name defaults to <code>dependencies.json</code> and can be overridden with
 * the system property <code>cqrs.dependencies.file</code>. The JSON structure is an array of typed
 * objects, each declaring the fully qualified namespaces it <strong>provides</strong> (the
 * <code>context.namespace</code> values, not the importer) as a <code>namespaces</code> array, a
 * <code>type</code> discriminator (always <code>maven</code>) and a <code>data</code> block with the
 * artifact's <code>groupId</code>/<code>artifactId</code>/<code>version</code>. Listing several
 * namespaces in one entry is handy because a single Maven artifact often holds more than one context
 * and namespace. An optional <code>local</code> directory overrides the artifact and is read
 * directly &mdash; useful while developing a model that is not published yet:</p>
 * 
 * <pre>
 * [
 *   { "type": "maven", "namespaces": ["common.types", "common.refs"],
 *     "data": { "groupId": "org.fuin.dsl.cqrs.contexts",
 *               "artifactId": "cqrs-common-model", "version": "0.1.0-SNAPSHOT" } },
 *   { "type": "maven", "namespaces": ["dev.workinprogress"],
 *     "data": { "groupId": "org.acme", "artifactId": "wip-model", "version": "0.0.1-SNAPSHOT",
 *               "local": "../wip-model/src/main/cqrs" } }
 * ]
 * </pre>
 * 
 * <p>So <em>any</em> model that contains <code>import common.types.*</code> resolves to that source,
 * regardless of the importing context. A namespace that is not present in the catalog yields
 * <code>null</code>, which lets the caller fall back to the standard file-based scoping mechanism.</p>
 */
@Singleton
@SuppressWarnings("all")
public class RemoteScopeCatalog {
  private static final Logger LOG = Logger.getLogger(RemoteScopeCatalog.class);

  private static final String DEFAULT_FILE_NAME = "dependencies.json";

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
   * Caches the parsed catalog per discovered catalog URI, together with the file's last-modified
   * time so the catalog is re-read when it is edited: {@code config -> (timestamp -> namespace -> entry)}.
   */
  private final Map<URI, Pair<Long, Map<String, RemoteScopeEntry>>> catalogByConfig = CollectionLiterals.<URI, Pair<Long, Map<String, RemoteScopeEntry>>>newHashMap();

  public String fileName() {
    return System.getProperty("cqrs.dependencies.file", RemoteScopeCatalog.DEFAULT_FILE_NAME);
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
   * Resolves the catalog entry that provides the given namespace, or <code>null</code> when no
   * catalog entry exists for it. A trailing <code>.*</code> wildcard is ignored, so
   * <code>import a.b</code> and <code>import a.b.*</code> resolve to the same entry.
   */
  public RemoteScopeEntry lookupEntry(final ResourceSet rs, final URI modelUri, final String namespace) {
    final Map<String, RemoteScopeEntry> catalog = this.catalog(rs, modelUri);
    RemoteScopeEntry _get = null;
    if (catalog!=null) {
      _get=catalog.get(RemoteScopeCatalog.stripWildcard(namespace));
    }
    return _get;
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

  private Map<String, RemoteScopeEntry> catalog(final ResourceSet rs, final URI modelUri) {
    final URI config = this.configUri(rs, modelUri);
    if ((config == RemoteScopeCatalog.NONE)) {
      return null;
    }
    final long stamp = this.timeStamp(rs, config);
    final Pair<Long, Map<String, RemoteScopeEntry>> cached = this.catalogByConfig.get(config);
    if (((cached != null) && ((cached.getKey()).longValue() == stamp))) {
      return cached.getValue();
    }
    final Map<String, RemoteScopeEntry> parsed = this.parse(rs, config);
    Pair<Long, Map<String, RemoteScopeEntry>> _mappedTo = Pair.<Long, Map<String, RemoteScopeEntry>>of(Long.valueOf(stamp), parsed);
    this.catalogByConfig.put(config, _mappedTo);
    return parsed;
  }

  /**
   * Discovers the catalog URI for the model's directory. A previously discovered URI is re-validated
   * so a deleted or moved catalog triggers a fresh search; a previously empty result ({@link #NONE})
   * stays cached, so adding a brand new catalog where none existed still requires a restart.
   */
  private URI configUri(final ResourceSet rs, final URI modelUri) {
    if ((modelUri == null)) {
      return RemoteScopeCatalog.NONE;
    }
    final URI dir = modelUri.trimSegments(1);
    final URI cached = this.configUriByDir.get(dir);
    if (((cached != null) && ((cached == RemoteScopeCatalog.NONE) || (this.locate(rs, cached) != null)))) {
      return cached;
    }
    final URI found = this.findConfigUri(rs, dir);
    this.configUriByDir.put(dir, found);
    return found;
  }

  /**
   * Last-modified time of the (file based) catalog, or 0 when it cannot be determined.
   */
  private long timeStamp(final ResourceSet rs, final URI uri) {
    final File file = this.toFile(rs, uri);
    long _xifexpression = (long) 0;
    if ((file != null)) {
      _xifexpression = file.lastModified();
    } else {
      _xifexpression = 0L;
    }
    return _xifexpression;
  }

  private URI findConfigUri(final ResourceSet rs, final URI startDir) {
    final String name = this.fileName();
    URI dir = startDir;
    while (((dir != null) && (dir.segmentCount() > 0))) {
      {
        final URI candidate = dir.appendSegment(name);
        final URI found = this.locate(rs, candidate);
        String _absolutePath = this.absolutePath(rs, candidate);
        String _plus = ("Looking for remote scope catalog at \'" + _absolutePath);
        String _plus_1 = (_plus + "\' -> ");
        String _xifexpression = null;
        if ((found != null)) {
          _xifexpression = "found";
        } else {
          _xifexpression = "not found";
        }
        String _plus_2 = (_plus_1 + _xifexpression);
        RemoteScopeCatalog.LOG.info(_plus_2);
        if ((found != null)) {
          return found;
        }
        dir = dir.trimSegments(1);
      }
    }
    String _absolutePath = this.absolutePath(rs, startDir);
    String _plus = ((("No remote scope catalog \'" + name) + "\' found above \'") + _absolutePath);
    String _plus_1 = (_plus + "\'");
    RemoteScopeCatalog.LOG.info(_plus_1);
    return RemoteScopeCatalog.NONE;
  }

  /**
   * Returns a readable URI for the candidate when it exists, or <code>null</code> otherwise.
   * The file system is checked first (so a catalog created outside Eclipse is found even when the
   * workspace has not been refreshed), falling back to the {@link org.eclipse.emf.ecore.resource.URIConverter}
   * for non-file URIs (e.g. archive or HTTP based resource sets).
   */
  private URI locate(final ResourceSet rs, final URI candidate) {
    final File file = this.toFile(rs, candidate);
    if (((file != null) && file.exists())) {
      return URI.createFileURI(file.getAbsolutePath());
    }
    URI _xifexpression = null;
    boolean _exists = rs.getURIConverter().exists(candidate, RemoteScopeCatalog.NO_OPTIONS);
    if (_exists) {
      _xifexpression = candidate;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }

  /**
   * Resolves a URI to a {@link File}, or <code>null</code> when it is not a file system URI.
   */
  private File toFile(final ResourceSet rs, final URI uri) {
    URI resolved = rs.getURIConverter().normalize(uri);
    if ((resolved.isPlatformResource() || resolved.isPlatformPlugin())) {
      resolved = CommonPlugin.resolve(resolved);
    }
    File _xifexpression = null;
    boolean _isFile = resolved.isFile();
    if (_isFile) {
      String _fileString = resolved.toFileString();
      _xifexpression = new File(_fileString);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }

  /**
   * Resolves a URI to an absolute file system path for logging, falling back to the URI string.
   */
  private String absolutePath(final ResourceSet rs, final URI uri) {
    final File file = this.toFile(rs, uri);
    String _xifexpression = null;
    if ((file != null)) {
      _xifexpression = file.getAbsolutePath();
    } else {
      _xifexpression = rs.getURIConverter().normalize(uri).toString();
    }
    return _xifexpression;
  }

  /**
   * Parses the catalog into a map of fully qualified namespace (<code>context.namespace</code>) to
   * its typed {@link RemoteScopeEntry}. Entries with an unknown <code>type</code> are logged and
   * skipped; a structurally invalid entry aborts the whole parse.
   */
  private Map<String, RemoteScopeEntry> parse(final ResourceSet rs, final URI configUri) {
    try {
      final LinkedHashMap<String, RemoteScopeEntry> result = CollectionLiterals.<String, RemoteScopeEntry>newLinkedHashMap();
      InputStream _createInputStream = rs.getURIConverter().createInputStream(configUri, RemoteScopeCatalog.NO_OPTIONS);
      final InputStreamReader reader = new InputStreamReader(_createInputStream, 
        StandardCharsets.UTF_8);
      String _xtrycatchfinallyexpression = null;
      try {
        _xtrycatchfinallyexpression = CharStreams.toString(reader);
      } finally {
        reader.close();
      }
      final String content = _xtrycatchfinallyexpression;
      try {
        final JsonElement rootElement = JsonParser.parseString(content);
        boolean _isJsonArray = rootElement.isJsonArray();
        boolean _not = (!_isJsonArray);
        if (_not) {
          throw new IllegalStateException("Expected a JSON array at the root");
        }
        JsonArray _asJsonArray = rootElement.getAsJsonArray();
        for (final JsonElement element : _asJsonArray) {
          {
            boolean _isJsonObject = element.isJsonObject();
            boolean _not_1 = (!_isJsonObject);
            if (_not_1) {
              throw new IllegalStateException((("Expected a JSON object entry, but got \'" + element) + "\'"));
            }
            final JsonObject obj = element.getAsJsonObject();
            final RemoteScopeEntry entry = this.toEntry(obj);
            if ((entry != null)) {
              List<String> _namespaces = RemoteScopeCatalog.namespaces(obj);
              for (final String ns : _namespaces) {
                result.put(ns, entry);
              }
            }
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception ex = (Exception)_t;
          String _absolutePath = this.absolutePath(rs, configUri);
          String _plus = ("Failed to parse remote scope catalog \'" + _absolutePath);
          String _plus_1 = (_plus + "\':\n");
          String _plus_2 = (_plus_1 + content);
          throw new IllegalStateException(_plus_2, ex);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Builds a typed source entry from a single catalog object <code>{ type, namespaces, data }</code>.
   * Returns <code>null</code> for an unknown type (logged and skipped); throws for a missing
   * <code>type</code> or <code>data</code>, or a missing type-specific field. The
   * <code>namespaces</code> array is read separately by {@link #namespaces(JsonObject)}.
   */
  private RemoteScopeEntry toEntry(final JsonObject obj) {
    RemoteScopeEntry _xblockexpression = null;
    {
      final String type = RemoteScopeCatalog.string(obj, "type");
      JsonObject _xifexpression = null;
      if ((obj.has("data") && obj.get("data").isJsonObject())) {
        _xifexpression = obj.getAsJsonObject("data");
      } else {
        _xifexpression = null;
      }
      final JsonObject data = _xifexpression;
      if (((type == null) || (data == null))) {
        throw new IllegalStateException(("Each catalog entry needs \'type\', \'namespaces\' and \'data\': " + obj));
      }
      RemoteScopeEntry _switchResult = null;
      if (type != null) {
        switch (type) {
          case RemoteScopeEntry.TYPE_MAVEN:
            _switchResult = RemoteScopeEntry.maven(RemoteScopeCatalog.required(data, "groupId"), RemoteScopeCatalog.required(data, "artifactId"), 
              RemoteScopeCatalog.required(data, "version"), RemoteScopeCatalog.string(data, "local"));
            break;
          default:
            Object _xblockexpression_1 = null;
            {
              RemoteScopeCatalog.LOG.warn(((("Ignoring remote scope entry with unknown type \'" + type) + "\': ") + obj));
              _xblockexpression_1 = null;
            }
            _switchResult = ((RemoteScopeEntry)_xblockexpression_1);
            break;
        }
      } else {
        Object _xblockexpression_1 = null;
        {
          RemoteScopeCatalog.LOG.warn(((("Ignoring remote scope entry with unknown type \'" + type) + "\': ") + obj));
          _xblockexpression_1 = null;
        }
        _switchResult = ((RemoteScopeEntry)_xblockexpression_1);
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }

  /**
   * Reads the required non-empty <code>namespaces</code> string array of a catalog object.
   */
  private static List<String> namespaces(final JsonObject obj) {
    JsonElement _xifexpression = null;
    boolean _has = obj.has("namespaces");
    if (_has) {
      _xifexpression = obj.get("namespaces");
    } else {
      _xifexpression = null;
    }
    final JsonElement element = _xifexpression;
    if ((((element == null) || (!element.isJsonArray())) || element.getAsJsonArray().isEmpty())) {
      throw new IllegalStateException(("Each catalog entry needs a non-empty \'namespaces\' array: " + obj));
    }
    final ArrayList<String> result = CollectionLiterals.<String>newArrayList();
    JsonArray _asJsonArray = element.getAsJsonArray();
    for (final JsonElement ns : _asJsonArray) {
      {
        boolean _isJsonPrimitive = ns.isJsonPrimitive();
        boolean _not = (!_isJsonPrimitive);
        if (_not) {
          throw new IllegalStateException(("\'namespaces\' must contain only strings: " + obj));
        }
        result.add(ns.getAsString());
      }
    }
    return result;
  }

  private static String string(final JsonObject obj, final String name) {
    String _xifexpression = null;
    if ((obj.has(name) && obj.get(name).isJsonPrimitive())) {
      _xifexpression = obj.get(name).getAsString();
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }

  private static String required(final JsonObject data, final String name) {
    final String value = RemoteScopeCatalog.string(data, name);
    if ((value == null)) {
      throw new IllegalStateException(((("Missing \'" + name) + "\' in \'data\': ") + data));
    }
    return value;
  }
}

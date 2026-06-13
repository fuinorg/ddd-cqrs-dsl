package org.fuin.dsl.cqrs.scoping;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * Downloads remote <code>.cqrs</code> models and caches them on disk under a
 * <code>.remote-scope-cache</code> directory placed next to the <code>.remote-scope.json</code>
 * catalog.
 * 
 * <p>The cache index (<code>.remote-scope-cache/index.json</code>) is read into memory the first
 * time a cache directory is seen and is held for the session, so subsequent lookups find the cached
 * file by its (context, namespace) key without any network access. A cache miss triggers a single
 * HTTP download, writes the file, appends an index entry, persists the index and returns the local
 * file URI.</p>
 */
@Singleton
@SuppressWarnings("all")
public class RemoteScopeCache {
  @Data
  public static class CacheEntry {
    private final String namespace;

    private final String url;

    private final String file;

    public CacheEntry(final String namespace, final String url, final String file) {
      super();
      this.namespace = namespace;
      this.url = url;
      this.file = file;
    }

    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.namespace== null) ? 0 : this.namespace.hashCode());
      result = prime * result + ((this.url== null) ? 0 : this.url.hashCode());
      return prime * result + ((this.file== null) ? 0 : this.file.hashCode());
    }

    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      RemoteScopeCache.CacheEntry other = (RemoteScopeCache.CacheEntry) obj;
      if (this.namespace == null) {
        if (other.namespace != null)
          return false;
      } else if (!this.namespace.equals(other.namespace))
        return false;
      if (this.url == null) {
        if (other.url != null)
          return false;
      } else if (!this.url.equals(other.url))
        return false;
      if (this.file == null) {
        if (other.file != null)
          return false;
      } else if (!this.file.equals(other.file))
        return false;
      return true;
    }

    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("namespace", this.namespace);
      b.add("url", this.url);
      b.add("file", this.file);
      return b.toString();
    }

    @Pure
    public String getNamespace() {
      return this.namespace;
    }

    @Pure
    public String getUrl() {
      return this.url;
    }

    @Pure
    public String getFile() {
      return this.file;
    }
  }

  private static final String CACHE_DIR_NAME = ".remote-scope-cache";

  private static final String INDEX_FILE_NAME = "index.json";

  /**
   * In-memory cache index per cache directory: key {@code "context namespace"} &rarr; entry.
   */
  private final Map<File, Map<String, RemoteScopeCache.CacheEntry>> indexByDir = CollectionLiterals.<File, Map<String, RemoteScopeCache.CacheEntry>>newHashMap();

  /**
   * Returns the local cache URI of the remote model that provides the given namespace, downloading
   * and caching it on a miss, or <code>null</code> when nothing is configured (so the caller falls
   * back to the standard mechanism).
   */
  public URI getCachedModelUri(final ResourceSet rs, final URI modelUri, final String namespace, final RemoteScopeCatalog catalog) {
    final URI root = catalog.rootDir(rs, modelUri);
    if ((root == null)) {
      return null;
    }
    final File cacheDir = this.toFile(rs, root.appendSegment(RemoteScopeCache.CACHE_DIR_NAME));
    if ((cacheDir == null)) {
      return null;
    }
    final String ns = RemoteScopeCatalog.stripWildcard(namespace);
    final String url = catalog.lookupUrl(rs, modelUri, namespace);
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(url);
    if (_isNullOrEmpty) {
      return null;
    }
    final Map<String, RemoteScopeCache.CacheEntry> index = this.indexFor(cacheDir);
    final RemoteScopeCache.CacheEntry existing = index.get(ns);
    if (((existing != null) && Objects.equals(existing.url, url))) {
      final File cached = new File(cacheDir, existing.file);
      if ((cached.exists() && this.upToDate(url, cached))) {
        return URI.createFileURI(cached.getAbsolutePath());
      }
    }
    String _sha1 = this.sha1(url);
    String _plus = ((ns + "-") + _sha1);
    final String fileName = (_plus + ".cqrs");
    final File target = new File(cacheDir, fileName);
    this.download(url, target);
    RemoteScopeCache.CacheEntry _cacheEntry = new RemoteScopeCache.CacheEntry(ns, url, fileName);
    index.put(ns, _cacheEntry);
    this.persist(cacheDir, index);
    return URI.createFileURI(target.getAbsolutePath());
  }

  private Map<String, RemoteScopeCache.CacheEntry> indexFor(final File cacheDir) {
    final Function<File, Map<String, RemoteScopeCache.CacheEntry>> _function = (File it) -> {
      return this.loadIndex(it);
    };
    return this.indexByDir.computeIfAbsent(cacheDir, _function);
  }

  private Map<String, RemoteScopeCache.CacheEntry> loadIndex(final File cacheDir) {
    try {
      final LinkedHashMap<String, RemoteScopeCache.CacheEntry> map = CollectionLiterals.<String, RemoteScopeCache.CacheEntry>newLinkedHashMap();
      final File indexFile = new File(cacheDir, RemoteScopeCache.INDEX_FILE_NAME);
      boolean _exists = indexFile.exists();
      if (_exists) {
        FileInputStream _fileInputStream = new FileInputStream(indexFile);
        final InputStreamReader reader = new InputStreamReader(_fileInputStream, StandardCharsets.UTF_8);
        try {
          final JsonArray entries = JsonParser.parseReader(reader).getAsJsonObject().getAsJsonArray("entries");
          if ((entries != null)) {
            for (final JsonElement element : entries) {
              {
                final JsonObject obj = element.getAsJsonObject();
                String _asString = obj.get("namespace").getAsString();
                String _asString_1 = obj.get("url").getAsString();
                String _asString_2 = obj.get("file").getAsString();
                final RemoteScopeCache.CacheEntry entry = new RemoteScopeCache.CacheEntry(_asString, _asString_1, _asString_2);
                map.put(entry.namespace, entry);
              }
            }
          }
        } finally {
          reader.close();
        }
      }
      return map;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void persist(final File cacheDir, final Map<String, RemoteScopeCache.CacheEntry> index) {
    try {
      cacheDir.mkdirs();
      final JsonArray array = new JsonArray();
      Collection<RemoteScopeCache.CacheEntry> _values = index.values();
      for (final RemoteScopeCache.CacheEntry entry : _values) {
        {
          final JsonObject obj = new JsonObject();
          obj.addProperty("namespace", entry.namespace);
          obj.addProperty("url", entry.url);
          obj.addProperty("file", entry.file);
          array.add(obj);
        }
      }
      final JsonObject root = new JsonObject();
      root.add("entries", array);
      File _file = new File(cacheDir, RemoteScopeCache.INDEX_FILE_NAME);
      FileOutputStream _fileOutputStream = new FileOutputStream(_file);
      final OutputStreamWriter writer = new OutputStreamWriter(_fileOutputStream, 
        StandardCharsets.UTF_8);
      try {
        new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
      } finally {
        writer.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A cached file is current unless its source is a local <code>file:</code> that has been modified
   * more recently. Non-file sources (e.g. HTTP) are always treated as up to date.
   */
  private boolean upToDate(final String url, final File cached) {
    final File source = this.sourceFile(url);
    return ((source == null) || (source.lastModified() <= cached.lastModified()));
  }

  /**
   * Returns the local source file for a <code>file:</code> URL, or <code>null</code> for other schemes.
   */
  private File sourceFile(final String url) {
    try {
      final java.net.URI uri = new java.net.URI(url);
      File _xifexpression = null;
      boolean _equals = "file".equals(uri.getScheme());
      if (_equals) {
        _xifexpression = new File(uri);
      } else {
        _xifexpression = null;
      }
      return _xifexpression;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        return null;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  private void download(final String url, final File target) {
    try {
      target.getParentFile().mkdirs();
      final InputStream input = new java.net.URI(url).toURL().openStream();
      try {
        Files.copy(input, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
      } finally {
        input.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

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

  private String sha1(final String value) {
    try {
      final byte[] bytes = MessageDigest.getInstance("SHA-1").digest(value.getBytes(StandardCharsets.UTF_8));
      final StringBuilder sb = new StringBuilder();
      for (final byte b : bytes) {
        sb.append(String.format("%02x", Byte.valueOf(b)));
      }
      return sb.toString();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

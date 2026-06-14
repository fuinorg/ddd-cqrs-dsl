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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * Materializes remote <code>.cqrs</code> models declared in the <code>dependencies.json</code>
 * catalog and caches them on disk under a <code>.dependencies-cache</code> directory placed next to
 * the catalog.
 * 
 * <p>Each resolved namespace is cached in its own sub-directory that holds one or more
 * <code>.cqrs</code> files: a {@code simple} source contributes the single downloaded file under
 * <code>&lt;namespace&gt;-&lt;sha1(source)&gt;</code>, a {@code maven} source contributes every
 * <code>.cqrs</code> unpacked from the artifact's <code>tar.gz</code> under
 * <code>&lt;namespace&gt;-&lt;version&gt;-&lt;sha1(source)&gt;</code> (the version is part of the name
 * so different versions of the same artifact stay distinct on disk). The cache index
 * (<code>.dependencies-cache/index.json</code>) is
 * read into memory the first time a cache directory is seen and held for the session, so subsequent
 * lookups serve files from disk without any network access.</p>
 * 
 * <p>A <code>file:</code> based {@code simple} source is refreshed when its source file changes;
 * <code>http(s):</code> sources and {@code maven} artifacts (including SNAPSHOTs) are treated as up to
 * date once cached &mdash; delete the cache directory or bump the version to force a refresh.</p>
 */
@Singleton
@SuppressWarnings("all")
public class RemoteScopeCache {
  @Data
  public static class CacheEntry {
    private final String namespace;

    private final String source;

    private final String dir;

    public CacheEntry(final String namespace, final String source, final String dir) {
      super();
      this.namespace = namespace;
      this.source = source;
      this.dir = dir;
    }

    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.namespace== null) ? 0 : this.namespace.hashCode());
      result = prime * result + ((this.source== null) ? 0 : this.source.hashCode());
      return prime * result + ((this.dir== null) ? 0 : this.dir.hashCode());
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
      if (this.source == null) {
        if (other.source != null)
          return false;
      } else if (!this.source.equals(other.source))
        return false;
      if (this.dir == null) {
        if (other.dir != null)
          return false;
      } else if (!this.dir.equals(other.dir))
        return false;
      return true;
    }

    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("namespace", this.namespace);
      b.add("source", this.source);
      b.add("dir", this.dir);
      return b.toString();
    }

    @Pure
    public String getNamespace() {
      return this.namespace;
    }

    @Pure
    public String getSource() {
      return this.source;
    }

    @Pure
    public String getDir() {
      return this.dir;
    }
  }

  private static final String CACHE_DIR_NAME = ".dependencies-cache";

  private static final String INDEX_FILE_NAME = "index.json";

  /**
   * File name of the single model cached for a {@code simple} source.
   */
  private static final String SIMPLE_FILE_NAME = "model.cqrs";

  /**
   * In-memory cache index per cache directory: key {@code namespace} &rarr; entry.
   */
  private final Map<File, Map<String, RemoteScopeCache.CacheEntry>> indexByDir = CollectionLiterals.<File, Map<String, RemoteScopeCache.CacheEntry>>newHashMap();

  /**
   * Returns the local cache URIs of the <code>.cqrs</code> models that provide the given namespace,
   * downloading and caching them on a miss, or an empty list when nothing is configured (so the
   * caller falls back to the standard mechanism).
   */
  public List<URI> getCachedModelUris(final ResourceSet rs, final URI modelUri, final String namespace, final RemoteScopeCatalog catalog) {
    final URI root = catalog.rootDir(rs, modelUri);
    if ((root == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    final File cacheDir = this.toFile(rs, root.appendSegment(RemoteScopeCache.CACHE_DIR_NAME));
    if ((cacheDir == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    final String ns = RemoteScopeCatalog.stripWildcard(namespace);
    final RemoteScopeEntry entry = catalog.lookupEntry(rs, modelUri, namespace);
    if ((entry == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    final String source = entry.getSourceId();
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(source);
    if (_isNullOrEmpty) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    String _xifexpression = null;
    String _type = entry.getType();
    boolean _equals = Objects.equals(_type, RemoteScopeEntry.TYPE_MAVEN);
    if (_equals) {
      String _version = entry.getVersion();
      String _plus = ((ns + "-") + _version);
      String _plus_1 = (_plus + "-");
      String _sha1 = this.sha1(source);
      _xifexpression = (_plus_1 + _sha1);
    } else {
      String _sha1_1 = this.sha1(source);
      _xifexpression = ((ns + "-") + _sha1_1);
    }
    final String dirName = _xifexpression;
    final File targetDir = new File(cacheDir, dirName);
    final Map<String, RemoteScopeCache.CacheEntry> index = this.indexFor(cacheDir);
    final RemoteScopeCache.CacheEntry existing = index.get(ns);
    if (((((existing != null) && Objects.equals(existing.source, source)) && targetDir.isDirectory()) && this.upToDate(entry, targetDir))) {
      final List<URI> cached = this.cqrsFiles(targetDir);
      boolean _isEmpty = cached.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        return cached;
      }
    }
    this.materialize(entry, targetDir);
    RemoteScopeCache.CacheEntry _cacheEntry = new RemoteScopeCache.CacheEntry(ns, source, dirName);
    index.put(ns, _cacheEntry);
    this.persist(cacheDir, index);
    return this.cqrsFiles(targetDir);
  }

  /**
   * Downloads / unpacks the entry's model(s) into <code>targetDir</code>, replacing any stale content.
   */
  private void materialize(final RemoteScopeEntry entry, final File targetDir) {
    try {
      this.cleanDir(targetDir);
      targetDir.mkdirs();
      String _type = entry.getType();
      if (_type != null) {
        switch (_type) {
          case RemoteScopeEntry.TYPE_SIMPLE:
            String _url = entry.getUrl();
            File _file = new File(targetDir, RemoteScopeCache.SIMPLE_FILE_NAME);
            this.download(_url, _file);
            break;
          case RemoteScopeEntry.TYPE_MAVEN:
            final InputStream stream = new MavenArtifactResolver().openArtifact(entry.getGroupId(), entry.getArtifactId(), entry.getVersion());
            try {
              TarGz.extractCqrsFiles(stream, targetDir);
            } finally {
              stream.close();
            }
            break;
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * All <code>.cqrs</code> files in <code>dir</code> as file URIs, sorted by name for stable order.
   */
  private List<URI> cqrsFiles(final File dir) {
    final File[] files = dir.listFiles();
    if ((files == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    final ArrayList<URI> result = CollectionLiterals.<URI>newArrayList();
    final Function1<File, String> _function = (File it) -> {
      return it.getName();
    };
    List<File> _sortBy = IterableExtensions.<File, String>sortBy(((Iterable<File>)Conversions.doWrapArray(files)), _function);
    for (final File f : _sortBy) {
      if ((f.isFile() && f.getName().endsWith(".cqrs"))) {
        result.add(URI.createFileURI(f.getAbsolutePath()));
      }
    }
    return result;
  }

  private void cleanDir(final File dir) {
    final File[] files = dir.listFiles();
    if ((files != null)) {
      for (final File f : files) {
        f.delete();
      }
    }
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
                if (((obj.has("namespace") && obj.has("source")) && obj.has("dir"))) {
                  String _asString = obj.get("namespace").getAsString();
                  String _asString_1 = obj.get("source").getAsString();
                  String _asString_2 = obj.get("dir").getAsString();
                  final RemoteScopeCache.CacheEntry entry = new RemoteScopeCache.CacheEntry(_asString, _asString_1, _asString_2);
                  map.put(entry.namespace, entry);
                }
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
          obj.addProperty("source", entry.source);
          obj.addProperty("dir", entry.dir);
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
   * A cached {@code simple} entry whose source is a local <code>file:</code> is stale once that file
   * has been modified more recently than the cached copy. All other sources (<code>http(s):</code>
   * and {@code maven}) are treated as up to date.
   */
  private boolean upToDate(final RemoteScopeEntry entry, final File targetDir) {
    String _type = entry.getType();
    boolean _notEquals = (!Objects.equals(_type, RemoteScopeEntry.TYPE_SIMPLE));
    if (_notEquals) {
      return true;
    }
    final File source = this.sourceFile(entry.getUrl());
    if ((source == null)) {
      return true;
    }
    final File cached = new File(targetDir, RemoteScopeCache.SIMPLE_FILE_NAME);
    return (cached.exists() && (source.lastModified() <= cached.lastModified()));
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

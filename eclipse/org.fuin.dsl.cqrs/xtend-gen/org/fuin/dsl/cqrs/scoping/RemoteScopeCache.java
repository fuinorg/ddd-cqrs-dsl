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
 * <p>Each Maven artifact is cached once in its own sub-directory
 * <code>&lt;artifactId&gt;-&lt;version&gt;-&lt;sha1(gav)&gt;</code> holding every <code>.cqrs</code>
 * file unpacked from the artifact's <code>tar.gz</code>. Keying the cache by the Maven coordinate
 * (rather than by namespace) means an artifact that provides several namespaces is downloaded and
 * unpacked only once and shared by all of them. The cache index
 * (<code>.dependencies-cache/index.json</code>) is read into memory the first time a cache directory
 * is seen and held for the session, so subsequent lookups serve files from disk without any network
 * access. {@code maven} artifacts (including SNAPSHOTs) are treated as up to date once cached &mdash;
 * delete the cache directory or bump the version to force a refresh.</p>
 * 
 * <p>When an entry declares a <code>local</code> directory the <code>.cqrs</code> files are read
 * straight from that folder (resolved relative to the catalog when not absolute); nothing is
 * downloaded or cached.</p>
 */
@Singleton
@SuppressWarnings("all")
public class RemoteScopeCache {
  @Data
  public static class CacheEntry {
    private final String source;

    private final String dir;

    public CacheEntry(final String source, final String dir) {
      super();
      this.source = source;
      this.dir = dir;
    }

    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
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
      b.add("source", this.source);
      b.add("dir", this.dir);
      return b.toString();
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
   * In-memory cache index per cache directory: key {@code source} (the Maven GAV) &rarr; entry.
   */
  private final Map<File, Map<String, RemoteScopeCache.CacheEntry>> indexByDir = CollectionLiterals.<File, Map<String, RemoteScopeCache.CacheEntry>>newHashMap();

  /**
   * Returns the local URIs of the <code>.cqrs</code> models that provide the given namespace,
   * downloading and caching the Maven artifact on a miss (or reading a {@code local} directory
   * directly), or an empty list when nothing is configured (so the caller falls back to the
   * standard mechanism).
   */
  public List<URI> getCachedModelUris(final ResourceSet rs, final URI modelUri, final String namespace, final RemoteScopeCatalog catalog) {
    final RemoteScopeEntry entry = catalog.lookupEntry(rs, modelUri, namespace);
    if ((entry == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    final URI root = catalog.rootDir(rs, modelUri);
    if ((root == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(entry.getLocal());
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      return this.localModelUris(rs, root, entry.getLocal());
    }
    final File cacheDir = this.toFile(rs, root.appendSegment(RemoteScopeCache.CACHE_DIR_NAME));
    if ((cacheDir == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    final String source = entry.getSourceId();
    boolean _isNullOrEmpty_1 = StringExtensions.isNullOrEmpty(source);
    if (_isNullOrEmpty_1) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    String _artifactId = entry.getArtifactId();
    String _plus = (_artifactId + "-");
    String _version = entry.getVersion();
    String _plus_1 = (_plus + _version);
    String _plus_2 = (_plus_1 + "-");
    String _sha1 = this.sha1(source);
    final String dirName = (_plus_2 + _sha1);
    final File targetDir = new File(cacheDir, dirName);
    final Map<String, RemoteScopeCache.CacheEntry> index = this.indexFor(cacheDir);
    final RemoteScopeCache.CacheEntry existing = index.get(source);
    if ((((existing != null) && Objects.equals(existing.source, source)) && targetDir.isDirectory())) {
      final List<URI> cached = this.cqrsFiles(targetDir);
      boolean _isEmpty = cached.isEmpty();
      boolean _not_1 = (!_isEmpty);
      if (_not_1) {
        return cached;
      }
    }
    this.materialize(entry, targetDir);
    RemoteScopeCache.CacheEntry _cacheEntry = new RemoteScopeCache.CacheEntry(source, dirName);
    index.put(source, _cacheEntry);
    this.persist(cacheDir, index);
    return this.cqrsFiles(targetDir);
  }

  /**
   * Reads the <code>.cqrs</code> files of a {@code local} directory (relative to the catalog root).
   */
  private List<URI> localModelUris(final ResourceSet rs, final URI root, final String local) {
    final File candidate = new File(local);
    File _xifexpression = null;
    boolean _isAbsolute = candidate.isAbsolute();
    if (_isAbsolute) {
      _xifexpression = candidate;
    } else {
      File _xblockexpression = null;
      {
        final File rootFile = this.toFile(rs, root);
        File _xifexpression_1 = null;
        if ((rootFile == null)) {
          _xifexpression_1 = null;
        } else {
          _xifexpression_1 = new File(rootFile, local);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    final File dir = _xifexpression;
    List<URI> _xifexpression_1 = null;
    if (((dir != null) && dir.isDirectory())) {
      _xifexpression_1 = this.cqrsFiles(dir);
    } else {
      _xifexpression_1 = Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    return _xifexpression_1;
  }

  /**
   * Downloads and unpacks the artifact's model(s) into <code>targetDir</code>, replacing stale content.
   */
  private void materialize(final RemoteScopeEntry entry, final File targetDir) {
    try {
      this.cleanDir(targetDir);
      targetDir.mkdirs();
      final InputStream stream = new MavenArtifactResolver().openArtifact(entry.getGroupId(), entry.getArtifactId(), entry.getVersion());
      try {
        TarGz.extractCqrsFiles(stream, targetDir);
      } finally {
        stream.close();
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
                if ((obj.has("source") && obj.has("dir"))) {
                  String _asString = obj.get("source").getAsString();
                  String _asString_1 = obj.get("dir").getAsString();
                  final RemoteScopeCache.CacheEntry entry = new RemoteScopeCache.CacheEntry(_asString, _asString_1);
                  map.put(entry.source, entry);
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

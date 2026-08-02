package org.fuin.dsl.cqrs.scoping;

import com.google.inject.Singleton;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

/**
 * Turns a <code>dependency</code> into the URIs of the <code>.cqrs</code> models it provides.
 * 
 * <p>A Maven dependency is resolved to its zip in the local repository by the
 * {@link CqrsArtifactResolver} of the environment, and the models are then addressed
 * <em>inside</em> that archive:</p>
 * 
 * <pre>archive:file:/home/me/.m2/repository/.../cqrs-common-model-0.1.0-SNAPSHOT.zip!/model/public/types.cqrs</pre>
 * 
 * <p>Nothing is ever unpacked. EMF resolves an <code>archive:</code> URI out of the box, the last
 * segment still ends in <code>.cqrs</code> so Xtext's resource factory applies as usual, and the local
 * Maven repository is the only cache there is. Only entries below {@link CqrsArtifactResolver#MODEL_DIR}
 * are considered, and they are taken recursively so a model may sit in a sub folder.</p>
 * 
 * <p>A dependency with a <code>local</code> clause skips all of that and reads a directory of
 * <code>.cqrs</code> files directly, relative to the model that declares it.</p>
 * 
 * <p>Resolution of an artifact is remembered for the session - success and failure alike - so a
 * coordinate that cannot be resolved is attempted once rather than on every validation run. The flip
 * side is that an artifact appearing later is picked up only after a restart.</p>
 */
@Singleton
@SuppressWarnings("all")
public class CqrsModelArchives {
  private static final Logger LOG = Logger.getLogger(CqrsModelArchives.class);

  /**
   * Model URIs of an artifact, keyed by {@link RemoteScopeEntry#getSourceId}.
   */
  private final Map<String, List<URI>> resolved = CollectionLiterals.<String, List<URI>>newHashMap();

  /**
   * Why an artifact could not be resolved, keyed by {@link RemoteScopeEntry#getSourceId}.
   */
  private final Map<String, String> problems = CollectionLiterals.<String, String>newHashMap();

  /**
   * The models the given dependency provides, or an empty list when it cannot be resolved (the
   * caller degrades to "nothing extra is visible" and the validator reports the problem).
   * 
   * @param rs Resource set, used to turn a platform URI into a file.
   * @param modelUri URI of the model declaring the dependency - a <code>local</code> path is
   *            relative to its directory.
   * @param entry Dependency to resolve.
   * 
   * @return Model URIs, never <code>null</code>.
   */
  public List<URI> modelUris(final ResourceSet rs, final URI modelUri, final RemoteScopeEntry entry) {
    if ((entry == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(entry.getLocal());
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      return this.localModelUris(rs, modelUri, entry);
    }
    return this.artifactModelUris(entry);
  }

  /**
   * Why the given dependency cannot be resolved, or <code>null</code> when it resolves.
   * 
   * @param rs Resource set.
   * @param modelUri URI of the model declaring the dependency.
   * @param entry Dependency to check.
   * 
   * @return Problem description, or <code>null</code>.
   */
  public String problem(final ResourceSet rs, final URI modelUri, final RemoteScopeEntry entry) {
    if ((entry == null)) {
      return null;
    }
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(entry.getLocal());
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      String _xifexpression = null;
      boolean _isEmpty = this.localModelUris(rs, modelUri, entry).isEmpty();
      if (_isEmpty) {
        String _local = entry.getLocal();
        String _plus = ("the local directory \'" + _local);
        _xifexpression = (_plus + "\' does not exist or holds no \'.cqrs\' files");
      } else {
        _xifexpression = null;
      }
      return _xifexpression;
    }
    this.artifactModelUris(entry);
    return this.problems.get(entry.getSourceId());
  }

  /**
   * Forgets what was resolved, so the next call resolves again.
   */
  public void invalidate() {
    this.resolved.clear();
    this.problems.clear();
  }

  private List<URI> artifactModelUris(final RemoteScopeEntry entry) {
    final String key = entry.getSourceId();
    if ((key == null)) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    boolean _containsKey = this.resolved.containsKey(key);
    if (_containsKey) {
      return this.resolved.get(key);
    }
    boolean _containsKey_1 = this.problems.containsKey(key);
    if (_containsKey_1) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
    try {
      final Path archive = CqrsArtifactResolvers.get().resolve(entry.getGroupId(), entry.getArtifactId(), entry.getVersion());
      if (((archive == null) || (!archive.toFile().isFile()))) {
        this.problems.put(key, "the artifact was not found in the local repository");
        return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
      }
      final List<URI> uris = this.entriesOf(archive.toFile());
      boolean _isEmpty = uris.isEmpty();
      if (_isEmpty) {
        this.problems.put(key, 
          (("the artifact holds no \'.cqrs\' files below \'" + CqrsArtifactResolver.MODEL_DIR) + "/\'"));
        return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
      }
      this.resolved.put(key, uris);
      return uris;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        final String message = this.message(ex);
        CqrsModelArchives.LOG.error(((("Could not resolve dependency \'" + key) + "\': ") + message), ex);
        this.problems.put(key, message);
        return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  /**
   * Every <code>.cqrs</code> below <code>model/</code>, as an <code>archive:</code> URI.
   */
  private List<URI> entriesOf(final File archive) {
    try {
      final String prefix = (CqrsArtifactResolver.MODEL_DIR + "/");
      final URI archiveUri = URI.createFileURI(archive.getAbsolutePath());
      final ArrayList<URI> result = CollectionLiterals.<URI>newArrayList();
      final ZipFile zip = new ZipFile(archive);
      try {
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
          {
            final ZipEntry entry = entries.nextElement();
            final String name = entry.getName();
            if ((((!entry.isDirectory()) && name.startsWith(prefix)) && name.endsWith(".cqrs"))) {
              result.add(URI.createURI(((("archive:" + archiveUri) + "!/") + name)));
            }
          }
        }
      } finally {
        zip.close();
      }
      final Function1<URI, String> _function = (URI it) -> {
        return it.toString();
      };
      ListExtensions.<URI, String>sortInplaceBy(result, _function);
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * The <code>.cqrs</code> files of a <code>local</code> directory, relative to the declaring model.
   */
  private List<URI> localModelUris(final ResourceSet rs, final URI modelUri, final RemoteScopeEntry entry) {
    String _local = entry.getLocal();
    final File candidate = new File(_local);
    File _xifexpression = null;
    boolean _isAbsolute = candidate.isAbsolute();
    if (_isAbsolute) {
      _xifexpression = candidate;
    } else {
      File _xblockexpression = null;
      {
        URI _trimSegments = null;
        if (modelUri!=null) {
          _trimSegments=modelUri.trimSegments(1);
        }
        final File base = this.toFile(rs, _trimSegments);
        File _xifexpression_1 = null;
        if ((base == null)) {
          _xifexpression_1 = null;
        } else {
          String _local_1 = entry.getLocal();
          _xifexpression_1 = new File(base, _local_1);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    final File dir = _xifexpression;
    if (((dir == null) || (!dir.isDirectory()))) {
      return Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList());
    }
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

  private File toFile(final ResourceSet rs, final URI uri) {
    if (((uri == null) || (uri.segmentCount() < 1))) {
      return null;
    }
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
   * The most telling message of a failure chain - the outermost one is often just a wrapper.
   */
  private String message(final Throwable ex) {
    Throwable current = ex;
    String result = null;
    while ((current != null)) {
      {
        boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(current.getMessage());
        boolean _not = (!_isNullOrEmpty);
        if (_not) {
          result = current.getMessage();
        }
        current = current.getCause();
      }
    }
    String _elvis = null;
    if (result != null) {
      _elvis = result;
    } else {
      String _simpleName = ex.getClass().getSimpleName();
      _elvis = _simpleName;
    }
    return _elvis;
  }
}

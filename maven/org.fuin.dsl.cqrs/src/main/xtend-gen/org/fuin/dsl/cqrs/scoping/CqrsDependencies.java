package org.fuin.dsl.cqrs.scoping;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.Dependency;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;

/**
 * Resolves the <code>dependency</code> declarations of a model to the <code>.cqrs</code> files they
 * provide.
 * 
 * <p>A dependency may be declared on a <code>context</code> - where it applies to every module of
 * that context - or on a single <code>module</code>. Because a context block may be split across
 * several files (all blocks with the same name denote the same logical context), the context level
 * dependencies are collected from <em>every</em> same-named context block in the resource set, not
 * only from the block the resource happens to contain. This mirrors how the generator finds a
 * context's "SrcGen4J" hint.</p>
 * 
 * <p>This class is shared by {@link CqrsDslGlobalScopeProvider} (which loads the models) and
 * {@link CqrsDslLocalScopeProvider} (which derives the visible modules from them), so
 * the two can never disagree about what a model depends on.</p>
 */
@Singleton
@SuppressWarnings("all")
public class CqrsDependencies {
  private static final Logger LOG = Logger.getLogger(CqrsDependencies.class);

  @Inject
  private CqrsModelArchives archives;

  @Inject
  private IQualifiedNameProvider qualifiedNameProvider;

  /**
   * All dependencies that apply to the given resource: those of every context block sharing a name
   * with a context declared here, plus those of the modules declared here. Duplicates (the same
   * coordinate declared more than once) are collapsed so an artifact is resolved only once.
   */
  public Collection<Dependency> declared(final Resource resource) {
    final LinkedHashMap<String, Dependency> result = CollectionLiterals.<String, Dependency>newLinkedHashMap();
    final ResourceSet rs = resource.getResourceSet();
    if ((rs == null)) {
      return result.values();
    }
    final LinkedHashSet<String> contextNames = CollectionLiterals.<String>newLinkedHashSet();
    Iterable<DomainModel> _filter = Iterables.<DomainModel>filter(resource.getContents(), DomainModel.class);
    for (final DomainModel model : _filter) {
      EList<Context> _contexts = model.getContexts();
      for (final Context context : _contexts) {
        {
          String _name = context.getName();
          boolean _tripleNotEquals = (_name != null);
          if (_tripleNotEquals) {
            contextNames.add(context.getName());
          }
          EList<Dependency> _dependencies = context.getDependencies();
          for (final Dependency dependency : _dependencies) {
            this.put(result, dependency);
          }
          EList<org.fuin.dsl.cqrs.cqrsDsl.Module> _modules = context.getModules();
          for (final org.fuin.dsl.cqrs.cqrsDsl.Module module : _modules) {
            EList<Dependency> _dependencies_1 = module.getDependencies();
            for (final Dependency dependency_1 : _dependencies_1) {
              this.put(result, dependency_1);
            }
          }
        }
      }
    }
    boolean _isEmpty = contextNames.isEmpty();
    if (_isEmpty) {
      return result.values();
    }
    EList<Resource> _resources = rs.getResources();
    ArrayList<Resource> _arrayList = new ArrayList<Resource>(_resources);
    for (final Resource other : _arrayList) {
      if (((other != resource) && other.isLoaded())) {
        Iterable<DomainModel> _filter_1 = Iterables.<DomainModel>filter(other.getContents(), DomainModel.class);
        for (final DomainModel model_1 : _filter_1) {
          EList<Context> _contexts_1 = model_1.getContexts();
          for (final Context context_1 : _contexts_1) {
            boolean _contains = contextNames.contains(context_1.getName());
            if (_contains) {
              EList<Dependency> _dependencies = context_1.getDependencies();
              for (final Dependency dependency : _dependencies) {
                this.put(result, dependency);
              }
            }
          }
        }
      }
    }
    return result.values();
  }

  private void put(final Map<String, Dependency> target, final Dependency dependency) {
    final String key = this.key(dependency);
    if (((key != null) && (!target.containsKey(key)))) {
      target.put(key, dependency);
    }
  }

  /**
   * Identity of a dependency: its coordinate plus any local override.
   */
  private String key(final Dependency dependency) {
    String _coordinate = null;
    if (dependency!=null) {
      _coordinate=dependency.getCoordinate();
    }
    boolean _tripleEquals = (_coordinate == null);
    if (_tripleEquals) {
      return null;
    }
    String _coordinate_1 = dependency.getCoordinate();
    String _plus = (_coordinate_1 + "|");
    String _elvis = null;
    String _local = dependency.getLocal();
    if (_local != null) {
      _elvis = _local;
    } else {
      _elvis = "";
    }
    return (_plus + _elvis);
  }

  /**
   * URIs of every <code>.cqrs</code> model the resource's dependencies provide - entries inside the
   * artifact's zip in the local repository, or files of a <code>local</code> directory. A malformed
   * coordinate contributes nothing (the validator reports it); a failing dependency is skipped so
   * the rest still resolve.
   */
  public List<URI> modelUris(final Resource resource) {
    final LinkedHashSet<URI> result = CollectionLiterals.<URI>newLinkedHashSet();
    Collection<Dependency> _declared = this.declared(resource);
    for (final Dependency dependency : _declared) {
      {
        final RemoteScopeEntry entry = RemoteScopeEntry.parse(dependency.getCoordinate(), dependency.getLocal());
        if ((entry == null)) {
          String _coordinate = dependency.getCoordinate();
          String _plus = ("Ignoring malformed dependency coordinate: " + _coordinate);
          CqrsDependencies.LOG.warn(_plus);
        } else {
          try {
            result.addAll(this.archives.modelUris(resource.getResourceSet(), this.declaringUri(dependency, resource), entry));
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception ex = (Exception)_t;
              String _coordinate_1 = dependency.getCoordinate();
              String _plus_1 = ("Could not resolve dependency \'" + _coordinate_1);
              String _plus_2 = (_plus_1 + "\': ");
              String _message = ex.getMessage();
              String _plus_3 = (_plus_2 + _message);
              CqrsDependencies.LOG.error(_plus_3, ex);
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      }
    }
    return new ArrayList<URI>(result);
  }

  /**
   * Why the given dependency cannot be resolved, or <code>null</code> when it resolves. A malformed
   * coordinate reports nothing here - {@code checkDependencyCoordinate} already covers it - and
   * neither does a model without a usable location on disk, where resolution is never attempted.
   */
  public String resolutionProblem(final Dependency dependency) {
    Resource _eResource = null;
    if (dependency!=null) {
      _eResource=dependency.eResource();
    }
    final Resource resource = _eResource;
    if (((resource == null) || (resource.getResourceSet() == null))) {
      return null;
    }
    final RemoteScopeEntry entry = RemoteScopeEntry.parse(dependency.getCoordinate(), dependency.getLocal());
    if ((entry == null)) {
      return null;
    }
    try {
      return this.archives.problem(resource.getResourceSet(), this.declaringUri(dependency, resource), entry);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        String _coordinate = dependency.getCoordinate();
        String _plus = ("Could not check dependency \'" + _coordinate);
        String _plus_1 = (_plus + "\': ");
        String _message = ex.getMessage();
        String _plus_2 = (_plus_1 + _message);
        CqrsDependencies.LOG.error(_plus_2, ex);
        return null;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }

  /**
   * The <code>context.module</code> names the dependency models declare. These are the scopes a
   * <code>dependency</code> makes implicitly visible.
   */
  public Iterable<QualifiedName> providedScopes(final Resource resource) {
    final LinkedHashSet<QualifiedName> result = CollectionLiterals.<QualifiedName>newLinkedHashSet();
    final ResourceSet rs = resource.getResourceSet();
    if ((rs == null)) {
      return result;
    }
    List<URI> _modelUris = this.modelUris(resource);
    for (final URI uri : _modelUris) {
      try {
        final Resource remote = rs.getResource(uri, true);
        Iterable<DomainModel> _filter = Iterables.<DomainModel>filter(remote.getContents(), DomainModel.class);
        for (final DomainModel model : _filter) {
          EList<Context> _contexts = model.getContexts();
          for (final Context context : _contexts) {
            {
              this.add(result, context);
              EList<org.fuin.dsl.cqrs.cqrsDsl.Module> _modules = context.getModules();
              for (final org.fuin.dsl.cqrs.cqrsDsl.Module module : _modules) {
                this.add(result, module);
              }
            }
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception ex = (Exception)_t;
          String _message = ex.getMessage();
          String _plus = ((("Could not read dependency model \'" + uri) + "\': ") + _message);
          CqrsDependencies.LOG.error(_plus, ex);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    return result;
  }

  private void add(final Set<QualifiedName> target, final EObject obj) {
    final QualifiedName name = this.qualifiedNameProvider.getFullyQualifiedName(obj);
    if ((name != null)) {
      target.add(name);
    }
  }

  /**
   * URI a 'local' directory is resolved against: the file that declares the dependency.
   */
  private URI declaringUri(final Dependency dependency, final Resource fallback) {
    URI _elvis = null;
    Resource _eResource = dependency.eResource();
    URI _uRI = null;
    if (_eResource!=null) {
      _uRI=_eResource.getURI();
    }
    if (_uRI != null) {
      _elvis = _uRI;
    } else {
      URI _uRI_1 = fallback.getURI();
      _elvis = _uRI_1;
    }
    return _elvis;
  }
}

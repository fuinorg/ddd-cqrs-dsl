package org.fuin.dsl.cqrs.scoping;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.DefaultGlobalScopeProvider;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Import;
import org.fuin.dsl.cqrs.cqrsDsl.Namespace;

/**
 * Global scope provider that makes elements of remote (HTTP-only) <code>.cqrs</code> models
 * resolvable.
 * 
 * <p>For every <code>import</code> in the model it consults the {@link RemoteScopeCatalog} using the
 * (enclosing context name, imported namespace) coordinate. Configured coordinates are downloaded
 * once and cached on disk by the {@link RemoteScopeCache}; their objects are then exposed by fully
 * qualified name on top of the standard global scope. Coordinates that are not configured add
 * nothing, so resolution falls back to the normal file-based mechanism. Any failure (missing
 * catalog, offline and uncached, parse error) is logged and degrades gracefully to the local scope
 * so editing and generation never break.</p>
 */
@SuppressWarnings("all")
public class CqrsDslGlobalScopeProvider extends DefaultGlobalScopeProvider {
  private static final Logger LOG = Logger.getLogger(CqrsDslGlobalScopeProvider.class);

  @Inject
  private IQualifiedNameProvider qualifiedNameProvider;

  @Inject
  private RemoteScopeCatalog catalog;

  @Inject
  private RemoteScopeCache cache;

  @Override
  protected IScope getScope(final Resource resource, final boolean ignoreCase, final EClass type, final Predicate<IEObjectDescription> filter) {
    final IScope parent = super.getScope(resource, ignoreCase, type, filter);
    final ResourceSet rs = resource.getResourceSet();
    if ((rs == null)) {
      return parent;
    }
    try {
      final LinkedHashSet<URI> remoteUris = CollectionLiterals.<URI>newLinkedHashSet();
      Iterable<DomainModel> _filter = Iterables.<DomainModel>filter(resource.getContents(), DomainModel.class);
      for (final DomainModel model : _filter) {
        EList<Context> _contexts = model.getContexts();
        for (final Context context : _contexts) {
          EList<Namespace> _namespaces = context.getNamespaces();
          for (final Namespace namespace : _namespaces) {
            EList<Import> _imports = namespace.getImports();
            for (final Import import_ : _imports) {
              {
                final URI uri = this.cache.getCachedModelUri(rs, resource.getURI(), context.getName(), 
                  import_.getImportedNamespace(), this.catalog);
                if ((uri != null)) {
                  remoteUris.add(uri);
                }
              }
            }
          }
        }
      }
      boolean _isEmpty = remoteUris.isEmpty();
      if (_isEmpty) {
        return parent;
      }
      final ArrayList<EObject> objects = CollectionLiterals.<EObject>newArrayList();
      for (final URI uri : remoteUris) {
        {
          final TreeIterator<EObject> contents = rs.getResource(uri, true).getAllContents();
          while (contents.hasNext()) {
            {
              final EObject object = contents.next();
              boolean _isInstance = type.isInstance(object);
              if (_isInstance) {
                objects.add(object);
              }
            }
          }
        }
      }
      final Function<EObject, QualifiedName> _function = (EObject it) -> {
        return this.qualifiedNameProvider.getFullyQualifiedName(it);
      };
      return Scopes.<EObject>scopeFor(objects, _function, parent);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        String _message = ex.getMessage();
        String _plus = ("Remote scope resolution failed; using local scope only: " + _message);
        CqrsDslGlobalScopeProvider.LOG.error(_plus, ex);
        return parent;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
}

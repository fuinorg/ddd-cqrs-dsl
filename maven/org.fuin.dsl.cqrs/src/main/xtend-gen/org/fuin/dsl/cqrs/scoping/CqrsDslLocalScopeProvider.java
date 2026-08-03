package org.fuin.dsl.cqrs.scoping;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.ISelectable;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Import;

/**
 * Turns the declared <code>import</code> statements into the resolvers that decide which names are
 * visible under their simple name.
 * 
 * <p>A <code>module</code> is the unit of visibility. Without any import only the module's own
 * elements can be addressed by their simple name - everything else, including a sibling module of
 * the very same context, has to be imported:</p>
 * 
 * <ul>
 * <li>inside a <code>module</code> &mdash; that module itself, so a module split across several
 * files still sees all of its own elements, plus everything the module imports;</li>
 * <li>inside a <code>context</code> &mdash; everything the context imports, which every module of
 * that context therefore inherits;</li>
 * <li>at the model root &mdash; nothing. A <code>dependency</code> only makes the models of another
 * project <em>resolvable</em>; an <code>import</code> decides what of them is visible.</li>
 * </ul>
 * 
 * <p>Because {@code ImportedNamespaceAwareLocalScopeProvider} applies these per container from the
 * outside in, the innermost block wins: an import of the module shadows a same named one the
 * context contributes. A name that really is ambiguous at one level resolves to nothing rather than
 * to an arbitrary pick, and has to be qualified. A fully qualified reference always resolves through
 * the global scope, with or without a matching import.</p>
 * 
 * <p>An import may end in a wildcard on any level - <code>ctx.*</code>, <code>ctx.mod.*</code> or the
 * single type <code>ctx.mod.Type</code>. A wildcard {@link ImportNormalizer} maps exactly <em>one</em>
 * segment and does not recurse, and both a context and a module name may themselves be dotted, so a
 * wildcard import cannot simply be handed over as it is written: besides the literal prefix, one
 * resolver per module whose qualified name starts with that prefix is added. Those modules are read
 * from the Xtext index rather than from the resource set, so the result is complete in the Eclipse
 * editor (builder state, live shadowed for dirty editors) as well as in a headless SrcGen4J run
 * (where the index is backed by the resource set) - plus the modules a declared
 * <code>dependency</code> provides, which no IDE index holds because they live inside an artifact's
 * zip. Note that the result is memoized per resource, so adding a module in one file may need the
 * referencing file to be re-parsed before it is seen.</p>
 */
@SuppressWarnings("all")
public class CqrsDslLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {
  private static final Logger LOG = Logger.getLogger(CqrsDslLocalScopeProvider.class);

  @Inject
  private IQualifiedNameProvider qualifiedNameProvider;

  @Inject
  private ResourceDescriptionsProvider resourceDescriptionsProvider;

  @Inject
  private IContainer.Manager containerManager;

  @Inject
  private CqrsDependencies dependencies;

  @Override
  protected List<ImportNormalizer> internalGetImportedNamespaceResolvers(final EObject obj, final boolean ignoreCase) {
    try {
      boolean _matched = false;
      if (obj instanceof DomainModel) {
        _matched=true;
        return CollectionLiterals.<ImportNormalizer>emptyList();
      }
      if (!_matched) {
        if (obj instanceof Context) {
          _matched=true;
          return this.resolvers(((Context)obj).getImports(), obj, ignoreCase);
        }
      }
      if (!_matched) {
        if (obj instanceof org.fuin.dsl.cqrs.cqrsDsl.Module) {
          _matched=true;
          return this.resolvers(((org.fuin.dsl.cqrs.cqrsDsl.Module)obj).getImports(), obj, ignoreCase);
        }
      }
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        String _message = ex.getMessage();
        String _plus = ((("Could not compute imports for " + obj) + ": ") + _message);
        CqrsDslLocalScopeProvider.LOG.error(_plus, ex);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return CollectionLiterals.<ImportNormalizer>emptyList();
  }

  /**
   * What the scope of a block's own elements is read from - the index rather than the one file the
   * block happens to be written in.
   * 
   * <p>The inherited implementation answers with the contents of {@code resource} alone, which
   * assumes a module lives in a single file. A module may be spread over several - a model that
   * publishes only part of itself has to be - and then the half using a name is not necessarily the
   * half declaring it. Reading from the index makes a module's own elements visible to all of its
   * halves, and keeps them shadowing its imports: this scope is nested inside the one carrying the
   * imports, so a name the module declares itself still wins over an imported one.</p>
   * 
   * <p>Only for a model the index knows, though. A model read out of a dependency's archive is in no
   * index, and answering with one would leave it unable to see even the elements of its own file -
   * while offering it the names of a project it is not part of. There the inherited implementation is
   * the right one.</p>
   */
  @Override
  protected ISelectable getAllDescriptions(final Resource resource) {
    ResourceSet _resourceSet = null;
    if (resource!=null) {
      _resourceSet=resource.getResourceSet();
    }
    final ResourceSet resourceSet = _resourceSet;
    if ((resourceSet == null)) {
      return super.getAllDescriptions(resource);
    }
    final IResourceDescriptions descriptions = this.resourceDescriptionsProvider.getResourceDescriptions(resourceSet);
    IResourceDescription _resourceDescription = descriptions.getResourceDescription(resource.getURI());
    boolean _tripleEquals = (_resourceDescription == null);
    if (_tripleEquals) {
      return super.getAllDescriptions(resource);
    }
    return descriptions;
  }

  /**
   * Turns every declared import of a block into its resolvers.
   */
  private List<ImportNormalizer> resolvers(final Iterable<Import> imports, final EObject context, final boolean ignoreCase) {
    final ArrayList<ImportNormalizer> result = CollectionLiterals.<ImportNormalizer>newArrayList();
    for (final Import imp : imports) {
      result.addAll(this.resolvers(imp.getImportedNamespace(), context, ignoreCase));
    }
    return result;
  }

  /**
   * The resolvers a single imported name contributes. The inherited
   * {@link #createImportedNamespaceResolver(String, boolean)} already turns the written name into
   * the one normalizer Xtext would use - a plain one for <code>ctx.mod.Type</code>, a wildcard one
   * for the prefix of <code>ctx.mod.*</code>. Only the wildcard case needs more, because such a
   * normalizer covers a single segment: one additional resolver per module below the prefix.
   */
  private List<ImportNormalizer> resolvers(final String importedNamespace, final EObject context, final boolean ignoreCase) {
    final ArrayList<ImportNormalizer> result = CollectionLiterals.<ImportNormalizer>newArrayList();
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(importedNamespace);
    if (_isNullOrEmpty) {
      return result;
    }
    final ImportNormalizer normalizer = this.createImportedNamespaceResolver(importedNamespace, ignoreCase);
    if ((normalizer == null)) {
      return result;
    }
    result.add(normalizer);
    boolean _hasWildCard = normalizer.hasWildCard();
    if (_hasWildCard) {
      Iterable<QualifiedName> _modulesBelow = this.modulesBelow(normalizer.getImportedNamespacePrefix(), context);
      for (final QualifiedName module : _modulesBelow) {
        result.add(this.doCreateImportNormalizer(module, true, ignoreCase));
      }
    }
    return result;
  }

  /**
   * Every module whose qualified name starts with (but is not equal to) the given prefix. This is
   * what makes <code>context.*</code> reach the types of a module, and what covers a module name
   * that is itself dotted.
   * 
   * <p>Two sources, because neither alone is complete: the index, which holds the workspace (and in
   * an IDE nothing else), and the modules the declared dependencies provide, which an IDE index
   * never holds because they are read out of an artifact's zip. A <em>module</em> level import of a
   * dependency happens to work without the second - the literal {@link ImportNormalizer} already
   * maps that one name - but a <em>context</em> level one needs the per-module resolvers.</p>
   */
  private Iterable<QualifiedName> modulesBelow(final QualifiedName prefix, final EObject context) {
    final LinkedHashSet<QualifiedName> result = CollectionLiterals.<QualifiedName>newLinkedHashSet();
    final Resource resource = context.eResource();
    ResourceSet _resourceSet = null;
    if (resource!=null) {
      _resourceSet=resource.getResourceSet();
    }
    final ResourceSet resourceSet = _resourceSet;
    if ((resourceSet == null)) {
      return result;
    }
    final IResourceDescriptions descriptions = this.resourceDescriptionsProvider.getResourceDescriptions(resourceSet);
    final IResourceDescription self = descriptions.getResourceDescription(resource.getURI());
    if ((self != null)) {
      List<IContainer> _visibleContainers = this.containerManager.getVisibleContainers(self, descriptions);
      for (final IContainer container : _visibleContainers) {
        Iterable<IEObjectDescription> _exportedObjectsByType = container.getExportedObjectsByType(CqrsDslPackage.Literals.MODULE);
        for (final IEObjectDescription description : _exportedObjectsByType) {
          {
            final QualifiedName name = description.getName();
            if ((((name != null) && (!Objects.equals(name, prefix))) && name.startsWith(prefix))) {
              result.add(name);
            }
          }
        }
      }
    }
    Iterable<QualifiedName> _providedModules = this.dependencies.providedModules(resource);
    for (final QualifiedName name : _providedModules) {
      if (((!Objects.equals(name, prefix)) && name.startsWith(prefix))) {
        result.add(name);
      }
    }
    return result;
  }
}

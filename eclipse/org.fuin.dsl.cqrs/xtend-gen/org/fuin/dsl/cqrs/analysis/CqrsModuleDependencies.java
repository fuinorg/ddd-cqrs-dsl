package org.fuin.dsl.cqrs.analysis;

import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions;
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives;

/**
 * Which module depends on which, read from the model rather than from its <code>import</code> lines.
 * 
 * <p><b>An import is not a dependency.</b> An import states what a module <em>may</em> address; the
 * dependency is what it actually addresses. The two differ in both directions - an unused import
 * claims a dependency that is not there, and a fully qualified reference creates one with no import
 * at all. Anything deciding whether a module can be switched off has to go by the second.</p>
 * 
 * <p>So an edge is a <b>resolved cross reference</b>: an attribute typed with another module's value
 * object, a command whose <code>target</code> is another module's method, a view over another
 * module's projection, a process manager that <code>reacts-to</code> another module's event. The walk
 * is the one {@code CqrsDslValidator.referencedNames} performs for the unused-import warning, and that
 * method delegates here so the two can never disagree about what a module refers to - the same
 * reasoning {@link org.fuin.dsl.cqrs.scoping.CqrsDependencies} gives for being shared between the two
 * scope providers.</p>
 * 
 * <p>References are <em>resolved</em> ({@code eGet(reference, true)}) rather than read as they lie.
 * Xtext links lazily, so an unresolved proxy would read as "refers to nothing" and silently drop an
 * edge - and whether the editor happened to resolve a name already, because a hover asked for it,
 * must not decide what the graph says.</p>
 * 
 * <p>Types from an archived dependency model are not edges: they belong to another artifact, which is
 * a dependency of the whole context rather than of one module of it.</p>
 */
@SuppressWarnings("all")
public class CqrsModuleDependencies {
  private CqrsModuleDependencies() {
  }

  /**
   * The modules the given one depends on, excluding itself.
   * 
   * @param module Module to inspect.
   * 
   * @return Modules it addresses, in encounter order, without duplicates.
   */
  public static Set<org.fuin.dsl.cqrs.cqrsDsl.Module> dependenciesOf(final org.fuin.dsl.cqrs.cqrsDsl.Module module) {
    final Set<org.fuin.dsl.cqrs.cqrsDsl.Module> result = new LinkedHashSet<org.fuin.dsl.cqrs.cqrsDsl.Module>();
    if ((module == null)) {
      return result;
    }
    Iterable<EObject> _referencedObjects = CqrsModuleDependencies.referencedObjects(module);
    for (final EObject target : _referencedObjects) {
      {
        final org.fuin.dsl.cqrs.cqrsDsl.Module other = CqrsEObjectExtensions.getModule(target);
        if (((other != null) && (other != module))) {
          result.add(other);
        }
      }
    }
    return result;
  }

  /**
   * The whole graph of a parsed model, keyed by module.
   * 
   * @param resourceSet Fully parsed model.
   * 
   * @return Every module of the model and what it depends on. Never null.
   */
  public static Map<org.fuin.dsl.cqrs.cqrsDsl.Module, Set<org.fuin.dsl.cqrs.cqrsDsl.Module>> graphOf(final ResourceSet resourceSet) {
    final Map<org.fuin.dsl.cqrs.cqrsDsl.Module, Set<org.fuin.dsl.cqrs.cqrsDsl.Module>> result = new LinkedHashMap<org.fuin.dsl.cqrs.cqrsDsl.Module, Set<org.fuin.dsl.cqrs.cqrsDsl.Module>>();
    if ((resourceSet == null)) {
      return result;
    }
    final Iterator<EObject> contents = Iterators.<EObject>filter(resourceSet.getAllContents(), EObject.class);
    while (contents.hasNext()) {
      {
        final EObject obj = contents.next();
        boolean _and = false;
        if (!(obj instanceof org.fuin.dsl.cqrs.cqrsDsl.Module)) {
          _and = false;
        } else {
          Resource _eResource = obj.eResource();
          URI _uRI = null;
          if (_eResource!=null) {
            _uRI=_eResource.getURI();
          }
          boolean _isArchived = CqrsModelArchives.isArchived(_uRI);
          boolean _not = (!_isArchived);
          _and = _not;
        }
        if (_and) {
          result.put(((org.fuin.dsl.cqrs.cqrsDsl.Module) obj), CqrsModuleDependencies.dependenciesOf(((org.fuin.dsl.cqrs.cqrsDsl.Module) obj)));
        }
      }
    }
    return result;
  }

  /**
   * The first dependency cycle found, as the path that closes it.
   * 
   * <p>Returned rather than reported, so a validator can mark it and a generator can fail on it
   * without either of them re-implementing the search. The path starts and ends at the same module,
   * so it reads as {@code a -> b -> a}.</p>
   * 
   * @param graph Graph to search, as {@link #graphOf(ResourceSet)} returns it.
   * 
   * @return The cycle, or an empty list when the graph is acyclic.
   */
  public static List<org.fuin.dsl.cqrs.cqrsDsl.Module> firstCycle(final Map<org.fuin.dsl.cqrs.cqrsDsl.Module, Set<org.fuin.dsl.cqrs.cqrsDsl.Module>> graph) {
    final Set<org.fuin.dsl.cqrs.cqrsDsl.Module> settled = new LinkedHashSet<org.fuin.dsl.cqrs.cqrsDsl.Module>();
    Set<org.fuin.dsl.cqrs.cqrsDsl.Module> _keySet = graph.keySet();
    for (final org.fuin.dsl.cqrs.cqrsDsl.Module start : _keySet) {
      boolean _contains = settled.contains(start);
      boolean _not = (!_contains);
      if (_not) {
        final List<org.fuin.dsl.cqrs.cqrsDsl.Module> path = new ArrayList<org.fuin.dsl.cqrs.cqrsDsl.Module>();
        final Set<org.fuin.dsl.cqrs.cqrsDsl.Module> onPath = new LinkedHashSet<org.fuin.dsl.cqrs.cqrsDsl.Module>();
        final List<org.fuin.dsl.cqrs.cqrsDsl.Module> found = CqrsModuleDependencies.walk(start, graph, settled, onPath, path);
        if ((found != null)) {
          return found;
        }
      }
    }
    return Collections.<org.fuin.dsl.cqrs.cqrsDsl.Module>emptyList();
  }

  /**
   * The cycle the given module takes part in, or an empty list when it takes part in none.
   * 
   * @param module Module to check.
   * @param graph Graph to search.
   * 
   * @return The cycle starting and ending at this module, or an empty list.
   */
  public static List<org.fuin.dsl.cqrs.cqrsDsl.Module> cycleThrough(final org.fuin.dsl.cqrs.cqrsDsl.Module module, final Map<org.fuin.dsl.cqrs.cqrsDsl.Module, Set<org.fuin.dsl.cqrs.cqrsDsl.Module>> graph) {
    final List<org.fuin.dsl.cqrs.cqrsDsl.Module> path = new ArrayList<org.fuin.dsl.cqrs.cqrsDsl.Module>();
    final Set<org.fuin.dsl.cqrs.cqrsDsl.Module> onPath = new LinkedHashSet<org.fuin.dsl.cqrs.cqrsDsl.Module>();
    LinkedHashSet<org.fuin.dsl.cqrs.cqrsDsl.Module> _linkedHashSet = new LinkedHashSet<org.fuin.dsl.cqrs.cqrsDsl.Module>();
    final List<org.fuin.dsl.cqrs.cqrsDsl.Module> found = CqrsModuleDependencies.walk(module, graph, _linkedHashSet, onPath, path);
    if (((found != null) && (found.get(0) == module))) {
      return found;
    }
    return Collections.<org.fuin.dsl.cqrs.cqrsDsl.Module>emptyList();
  }

  /**
   * Depth first search that returns the closing path as soon as it steps onto a module already on
   * the current path.
   */
  private static List<org.fuin.dsl.cqrs.cqrsDsl.Module> walk(final org.fuin.dsl.cqrs.cqrsDsl.Module current, final Map<org.fuin.dsl.cqrs.cqrsDsl.Module, Set<org.fuin.dsl.cqrs.cqrsDsl.Module>> graph, final Set<org.fuin.dsl.cqrs.cqrsDsl.Module> settled, final Set<org.fuin.dsl.cqrs.cqrsDsl.Module> onPath, final List<org.fuin.dsl.cqrs.cqrsDsl.Module> path) {
    boolean _contains = onPath.contains(current);
    if (_contains) {
      final int start = path.indexOf(current);
      List<org.fuin.dsl.cqrs.cqrsDsl.Module> _subList = path.subList(start, path.size());
      final List<org.fuin.dsl.cqrs.cqrsDsl.Module> cycle = new ArrayList<org.fuin.dsl.cqrs.cqrsDsl.Module>(_subList);
      cycle.add(current);
      return cycle;
    }
    boolean _contains_1 = settled.contains(current);
    if (_contains_1) {
      return null;
    }
    onPath.add(current);
    path.add(current);
    Set<org.fuin.dsl.cqrs.cqrsDsl.Module> _elvis = null;
    Set<org.fuin.dsl.cqrs.cqrsDsl.Module> _get = graph.get(current);
    if (_get != null) {
      _elvis = _get;
    } else {
      Set<org.fuin.dsl.cqrs.cqrsDsl.Module> _emptySet = Collections.<org.fuin.dsl.cqrs.cqrsDsl.Module>emptySet();
      _elvis = _emptySet;
    }
    for (final org.fuin.dsl.cqrs.cqrsDsl.Module next : _elvis) {
      {
        final List<org.fuin.dsl.cqrs.cqrsDsl.Module> found = CqrsModuleDependencies.walk(next, graph, settled, onPath, path);
        if ((found != null)) {
          return found;
        }
      }
    }
    int _size = path.size();
    int _minus = (_size - 1);
    path.remove(_minus);
    onPath.remove(current);
    settled.add(current);
    return null;
  }

  /**
   * Everything the cross references inside the given block resolve to.
   * 
   * <p>Public because the unused-import check needs the same targets, only to ask them for their
   * qualified name instead of their module.</p>
   * 
   * @param container Block to walk.
   * 
   * @return The resolved targets, proxies dropped.
   */
  public static Iterable<EObject> referencedObjects(final EObject container) {
    final List<EObject> result = new ArrayList<EObject>();
    if ((container == null)) {
      return result;
    }
    final TreeIterator<EObject> contents = container.eAllContents();
    while (contents.hasNext()) {
      {
        final EObject obj = contents.next();
        final Function1<EReference, Boolean> _function = (EReference it) -> {
          return Boolean.valueOf(((!it.isContainment()) && (!it.isDerived())));
        };
        Iterable<EReference> _filter = IterableExtensions.<EReference>filter(obj.eClass().getEAllReferences(), _function);
        for (final EReference reference : _filter) {
          {
            final Object value = obj.eGet(reference, true);
            List<?> _xifexpression = null;
            boolean _isMany = reference.isMany();
            if (_isMany) {
              _xifexpression = ((List<?>) value);
            } else {
              _xifexpression = Collections.<Object>singletonList(value);
            }
            final List<?> targets = _xifexpression;
            Iterable<?> _filterNull = IterableExtensions.filterNull(targets);
            for (final Object target : _filterNull) {
              if ((target instanceof EObject)) {
                boolean _eIsProxy = ((EObject)target).eIsProxy();
                boolean _not = (!_eIsProxy);
                if (_not) {
                  result.add(((EObject)target));
                }
              }
            }
          }
        }
      }
    }
    return result;
  }

  /**
   * The qualified names of everything the cross references inside the given block resolve to.
   * 
   * @param container Block to walk.
   * @param names Provider used to name a target.
   * 
   * @return The names, in encounter order.
   */
  public static Iterable<String> referencedNames(final EObject container, final IQualifiedNameProvider names) {
    final List<String> result = new ArrayList<String>();
    Iterable<EObject> _referencedObjects = CqrsModuleDependencies.referencedObjects(container);
    for (final EObject target : _referencedObjects) {
      {
        final QualifiedName name = names.getFullyQualifiedName(target);
        if ((name != null)) {
          result.add(name.toString());
        }
      }
    }
    return result;
  }
}

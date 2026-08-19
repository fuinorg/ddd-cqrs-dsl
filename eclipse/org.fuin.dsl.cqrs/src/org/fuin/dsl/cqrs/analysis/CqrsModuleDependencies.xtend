package org.fuin.dsl.cqrs.analysis

import java.util.ArrayList
import java.util.Collections
import java.util.LinkedHashSet
import java.util.List
import java.util.Map
import java.util.Set
import java.util.TreeMap
import java.util.TreeSet
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

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
 *
 * <p><b>A module is its name, not its block.</b> One module may be declared in more than one file - a
 * bounded context whose aggregates are split off has a block of the same name in both halves - and
 * those blocks are separate objects holding one logical module. The graph is therefore keyed by name
 * and the blocks are merged, or a module would be reported as depending on itself the moment its two
 * halves referred to each other.</p>
 */
class CqrsModuleDependencies {

    private new() {
    }

    /**
     * The names of the modules the given block depends on, excluding its own.
     *
     * @param module Module block to inspect.
     *
     * @return Module names it addresses, sorted, without duplicates.
     */
    def static Set<String> dependencyNamesOf(Module module) {
        val Set<String> result = new TreeSet<String>()
        if (module === null) {
            return result
        }
        for (target : referencedObjects(module)) {
            val other = target.module
            if (other !== null && other.name !== null && other.name != module.name) {
                result.add(other.name)
            }
        }
        return result
    }

    /**
     * The whole graph of a parsed model, keyed by module name.
     *
     * <p>Blocks sharing a name are one node: their dependencies are unioned, because a module split
     * over two files is one module.</p>
     *
     * @param resourceSet Fully parsed model.
     *
     * @return Every module of the model and what it depends on. Never null.
     */
    def static Map<String, Set<String>> graphOf(ResourceSet resourceSet) {
        val Map<String, Set<String>> result = new TreeMap<String, Set<String>>()
        if (resourceSet === null) {
            return result
        }
        // allContents yields Notifier, so narrow before walking - the same filter the permission
        // catalogue applies for the same reason.
        val contents = resourceSet.allContents.filter(typeof(EObject))
        while (contents.hasNext) {
            val EObject obj = contents.next
            if (obj instanceof Module && !CqrsModelArchives.isArchived(obj.eResource?.URI)) {
                val module = obj as Module
                if (module.name !== null) {
                    var known = result.get(module.name)
                    if (known === null) {
                        known = new TreeSet<String>()
                        result.put(module.name, known)
                    }
                    known.addAll(dependencyNamesOf(module))
                }
            }
        }
        // A name that is only ever depended on - an archived model, say - is not a node of this graph.
        result.values.forEach[retainAll(result.keySet)]
        return result
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
    def static List<String> firstCycle(Map<String, Set<String>> graph) {
        for (start : graph.keySet) {
            val found = cycleThrough(start, graph)
            if (!found.empty) {
                return found
            }
        }
        return Collections.emptyList
    }

    /**
     * The cycle the named module takes part in, or an empty list when it takes part in none.
     *
     * @param moduleName Module to check.
     * @param graph Graph to search.
     *
     * @return The cycle starting and ending at this module, or an empty list.
     */
    def static List<String> cycleThrough(String moduleName, Map<String, Set<String>> graph) {
        val List<String> path = new ArrayList<String>()
        val found = walk(moduleName, graph, new LinkedHashSet<String>(), new LinkedHashSet<String>(), path)
        if (found !== null && found.get(0) == moduleName) {
            return found
        }
        return Collections.emptyList
    }

    /**
     * Depth first search that returns the closing path as soon as it steps onto a module already on
     * the current path.
     */
    private def static List<String> walk(String current, Map<String, Set<String>> graph,
            Set<String> settled, Set<String> onPath, List<String> path) {
        if (onPath.contains(current)) {
            // Closed: everything from the first occurrence onwards, plus the module itself.
            val start = path.indexOf(current)
            val List<String> cycle = new ArrayList<String>(path.subList(start, path.size))
            cycle.add(current)
            return cycle
        }
        if (settled.contains(current)) {
            return null
        }
        onPath.add(current)
        path.add(current)
        for (next : graph.get(current) ?: Collections.<String>emptySet) {
            val found = walk(next, graph, settled, onPath, path)
            if (found !== null) {
                return found
            }
        }
        path.remove(path.size - 1)
        onPath.remove(current)
        settled.add(current)
        return null
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
    def static Iterable<EObject> referencedObjects(EObject container) {
        val List<EObject> result = new ArrayList<EObject>()
        if (container === null) {
            return result
        }
        val contents = container.eAllContents
        while (contents.hasNext) {
            val EObject obj = contents.next
            for (reference : obj.eClass.EAllReferences.filter[!containment && !derived]) {
                val value = obj.eGet(reference, true)
                val targets = if (reference.many) value as List<?> else Collections.singletonList(value)
                for (target : targets.filterNull) {
                    if (target instanceof EObject) {
                        if (!target.eIsProxy) {
                            result.add(target)
                        }
                    }
                }
            }
        }
        return result
    }

    /**
     * The qualified names of everything the cross references inside the given block resolve to.
     *
     * @param container Block to walk.
     * @param names Provider used to name a target.
     *
     * @return The names, in encounter order.
     */
    def static Iterable<String> referencedNames(EObject container, IQualifiedNameProvider names) {
        val List<String> result = new ArrayList<String>()
        for (target : referencedObjects(container)) {
            val name = names.getFullyQualifiedName(target)
            if (name !== null) {
                result.add(name.toString)
            }
        }
        return result
    }

}

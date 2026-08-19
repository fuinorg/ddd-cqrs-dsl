package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.LinkedHashSet
import java.util.List
import java.util.Map
import java.util.Set
import java.util.TreeMap
import java.util.TreeSet
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.analysis.CqrsModuleDependencies
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Creates the application's <b>module dependency graph</b> from the model, as JSON a backend or a
 * client can read.
 *
 * <p>It exists because a module cannot be switched off in isolation: disabling one that another still
 * needs breaks the second. Which module needs which is stated nowhere but in the model, and only a
 * fully parsed one can answer it - which is why this is generated rather than maintained.</p>
 *
 * <p><b>An import is not a dependency.</b> The edges are resolved cross references - an attribute
 * typed with another module's value object, a command whose {@code target} is another module's
 * method, a view over another module's projection, a process manager that {@code reacts-to} another
 * module's event. An unused import would otherwise invent a dependency, and a fully qualified
 * reference would hide one. See {@link CqrsModuleDependencies}, which the validator's unused-import
 * rule shares, so the two cannot disagree.</p>
 *
 * <p><b>Two granularities, because they answer different questions.</b> {@code modules} are the
 * {@code module} blocks as declared, which is the granularity the cycle check enforces and the one
 * that keeps the seams between contexts legal - a process manager may react to another context's
 * events without dragging the context it lives beside into a cycle. {@code groups} roll those up by
 * leading segment, which is the bounded context and the unit a user interface offers. The rollup can
 * be mutually dependent where the modules are not, and {@code mutuallyDependent} names exactly that:
 * groups that cannot be switched independently of each other.</p>
 *
 * <p>{@code order} is a topological order of the modules, so a consumer can enable from the leaves
 * inward without re-deriving anything. A model whose graph has a cycle - which the validator refuses -
 * still produces a file: the modules it could order come first, the rest follow alphabetically.</p>
 */
class ModuleDependencyArtifactFactory extends AbstractSource<ResourceSet> {

    /** Module the graph is written to - it is needed on every side, like the permission catalogue. */
    static val MODULE = "shared"

    /** Name of the generated file. */
    static val FILE_NAME = "MODULES.json"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.RES_MODULE_DEPENDENCIES
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val graph = CqrsModuleDependencies.graphOf(resourceSet)
        if (graph.empty) {
            return null
        }

        val byName = graph
        val project = contextName(resourceSet)
        if (project === null) {
            return null
        }

        val groups = groupsOf(byName)
        val order = topological(byName)

        return List.of(newArtifact(FILE_NAME, json(project, byName, groups, order).getBytes("UTF-8"),
            MODULE, "genMainRes"))
    }

    /** The context every module of this model belongs to - the deployable unit the graph describes. */
    private def String contextName(ResourceSet resourceSet) {
        val contents = resourceSet.allContents.filter(typeof(EObject))
        while (contents.hasNext) {
            val EObject obj = contents.next
            if (obj instanceof Module) {
                val ctx = (obj as Module).context
                if (ctx !== null) {
                    return ctx.name
                }
            }
        }
        return null
    }

    // ---- The graph ------------------------------------------------------------------------------

    /** The bounded context a module belongs to - its leading segment. */
    private def static String groupOf(String moduleName) {
        val idx = moduleName.indexOf('.')
        return if (idx < 0) moduleName else moduleName.substring(0, idx)
    }

    /** Rolls the modules up by leading segment and works out which rollups depend on each other. */
    private def Map<String, GroupEntry> groupsOf(Map<String, Set<String>> byName) {
        val Map<String, GroupEntry> result = new TreeMap<String, GroupEntry>()
        for (name : byName.keySet) {
            val group = groupOf(name)
            var entry = result.get(group)
            if (entry === null) {
                entry = new GroupEntry()
                entry.name = group
                result.put(group, entry)
            }
            entry.modules.add(name)
            for (dependency : byName.get(name)) {
                val other = groupOf(dependency)
                if (other != group) {
                    entry.dependsOn.add(other)
                }
            }
        }
        // Mutual dependency is what makes two groups inseparable, and it is invisible at module level:
        // journal depends on bankaccounts only through a process manager, while bankaccounts depends on
        // journal directly. Neither module is in a cycle; the two contexts are.
        for (entry : result.values) {
            for (other : entry.dependsOn) {
                val back = result.get(other)
                if (back !== null && back.dependsOn.contains(entry.name)) {
                    entry.mutuallyDependent.add(other)
                }
            }
        }
        return result
    }

    /**
     * Kahn's algorithm: modules with nothing left to wait for, over and over.
     * <p>
     * Anything still standing when no module is free took part in a cycle. The validator refuses those,
     * so this only decides what an already broken model looks like: the rest is appended in name order
     * rather than dropped, because a file missing half its modules is harder to diagnose than one whose
     * order is merely not meaningful.
     */
    private def List<String> topological(Map<String, Set<String>> byName) {
        val Map<String, Set<String>> waiting = new LinkedHashMap<String, Set<String>>()
        for (name : byName.keySet) {
            waiting.put(name, new TreeSet<String>(byName.get(name).filter[byName.containsKey(it)].toList))
        }
        val List<String> result = new ArrayList<String>()
        var progress = true
        while (progress && !waiting.empty) {
            progress = false
            for (name : new ArrayList<String>(waiting.keySet).sort) {
                if (waiting.get(name).empty) {
                    result.add(name)
                    waiting.remove(name)
                    waiting.values.forEach[remove(name)]
                    progress = true
                }
            }
        }
        result.addAll(new TreeSet<String>(waiting.keySet))
        return result
    }

    // ---- JSON -----------------------------------------------------------------------------------

    /** Writes the graph out. Sorted throughout, so the file is stable in version control. */
    private def String json(String project, Map<String, Set<String>> byName,
            Map<String, GroupEntry> groups, List<String> order) {
        return '''
            {
              "context": "«project.escaped»",
              "modules": [
            «FOR name : byName.keySet SEPARATOR ",\n"»    {
                  "name": "«name.escaped»",
                  "group": "«groupOf(name).escaped»",
                  "dependsOn": [«FOR d : byName.get(name) SEPARATOR ", "»"«d.escaped»"«ENDFOR»]
                }«ENDFOR»
              ],
              "order": [«FOR name : order SEPARATOR ", "»"«name.escaped»"«ENDFOR»],
              "groups": [
            «FOR group : groups.values SEPARATOR ",\n"»    {
                  "name": "«group.name.escaped»",
                  "modules": [«FOR m : group.modules SEPARATOR ", "»"«m.escaped»"«ENDFOR»],
                  "dependsOn": [«FOR d : group.dependsOn SEPARATOR ", "»"«d.escaped»"«ENDFOR»],
                  "mutuallyDependent": [«FOR d : group.mutuallyDependent SEPARATOR ", "»"«d.escaped»"«ENDFOR»]
                }«ENDFOR»
              ]
            }
        '''
    }

    /** A model name as a JSON string body. Names are identifiers today; this keeps that from mattering. */
    private def static String escaped(String value) {
        if (value === null) {
            return ""
        }
        return value.replace("\\", "\\\\").replace('"', '\\"')
    }

    // ---- Collected model data -------------------------------------------------------------------

    /** One bounded context: the modules below it and what they reach outside it. */
    private static class GroupEntry {
        public String name
        public Set<String> modules = new TreeSet<String>()
        public Set<String> dependsOn = new TreeSet<String>()
        public Set<String> mutuallyDependent = new LinkedHashSet<String>()
    }

}

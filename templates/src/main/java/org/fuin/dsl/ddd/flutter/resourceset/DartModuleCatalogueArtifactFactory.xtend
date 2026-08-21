package org.fuin.dsl.ddd.flutter.resourceset

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.analysis.CqrsModuleDependencies
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Creates the one const value navigation is built from: every module this release offers, the views
 * inside each, and the commands they carry.
 *
 * <p><b>The user-facing unit is a group, not a model module.</b> The model splits a bounded context in
 * two - <code>categories</code> for the aggregate and its commands, <code>categories.categoryview</code>
 * for the read side - and a user has no use for that distinction. The group is what appears in the hub;
 * its views are the tabs inside it. Enablement stays per model module, because that is the granularity
 * the installation's switch works at, so each view carries the module it is decided on.
 *
 * <p>A client that hard-codes a module list stops being correct the moment a bounded context is added.
 * One that reads this is navigation-complete the day the model compiles.
 */
class DartModuleCatalogueArtifactFactory extends AbstractDartSource<ResourceSet> {

    /**
     * Name of the generated file.
     *
     * <p>Named after what it holds rather than after the modules it lists, because the per-context
     * barrels are named after their context and a model is free to call one of those "modules".
     */
    static val FILE_NAME = "module_catalogue.dart"

    /** Where it goes. Named outright because a catalogue belongs to no single model element, so there
     * is nothing for the target mapping script to look at. */
    static val MODULE = "flutter.contract"

    /** See {@link #MODULE}. */
    static val FOLDER = "genMainDart"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.DART_MODULE_CATALOGUE
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (preparationRun) {
            return null
        }

        val modules = new ArrayList<Module>()
        var String project = null
        val it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val container = it.next
            if (container instanceof Module) {
                project = container.context.name
                modules.add(container)
            }
        }
        if (project === null || modules.empty) {
            return null
        }

        val groups = groupsOf(modules)
        if (groups.empty) {
            return null
        }

        return List.of(newArtifact(FILE_NAME,
            tidy(catalogue(project, groups).toString).getBytes("UTF-8"), MODULE, FOLDER))
    }

    def private catalogue(String project, Map<String, Group> groups) {
        '''
        «FOR imp : imports(groups)»
        import '«imp»';
        «ENDFOR»

        /// Everything this release of the model offers - the value navigation is built from.
        ///
        /// Adding a bounded context is a change here and no new screen anywhere: a module is offered the
        /// moment its model compiles and the installation switches it on.
        const ModuleCatalogue modules = ModuleCatalogue(
          context: «dartString(project)»,
          modules: <ModuleDescriptor>[
            «FOR group : groups.values»
            «groupOf(group)»
            «ENDFOR»
          ],
        );
        '''
    }

    def private groupOf(Group group) {
        '''
        ModuleDescriptor(
          group: «dartString(group.name)»,
          modules: <String>[«FOR m : group.modules SEPARATOR ", "»«dartString(m)»«ENDFOR»],
          dependsOn: <String>[«FOR d : group.dependsOn SEPARATOR ", "»«dartString(d)»«ENDFOR»],«IF states(group.text)»
          text: ModelText(
            bundle: «dartString(group.bundle)»,
            key: «dartString(group.name)»,
            shortLabel: «dartStringOrNull(group.text?.slabel)»,
            label: «dartStringOrNull(group.text?.label)»,
            tooltip: «dartStringOrNull(group.text?.tooltip)»,
          ),«ENDIF»
          views: <ViewDescriptor>[«FOR v : group.views SEPARATOR ", "»«lowerFirst(v.name)»«ENDFOR»],
          commands: <CommandDescriptor>[«commands(group)»],
        ),'''
    }

    /**
     * The commands of a group, as one block.
     *
     * <p>Assembled rather than looped in the template, for the reason every such block in this target
     * is: Xtend re-indents whatever a template interpolates, and a conditional loop inside a literal
     * ends up fighting that.
     */
    def private static String commands(Group group) {
        if (group.commands.empty) {
            return ""
        }
        val out = new ArrayList<String>()
        for (command : group.commands) {
            out.add("  " + command.name + ".descriptor,")
        }
        return "\n" + out.join("\n") + "\n"
    }

    /** Rolls the model's modules up into the groups a user navigates by. */
    def private Map<String, Group> groupsOf(List<Module> modules) {
        val out = new LinkedHashMap<String, Group>()
        for (module : modules.sortBy[m | m.name]) {
            val name = groupName(module.name)
            var group = out.get(name)
            if (group === null) {
                group = new Group(name)
                out.put(name, group)
            }
            // Once per name, not once per declaration: a module is routinely split over a public and a
            // private file, and both halves are the same module to an installation switching it on.
            if (!group.modules.contains(module.name)) {
                group.modules.add(module.name)
            }
            if (module.name == name) {
                group.text = module.metaInfo
                group.bundle = bundleName(module)
            }
            if (group.text === null && states(module.metaInfo)) {
                group.text = module.metaInfo
                group.bundle = bundleName(module)
            }
            for (element : module.elements) {
                if (element instanceof View) {
                    group.views.add(element)
                } else if (element instanceof Command) {
                    group.commands.add(element)
                }
            }
            for (dependency : CqrsModuleDependencies.dependencyNamesOf(module)) {
                val other = groupName(dependency)
                if (other != name) {
                    group.dependsOn.add(other)
                }
            }
        }
        // A group with nothing to show is not a place a user can go.
        out.values.removeIf[views.empty && commands.empty]

        // And a dependency is only a dependency of this catalogue when it names a group that is in it.
        // A model reaches into the common types of another bounded context all the time; that is not
        // something an installation can switch on or off here, so recording it would invite a client to
        // try.
        for (group : out.values) {
            group.dependsOn.retainAll(out.keySet)
        }
        return out
    }

    def private imports(Map<String, Group> groups) {
        val out = new TreeSet<String>()
        var needsText = false
        for (group : groups.values) {
            for (view : group.views) {
                out.add(DartNames.importOf(dartPackage, view.module, view.name + "Descriptor"))
            }
            for (command : group.commands) {
                out.add(DartNames.importOf(dartPackage, command.module, command.name))
            }
            if (states(group.text)) {
                needsText = true
            }
        }
        out.add(runtimeImport("src/descriptor/command_descriptor.dart"))
        if (needsText) {
            out.add(runtimeImport("src/descriptor/model_text.dart"))
        }
        out.add(runtimeImport("src/descriptor/module_catalogue.dart"))
        out.add(runtimeImport("src/descriptor/view_descriptor.dart"))
        return out
    }

    def private static String groupName(String moduleName) {
        val idx = moduleName.indexOf('.')
        return if(idx < 0) moduleName else moduleName.substring(0, idx)
    }

    def private static String lowerFirst(String name) {
        Character.toLowerCase(name.charAt(0)) + name.substring(1)
    }

    /** One entry in the hub, and everything the model puts inside it. */
    private static class Group {
        public val String name
        public val List<String> modules = new ArrayList<String>()
        public val TreeSet<String> dependsOn = new TreeSet<String>()
        public val List<View> views = new ArrayList<View>()
        public val List<Command> commands = new ArrayList<Command>()
        public var TypeMetaInfo text
        public var String bundle

        new(String name) {
            this.name = name
        }
    }

}

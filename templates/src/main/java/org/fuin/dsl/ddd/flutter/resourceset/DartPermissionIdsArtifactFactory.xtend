package org.fuin.dsl.ddd.flutter.resourceset

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.TreeMap
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Creates the Dart mirror of the permission catalogue: one id per command and one per view method.
 *
 * <p>Generated rather than written down, for the reason the JVM side is: an operation cannot ship
 * without an entry, which on the read side would mean an unchecked read. The <code>.*</code> forms are
 * an assigning shorthand only - they are expanded server-side, and a client always checks a single
 * operation.
 *
 * <p>None of this is enforcement. The server refuses on every call regardless; these ids only stop a
 * client offering what it knows will be refused.
 */
class DartPermissionIdsArtifactFactory extends AbstractDartSource<ResourceSet> {

    static val FILE_NAME = "permission_ids.dart"

    static val MODULE = "flutter.contract"

    static val FOLDER = "genMainDart"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.DART_PERMISSION_IDS
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (preparationRun) {
            return null
        }

        val commands = new TreeMap<String, Command>()
        val views = new TreeMap<String, View>()
        val it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val container = it.next
            if (container instanceof Module) {
                for (element : container.elements) {
                    if (element instanceof Command) {
                        commands.put(element.name, element)
                    } else if (element instanceof View) {
                        views.put(element.name, element)
                    }
                }
            }
        }
        if (commands.empty && views.empty) {
            return null
        }

        return List.of(newArtifact(FILE_NAME,
            tidy(source(commands, views).toString).getBytes("UTF-8"), MODULE, FOLDER))
    }

    def private source(Map<String, Command> commands, Map<String, View> views) {
        val ids = new ArrayList<String>()
        for (command : commands.values) {
            ids.add(command.name)
        }
        for (view : views.values) {
            for (method : view.methods) {
                ids.add(view.name + "." + method.name)
            }
        }

        '''
        /// Every operation this release can be given permission for.
        ///
        /// One id per command and one per view method, so an operation cannot ship without an entry -
        /// which on the read side would mean an unchecked read. The `.*` forms are an assigning
        /// shorthand only: they are expanded server-side, and a client always checks a single operation.
        ///
        /// None of this is enforcement. The server refuses on every call regardless; these only stop a
        /// client offering what it knows will be refused.
        class PermissionIds {
          const PermissionIds._();
          «FOR command : commands.values»

          «IF !dartDoc(command.doc, "").empty»«dartDoc(command.doc, "")»
          «ENDIF»static const String «constant(command.name)» = «dartString(command.name)»;
          «ENDFOR»
          «FOR view : views.values»

          /// Every read method of `«view.name»`.
          static const String «constant(view.name + ".*")» = «dartString(view.name + ".*")»;
          «FOR method : view.methods»

          «IF !dartDoc(method.doc, "").empty»«dartDoc(method.doc, "")»
          «ENDIF»static const String «constant(view.name + "." + method.name)» = «dartString(view.name + "." + method.name)»;
          «ENDFOR»
          «ENDFOR»

          /// Every id in this release.
          static const Set<String> all = <String>{
            «FOR id : ids»
            «constant(id)»,
            «ENDFOR»
          };

          /// What each whole-view id expands to.
          static const Map<String, Set<String>> viewMethods = <String, Set<String>>{
            «FOR view : views.values»
            «dartString(view.name)»: <String>{«FOR m : view.methods SEPARATOR ", "»«constant(view.name + "." + m.name)»«ENDFOR»},
            «ENDFOR»
          };

          /// The aggregate each command targets.
          static const Map<String, String> commandTargets = <String, String>{
            «FOR command : commands.values»
            «constant(command.name)»: «dartString(command.aggregate?.name)»,
            «ENDFOR»
          };
        }
        '''
    }

    /**
     * The Dart constant an id is held in.
     *
     * <p><code>CreateCategoryCommand</code> becomes <code>createCategoryCommand</code> and
     * <code>CategoryView.listCategories</code> becomes <code>categoryViewListCategories</code>; the
     * whole-view form ends in <code>All</code>, because <code>*</code> is not a name.
     */
    def private static String constant(String id) {
        val parts = id.replace(".*", ".All").split("\\.")
        val out = new StringBuilder()
        for (var i = 0; i < parts.length; i++) {
            val part = parts.get(i)
            if (i === 0) {
                out.append(Character.toLowerCase(part.charAt(0))).append(part.substring(1))
            } else {
                out.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1))
            }
        }
        return out.toString
    }

}

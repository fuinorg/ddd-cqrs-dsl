package org.fuin.dsl.ddd.flutter.view

import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates the typed client for one <code>view</code>: one method per read the model declares.
 *
 * <p>It takes the transport rather than owning one, so this package stays pure Dart and knows nothing
 * about bearer tokens, retries or which HTTP library is in use. The paths are the ones the model states
 * through <code>rest-path</code>, with a method name turned into a segment the same way the generated
 * Spring and JAX-RS contracts turn it - camel case becomes kebab case, so the three cannot drift.
 */
class DartViewClientArtifactFactory extends AbstractDartSource<View> {

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.DART_VIEW_CLIENT
    }

    override create(View view, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val refReg = context.codeReferenceRegistry
        val filename = DartNames.file(view.module, view.name + "Client")
        refReg.putReference(TypeKeys.refKey(view, typeKey), filename)

        if (preparationRun) {
            return null
        }

        return List.of(newArtifact(filename, tidy(create2(view).toString).getBytes("UTF-8"), view, typeKey))
    }

    def private create2(View view) {
        val className = view.name + "Client"
        val methods = new ArrayList<DartViewMethod>()
        for (method : view.methods) {
            methods.add(new DartViewMethod(view, method))
        }

        '''
        «FOR imp : imports(methods)»
        import '«imp»';
        «ENDFOR»

        «dartDoc(view.doc, "")»
        class «className» {
          /// Constructor with mandatory data.
          const «className»(this.transport);

          /// Path the view is served under.
          static const String basePath = «dartString(view.restPath)»;

          /// Reads the view over whatever transport the application is wired with.
          final ViewTransport transport;
          «FOR m : methods»

          «IF !dartDoc(m.method.doc, "").empty»«dartDoc(m.method.doc, "")»
          «ENDIF»«signature(m)» async {
            «body(m)»
          }
          «ENDFOR»
        }
        '''
    }

    def private signature(DartViewMethod m) {
        val returned = m.returnedType
        val result = if (m.returnsMany) {
                "List<" + returned + ">"
            } else if (m.returnsOptional) {
                returned + "?"
            } else {
                returned
            }
        val params = new ArrayList<String>()
        for (p : m.parameters) {
            params.add(p.type + " " + p.name)
        }
        return "Future<" + result + "> " + m.method.name + "(" + params.join(", ") + ")"
    }

    def private body(DartViewMethod m) {
        val query = new ArrayList<String>()
        for (p : m.parameters) {
            query.add("'" + p.name + "': " + p.toJson)
        }
        // Relative indentation only. Xtend prefixes every line of an interpolated value with the
        // indentation of the place it is interpolated into, so any absolute indentation written here is
        // added to that one.
        val request = if (query.empty) {
                "await transport.get('$basePath" + m.path + "');"
            } else {
                "await transport.get(\n  '$basePath" + m.path + "',\n  query: <String, Object?>{"
                    + query.join(", ") + "},\n);"
            }
        val returned = m.returnedType
        val read = if (m.returnsMany && m.returnsRow) {
                "return objectList(body).map(" + returned + ".fromJson).toList(growable: false);"
            } else if (m.returnsRow && m.returnsOptional) {
                "return body == null ? null : " + returned + ".fromJson(body as Map<String, dynamic>);"
            } else if (m.returnsRow) {
                "return " + returned + ".fromJson(body as Map<String, dynamic>);"
            } else if (m.wrapped !== null && m.returnsMany) {
                // The wrapper travels as its own value, so the list is a list of values.
                "return (body! as List<Object?>).map(" + returned
                    + ".fromWire).toList(growable: false);"
            } else if (m.wrapped !== null && m.returnsOptional) {
                "return optionalOf(body, " + returned + ".fromWire);"
            } else if (m.wrapped !== null) {
                "return " + returned + ".fromWire(body!);"
            } else {
                "return body as " + returned + (if(m.returnsOptional) "?" else "") + ";"
            }
        return "final body = " + request + "\n" + read
    }

    def private imports(List<DartViewMethod> methods) {
        val out = new TreeSet<String>()
        var needsJson = false
        for (m : methods) {
            for (p : m.parameters) {
                val referenced = p.referenced
                if (referenced !== null) {
                    out.add(importOf(referenced))
                }
            }
            val returns = m.method.returnType
            if (returns !== null && m.wrapped !== null) {
                out.add(importOf(m.wrapped))
                if (m.returnsOptional) {
                    needsJson = true
                }
            }
            if (returns !== null && m.returnsRow) {
                val generics = returns.generics
                val type = if(generics !== null && !generics.args.empty) generics.args.get(0) else returns.type
                out.add(importOf(type))
                if (m.returnsMany) {
                    needsJson = true
                }
            }
        }
        if (needsJson) {
            out.add(runtimeImport("src/json/json.dart"))
        }
        out.add(runtimeImport("src/transport.dart"))
        return out
    }

}

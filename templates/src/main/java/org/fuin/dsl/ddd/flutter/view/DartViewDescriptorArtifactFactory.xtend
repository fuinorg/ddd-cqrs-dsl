package org.fuin.dsl.ddd.flutter.view

import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartAttribute
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates the const descriptor of one <code>view</code> - what a generic renderer draws it from.
 *
 * <p>This is the artifact the whole approach rests on. A JVM client can read a screen's wording off
 * annotations at runtime; Flutter cannot, so everything a renderer needs arrives as const data: what
 * each method is called, what shape of screen it is, which parameters it filters by, what the rows it
 * returns look like, and the permission id that decides whether it is offered at all.
 *
 * <p><b>Descriptors, not widgets.</b> A generated screen is a file people edit, and regeneration
 * silently reverts them. A descriptor cannot be edited usefully, so nobody tries - and the polish goes
 * into one renderer that every module benefits from.
 */
class DartViewDescriptorArtifactFactory extends AbstractDartSource<View> {

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.DART_VIEW_DESCRIPTOR
    }

    override create(View view, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val refReg = context.codeReferenceRegistry
        val filename = DartNames.file(view.module, view.name + "Descriptor")
        refReg.putReference(TypeKeys.refKey(view, typeKey), filename)

        if (preparationRun) {
            return null
        }

        return List.of(newArtifact(filename, tidy(descriptor(view).toString).getBytes("UTF-8"),
            view, typeKey))
    }

    def private descriptor(View view) {
        val methods = new ArrayList<DartViewMethod>()
        for (method : view.methods) {
            methods.add(new DartViewMethod(view, method))
        }
        val bundle = bundleName(view.module)

        '''
        «FOR imp : imports(methods)»
        import '«imp»';
        «ENDFOR»

        «dartDoc(view.doc, "")»
        const ViewDescriptor «lowerFirst(view.name)» = ViewDescriptor(
          id: «dartString(view.name)»,
          module: «dartString(view.module?.name)»,
          restPath: «dartString(view.restPath)»,
          doc: «dartStringWrapped(docText(view.doc), "    ")»,«IF states(view.metaInfo)»
          text: «modelText(bundle, view.name, view.metaInfo)»,«ENDIF»
          methods: <MethodDescriptor>[
            «FOR m : methods»
            «methodOf(m, bundle)»
            «ENDFOR»
          ],
        );
        '''
    }

    def private methodOf(DartViewMethod m, String bundle) {
        val meta = m.method.metaInfo
        val parameters = m.parameters
        '''
        MethodDescriptor(
          id: «dartString(m.id)»,
          name: «dartString(m.method.name)»,
          path: «dartString(m.path)»,
          kind: «m.kind»,
          doc: «dartStringWrapped(docText(m.method.doc), "    ")»,«IF states(meta)»
          text: «modelText(bundle, m.id, meta)»,«ENDIF»«IF !parameters.empty»
          params: <AttributeDescriptor>[
            «FOR p : parameters»
            «parameterOf(p, bundle)»
            «ENDFOR»
          ],«ENDIF»«IF m.returnsRow»
          returns: «m.returnedType».descriptor,«ELSEIF m.returnedType !== null»
          scalarKind: «scalarKind(m.wrappedBase ?: m.returnedType)»,«ENDIF»
        ),'''
    }

    def private parameterOf(DartAttribute p, String bundle) {
        val meta = p.meta
        '''
        AttributeDescriptor(
          name: «dartString(p.name)»,
          kind: «p.valueKind»,«IF p.modelType !== null»
          modelType: «dartString(p.modelType)»,«ENDIF»«IF p.nestedDescriptor !== null»
          nested: «p.nestedDescriptor»,«ENDIF»«IF p.optional»
          optional: true,«ENDIF»«IF p.multiple»
          multiple: true,«ENDIF»«IF states(meta)»
          text: «modelText(bundle, p.name, meta)»,«ENDIF»«IF p.constraints !== null»
          constraints: «p.constraints»,«ENDIF»«IF p.values !== null»
          values: «p.values»,«ENDIF»
        ),'''
    }

    def private modelText(String bundle, String key, Object meta) {
        val m = meta as org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo
        '''
        ModelText(
          bundle: «dartString(bundle)»,
          key: «dartString(key)»,
          shortLabel: «dartStringOrNull(m?.slabel)»,
          label: «dartStringOrNull(m?.label)»,
          tooltip: «dartStringOrNull(m?.tooltip)»,«IF m?.prompt !== null»
          prompt: «dartString(m.prompt)»,«ENDIF»
        )'''
    }

    def private static String scalarKind(String dartType) {
        switch (dartType) {
            case "int": "ValueKind.integer"
            case "double",
            case "Decimal",
            case "num": "ValueKind.decimal"
            case "bool": "ValueKind.boolean"
            case "DateTime": "ValueKind.timestamp"
            default: "ValueKind.text"
        }
    }

    def private static String lowerFirst(String name) {
        if (name === null || name.empty) {
            return name
        }
        return Character.toLowerCase(name.charAt(0)) + name.substring(1)
    }

    def private imports(List<DartViewMethod> methods) {
        val out = new TreeSet<String>()
        var needsAttributes = false
        var needsText = false
        for (m : methods) {
            if (m.returnsRow) {
                val returns = m.method.returnType
                val generics = returns.generics
                val type = if(generics !== null && !generics.args.empty) generics.args.get(0) else returns.type
                out.add(importOf(type))
            }
            // Not only for parameters: a scalar answer names a ValueKind too, and a view whose
            // methods take nothing would otherwise reference one it never imported.
            if (!m.parameters.empty || (!m.returnsRow && m.returnedType !== null)) {
                needsAttributes = true
            }
            for (p : m.parameters) {
                // Only when the descriptor actually names the type. A parameter contributes its kind
                // and its wording, both of them plain data; the type itself is named only where its
                // invariants or its instances are, and importing it otherwise is an import for a name
                // the file never writes.
                val referenced = p.referenced
                if (referenced !== null && (p.constraints !== null || p.values !== null
                        || p.nestedDescriptor !== null)) {
                    out.add(importOf(referenced))
                }
                if (states(p.meta)) {
                    needsText = true
                }
            }
            if (states(m.method.metaInfo)) {
                needsText = true
            }
        }
        if (needsAttributes) {
            out.add(runtimeImport("src/descriptor/attribute_descriptor.dart"))
        }
        if (needsText) {
            out.add(runtimeImport("src/descriptor/model_text.dart"))
        }
        out.add(runtimeImport("src/descriptor/view_descriptor.dart"))
        return out
    }

}

package org.fuin.dsl.ddd.flutter.enumobject

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.EnumInstance
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates a Dart <code>enum</code> from an <code>enum</code> in the model, together with the wording
 * each of its instances carries.
 *
 * <p><b>The wording travels as const data, not as annotations.</b> On the JVM the same model produces
 * <code>@ShortLabel</code>/<code>@Label</code>/<code>@Tooltip</code> on the constant, and a client reads
 * them back at runtime through an annotation analyzer. Flutter cannot: <code>dart:mirrors</code> is
 * unsupported, so there is nothing to read them with. A client that cannot reflect has two options -
 * drop the wording and hard-code it per screen, or generate it. This generates it.
 *
 * <p>The instance names are lower-cased for Dart, which conventionally names enum values that way, and
 * each carries the name it has <em>on the wire</em> so the two never have to be guessed from one
 * another.
 */
class DartEnumArtifactFactory extends AbstractDartSource<EnumObject> {

    override getModelType() {
        typeof(EnumObject)
    }

    override getTypeKey() {
        TypeKeys.DART_ENUM
    }

    override create(EnumObject enu, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val refReg = context.codeReferenceRegistry
        val filename = DartNames.file(enu.module, enu.name)
        refReg.putReference(TypeKeys.refKey(enu, typeKey), filename)

        if (preparationRun) {
            // Nothing is emitted in the preparation phase; the reference above is the point of it.
            return null
        }

        return List.of(newArtifact(filename, tidy(create(enu).toString).getBytes("UTF-8"), enu, typeKey))
    }

    def private create(EnumObject enu) {
        val className = enu.name
        val instances = enu.instances
        val valid = instances.filter[deprecated === null].toList
        val deprecated = instances.filter[deprecated !== null].toList
        val bundle = bundleName(enu.module)
        val captioned = instances.exists[states(instanceMeta(it))]

        '''
        «FOR imp : imports(captioned)»
        import '«imp»';
        «ENDFOR»

        «dartDoc(enu.doc, "")»
        enum «className» {
          «instanceBlock(instances)»

          /// Constructor with mandatory data.
          const «className»(this.wireName);

          /// The instance as it appears on the wire.
          final String wireName;

          /// All instances, in model order.
          static const List<«className»> all = <«className»>[«names(instances)»];

          /// Valid instances - those not marked deprecated in the model.
          static const List<«className»> valid = <«className»>[«names(valid)»];

          /// Deprecated instances.
          static const List<«className»> deprecated = <«className»>[«names(deprecated)»];

          /// What to call each instance on screen.
          ///
          /// Always present, empty when the model captions nothing. A renderer that is handed this then
          /// shows the wire name, which is honest - and a member that appears and disappears would make
          /// every descriptor referencing it depend on whether somebody happened to write a label.
          static const List<EnumValueDescriptor> descriptors = <EnumValueDescriptor>[
            «IF captioned»
            «FOR instance : instances»
            EnumValueDescriptor(
              name: «dartString(instance.name)»,
              text: ModelText(
                bundle: «dartString(bundle)»,
                key: «dartString(instance.name)»,
                shortLabel: «dartStringOrNull(instanceMeta(instance)?.slabel)»,
                label: «dartStringOrNull(instanceMeta(instance)?.label)»,
                tooltip: «dartStringOrNull(instanceMeta(instance)?.tooltip)»,
              ),
            ),
            «ENDFOR»
            «ENDIF»
          ];

          /// Reads an instance off its wire name.
          static «className» fromWire(String wireName) => all.firstWhere(
                (v) => v.wireName == wireName,
                orElse: () => throw FormatException('Unknown «className»: $wireName'),
              );
        }
        '''
    }

    /**
     * The instance list, as one block.
     *
     * <p>Assembled here rather than with a rich-string loop and a multi-line separator: Xtend re-indents
     * whatever a template interpolates, and a separator carrying its own newline ends up fighting that.
     * Returning unindented lines and letting the template place them is the predictable way round.
     *
     * @param instances Instances in model order.
     *
     * @return Lines of Dart, unindented, ending in the semicolon the last one needs.
     */
    def private String instanceBlock(List<EnumInstance> instances) {
        val blocks = new ArrayList<String>()
        for (instance : instances) {
            val doc = dartDoc(instance.doc, "")
            val declaration = value(instance.name) + "(" + dartString(instance.name) + ")"
            blocks.add(if(doc.empty) declaration else doc + "\n" + declaration)
        }
        return blocks.join(",\n\n") + ";"
    }

    /** Dart names an enum value in lower camel case; the wire name stays as the model wrote it. */
    /**
     * The imports this file needs, once each.
     *
     * <p>Collected rather than written out inline, because two runtime paths can resolve to the same
     * import: a package that owns the runtime reaches into it precisely, and one that does not reaches
     * the other package's public library, where every path lands on the same line.
     *
     * @param captioned Whether any instance states wording.
     *
     * @return Import URIs, in a stable order.
     */
    def private imports(boolean captioned) {
        val out = new java.util.TreeSet<String>()
        out.add(runtimeImport("src/descriptor/attribute_descriptor.dart"))
        if (captioned) {
            out.add(runtimeImport("src/descriptor/model_text.dart"))
        }
        return out
    }

    def private static String value(String name) {
        val lower = name.toLowerCase
        val out = new StringBuilder()
        var upper = false
        for (var i = 0; i < lower.length; i++) {
            val c = lower.charAt(i)
            if (c == '_'.charAt(0)) {
                upper = true
            } else {
                out.append(if(upper) Character.toUpperCase(c) else c)
                upper = false
            }
        }
        return out.toString
    }

    def private static String names(List<EnumInstance> instances) {
        val out = new ArrayList<String>()
        for (instance : instances) {
            out.add(value(instance.name))
        }
        return out.join(", ")
    }

}

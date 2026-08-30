package org.fuin.dsl.ddd.flutter.valueobject

import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartAttribute
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates a Dart class from a <code>value-object</code> with several attributes: a read-model row, with
 * the JSON to read it and the descriptor a generic renderer draws it from.
 *
 * <p><b>The descriptor is the point.</b> A JVM client reads a row's wording off annotations at runtime;
 * Flutter cannot, because <code>dart:mirrors</code> is unsupported. So every label, short label, tooltip
 * and placeholder the model states is emitted here as const data, together with what kind of value each
 * attribute holds and whether a screen shows it at all - and a renderer that has one of these needs to
 * know nothing else about the context it came from.
 *
 * <p>The JSON is written out rather than left to a second code generator. Adding
 * <code>json_serializable</code> would mean running a generator over generated code, and two generators
 * disagreeing about one file is a debugging session nobody needs.
 */
class DartRowArtifactFactory extends AbstractDartSource<ValueObject> {

    override getModelType() {
        typeof(ValueObject)
    }

    override getTypeKey() {
        TypeKeys.DART_VALUE_OBJECT_ROW
    }

    /** Whether the value object wraps one value and carries nothing else. */
    def private static boolean singleValued(ValueObject vo) {
        return vo.base !== null && vo.attributes !== null && vo.attributes.size === 1
    }

    override create(ValueObject vo, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (vo.attributes === null || vo.attributes.empty || singleValued(vo)) {
            // A wrapper around a single value is a different artifact and a different factory, and what
            // makes it one is a declared base **and** nothing but the value itself. Neither half alone
            // decides: a value object of one named attribute and no base is a row that happens to have
            // one column, and a value object with a base that carries a second attribute is a row too -
            // there is no single value left for a wrapper to be.
            return null
        }

        val refReg = context.codeReferenceRegistry
        val filename = DartNames.file(vo.module, vo.name)
        refReg.putReference(TypeKeys.refKey(vo, typeKey), filename)

        if (preparationRun) {
            return null
        }

        return List.of(newArtifact(filename, tidy(create(vo).toString).getBytes("UTF-8"), vo, typeKey))
    }

    def private create(ValueObject vo) {
        val className = vo.name
        val key = declaredKey(vo)
        val attributes = new ArrayList<DartAttribute>()
        for (attribute : vo.attributes) {
            val dart = new DartAttribute(attribute)
            dart.declaredKey(key)
            attributes.add(dart)
        }
        val bundle = bundleName(vo.module)

        '''
        «FOR imp : imports(vo, attributes)»
        import '«imp»';
        «ENDFOR»

        «dartDoc(vo.doc, "")»
        class «className» {
          /// Constructor with all data.
          const «className»({
            «FOR a : attributes»
            «IF a.optional»this.«a.name»,«ELSE»required this.«a.name»,«ENDIF»
            «ENDFOR»
          });

          /// Reads the row off the server's JSON.
          factory «className».fromJson(Map<String, dynamic> json) => «className»(
                «FOR a : attributes»
                «a.name»: «a.fromJson("json")»,
                «ENDFOR»
              );

          /// What this type is called on screen, attribute by attribute.
          static const TypeDescriptor descriptor = TypeDescriptor(
            name: «dartString(className)»,
            attributes: <AttributeDescriptor>[
              «FOR a : attributes»
              «descriptorOf(a, bundle, className)»
              «ENDFOR»
            ],«IF displayFormat(attributes) !== null»
            displayFormat: «dartStringRaw(displayFormat(attributes))»,«ENDIF»
          );
          «FOR a : attributes»

          «IF !dartDoc(a.attribute.doc, "").empty»«dartDoc(a.attribute.doc, "")»
          «ENDIF»final «a.type» «a.name»;
          «ENDFOR»

          /// Reads the attribute called [attribute] off this row, for a renderer that has only a
          /// descriptor.
          ///
          /// An operator rather than a method, because a method needs a name and every name is one a
          /// model is entitled to give an attribute - `value` among them, which is what a wrapped
          /// single value is habitually called.
          Object? operator [](String attribute) => switch (attribute) {
                «FOR a : attributes»
                «dartString(a.name)» => «a.valueExpression»,
                «ENDFOR»
                _ => throw ArgumentError("«className» has no attribute '$attribute'"),
              };

          /// Writes the row back as JSON.
          Map<String, Object?> toJson() => <String, Object?>{
                «FOR a : attributes»
                «dartString(a.name)»: «a.toJson»,
                «ENDFOR»
              };

          @override
          bool operator ==(Object other) =>
              identical(this, other) ||
              other is «className» &&
                  «equality(attributes)»;

          @override
          int get hashCode => «hash(attributes)»;

          @override
          String toString() => «description(className, attributes)»;
        }
        '''
    }

    /**
     * One value inside a string literal.
     *
     * <p>Braces only where Dart needs them. A bare name does not: <code>$total</code> reads as the
     * value and <code>${total}</code> is the same thing said more loudly, which is why the analyzer
     * asks for the shorter form. Anything with a dot in it does need them - <code>$id.typed</code>
     * would interpolate the id and then write the word.
     *
     * @param expression Dart expression.
     *
     * @return Interpolation, braces included only when they carry meaning.
     */
    def private static String interpolate(String expression) {
        if (expression.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            return "$" + expression
        }
        return "${" + expression + "}"
    }

    /**
     * The hash, over every field.
     *
     * <p><code>Object.hash</code> takes two values at least, so a row of one field hashes that field
     * directly rather than calling it.
     *
     * @param attributes Fields of the row.
     *
     * @return Dart expression.
     */
    def private static String hash(List<DartAttribute> attributes) {
        if (attributes.size === 1) {
            return attributes.get(0).name + ".hashCode"
        }
        val names = new ArrayList<String>()
        for (a : attributes) {
            names.add(a.name)
        }
        return "Object.hash(" + names.join(", ") + ")"
    }

    /**
     * The field comparison, as one block.
     *
     * <p>Assembled rather than looped in the template for the reason every such block here is: Xtend
     * re-indents whatever a template interpolates, and a separator carrying a newline ends up fighting
     * that.
     */
    def private static String equality(List<DartAttribute> attributes) {
        val out = new ArrayList<String>()
        for (a : attributes) {
            out.add("other." + a.name + " == " + a.name)
        }
        return out.join(" &&\n")
    }

    /**
     * What the row says about itself in a log.
     *
     * <p>The identity and the attributes a screen shows, which is what somebody reading a log is trying
     * to recognise. The source is left out: it is the projection's bookkeeping, not the row.
     */
    def private static String description(String className, List<DartAttribute> attributes) {
        val out = new ArrayList<String>()
        for (a : attributes) {
            if (a.role != "AttributeRole.source") {
                out.add(a.name + "=" + interpolate(a.valueExpression))
            }
        }
        return "'" + className + "[" + out.join(", ") + "]'"
    }

    /**
     * How to name one row of this type to a person, or <code>null</code> where nothing says.
     *
     * <p>The row reaches the type it projects through its own identity: that attribute is typed as the
     * id which <code>identifies</code> an aggregate or an entity, and the business key of that type
     * carries the format. It has to be the <em>identity</em> rather than any id on the row - a row
     * carrying another type's id is referring to it rather than being it, and would otherwise be named
     * after the wrong thing.
     *
     * <p><b>The row has to carry every attribute the format names.</b> A key is declared over the write
     * model and a row is a projection of it, so the two need not agree; where they do not, this says
     * nothing and the client falls back to the first displayed attribute - visibly, rather than
     * rendering a label with a placeholder standing in it.
     */
    def private static String displayFormat(List<DartAttribute> attributes) {
        val identity = attributes.findFirst[role == "AttributeRole.identifier" || role == "AttributeRole.key"]
        if (identity === null) {
            return null
        }
        val idType = identity.attribute.type
        val AbstractEntity owner = switch (idType) {
            AggregateId: idType.aggregate
            EntityId: idType.entity
            default: null
        }
        if (owner === null) {
            return null
        }
        val key = owner.keys.nullSafe.findFirst[displayAs !== null]
        if (key === null) {
            return null
        }
        val carried = attributes.map[name].toSet
        for (named : variablesIn(key.displayAs)) {
            if (!carried.contains(named)) {
                return null
            }
        }
        return key.displayAs
    }

    /** The names a "${...}" format asks for, in the order it asks. */
    def private static List<String> variablesIn(String format) {
        val out = new ArrayList<String>()
        var from = 0
        while (true) {
            val start = format.indexOf("${", from)
            if (start < 0) {
                return out
            }
            val end = format.indexOf("}", start + 2)
            if (end < 0) {
                return out
            }
            out.add(format.substring(start + 2, end))
            from = end + 1
        }
    }

    /**
     * The attribute this row declares as its identity.
     *
     * <p>A cross-reference, so the model itself guarantees the name is an attribute of this row. This
     * replaced a <code>@Key("...")</code> annotation carrying the same fact as an unchecked string,
     * where a name that matched nothing was found - if at all - as a screen with no identity, at the
     * far end of a release chain.
     */
    def private static String declaredKey(ValueObject vo) {
        return vo.identifiedBy?.name
    }

    def private descriptorOf(DartAttribute a, String bundle, String owner) {
        val meta = a.meta
        '''
        AttributeDescriptor(
          name: «dartString(a.name)»,
          kind: «a.valueKind»,«IF a.modelType !== null»
          modelType: «dartString(a.modelType)»,«ENDIF»«IF a.nestedDescriptor !== null»
          nested: «a.nestedDescriptor»,«ENDIF»«IF a.role != "AttributeRole.data"»
          role: «a.role»,«ENDIF»«IF a.optional»
          optional: true,«ENDIF»«IF a.multiple»
          multiple: true,«ENDIF»«IF states(meta)»
          text: ModelText(
            bundle: «dartString(a.wordingBundle(bundle))»,
            key: «dartString(a.metaKey(owner))»,
            shortLabel: «dartStringOrNull(meta?.slabel)»,
            label: «dartStringOrNull(meta?.label)»,
            tooltip: «dartStringOrNull(meta?.tooltip)»,«IF meta?.prompt !== null»
            prompt: «dartString(meta.prompt)»,«ENDIF»
          ),«ENDIF»«IF a.constraints !== null»
          constraints: «a.constraints»,«ENDIF»«IF a.values !== null»
          values: «a.values»,«ENDIF»
        ),'''
    }

    /** Every import the generated file needs, sorted the way the analyzer wants them. */
    def private imports(ValueObject vo, List<DartAttribute> attributes) {
        val out = new TreeSet<String>()
        for (a : attributes) {
            val referenced = a.referenced
            if (referenced !== null) {
                out.add(importOf(referenced))
            }
        }
        out.add(runtimeImport("src/descriptor/attribute_descriptor.dart"))
        // Only when some attribute is captioned. A row of a model that words nothing writes no
        // ModelText, and importing it would be an import for a name the file never uses.
        if (attributes.exists[states(meta)]) {
            out.add(runtimeImport("src/descriptor/model_text.dart"))
        }
        out.add(runtimeImport("src/descriptor/view_descriptor.dart"))
        out.add(runtimeImport("src/json/json.dart"))
        return out
    }

}

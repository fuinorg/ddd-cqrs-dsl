package org.fuin.dsl.ddd.flutter.entityid

import java.util.List
import java.util.Map
import java.util.TreeSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartAttribute
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.flutter.base.DartTypes
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.TypeExtensions.*

/**
 * Creates a Dart class from an <code>aggregate-id</code> or an <code>entity-id</code>.
 *
 * <p>One factory for both, because to a client they are the same thing: an identifier that travels
 * <b>typed</b>. <code>EntityId.asTypedString()</code> is <code>"«TYPE» «id»"</code>, and that is what
 * goes on the wire in both directions - the <code>entity-id-path</code> of every command and the
 * <code>id</code> of every read-model row. A bare identifier is refused with a
 * <code>ConstraintViolationException</code> and a 400 whose message does not say which field was wrong.
 *
 * <p>The type constant is carried here rather than spelled out by callers, and it is derived the same
 * way the JVM side derives it: the identified aggregate or entity's name in upper snake case. Changing
 * that convention renames event streams and invalidates every stored path, so the two targets must
 * agree about it by construction rather than by coincidence.
 *
 * <p><b>An identifier composed of several parts also gets a constructor that builds it from them.</b>
 * A natural key - <code>(provider, date)</code> - is not something a client can mint, and until this
 * existed the parts were simply dropped and the class was an opaque string wrapper, which left a client
 * unable to address such an aggregate at all: it could read an identifier back off a row but could not
 * name one that did not exist yet. The encoding it emits is the same one
 * <code>SrcIdStringMethods</code> emits for the JVM, from the same declaration - which is why that one
 * is generated into the abstract class rather than left as a per-project override.
 */
class DartIdArtifactFactory extends AbstractDartSource<AbstractEntityId> {

    override getModelType() {
        typeof(AbstractEntityId)
    }

    override getTypeKey() {
        TypeKeys.DART_ENTITY_ID
    }

    override create(AbstractEntityId id, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val refReg = context.codeReferenceRegistry
        val filename = DartNames.file(id.module, id.name)
        refReg.putReference(TypeKeys.refKey(id, typeKey), filename)

        if (preparationRun) {
            return null
        }

        return List.of(newArtifact(filename, tidy(create(id, entityName(id)).toString).getBytes("UTF-8"),
            id, typeKey))
    }

    def private create(AbstractEntityId id, String entity) {
        val className = id.name
        val base = id.base
        val dartType = if(base === null) "String" else DartTypes.of(base.name) ?: "String"
        // The model's own type name, not the Dart one: an id declared "base UUID" reads as
        // "CATEGORY <uuid>" even though Dart carries it as a string.
        val modelType = (base?.name ?: "String").toLowerCase

        // Two or more, because a single-attribute identifier is already reachable through its own
        // value - and only where the JVM generates the matching round trip, see composableIdPart.
        val all = if(base === null) id.attributes.nullSafe.map[new DartAttribute(it)].toList else emptyList
        val parts = if(all.size >= 2 && all.forall[composableIdPart]) all else emptyList

        val imports = imports(parts)

        '''
        «FOR imp : imports»
        import '«imp»';
        «ENDFOR»
        «IF !imports.empty»

        «ENDIF»
        «dartDoc(id.doc, "")»
        ///
        /// On the wire an identifier is **typed** - `«entity.asEntityTypeConstant» <«modelType»>` - and that is what both the
        /// `entity-id-path` of a command and the `id` of a read-model row carry. The type is held here
        /// so that no caller ever spells it out.
        class «className» {
          /// Constructor with mandatory data.
          const «className»(this.value);

          «IF parts.size >= 2»
          /// Builds the identifier from its parts, the way the write side composes it.
          ///
          /// A natural key rather than a surrogate, so there is nothing to mint: the identifier follows
          /// from the values themselves.
          factory «className».of(«FOR p : parts SEPARATOR ', '»«p.baseType» «p.name»«ENDFOR») =>
              «className»('«FOR i : 0 ..< parts.size SEPARATOR '-'»${«IF i == parts.size - 1»«parts.get(i).idPart»«ELSE»_escaped(«parts.get(i).idPart»)«ENDIF»}«ENDFOR»');

          «ENDIF»
          /// Reads an identifier off the wire, in either the typed or the bare form.
          factory «className».fromWire(String wire) {
            final space = wire.indexOf(' ');
            final id = space < 0 ? wire : wire.substring(space + 1);
            return «className»(«parse(dartType)»);
          }

          /// The entity type, as the model declares it.
          static const String type = «dartString(entity.asEntityTypeConstant)»;

          /// The identifier itself.
          final «dartType» value;

          /// The form that travels on the wire.
          String get typed => '$type $value';

          @override
          bool operator ==(Object other) =>
              identical(this, other) || other is «className» && other.value == value;

          @override
          int get hashCode => value.hashCode;

          @override
          String toString() => typed;
        }
        «IF parts.size >= 2»

        /// Escapes one part of a composite identifier so that it cannot be mistaken for two.
        ///
        /// The write side joins the parts with `-` and splits them back on it, letting only the last
        /// part carry one - so every earlier part has its separators escaped, and the escape character
        /// with them. This has to produce exactly what the JVM's `escape` produces, down to doing the
        /// escape character first: reverse the two and an escaped separator comes back double-escaped.
        String _escaped(String value) => value.replaceAll('%', '%25').replaceAll('-', '%2D');
        «ENDIF»
        '''
    }

    /**
     * What a composite identifier's parts have to be imported from.
     *
     * <p>Empty for every other identifier, which is why this file wrote no import block at all until
     * composite ones gained a constructor. A part that is an enum or another identifier is a generated
     * type of its own - possibly in another package, which is what <code>importOf</code> settles - and a
     * date needs the runtime's wire helpers.
     *
     * @param parts Parts of the identifier, empty when it has none.
     *
     * @return Import URIs, sorted so the block does not depend on declaration order.
     */
    def private imports(List<DartAttribute> parts) {
        val out = new TreeSet<String>()
        for (p : parts) {
            val referenced = p.referenced
            if (referenced !== null) {
                out.add(importOf(referenced))
            }
            // A part whose Dart type is not built in - Decimal, and the base64 of a binary - brings its
            // own package with it, the same way a value object of that type does.
            val external = DartTypes.importFor(p.modelTypeName)
            if (external !== null) {
                out.add(external)
            }
            if (p.idPartNeedsRuntime) {
                out.add(runtimeImport("src/json/json.dart"))
            }
        }
        return out
    }

    /**
     * How the identifier's own value is read out of the typed form.
     *
     * <p>An id is a string on the wire whatever the model bases it on, so an id based on an integer -
     * a child entity numbered by its parent, for instance - has to be parsed rather than assigned.
     *
     * @param dartType Dart type the id carries.
     *
     * @return Expression over a local called <code>id</code>.
     */
    def private static String parse(String dartType) {
        return switch (dartType) {
            case "int": "int.parse(id)"
            case "double": "double.parse(id)"
            default: "id"
        }
    }

    /**
     * The aggregate or entity the id identifies.
     *
     * <p>Falls back to the id's own name with the <code>Id</code> suffix taken off, for a model that
     * declares an id without saying what it identifies. That is what the JVM side would produce for the
     * stream name too, so the two stay in step even where the model is incomplete.
     */
    def static String entityName(AbstractEntityId id) {
        val named = switch (id) {
            AggregateId: id.aggregate?.name
            EntityId: id.entity?.name
            default: null
        }
        if (named !== null) {
            return named
        }
        val name = id.name
        return if(name.endsWith("Id")) name.substring(0, name.length - 2) else name
    }

}

package org.fuin.dsl.ddd.flutter.entityid

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.flutter.base.DartTypes
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

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

        '''
        «dartDoc(id.doc, "")»
        ///
        /// On the wire an identifier is **typed** - `«entity.asEntityTypeConstant» <«modelType»>` - and that is what both the
        /// `entity-id-path` of a command and the `id` of a read-model row carry. The type is held here
        /// so that no caller ever spells it out.
        class «className» {
          /// Constructor with mandatory data.
          const «className»(this.value);

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
        '''
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

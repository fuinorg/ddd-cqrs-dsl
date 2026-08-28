package org.fuin.dsl.ddd.flutter.entityid

import java.util.List
import java.util.Map
import java.util.TreeSet
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType
import org.fuin.dsl.cqrs.cqrsDsl.PathSegment
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractVOExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.TypeExtensions.*

/**
 * Generates the client type for a declared entity identifier path.
 *
 * <p>On the wire a path is typed segments separated by a slash -
 * <code>ANNUAL_TRANSACTIONS 2026-a/TRANSACTION 45</code> - and a client holding it as a bare string can
 * say nothing about what it addresses. That is why a screen cannot tell one row's target from another's
 * when the row is identified by a path.
 *
 * <p>The shape travels as a constant and the matching is done by <code>EntityIdPathSpec</code> in
 * <code>cqrs_common</code>, which matches the same way as its counterpart on the JVM. What the generated
 * type adds on top is a <code>last</code> typed to the thing the path points at.
 */
class DartEntityIdPathArtifactFactory extends AbstractDartSource<EntityIdPathType> {

    override getModelType() {
        typeof(EntityIdPathType)
    }

    override getTypeKey() {
        TypeKeys.DART_ENTITY_ID_PATH
    }

    override create(EntityIdPathType path, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val refReg = context.codeReferenceRegistry
        val filename = DartNames.file(path.module, path.name)
        refReg.putReference(TypeKeys.refKey(path, typeKey), filename)

        if (preparationRun) {
            return null
        }

        return List.of(newArtifact(filename, tidy(create(path).toString).getBytes("UTF-8"), path, typeKey))
    }

    def private create(EntityIdPathType path) throws GenerateException {
        val className = path.name
        val leaf = path.segments.nullSafe.last
        val leafType = leaf.type
        val leafEntity = DartIdArtifactFactory.entityName(leafType)

        '''
        «FOR imp : imports(path)»
        import '«imp»';
        «ENDFOR»

        «dartDoc(path.doc, "")»
        ///
        /// On the wire a path is **typed segments separated by a slash** -
        /// `«wireExample(path)»` - because a child of a root there are many of cannot be
        /// addressed by its own identifier alone. Held as a bare string such a path says nothing about
        /// what it points at; the shape below is what makes it readable.
        class «className» {
          /// Constructor with the segments as they travel.
          const «className»(this.segments);

          /// Reads a path off the wire, refusing one that is not this shape.
          ///
          /// Refusing rather than accepting whatever arrived: a path of the wrong shape addresses
          /// something other than what the caller believes, and that is the failure this type exists to
          /// stop.
          factory «className».fromWire(String wire) {
            final segments = wire.split('/');
            if (!shape.matchesSegments(segments)) {
              throw ArgumentError.value(wire, 'wire', 'Not a «className»: expected $shape');
            }
            return «className»(segments);
          }

          /// The shape a path of this type has.
          static const EntityIdPathSpec shape = EntityIdPathSpec(<EntityIdPathStep>[
            «FOR segment : path.segments.nullSafe»
            «step(segment)»
            «ENDFOR»
          ]);

          /// The entity type this path addresses, which is what a row's identity is matched on.
          static const String type = «dartString(leafEntity.asEntityTypeConstant)»;

          /// The segments, as they travel.
          final List<String> segments;

          /// What this path addresses.
          «leafType.name» get last => «leafType.name».fromWire(segments.last);

          /// The form that travels on the wire.
          String get typed => segments.join('/');

          @override
          bool operator ==(Object other) =>
              identical(this, other) || other is «className» && other.typed == typed;

          @override
          int get hashCode => typed.hashCode;

          @override
          String toString() => typed;
        }
        '''
    }

    /** One step of the shape, written the way the model wrote it. */
    def private String step(PathSegment segment) {
        val entity = DartIdArtifactFactory.entityName(segment.type)
        val type = dartString(entity.asEntityTypeConstant)
        val range = segment.range
        if (range === null) {
            return "EntityIdPathStep(" + type + "),"
        }
        val max = if(range.unbounded) "null" else String.valueOf(range.max)
        return "EntityIdPathStep(" + type + ", min: " + range.min + ", max: " + max + "),"
    }

    /** A path of this shape as it would look on the wire, for the class documentation. */
    def private String wireExample(EntityIdPathType path) {
        val out = <String>newArrayList
        for (segment : path.segments.nullSafe) {
            out.add(DartIdArtifactFactory.entityName(segment.type).asEntityTypeConstant + " <"
                + (segment.type.baseTypeName ?: "id") + ">")
        }
        return out.join("/")
    }

    def private imports(EntityIdPathType path) {
        val out = new TreeSet<String>()
        out.add(runtimeImport("src/paths/entity_id_path_spec.dart"))
        out.add(importOf(path.segments.nullSafe.last.type))
        return out
    }

}

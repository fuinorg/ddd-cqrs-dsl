package org.fuin.dsl.ddd.flutter.valueobject

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.flutter.base.DartTypes
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsLiteralExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates a Dart class from a <code>value-object</code> that wraps a single value.
 *
 * <p>A single-value value object reaches the wire as a bare scalar - <code>CategoryName</code> is
 * <code>"Office supplies"</code>, not an object wrapping one - so the class exists for the invariants
 * rather than for the shape. Those invariants are what let a form refuse before the round trip; they
 * never replace the server's answer, which is checked again on every call regardless.
 *
 * <p>A value object with several attributes is a different artifact and a different factory: it is a
 * read-model row, and what it needs is JSON and a descriptor rather than a wrapper.
 */
class DartValueObjectArtifactFactory extends AbstractDartSource<ValueObject> {

    override getModelType() {
        typeof(ValueObject)
    }

    override getTypeKey() {
        TypeKeys.DART_VALUE_OBJECT
    }

    override create(ValueObject vo, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (!vo.singleValued) {
            // Not this factory's business. Said out loud rather than silently emitting nothing, because
            // "the file is missing" is a worse thing to debug than "no factory claimed it".
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

    /** Whether this is a wrapper around one value rather than a row of several. */
    def private static boolean isSingleValued(ValueObject vo) {
        return vo.base !== null && vo.attributes !== null && vo.attributes.size === 1
    }

    def private create(ValueObject vo) {
        val className = vo.name
        val attribute = vo.attributes.get(0)
        val dartType = DartTypes.of(vo.base.name) ?: "String"
        val constraints = constraintsOf(attribute)
        val decimal = DartTypes.importFor(vo.base.name)

        '''
        import '«runtimeImport("src/descriptor/constraint.dart")»';
        «IF decimal !== null»
        import '«decimal»';
        «ENDIF»

        «dartDoc(vo.doc, "")»
        class «className» {
          /// Constructor with mandatory data.
          const «className»(this.value);

          /// Reads the value off the wire, where a wrapper of one value travels as that value.
          ///
          /// A single-value object is not an object on the wire - the server writes the value itself -
          /// so there is nothing here for a `fromJson` to read a field out of. This is the same door an
          /// id and an enum offer, for the same reason.
          factory «className».fromWire(Object value) => «className»(«DartTypes.fromWire(dartType, "value")»);

          /// The invariants the model declares on this type, as far as this target can express them.
          ///
          /// Always present, even when empty. Whether a constraint has a Dart equivalent is this
          /// target's business, not a caller's: a descriptor naming this type's constraints has to
          /// compile whatever the model happens to declare, and a member that appears and disappears
          /// makes one factory's output depend on another's.
          static const List<Constraint> constraints = <Constraint>[«constraints.join(", ")»];

          «IF !dartDoc(attribute.doc, "").empty»«dartDoc(attribute.doc, "")»
          «ENDIF»final «dartType» value;

          /// Whether [value] satisfies the model's invariants.
          static bool isValid(Object? value) => validate(value, «dartString(className)») == null;

          /// What is wrong with [value], or `null` when it satisfies the model's invariants.
          ///
          /// [label] is what to call the value in the message - a field's label from the model, so the
          /// wording a user reads is the wording the model states.
          static String? validate(Object? value, String label) {
            for (final constraint in constraints) {
              final message = constraint.validate(value, label);
              if (message != null) {
                return message;
              }
            }
            return null;
          }

          @override
          bool operator ==(Object other) =>
              identical(this, other) || other is «className» && other.value == value;

          @override
          int get hashCode => value.hashCode;

          @override
          String toString() => value.toString();
        }
        '''
    }

    /**
     * The invariants of an attribute, as Dart expressions.
     *
     * <p>Only the ones the contract package defines a type for. A constraint it does not know is left
     * out rather than guessed at: the server checks every rule again and refuses on its own account, so
     * the cost of omitting one here is a round trip, and the cost of inventing one is a form that
     * refuses something the model allows.
     */
    def private static List<String> constraintsOf(Attribute attribute) {
        val out = new ArrayList<String>()
        val invariants = attribute?.invariants
        if (invariants === null) {
            return out
        }
        for (ConstraintInstance instance : invariants.constraintInstances) {
            val name = instance?.constraint?.name
            if (SUPPORTED.contains(name)) {
                val params = new ArrayList<String>()
                for (param : instance.params) {
                    params.add(param.str)
                }
                out.add(name + "(" + params.join(", ") + ")")
            }
        }
        return out
    }

    /** Constraints the contract package has a type for. */
    static val List<String> SUPPORTED = List.of("Length")

}

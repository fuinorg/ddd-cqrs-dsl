package org.fuin.dsl.ddd.flutter.base

import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.Type
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * What one attribute of a generated Dart type looks like: its type, how it is read off JSON and written
 * back, and what a renderer needs to know about it.
 *
 * <p>Everything here is decided from the model's type of the attribute rather than from its name. A
 * generator that switches on <code>"source"</code> or <code>"id"</code> is one the next bounded context
 * has to be taught about.
 */
class DartAttribute {

    /**
     * The one type from the common model this target knows by name.
     *
     * <p>It is what <code>source</code> means in every read model - the aggregate a row was projected
     * from and the version it reflects - and it is what makes "has the projection caught up with my
     * write" answerable rather than guessed. Recognising it here is what lets a renderer leave it out
     * of a screen without any screen knowing it exists.
     */
    public static val String SOURCE_TYPE = "VersionedEntityIdPath"

    /**
     * The other type from the common model this target knows by name: an entity id path.
     *
     * <p>A child of a root that is not a singleton cannot be addressed by its own id - <code>TRANSACTION
     * 45</code> exists in every account-year - so the row's identity is the whole path, and its last
     * segment is what says what kind of thing the row is. It is an external type rather than an
     * <code>entity-id</code>, so a row that declares one as its identity would otherwise fall through
     * to "some value a screen shows": a column of raw paths, and no actions at all.
     *
     * <p>Only where the row <em>declares</em> it. A path is far more often a reference to something
     * else - the transaction a journal entry was matched to - and a reference is a column.
     */
    public static val String PATH_TYPE = "EntityIdPath"

    /** The attribute or parameter this describes. */
    public val Variable attribute

    /** The attribute its owning type declares as its identity. See {@link #role()}. */
    var String declaredKey

    new(Variable attribute) {
        this.attribute = attribute
    }

    /** Tells this attribute which one its owning type declares as the identity, from <code>identified-by</code>. */
    def void declaredKey(String name) {
        this.declaredKey = name
    }

    /** Name on the wire, and the key everything else looks it up by. */
    def String name() {
        attribute.name
    }

    /** Whether the model allows it to be absent. */
    def boolean optional() {
        attribute.optional !== null
    }

    /** The Dart type, with a trailing <code>?</code> when the model allows it to be absent. */
    def String type() {
        val base = baseType()
        return if(optional) base + "?" else base
    }

    /** The Dart type, without the nullability marker. */
    def String baseType() {
        val element = elementType
        if (element !== null) {
            return "List<" + dartNameOf(element) + ">"
        }
        return dartNameOf(attribute.type)
    }

    /**
     * The element type of <code>List&lt;X&gt;</code>, or <code>null</code> when the attribute holds one
     * value.
     *
     * <p>The declared type of such an attribute is <code>List</code> itself, which says nothing anybody
     * wants to know: what it holds, what it is called on screen, and how to read it are all questions
     * about <code>X</code>. So everything below asks {@link #effectiveType()} rather than the declared
     * one, and only the arity is read from the attribute itself.
     */
    def Type elementType() {
        val generics = attribute.generics
        if (generics === null || generics.args === null || generics.args.empty) {
            return null
        }
        return generics.args.get(0)
    }

    /** Whether the attribute holds several values rather than one. */
    def boolean multiple() {
        elementType !== null
    }

    /** What the attribute is really about: the element type of a list, the declared type otherwise. */
    def private Type effectiveType() {
        elementType ?: attribute.type
    }

    def private static String dartNameOf(Type type) {
        return switch (type) {
            ExternalType: DartTypes.of(type.name) ?: "String"
            default: type.name
        }
    }

    /** What a renderer needs to know about the kind of value this holds. */
    def String valueKind() {
        val type = effectiveType
        return switch (type) {
            EnumObject: "ValueKind.enumeration"
            AggregateId: "ValueKind.identifier"
            EntityId: "ValueKind.identifier"
            EntityIdPathType: "ValueKind.identifier"
            ValueObject case type.name == SOURCE_TYPE: "ValueKind.identifier"
            ValueObject case single(type): kindOfBase(type)
            ExternalType: kindOfExternal(type.name)
            default: "ValueKind.text"
        }
    }

    /**
     * The model's own name for what this attribute holds, so a form can tell a rename's
     * <code>newName</code> and a row's <code>name</code> are the same thing. External types are left
     * out: two attributes that are both a <code>String</code> are not about the same thing.
     */
    def String modelType() {
        val type = effectiveType
        if (type === null || type instanceof ExternalType) {
            return null
        }
        return type.name
    }

    /**
     * The descriptor of the composite this attribute holds, which a cell would otherwise render as a
     * JSON map. The condition mirrors what the row factory emits a descriptor for, or the reference
     * would not compile; the bookkeeping type is left out because no screen draws it.
     */
    def String nestedDescriptor() {
        val type = effectiveType
        if (type instanceof ValueObject && (type as ValueObject).name != SOURCE_TYPE) {
            val vo = type as ValueObject
            val attributes = vo.attributes
            if (attributes !== null && !attributes.empty
                    && !(vo.base !== null && attributes.size === 1)) {
                return vo.name + ".descriptor"
            }
        }
        return null
    }

    /**
     * What the attribute is for, derived from its type so no generator switches on a name.
     * <code>identified-by</code> says which attribute identifies the row; whether it is shown stays a
     * question about its type, so a surrogate is hidden and a natural key is a column, and an id-typed
     * attribute that is <em>not</em> the identity is a reference to something else and therefore a
     * column too.
     */
    def String role() {
        val type = attribute.type
        if (type instanceof ValueObject && (type as ValueObject).name == SOURCE_TYPE) {
            return "AttributeRole.source"
        }
        if (multiple) {
            // Several ids are a choice somebody made, not the identity of the thing showing them. A
            // row is identified by one id; a list of them is a field a form has to offer.
            return "AttributeRole.data"
        }
        if (declaredKey !== null) {
            if (attribute.name != declaredKey) {
                // A second id on the row is a reference, not the identity - and the model gives it
                // wording, so it is a column.
                return "AttributeRole.data"
            }
            // Naming the key says which attribute identifies the row, not that it is worth reading:
            // a surrogate stays hidden, a natural key is the content.
            if (identifying(type)) {
                return "AttributeRole.identifier"
            }
            return "AttributeRole.key"
        }
        // No declaration, so only the type can say - and a bare path cannot. Rows carry paths to other
        // things as references far more often than as their own identity, so an undeclared one stays
        // a column rather than silently becoming the row's identity. A *declared* path is different:
        // it says which entity it addresses, so it is as good an answer as an id.
        return switch (type) {
            AggregateId: "AttributeRole.identifier"
            EntityId: "AttributeRole.identifier"
            EntityIdPathType: "AttributeRole.identifier"
            default: "AttributeRole.data"
        }
    }

    /**
     * Whether the attribute a row <em>declares</em> as its identity addresses that row rather than
     * describing it: a surrogate id, or the path that addresses a child of a non-singleton root. A
     * natural key is neither - it identifies the row and is also worth reading, so it stays a column.
     */
    def private static boolean identifying(Type type) {
        switch (type) {
            AggregateId: true
            EntityId: true
            EntityIdPathType: true
            ExternalType: (type as ExternalType).name == PATH_TYPE
            default: false
        }
    }

    /** Whether a screen shows this attribute, and therefore whether it must carry wording. */
    def boolean displayed() {
        val role = role()
        role == "AttributeRole.data" || role == "AttributeRole.key"
    }

    /** How the attribute is read out of the server's JSON. */
    def String fromJson(String json) {
        val key = "'" + attribute.name + "'"
        val element = elementType
        if (element !== null) {
            return (if(optional) "optionalList" else "requiredList") + "(" + json + ", " + key + ", "
                + elementReader(element) + ")"
        }
        val type = attribute.type
        return switch (type) {
            EnumObject: wrap(type.name + ".fromWire", required("String", json, key))
            AggregateId: wrap(type.name + ".fromWire", required("String", json, key))
            EntityId: wrap(type.name + ".fromWire", required("String", json, key))
            EntityIdPathType: wrap(type.name + ".fromWire", required("String", json, key))
            ValueObject case single(type): wrap(type.name, required(baseOf(type), json, key))
            ValueObject: wrap(type.name + ".fromJson", required("Object", json, key))
            default: required(baseType, json, key)
        }
    }

    /** How the attribute is written back, as an expression over a field called by its own name. */
    def String toJson() {
        val field = attribute.name
        val element = elementType
        if (element !== null) {
            val each = elementToJson(element)
            // A list of plain values already is what the wire wants; only wrappers need unwrapping.
            if (each === null) {
                return field
            }
            return field + (if(optional) "?" else "") + ".map((e) => " + each
                + ").toList(growable: false)"
        }
        val type = attribute.type
        val wire = wireHelperFor(type)
        if (wire !== null) {
            return wire + "(" + field + ")"
        }
        return switch (type) {
            EnumObject: field + (if(optional) "?" else "") + ".wireName"
            AggregateId: field + (if(optional) "?" else "") + ".typed"
            EntityId: field + (if(optional) "?" else "") + ".typed"
            EntityIdPathType: field + (if(optional) "?" else "") + ".typed"
            ValueObject case single(type): field + (if(optional) "?" else "") + ".value"
            ValueObject: field + (if(optional) "?" else "") + ".toJson()"
            default: field
        }
    }

    /**
     * How one part of a composite identifier is rendered into that identifier's string form.
     *
     * <p><b>Not {@link #toJson()}, and the difference is not cosmetic.</b> The JVM builds the string
     * form by concatenating the parts, so each one arrives as whatever
     * <code>String.valueOf(part)</code> gives: an enum's constant name, a nested identifier's
     * <code>asString()</code> - which is <b>bare</b> - and a date's ISO form. <code>toJson()</code>
     * writes a nested identifier as <code>.typed</code>, which is right for a JSON field and wrong
     * here: it would put <code>BOOK 123-2026-08-30</code> where the write side expects
     * <code>123-2026-08-30</code>, and the command would be refused for an identifier that looks
     * plausible.
     *
     * <p>A part is never optional - an identifier with a missing part is not an identifier - so the
     * null-aware forms {@link #toJson()} needs have no counterpart here. The wire helpers do return a
     * nullable, though, so what they produce is asserted non-null.
     *
     * @return Expression over a parameter called by the attribute's own name.
     */
    /** The model's own name for the attribute's type, which is what the type tables are keyed by. */
    def String modelTypeName() {
        attribute.type?.name
    }

    /** Whether {@link #idPart()} reaches for a wire helper, and so needs the runtime imported. */
    def boolean idPartNeedsRuntime() {
        wireHelperFor(attribute.type) !== null
    }

    def String idPart() {
        val field = attribute.name
        val type = attribute.type
        val wire = wireHelperFor(type)
        if (wire !== null) {
            return wire + "(" + field + ")!"
        }
        return switch (type) {
            EnumObject: field + ".wireName"
            // Bare, not typed: the JVM joins asString(), which carries no type prefix.
            AggregateId: field + ".value"
            EntityId: field + ".value"
            ValueObject case single(type): field + ".value"
            default: field
        }
    }

    /**
     * How the attribute is read by a renderer that has only a descriptor.
     *
     * <p>Not the same as {@link #toJson()}, and the difference matters: a scalar is handed over in the
     * form a cell shows it, but an attribute whose type is an object of its own is handed over as that
     * object. A row's <code>source</code> is the case in point - a renderer never draws it, but the
     * code that asks "has the projection caught up with my write" needs the aggregate version off it,
     * and a JSON map is no use for that.
     */
    def String valueExpression() {
        if (multiple) {
            return attribute.name
        }
        val type = attribute.type
        return switch (type) {
            ValueObject case type.base === null: attribute.name
            default: toJson()
        }
    }

    /** The generated type this attribute refers to, or <code>null</code> when it needs no import. */
    def Type referenced() {
        val type = effectiveType
        return switch (type) {
            ExternalType: null
            default: type
        }
    }

    /** The expression naming this attribute's invariants, or <code>null</code> when it declares none. */
    def String constraints() {
        val type = effectiveType
        if (type instanceof ValueObject) {
            if (single(type) && type.attributes.get(0).invariants !== null) {
                return type.name + ".constraints"
            }
        }
        return null
    }

    /** The expression naming this attribute's instances, or <code>null</code> when it is no enum. */
    def String values() {
        val type = effectiveType
        return if(type instanceof EnumObject) type.name + ".descriptors" else null
    }

    /**
     * The wording the model states about it.
     *
     * <p>The attribute's own first, and the **type's** when the attribute states none. A value object
     * used in several places - a read-model row and the form of every command that sets it - deserves
     * to be captioned once on the type rather than repeated at each use, and the row's own wording is
     * then an override of it rather than the only copy.
     *
     * <p>Every kind of type that can carry wording is asked, ids included. What a type is called does
     * not depend on what sort of type it is, and an id whose model states <code>label "Category
     * ID"</code> has said what to call it; leaving that on the floor because the attribute happens to
     * be an identifier would make the model's wording conditional on the target's own categories.
     * Whether a screen *shows* the attribute is a separate question, and {@link #role()} answers it.
     */
    def getMeta() {
        val own = attribute.overridden?.metaInfo
        if (states(own)) {
            return own
        }
        val type = effectiveType
        return switch (type) {
            ValueObject: type.metaInfo
            EnumObject: type.metaInfo
            AbstractEntityId: type.metaInfo
            EntityIdPathType: type.metaInfo
            default: own
        }
    }

    /**
     * What reads one element of a list.
     *
     * <p>A callable rather than an expression, so the list reader applies it once per element. Most
     * types already offer one to tear off - the same <code>fromWire</code> a single value is read
     * through - and only the shapes that take something narrower than <code>Object</code> need
     * wrapping.
     *
     * @param type Element type.
     *
     * @return Dart expression of type <code>T Function(Object)</code>.
     */
    def private static String elementReader(Type type) {
        return switch (type) {
            // An enum and an id read themselves from the text they travel as, so the element is cast to
            // it first. A value object's own reader already takes whatever the wire had.
            EnumObject: "(e) => " + type.name + ".fromWire(e as String)"
            AggregateId: "(e) => " + type.name + ".fromWire(e as String)"
            EntityId: "(e) => " + type.name + ".fromWire(e as String)"
            ValueObject case single(type): type.name + ".fromWire"
            ValueObject: "(e) => " + type.name + ".fromJson(e as Map<String, dynamic>)"
            ExternalType: "(e) => " + DartTypes.fromWire(DartTypes.of(type.name) ?: "String", "e")
            default: "(e) => e as " + type.name
        }
    }

    /**
     * How one element is written back, or <code>null</code> when it travels as it stands.
     *
     * @param type Element type.
     *
     * @return Expression over a local called <code>e</code>, or <code>null</code>.
     */
    def private static String elementToJson(Type type) {
        val wire = wireHelperFor(type)
        if (wire !== null) {
            return wire + "(e)"
        }
        return switch (type) {
            EnumObject: "e.wireName"
            AggregateId: "e.typed"
            EntityId: "e.typed"
            ValueObject case single(type): "e.value"
            ValueObject: "e.toJson()"
            default: null
        }
    }

    /**
     * The runtime helper that turns this attribute into what the wire carries. A <code>DateTime</code>
     * is refused by a JSON encoder and rendered <code>2026-08-21 00:00:00.000</code> in a query string.
     */
    def private static String wireHelperFor(Type type) {
        if (type instanceof ExternalType) {
            return switch (type.name) {
                case "Date": "wireDate"
                case "Time": "wireTimestamp"
                case "Timestamp": "wireTimestamp"
                default: null
            }
        }
        return null
    }

    /** Whether {@link #toJson()} needs the runtime's wire helpers to be imported. */
    def boolean usesWireHelper() {
        wireHelperFor(effectiveType) !== null
    }

    /** Whether a block of wording states anything at all. */
    private static def boolean states(org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo meta) {
        return meta !== null && (meta.slabel !== null || meta.label !== null || meta.tooltip !== null
            || meta.prompt !== null)
    }

    private static def boolean single(ValueObject vo) {
        vo.base !== null && vo.attributes !== null && vo.attributes.size === 1
    }

    private static def String baseOf(ValueObject vo) {
        DartTypes.of(vo.base.name) ?: "String"
    }

    private static def String kindOfBase(ValueObject vo) {
        kindOfExternal(vo.base.name)
    }

    private static def String kindOfExternal(String name) {
        switch (name) {
            case "Integer",
            case "Long",
            case "Short",
            case "Byte",
            case "BigInteger": "ValueKind.integer"
            case "BigDecimal": "ValueKind.decimal"
            case "Float",
            case "Double",
            case "Number": "ValueKind.decimal"
            case "Boolean": "ValueKind.boolean"
            case "Date": "ValueKind.date"
            case "Time",
            case "Timestamp": "ValueKind.timestamp"
            case "UUID": "ValueKind.identifier"
            default: "ValueKind.text"
        }
    }

    /**
     * Applies a type's own reader to what came off the wire.
     *
     * <p>When the attribute is optional the absence is handled outside the reader rather than inside
     * it: every one of these readers takes a value, because a wrapper cannot be built from nothing.
     *
     * @param callee What reads the value - a constructor when it is a bare type name, a named one
     *               otherwise.
     * @param read Expression producing what came off the wire.
     *
     * @return Dart expression.
     */
    private def String wrap(String callee, String read) {
        if (optional) {
            return "optionalOf(" + read + ", " + (if(callee.contains(".")) callee else callee + ".new")
                + ")"
        }
        return callee + "(" + read + ")"
    }

    /**
     * The reader for one attribute, chosen by what the value *is* rather than by what Dart calls it.
     *
     * <p>A date and a string are both a `String` on the wire, and reading a date with the string reader
     * hands a `String` to something expecting a `DateTime`. The distinction the model makes has to
     * survive into the reader, which is why this switches on the external type and not on the Dart one.
     */
    private def String required(String dartType, String json, String key) {
        val fn = switch (dartType) {
            case "int": if(optional) "optionalInt" else "requiredInt"
            case "Decimal": if(optional) "optionalDecimal" else "requiredDecimal"
            case "double": if(optional) "optionalDouble" else "requiredDouble"
            case "bool": if(optional) "optionalBool" else "requiredBool"
            case "DateTime": if(optional) "optionalDate" else "requiredDate"
            case "List<int>": if(optional) "optionalBinary" else "requiredBinary"
            case "Object": if(optional) "optionalObject" else "requiredObject"
            default: if(optional) "optionalString" else "requiredString"
        }
        return fn + "(" + json + ", " + key + ")"
    }

}

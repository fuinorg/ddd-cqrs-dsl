package org.fuin.dsl.ddd.flutter.base

import java.util.LinkedHashMap
import java.util.Map

/**
 * What each of the DSL's external types is called in Dart.
 *
 * <p>External type mapping is its own decision for any target, and Dart's differs from the JVM's in
 * three places worth stating:
 *
 * <ul>
 * <li><code>UUID</code> is a <code>String</code>. Dart has no UUID type, nothing in a client does
 * arithmetic on one, and parsing it would only create a second way to be wrong about the same
 * characters.</li>
 * <li><code>Currency</code> is a <code>String</code> - the ISO code, which is what the wire carries.</li>
 * <li><code>BigDecimal</code> is <code>Decimal</code> from the <code>decimal</code> package, never
 * <code>double</code>. A tax rate of 19.00 that arrives as 18.999999 is a rounding bug with a delay
 * fuse, and money is minor-unit <code>int</code> for the same reason.</li>
 * </ul>
 */
class DartTypes {

    static val Map<String, String> TYPES = types()

    private new() {
    }

    /**
     * The Dart type an external type maps to.
     *
     * @param name Simple name of the external type, e.g. <code>String</code>.
     *
     * @return Dart type name, or <code>null</code> when this target has no mapping for it.
     */
    static def String of(String name) {
        return if(name === null) null else TYPES.get(name)
    }

    /** The package an import is needed from for a mapped type, or <code>null</code> when none is. */
    static def String importFor(String name) {
        return switch (name) {
            case "BigDecimal": "package:decimal/decimal.dart"
            // Binary travels base64-encoded, and decoding it is where dart:convert comes in.
            case "Binary": "dart:convert"
            default: null
        }
    }

    private static def Map<String, String> types() {
        val map = new LinkedHashMap<String, String>()
        map.put("String", "String")
        map.put("Boolean", "bool")
        map.put("Byte", "int")
        map.put("Short", "int")
        map.put("Integer", "int")
        map.put("Long", "int")
        map.put("Float", "double")
        map.put("Double", "double")
        map.put("Character", "String")
        map.put("UUID", "String")
        map.put("Currency", "String")
        map.put("BigDecimal", "Decimal")
        map.put("BigInteger", "int")
        map.put("Number", "num")
        map.put("Date", "DateTime")
        map.put("Time", "DateTime")
        map.put("Timestamp", "DateTime")
        map.put("EntityIdPath", "String")
        map.put("Binary", "List<int>")
        return map
    }


    /**
     * How a value coming off the wire becomes the Dart type that holds it.
     *
     * <p>The wire has no types of its own beyond what JSON offers, so a Dart type that is not one of
     * those has to be built from the one that is: an exact decimal from its digits, an instant from its
     * ISO-8601 text. Shared rather than written at each use, because a wrapper reading its own value
     * and a list reading its elements are the same conversion over a different expression.
     *
     * @param dartType Type to end up with.
     * @param value Expression holding what came off the wire.
     *
     * @return Dart expression.
     */
    def static String fromWire(String dartType, String value) {
        switch (dartType) {
            case "int": value + " as int"
            case "double": "(" + value + " as num).toDouble()"
            case "bool": value + " as bool"
            case "Decimal": "Decimal.parse(" + value + ".toString())"
            case "DateTime": "DateTime.parse(" + value + " as String)"
            case "List<int>": "base64Decode(" + value + " as String)"
            default: value + " as " + dartType
        }
    }

}

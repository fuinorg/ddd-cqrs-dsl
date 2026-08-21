package org.fuin.dsl.ddd.flutter.view

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.flutter.base.DartAttribute
import org.fuin.dsl.ddd.flutter.base.DartTypes

/**
 * One method of a view, seen the way a client and a renderer need it.
 *
 * <p>Shared by the two factories that read a view - the typed client and the const descriptor - because
 * they must agree about the path, the parameters and the shape of the answer. Two copies of that
 * reasoning would be two things to keep in step.
 */
class DartViewMethod {

    /** The view the method belongs to. */
    public val View view

    /** The method itself. */
    public val Method method

    new(View view, Method method) {
        this.view = view
        this.method = method
    }

    /** <code>«View».«method»</code> - the permission id, and the key into the UI catalogue. */
    def String id() {
        view.name + "." + method.name
    }

    /** Path segment below the view's rest path, e.g. <code>/list-categories</code>. */
    def String path() {
        return method.restPath ?: "/" + kebab(method.name)
    }

    /** The parameters, in model order. */
    def List<DartAttribute> parameters() {
        val out = new ArrayList<DartAttribute>()
        for (parameter : method.parameters) {
            out.add(new DartAttribute(parameter))
        }
        return out
    }

    /** The type the answer carries, or <code>null</code> when the method returns nothing. */
    def String returnedType() {
        val returns = method.returnType
        if (returns === null) {
            return null
        }
        val generics = returns.generics
        val type = if(generics !== null && !generics.args.empty) generics.args.get(0) else returns.type
        return DartTypes.of(type.name) ?: type.name
    }

    /** Whether the answer is a list of them. */
    def boolean returnsMany() {
        val returns = method.returnType
        return returns?.generics !== null && !returns.generics.args.empty
    }

    /** Whether the answer may be absent. */
    def boolean returnsOptional() {
        method.returnType?.optional !== null
    }

    /**
     * What shape of screen this is.
     *
     * <p>Several rows is a list, one row or nothing is a detail, and a single value is neither - it is
     * a badge or a guard on an action, never a screen of its own.
     */
    def String kind() {
        if (returnsMany) {
            return "MethodKind.list"
        }
        val returned = returnedType
        if (returned === null || DartTypes.of(returned) !== null || isPrimitive(returned)
                || wrapped !== null) {
            return "MethodKind.scalar"
        }
        return "MethodKind.detail"
    }

    /**
     * The single-value value object the answer wraps, or <code>null</code> when it wraps none.
     *
     * <p>A wrapper of one value is a value, not a row: the server writes the value itself, so a method
     * returning an <code>ExchangeRateValue</code> answers with a number and not with an object. A
     * client that reads it as a row asks for a field that is not there, and a renderer that treats it
     * as one draws a table of one column.
     */
    def ValueObject wrapped() {
        val returns = method.returnType
        if (returns === null) {
            return null
        }
        val generics = returns.generics
        val type = if(generics !== null && !generics.args.empty) generics.args.get(0) else returns.type
        if (type instanceof ValueObject) {
            if (type.base !== null && type.attributes !== null && type.attributes.size === 1) {
                return type
            }
        }
        return null
    }

    /** The Dart type a wrapped answer holds, or <code>null</code> when it wraps none. */
    def String wrappedBase() {
        val vo = wrapped
        return if(vo === null) null else DartTypes.of(vo.base.name) ?: "String"
    }

    /** Whether what it returns is a row rather than a bare value. */
    def boolean returnsRow() {
        kind() != "MethodKind.scalar"
    }

    private static def boolean isPrimitive(String dartType) {
        #["int", "double", "bool", "String", "num", "DateTime"].contains(dartType)
    }

    /** <code>listCategories</code> becomes <code>list-categories</code>, as the REST contracts do. */
    static def String kebab(String name) {
        val out = new StringBuilder()
        for (var i = 0; i < name.length; i++) {
            val c = name.charAt(i)
            if (Character.isUpperCase(c)) {
                out.append('-').append(Character.toLowerCase(c))
            } else {
                out.append(c)
            }
        }
        return out.toString
    }

}

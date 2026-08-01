package org.fuin.dsl.ddd.gen.view

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.dsl.ddd.gen.base.TypeKeys

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.TypeExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.VariableExtensions.*

/**
 * Shared logic for turning a view's methods into REST operations. Used by the contract interface
 * factories ({@link ViewSpringApiArtifactFactory} / {@link ViewQuarkusApiArtifactFactory}) and by
 * {@link FinalViewArtifactFactory} (the controller/resource implementing one of them) so they can never
 * drift apart.
 */
class ViewRestSupport {

    /**
     * Returns the REST sub path of a view method. Defaults to the dash-separated method name if the
     * model declares no explicit 'rest-path' - the same convention the view level uses for its base
     * path.
     *
     * @param method Method to determine the path for.
     *
     * @return Path starting with a slash.
     */
    def static String restPath(Method method) {
        if (method.restPath === null) {
            return "/" + kebabCase(method.name)
        }
        method.restPath
    }

    /**
     * Converts a camel case name into its lower case dash-separated form, which reads far better in
     * a URL ("listReceipts" becomes "list-receipts"). A run of capitals is kept together, so an
     * acronym does not become one dashed letter per character ("readPDFFile" becomes
     * "read-pdf-file").
     *
     * @param name Name to convert.
     *
     * @return Dash-separated lower case name.
     */
    def static String kebabCase(String name) {
        val StringBuilder sb = new StringBuilder()
        for (var int i = 0; i < name.length; i++) {
            val char ch = name.charAt(i)
            if (Character.isUpperCase(ch)) {
                // A dash is only needed when a new word starts: either after a lower case character
                // or at the end of a run of capitals (the last capital belongs to the next word).
                val boolean prevIsLower = i > 0 && !Character.isUpperCase(name.charAt(i - 1))
                val boolean nextIsLower = i + 1 < name.length && !Character.isUpperCase(name.charAt(i + 1))
                if (i > 0 && (prevIsLower || nextIsLower)) {
                    sb.append("-")
                }
                sb.append(Character.toLowerCase(ch))
            } else {
                sb.append(ch)
            }
        }
        sb.toString
    }

    /**
     * Returns the names of all "{name}" placeholders of a REST path, in order of appearance.
     * The model guarantees (via validation) that each of them is a declared parameter.
     *
     * @param path Path to scan.
     *
     * @return Placeholder names - may be empty, never NULL.
     */
    def static List<String> pathVariables(String path) {
        val List<String> names = new ArrayList<String>()
        var int from = 0
        var int start = -1
        while ((start = path.indexOf("{", from)) > -1) {
            val int end = path.indexOf("}", start + 1)
            if (end == -1) {
                from = path.length()
            } else {
                names.add(path.substring(start + 1, end))
                from = end + 1
            }
        }
        names
    }

    /**
     * Returns the Java type a view method produces, including generic arguments, and registers the
     * required references. An 'optional' result is NOT wrapped: over HTTP an absent value is a 404,
     * not an empty Optional.
     *
     * @param ctx Context to add the requirements to.
     * @param method Method to determine the return type for.
     *
     * @return Simple type name like "ReceiptDetails" or "List&lt;ReceiptListItem&gt;", or "Void".
     */
    def static String returnType(CodeSnippetContext ctx, Method method) {
        if (method.returnType === null) {
            return "Void"
        }
        ctx.requiresReference(TypeKeys.refKey(method.returnType.type))
        val name = method.returnType.type.simpleName(ctx)
        if (method.returnType.generics === null) {
            return name
        }
        val StringBuilder sb = new StringBuilder()
        for (arg : method.returnType.generics.args) {
            if (sb.length > 0) {
                sb.append(", ")
            }
            ctx.requiresReference(TypeKeys.refKey(arg))
            sb.append(arg.simpleName(ctx))
        }
        name + "<" + sb + ">"
    }

    /**
     * Renders the parameter declarations of a view method, annotated for the given runtime. A
     * parameter named by a "{name}" placeholder of the path becomes a path variable, every other
     * parameter becomes a query parameter (not required if the model declares it 'optional').
     *
     * @param ctx Context to add the requirements to.
     * @param method Method to render the parameters of.
     * @param runtime Either "quarkus" or "spring".
     *
     * @return Comma separated declarations - empty if the method has no parameters.
     */
    def static String params(CodeSnippetContext ctx, Method method, String runtime) {
        val vars = pathVariables(restPath(method))
        val StringBuilder sb = new StringBuilder()
        for (Parameter param : method.parameters) {
            if (sb.length > 0) {
                sb.append(", ")
            }
            addRequiredReferences(param, ctx)
            sb.append(annotation(ctx, param, vars.contains(param.name), runtime))
            sb.append(" final ")
            sb.append(param.type(ctx))
            sb.append(" ")
            sb.append(param.name)
        }
        sb.toString
    }

    /** Returns the binding annotation for a single parameter. */
    private def static String annotation(CodeSnippetContext ctx, Parameter param, boolean pathVariable,
        String runtime) {
        if (runtime == "quarkus") {
            if (pathVariable) {
                ctx.requiresImport("jakarta.ws.rs.PathParam")
                return '''@PathParam("«param.name»")'''
            }
            ctx.requiresImport("jakarta.ws.rs.QueryParam")
            return '''@QueryParam("«param.name»")'''
        }
        if (pathVariable) {
            ctx.requiresImport("org.springframework.web.bind.annotation.PathVariable")
            return '''@PathVariable("«param.name»")'''
        }
        ctx.requiresImport("org.springframework.web.bind.annotation.RequestParam")
        if (param.optional === null) {
            return '''@RequestParam("«param.name»")'''
        }
        '''@RequestParam(value = "«param.name»", required = false)'''
    }

}

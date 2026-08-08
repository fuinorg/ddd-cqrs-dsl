package org.fuin.dsl.ddd.gen.extensions

import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.dsl.ddd.gen.base.TypeKeys

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.TypeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsTypeExtensions.*
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import java.util.Collections
import jakarta.validation.constraints.NotNull

/**
 * Provides extension methods for Variable.
 */
class VariableExtensions {

	/**
	 * Returns either invariant or precondition constraints.
	 * 
	 * @param variable Attribute or parameter.
	 * @return List of constraints.  
	 */
	@NotNull
    def static List<ConstraintInstance> getConstraints(Variable variable) {
    	if (variable instanceof Attribute) {
    		if (variable.invariants === null || variable.invariants.constraintInstances === null) {
    			return Collections.emptyList();
    		}
    		return variable.invariants.constraintInstances
    	}
    	if (variable instanceof Parameter) {
    		if (variable.preconditions === null || variable.preconditions.constraintInstances === null) {
    			return Collections.emptyList();
    		}
    		return variable.preconditions.constraintInstances
    	}
	}

    /**
     * Returns the simple type name. If there is a multiplicity <code>List</code> 
     * with the type as generic argument will be returned.
     * 
     * @param variable Variable.
     * @param ctx Context.
     * 
     * @return <code>Type name</code> or <code>List&lt;type name&gt;</code>
     */
    def static String type(Variable variable, CodeSnippetContext ctx) {
        if (variable.isPrimitive(ctx)) {
            return variable.type.asJavaPrimitive
        }
        var String name = variable.type.simpleName(ctx);
        if (variable.generics === null) {
            return name;
        }
        val StringBuilder sb = new StringBuilder()
        for (arg : variable.generics.args) {
            if (sb.length > 0) {
                sb.append(", ");                
            }
            sb.append(arg.simpleName(ctx));
        }
        return name + "<" + sb + ">";
    }
    
    /**
     * Adds the required references for a variable to the context.
     * 
     * @param variable Variable to add the required references for.
     * @param ctx Context to add the requirements to.
     */
    def static void addRequiredReferences(Variable variable, CodeSnippetContext ctx) {
        if (variable.generics !== null) {
            for (arg : variable.generics.args) {
                ctx.requiresReference(TypeKeys.refKey(arg))
            }
        }
        ctx.requiresReference(TypeKeys.refKey(variable.type))
    }

    /**
     * Determines if the variable will be rendered as a Java primitive. A primitive can never be
     * {@code null}, so it must not be annotated with {@link org.jspecify.annotations.Nullable} and needs
     * no {@code NullAway.Init} suppression.
     * <p>
     * Three things prevent it, and each of them is the model saying something:
     * <ul>
     * <li>a <b>multiplicity</b> or generic arguments - a primitive cannot be a type argument, so
     * {@code List<Integer>} stays a list of wrappers</li>
     * <li><b>optional</b> - the whole point of declaring it optional is that the value may be absent,
     * which a primitive cannot express</li>
     * <li>a type with <b>no primitive counterpart</b> - {@code String}, {@code Date}, a value object</li>
     * </ul>
     * Everything else is a value the model says is always present, so it is rendered as the primitive
     * rather than as a wrapper that invites a null check which can never fire.
     *
     * @param variable Variable.
     * @param ctx Context.
     *
     * @return {@code true} if the variable is rendered as one of the eight Java primitives.
     */
    def static boolean isPrimitive(Variable variable, CodeSnippetContext ctx) {
        if (variable.generics !== null || variable.optional !== null) {
            return false
        }
        return #{"boolean", "byte", "char", "short", "int", "long", "float", "double"}.contains(
            variable.type.asJavaPrimitive)
    }

}

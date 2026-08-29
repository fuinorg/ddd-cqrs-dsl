package org.fuin.dsl.ddd.gen.rule

import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance
import org.fuin.dsl.cqrs.cqrsDsl.CarrierAttributeArgument
import org.fuin.dsl.cqrs.cqrsDsl.EntityPathArgument
import org.fuin.dsl.cqrs.cqrsDsl.IdentityArgument
import org.fuin.dsl.cqrs.cqrsDsl.LiteralArgument
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.RuleArgument
import org.fuin.dsl.cqrs.cqrsDsl.Service
import org.fuin.dsl.cqrs.cqrsDsl.ServiceCallArgument
import org.fuin.dsl.cqrs.cqrsDsl.VariableArgument
import org.fuin.srcgen4j.commons.GenerateException

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsLiteralExtensions.*

/**
 * Renders one usage of a business rule as the Java that constructs and verifies it.
 *
 * <p><b>The actuals are bound at the usage site, not in the rule.</b> One rule is commonly carried by
 * two operations that agree on nothing: the same value is a field on one and a parameter on the other,
 * under a different name, answered by a different service. A predicate written once against fixed names
 * could not resolve on both, so what each operation hands the rule is stated where it is used.
 *
 * <p>Where a value comes from is decided by what the reference resolves to, which is the same decision
 * the scope provider makes when the model is compiled: a parameter of this operation is passed
 * straight through, anything else the carrier holds is read off it, and a service call is asked of the
 * service the operation was given.
 */
class SrcRuleConstruction {

    val BusinessRuleInstance instance

    val AbstractMethod operation

    val String carrier

    /**
     * Constructor with the usage, the operation carrying it and how to reach the carrier.
     *
     * @param instance Usage of the rule, with its actuals.
     * @param operation Operation the rule is carried by, whose parameters are in scope.
     * @param carrier Expression the carrier's own state is read from, or <code>null</code> for a creating
     *             operation, which has no prior state to read.
     */
    new(BusinessRuleInstance instance, AbstractMethod operation, String carrier) {
        this.instance = instance
        this.operation = operation
        this.carrier = carrier
    }

    override toString() {
        val args = instance.params.nullSafe.map[argument].join(", ")
        return "new " + instance.businessRule.name + "(" + args + ").verify();"
    }

    def private String argument(RuleArgument arg) throws GenerateException {
        switch (arg) {
            LiteralArgument: return arg.literal.str
            IdentityArgument: {
                if (carrier === null) {
                    throw new GenerateException("'own-id' has nothing to read in " + operation.name
                        + ": a creating operation has no instance yet")
                }
                return carrier + ".getId()"
            }
            EntityPathArgument: {
                if (carrier === null) {
                    throw new GenerateException("'own-path' has nothing to read in " + operation.name
                        + ": a creating operation has no instance yet")
                }
                return carrier + ".getPath()"
            }
            CarrierAttributeArgument: {
                if (carrier === null) {
                    throw new GenerateException("'own " + arg.attribute.name + "' has nothing to read in "
                        + operation.name + ": a creating operation has no prior state")
                }
                return carrier + ".get" + arg.attribute.name.toFirstUpper + "()"
            }
            VariableArgument: return valueOf(arg.variable.name)
            ServiceCallArgument: {
                // The same spellings a rule takes, one level down - a service asked about the thing
                // being acted on needs its identity as much as the refusal naming it does.
                val call = arg.args.nullSafe.map[argument].join(", ")
                val invocation = serviceParameter(arg.method) + "." + arg.method.name + "(" + call + ")"
                // A service says "there may be none" with an empty Optional, a rule attribute says it
                // with null - the model writes 'optional' for both, and the two spellings meet here.
                // Unwrapped rather than held, because the condition asks '== null' and nothing else.
                if (arg.method.returnType?.optional !== null) {
                    return invocation + ".orElse(null)"
                }
                return invocation
            }
            default: throw new GenerateException("Unknown rule argument: " + arg?.class?.simpleName)
        }
    }

    /**
     * Where one named value comes from: the operation's own parameter list first, then the carrier.
     *
     * <p>The order matters. An operation that renames something has both a <code>newName</code>
     * parameter and a <code>name</code> field, and a rule carried by it may want either.
     */
    def private String valueOf(String name) throws GenerateException {
        for (parameter : operation.parameters.nullSafe) {
            if (parameter.name == name) {
                return name
            }
        }
        if (carrier === null) {
            throw new GenerateException("'" + name + "' is not a parameter of " + operation.name
                + ", and a creating operation has no prior state to read it from")
        }
        return carrier + ".get" + name.toFirstUpper + "()"
    }

    /**
     * The parameter holding the service a call is asked of.
     *
     * <p>A service reaches an operation as a parameter named after it, which is what the generated
     * operation already takes, so the validator asks for the same thing rather than inventing a way of
     * its own to reach it.
     */
    def private String serviceParameter(Method method) throws GenerateException {
        val owner = method.eContainer
        if (owner instanceof Service) {
            return owner.name.toFirstLower
        }
        throw new GenerateException("'" + method.name + "' is not declared in a service")
    }

    def private static String toFirstUpper(String value) {
        return value.substring(0, 1).toUpperCase + value.substring(1)
    }

    def private static String toFirstLower(String value) {
        return value.substring(0, 1).toLowerCase + value.substring(1)
    }

}

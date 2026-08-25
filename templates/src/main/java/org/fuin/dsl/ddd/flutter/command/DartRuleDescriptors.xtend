package org.fuin.dsl.ddd.flutter.command

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance
import org.fuin.dsl.cqrs.cqrsDsl.CompareOp
import org.fuin.dsl.cqrs.cqrsDsl.EnumInstance
import org.fuin.dsl.cqrs.cqrsDsl.IdentityArgument
import org.fuin.dsl.cqrs.cqrsDsl.RuleAnd
import org.fuin.dsl.cqrs.cqrsDsl.RuleAttrRef
import org.fuin.dsl.cqrs.cqrsDsl.RuleComparison
import org.fuin.dsl.cqrs.cqrsDsl.RuleExpr
import org.fuin.dsl.cqrs.cqrsDsl.RuleIsEmpty
import org.fuin.dsl.cqrs.cqrsDsl.RuleNot
import org.fuin.dsl.cqrs.cqrsDsl.RuleNullOperand
import org.fuin.dsl.cqrs.cqrsDsl.RuleOr
import org.fuin.dsl.cqrs.cqrsDsl.RuleRefOperand
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.dsl.cqrs.cqrsDsl.VariableArgument
import org.fuin.srcgen4j.commons.GenerateException

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * The rules guarding an operation, as far as a client can answer them.
 *
 * <p><b>Advisory, and deliberately incomplete.</b> The server verifies everything the model declares
 * and refuses with a typed exception. What is emitted here is the subset a screen could decide for
 * itself, so it can avoid offering an action that is certain to be refused - and getting that subset
 * from the model is the point: "can a client decide this" stops being a question somebody answers by
 * reading Java.
 *
 * <p>A rule is left out, rather than half described, when any value it is handed is not on the client:
 *
 * <ul>
 * <li>a <b>service call</b> - a question only the server can ask;</li>
 * <li>a <b>parameter of the operation</b> - nobody has typed it yet at the moment the screen decides
 * whether to offer the action;</li>
 * <li>a <b>literal</b> - the client could hold it, but the descriptor has nowhere to put a value that
 * comes from neither the row nor its identity, and no rule in any model needs one yet.</li>
 * </ul>
 *
 * <p>A rule with no condition is left out too: it is written by hand, so there is no predicate to ship.
 */
class DartRuleDescriptors {

    val AbstractMethod operation

    /**
     * Constructor with the operation whose rules these are.
     *
     * @param operation Operation the command targets, or <code>null</code> when it targets none.
     */
    new(AbstractMethod operation) {
        this.operation = operation
    }

    /** Whether anything is worth emitting, so the caller can leave the field out entirely. */
    def boolean empty() {
        return usable.empty
    }

    /** The usages a client could answer, in the order the model declares them. */
    def private List<BusinessRuleInstance> usable() {
        val out = new ArrayList<BusinessRuleInstance>()
        if (operation === null || operation.businessRules === null) {
            return out
        }
        for (instance : operation.businessRules.businessRuleInstances.nullSafe) {
            if (answerable(instance)) {
                out.add(instance)
            }
        }
        return out
    }

    def private boolean answerable(BusinessRuleInstance instance) {
        val rule = instance.businessRule
        if (rule === null || rule.requires === null) {
            return false
        }
        if (rule.attributes.nullSafe.size !== instance.params.nullSafe.size) {
            return false
        }
        for (actual : instance.params.nullSafe) {
            switch (actual) {
                IdentityArgument: { /* the row knows its own identity */ }
                VariableArgument: {
                    if (parameterOfOperation(actual.variable)) {
                        return false
                    }
                }
                default:
                    return false
            }
        }
        return true
    }

    def private boolean parameterOfOperation(Variable variable) {
        for (parameter : operation.parameters.nullSafe) {
            if (parameter === variable) {
                return true
            }
        }
        return false
    }

    override toString() {
        val out = new ArrayList<String>()
        for (instance : usable) {
            out.add(descriptor(instance))
        }
        return out.join("\n")
    }

    def private String descriptor(BusinessRuleInstance instance) throws GenerateException {
        val rule = instance.businessRule
        val fromAttribute = new LinkedHashMap<String, String>()
        val fromIdentity = new ArrayList<String>()
        val attributes = rule.attributes.nullSafe.toList
        val actuals = instance.params.nullSafe.toList
        for (var i = 0; i < attributes.size; i++) {
            val name = attributes.get(i).name
            val actual = actuals.get(i)
            if (actual instanceof IdentityArgument) {
                fromIdentity.add(name)
            } else if (actual instanceof VariableArgument) {
                fromAttribute.put(name, actual.variable.name)
            }
        }
        val parts = new ArrayList<String>()
        parts.add("  rule: '" + rule.name + "',")
        parts.add("  predicate: " + predicate(rule.requires) + ",")
        if (!fromAttribute.empty) {
            val entries = new ArrayList<String>()
            for (entry : fromAttribute.entrySet) {
                entries.add("'" + entry.key + "': '" + entry.value + "'")
            }
            parts.add("  fromAttribute: <String, String>{" + entries.join(", ") + "},")
        }
        if (!fromIdentity.empty) {
            val names = new ArrayList<String>()
            for (name : fromIdentity) {
                names.add("'" + name + "'")
            }
            parts.add("  fromIdentity: <String>[" + names.join(", ") + "],")
        }
        return "RuleDescriptor(\n" + parts.join("\n") + "\n),"
    }

    /**
     * The condition as a const Dart tree.
     *
     * <p>The same shape the JVM generates its check from, which is what the shared conformance vectors
     * exist to keep true as the language grows.
     */
    def private String predicate(RuleExpr node) throws GenerateException {
        switch (node) {
            RuleOr: return "RuleOr(" + predicate(node.left) + ", " + predicate(node.right) + ")"
            RuleAnd: return "RuleAnd(" + predicate(node.left) + ", " + predicate(node.right) + ")"
            RuleNot: return "RuleNot(" + predicate(node.expr) + ")"
            RuleIsEmpty: return "RuleIsEmpty('" + attributeName(node.left) + "')"
            RuleAttrRef: return "RuleAttrRef('" + node.attribute.name + "')"
            RuleComparison: return "RuleComparison('" + attributeName(node.left) + "', CompareOp."
                + node.op.getName().toLowerCase + ", " + operand(node) + ")"
            default: throw new GenerateException("Cannot render a " + node.class.simpleName
                + " as a Dart predicate")
        }
    }

    def private String operand(RuleComparison node) throws GenerateException {
        val right = node.right
        if (right instanceof RuleNullOperand) {
            return "RuleNullOperand()"
        }
        if (right instanceof RuleRefOperand) {
            val target = right.target
            switch (target) {
                // A named value of an enumeration travels as its wire name, which is what a row holds.
                EnumInstance: return "RuleValueOperand('" + target.name + "')"
                Variable: return "RuleAttributeOperand('" + target.name + "')"
                default: throw new GenerateException("Cannot compare against a "
                    + target?.class?.simpleName + " on the client")
            }
        }
        throw new GenerateException("Unknown operand: " + right)
    }

    def private String attributeName(RuleExpr node) throws GenerateException {
        if (node instanceof RuleAttrRef) {
            return node.attribute.name
        }
        throw new GenerateException("Expected an attribute of the rule, but found a "
            + node.class.simpleName)
    }

}

package org.fuin.dsl.ddd.flutter.command

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance
import org.fuin.dsl.cqrs.cqrsDsl.CompareOp
import org.fuin.dsl.cqrs.cqrsDsl.EnumInstance
import org.fuin.dsl.cqrs.cqrsDsl.CarrierAttributeArgument
import org.fuin.dsl.cqrs.cqrsDsl.EntityPathArgument
import org.fuin.dsl.cqrs.cqrsDsl.IdentityArgument
import org.fuin.dsl.cqrs.cqrsDsl.RuleLiteralOperand
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
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.GenerateException

import static extension org.fuin.dsl.cqrs.extensions.CqrsBusinessRulesExtensions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsLiteralExtensions.*

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
 *
 * <p>Each one carries the refusal's own wording, so an action shown greyed out and the same action
 * pressed anyway say one thing rather than two. It travels as the template the model wrote, because it
 * names the thing it refused and only this side knows which thing that is. Its placeholders always
 * resolve: the generator already refuses a model whose exception asks for something the rule does not
 * hold, so every name in the message is a name the rule was handed.
 *
 * <p>It travels with the key it is translated under, too. A disabled action's reason is the one caption
 * on a screen that used to be the model's English whatever language the rest of it was in - the caption
 * above it comes from the bundle and this did not.
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
            // Which usages a client could answer is the model's own question, and the validator asks it
            // of the views on the other side. One definition, so the two cannot drift apart.
            if (instance.clientAnswerable) {
                out.add(instance)
            }
        }
        return out
    }

    override toString() {
        val out = new ArrayList<String>()
        for (instance : usable) {
            out.add(descriptor(instance))
        }
        return out.join("\n")
    }

    def private String descriptor(BusinessRuleInstance instance) throws GenerateException {
        val rule = instance.declaredRule
        val fromAttribute = new LinkedHashMap<String, String>()
        val fromIdentity = new ArrayList<String>()
        val attributes = rule.attributes.nullSafe.toList
        val actuals = instance.params.nullSafe.toList
        for (var i = 0; i < attributes.size; i++) {
            val name = attributes.get(i).name
            val actual = actuals.get(i)
            if (actual instanceof IdentityArgument || actual instanceof EntityPathArgument) {
                // Both read off the row's declared identity - a path is what identifies a child row.
                fromIdentity.add(name)
            } else if (actual instanceof CarrierAttributeArgument) {
                fromAttribute.put(name, actual.attribute.name)
            } else if (actual instanceof VariableArgument) {
                fromAttribute.put(name, actual.variable.name)
            }
        }
        val parts = new ArrayList<String>()
        parts.add("  rule: '" + rule.name + "',")
        parts.add("  predicate: " + predicate(rule.requires) + ",")
        parts.add("  reason: " + AbstractDartSource.dartStringRaw(rule.exception.message) + ",")
        // Where that sentence is translated. Keyed to the exception, in the module the exception is
        // declared in rather than the one the rule is used in: wording is written once, where the thing
        // it describes lives. The template stays beside it as the fallback, the way every ModelText
        // carries the model's own wording.
        parts.add("  text: ModelText(\n    bundle: " + AbstractDartSource.dartString(
            AbstractSource.bundleName(rule.exception.module)) + ",\n    key: "
            + AbstractDartSource.dartString(rule.exception.name) + ",\n  ),")
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
        // A value written out in the condition travels as itself: Dart spells a number, a string and a
        // Boolean the same way Java does, which is why the literal is emitted rather than described.
        if (right instanceof RuleLiteralOperand) {
            return "RuleLiteralOperand(" + right.literal.str + ")"
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

package org.fuin.dsl.ddd.gen.rule

import org.fuin.dsl.cqrs.cqrsDsl.CompareOp
import org.fuin.dsl.cqrs.cqrsDsl.EnumInstance
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
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
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

/**
 * Renders a rule's <code>requires</code> condition as the Java expression that says it holds.
 *
 * <p>The same condition is also shipped to a client as data and answered by its own evaluator, so the
 * two must agree exactly. Two places where the obvious Java would not:
 *
 * <ul>
 * <li><b><code>==</code> is value equality.</b> Java's <code>==</code> is identity and every attribute
 * here is a value object, so this emits <code>Objects.equals(a, b)</code>. Only a comparison against
 * <code>null</code> is written as <code>a == null</code>, where identity and equality are the same
 * thing and the idiom reads better.</li>
 * <li><b>Ordering is <code>compareTo</code>.</b> Nothing but a date is ordered in this language, and a
 * date is a value object rather than a primitive.</li>
 * </ul>
 */
class SrcRuleCondition {

    val CodeSnippetContext ctx

    val RuleExpr expr

    /**
     * Constructor with the context to add imports to and the condition to render.
     *
     * @param ctx Context the rendered expression may need an import from.
     * @param expr Condition as the model declares it.
     */
    new(CodeSnippetContext ctx, RuleExpr expr) {
        this.ctx = ctx
        this.expr = expr
    }

    override toString() {
        return render(expr)
    }

    /**
     * One node of the condition.
     *
     * <p>Every compound node parenthesises its operands rather than tracking precedence. The model's
     * own grammar has already decided the shape - what is left is to make sure the Java parses back to
     * the same tree, and brackets are cheaper to be sure about than a precedence table.
     */
    def private String render(RuleExpr node) throws GenerateException {
        switch (node) {
            RuleOr: return "(" + render(node.left) + " || " + render(node.right) + ")"
            RuleAnd: return "(" + render(node.left) + " && " + render(node.right) + ")"
            RuleNot: return "!(" + render(node.expr) + ")"
            RuleIsEmpty: return name(node.left) + ".isEmpty()"
            RuleComparison: return comparison(node)
            RuleAttrRef: return name(node)
            default: throw new GenerateException("Cannot render a " + node.class.simpleName
                + " as a business rule condition")
        }
    }

    def private String comparison(RuleComparison node) throws GenerateException {
        val String left = name(node.left)
        val operand = node.right
        if (operand instanceof RuleNullOperand) {
            // Absence is the case several rules exist to report, and for it identity and equality are
            // the same question.
            return switch (node.op) {
                case CompareOp.EQ: left + " == null"
                case CompareOp.NE: left + " != null"
                default: throw new GenerateException("'" + node.op.literal
                    + "' has no meaning against null; only '==' and '!=' do")
            }
        }
        val String right = operandOf(operand)
        return switch (node.op) {
            case CompareOp.EQ: {
                ctx.requiresImport("java.util.Objects")
                "Objects.equals(" + left + ", " + right + ")"
            }
            case CompareOp.NE: {
                ctx.requiresImport("java.util.Objects")
                "!Objects.equals(" + left + ", " + right + ")"
            }
            case CompareOp.LT: left + ".compareTo(" + right + ") < 0"
            case CompareOp.LE: left + ".compareTo(" + right + ") <= 0"
            case CompareOp.GT: left + ".compareTo(" + right + ") > 0"
            case CompareOp.GE: left + ".compareTo(" + right + ") >= 0"
        }
    }

    /**
     * The right hand side: one of the rule's own attributes, or a named value of an enumeration.
     *
     * <p>Both are a bare identifier in the model and are told apart by what they resolve to, which is
     * the same decision the scope provider makes when the model is compiled.
     */
    def private String operandOf(Object operand) throws GenerateException {
        if (operand instanceof RuleRefOperand) {
            val target = operand.target
            switch (target) {
                Variable: return target.name
                EnumInstance: {
                    val owner = target.eContainer
                    if (owner instanceof EnumObject) {
                        return owner.name + "." + target.name
                    }
                    throw new GenerateException("'" + target.name + "' is not inside an enumeration")
                }
                default: throw new GenerateException("Cannot compare against a "
                    + target?.class?.simpleName)
            }
        }
        throw new GenerateException("Unknown operand: " + operand)
    }

    def private String name(RuleExpr node) throws GenerateException {
        if (node instanceof RuleAttrRef) {
            return node.attribute.name
        }
        throw new GenerateException("Expected an attribute of the rule, but found a "
            + node.class.simpleName)
    }

}

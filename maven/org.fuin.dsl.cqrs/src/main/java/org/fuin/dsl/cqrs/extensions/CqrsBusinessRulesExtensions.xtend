package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.eclipse.xtext.EcoreUtil2
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRules
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance
import org.fuin.dsl.cqrs.cqrsDsl.CarrierAttributeArgument
import org.fuin.dsl.cqrs.cqrsDsl.EntityPathArgument
import org.fuin.dsl.cqrs.cqrsDsl.IdentityArgument
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.dsl.cqrs.cqrsDsl.VariableArgument

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

class CqrsBusinessRulesExtensions {
	
	/**
	 * Returns a non-null constraint list.
	 * 
	 * @param businessRules Container with constraints.
	 * 
	 * @return List of constraints that is never <code<null</code>.
	 */
	static def List<BusinessRuleInstance> nullSafe(BusinessRules businessRules) {
		if ((businessRules === null) || (businessRules.businessRuleInstances === null)) {
			return new ArrayList<BusinessRuleInstance>()
		}
		return businessRules.businessRuleInstances
	}

	/**
	 * Whether a client could answer this usage of a rule for itself, from the row it is looking at.
	 *
	 * <p>The server verifies everything the model declares. This is the subset a screen could decide
	 * before sending anything, so it can avoid offering an action that is certain to be refused - and
	 * the model is what answers it, rather than somebody reading the generated Java.
	 *
	 * <p>A rule is answerable only when every value it is handed is on the client. A <b>service call</b>
	 * is a question only the server can ask; a <b>parameter of the operation</b> has not been typed yet
	 * at the moment a menu decides whether to offer the action; a <b>literal</b> comes from neither the
	 * row nor its identity. A rule with no condition is written by hand and has no predicate to ship.
	 *
	 * <p>Deliberately all-or-nothing: an attribute bound to something the client cannot reach makes the
	 * whole usage unanswerable, even where the condition never reads it. Half a rule decided from what
	 * the client happened to have would be worse than none.
	 *
	 * @param instance Usage of a rule by one operation.
	 *
	 * @return <code>true</code> when a client could decide it.
	 */
	/**
	 * The declared rule a usage names, or <code>null</code> where it names a business key.
	 *
	 * <p>A key derives its rule rather than declaring one, and nothing generates that yet. Until it
	 * does, a key usage answers like a rule the model has said nothing about: it keeps its
	 * <code>// TODO Verify</code> line with the operation instead of being quietly left out.</p>
	 *
	 * @param instance Usage of a rule or a key by one operation.
	 *
	 * @return Declared rule or <code>null</code>.
	 */
	static def BusinessRule declaredRule(BusinessRuleInstance instance) {
		val rule = instance?.businessRule
		return if (rule instanceof BusinessRule) rule else null
	}

	static def boolean clientAnswerable(BusinessRuleInstance instance) {
		if (instance === null) {
			return false
		}
		val rule = instance.businessRule
		if (!(rule instanceof BusinessRule)) {
			// A business key asks the server whether the key is taken, so no client can answer it.
			return false
		}
		val declaring = rule as BusinessRule
		if (declaring.requires === null) {
			return false
		}
		val operation = EcoreUtil2.getContainerOfType(instance, AbstractMethod)
		if (operation === null) {
			return false
		}
		if (declaring.attributes.nullSafe.size !== instance.params.nullSafe.size) {
			return false
		}
		for (actual : instance.params.nullSafe) {
			switch (actual) {
				IdentityArgument: { /* the row knows its own identity */ }
				EntityPathArgument: { /* likewise: a row addressed by a path carries that path */ }
				CarrierAttributeArgument: { /* the row carries the state the carrier holds now */ }
				VariableArgument: {
					if (operation.declares(actual.variable)) {
						return false
					}
				}
				default:
					return false
			}
		}
		return true
	}

	/**
	 * What the thing being acted on has to publish for a client to answer this usage.
	 *
	 * <p>Names as the <em>carrier</em> spells them, not as the rule does: the actuals are bound where
	 * the rule is used, so one rule carried by two operations is handed the same value under whatever
	 * name each of them has for it. The carrier's own identity is left out - a row always knows it.
	 *
	 * <p>Empty for a usage no client can answer, which is not the same as "needs nothing".
	 *
	 * @param instance Usage of a rule by one operation.
	 *
	 * @return Attribute names, in the order the rule declares them.
	 */
	static def List<String> carrierAttributesRead(BusinessRuleInstance instance) {
		val out = new ArrayList<String>()
		if (!instance.clientAnswerable) {
			return out
		}
		for (actual : instance.params.nullSafe) {
			switch (actual) {
				CarrierAttributeArgument: out.add(actual.attribute.name)
				VariableArgument: out.add(actual.variable.name)
			}
		}
		return out
	}

	/** Whether the operation declares the given name as one of its own parameters. */
	static def private boolean declares(AbstractMethod operation, Variable variable) {
		for (parameter : operation.parameters.nullSafe) {
			if (parameter === variable) {
				return true
			}
		}
		return false
	}

}

package org.fuin.dsl.cqrs.scoping

import java.util.ArrayList
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes


import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.cqrs.cqrsDsl.InternalType
import org.fuin.dsl.cqrs.cqrsDsl.RuleAttrRef
import org.fuin.dsl.cqrs.cqrsDsl.RuleComparison
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject

/**
 * Narrows the cross references that must not be resolved by name across the whole model.
 *
 * <p>Everything the DSL referenced before this class had content was a <em>type</em>, addressed under
 * the import rules {@link CqrsDslLocalScopeProvider} implements. The references added for business
 * keys and business rules are different in kind: they name a part of the very element they are written
 * in - an attribute of this row, an attribute of this rule, a parameter of the method carrying it.
 * Left to the import graph, <code>identified-by id</code> would resolve to any attribute called
 * "id" anywhere in sight, and the model would link to the wrong thing without saying so.</p>
 *
 * <p>Each method here therefore returns a closed scope, without delegating to the parent: a name that
 * is not part of the enclosing element is a linking error, which is the whole point of making these
 * constructs cross references rather than strings.</p>
 */
class CqrsDslScopeProvider extends AbstractCqrsDslScopeProvider {

	/**
	 * Answers the references that are local to the element they are written in, and hands everything
	 * else - which is every reference to a type - to the import aware delegate.
	 */
	override IScope getScope(EObject context, EReference reference) {
		switch reference {
			// The row's identity is one of its own attributes.
			case CqrsDslPackage.Literals.VALUE_OBJECT__IDENTIFIED_BY:
				Scopes.scopeFor(EcoreUtil2.getContainerOfType(context, ValueObject).attributes)
			// A business key is made of attributes of the type that declares it.
			case CqrsDslPackage.Literals.KEY__ATTRIBUTES:
				Scopes.scopeFor(context.declaringAttributes)
			// The left hand side of a condition is an attribute the rule itself declares.
			case CqrsDslPackage.Literals.RULE_ATTR_REF__ATTRIBUTE:
				Scopes.scopeFor(context.ruleAttributes)
			// The right hand side of a comparison. Both an attribute and a named value of an
			// enumeration are a bare identifier, so the attribute on the left decides which of the two
			// can be meant - and when it is enum typed, both can: "kind == categoryKind" compares two
			// attributes of the same enumeration.
			case CqrsDslPackage.Literals.RULE_REF_OPERAND__TARGET: {
				val candidates = new ArrayList<EObject>(context.ruleAttributes)
				candidates.addAll(context.leftAttribute.enumValues)
				Scopes.scopeFor(candidates)
			}
			// An actual is something the operation carrying the rule holds, and so is an argument
			// handed to a service method.
			case CqrsDslPackage.Literals.VARIABLE_ARGUMENT__VARIABLE:
				Scopes.scopeFor(context.carrierVariables)
			// 'own' reaches past the operation's parameters to the state of the type itself, which is
			// the one case a bare name cannot express: an edit's parameter commonly carries the very
			// name of the field it would overwrite.
			case CqrsDslPackage.Literals.CARRIER_ATTRIBUTE_ARGUMENT__ATTRIBUTE:
				Scopes.scopeFor(context.declaringAttributes)
			// A rule reaches outside itself only through the operation's declared context.
			case CqrsDslPackage.Literals.SERVICE_CALL_ARGUMENT__METHOD:
				Scopes.scopeFor(context.contextMethods)
			default:
				super.getScope(context, reference)
		}
	}

	/** Attributes of the aggregate, entity or value object a key is declared in. */
	private def List<Attribute> declaringAttributes(EObject obj) {
		val type = EcoreUtil2.getContainerOfType(obj, InternalType)
		if (type === null) emptyList else type.attributes
	}

	/** Attributes the enclosing business rule declares for itself. */
	private def List<Attribute> ruleAttributes(EObject obj) {
		val rule = EcoreUtil2.getContainerOfType(obj, BusinessRule)
		if (rule === null) emptyList else rule.attributes
	}

	/** The attribute a comparison is written about, which the enclosing comparison holds. */
	private def Attribute leftAttribute(EObject obj) {
		val container = obj.eContainer
		val left = if (container instanceof RuleComparison) container.left else null
		if (left instanceof RuleAttrRef) left.attribute else null
	}

	/** The values of the enumeration an attribute is typed with, or nothing when it is not one. */
	private def List<? extends EObject> enumValues(Attribute attribute) {
		if (attribute === null) {
			return emptyList
		}
		val type = attribute.type
		if (type instanceof EnumObject) type.instances else emptyList
	}

	/**
	 * What the operation carrying the rule can hand it: its own parameters, and the state of the type
	 * it belongs to. The two together are why one rule can be carried by operations that agree on
	 * nothing - the same value is a parameter on one and a field on another.
	 */
	private def List<EObject> carrierVariables(EObject obj) {
		val result = new ArrayList<EObject>
		val method = EcoreUtil2.getContainerOfType(obj, AbstractMethod)
		if (method !== null) {
			result.addAll(method.parameters)
		}
		val type = EcoreUtil2.getContainerOfType(obj, InternalType)
		if (type !== null) {
			result.addAll(type.attributes)
			// Deliberately not the identity. An aggregate holds one and a rule commonly wants it - it is
			// what lets a refusal name the thing it refused - but "id" is never declared as an
			// attribute, and a synthetic one belongs to no resource, so linking to it yields a dangling
			// reference. Naming the identity in an actual needs the grammar to say so.
		}
		result
	}

	/** Methods of the operation context, plus those of any service the operation declares inline. */
	private def List<EObject> contextMethods(EObject obj) {
		val result = new ArrayList<EObject>
		val method = EcoreUtil2.getContainerOfType(obj, AbstractMethod)
		if (method !== null) {
			val context = method.operationContext
			if (context !== null) {
				result.addAll(context.methods)
			}
			for (service : method.services) {
				result.addAll(service.methods)
			}
		}
		result
	}
}

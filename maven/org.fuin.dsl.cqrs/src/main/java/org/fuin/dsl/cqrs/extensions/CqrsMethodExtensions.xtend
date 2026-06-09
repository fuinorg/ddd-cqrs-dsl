package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Exception
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Parameter

import static extension org.fuin.dsl.cqrs.extensions.CqrsBusinessRulesExtensions.*
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance

/**
 * Provides extension methods for Method.
 */
class CqrsMethodExtensions {


	def static List<BusinessRule> allBusinessRules(Method method) {
		val List<BusinessRule> list = new ArrayList<BusinessRule>();
		if ((method.refMethod !== null) && (method != method.refMethod)) {
			list.addAll(method.refMethod.allBusinessRules);
		}
		for (BusinessRuleInstance ci : method.businessRules.nullSafe) {
			list.add(ci.businessRule);
		}
		return list;
	}

	def static List<Exception> allExceptions(Method method) {
		var List<Exception> list = new ArrayList<Exception>();
		if ((method.refMethod !== null) && (method != method.refMethod)) {
			list.addAll(method.refMethod.allExceptions);
		}
		for (BusinessRuleInstance ci : method.businessRules.nullSafe) {
			list.add(ci.businessRule.exception);
		}
		return list;
	}
	
	def static List<Parameter> allParameters(Method method) {
		var List<Parameter> list = new ArrayList<Parameter>();
		list.addAll(method.parameters);
		if ((method.refMethod !== null) && (method != method.refMethod)) {
			list.addAll(method.refMethod.allParameters);
		}
		return list;
	}

}

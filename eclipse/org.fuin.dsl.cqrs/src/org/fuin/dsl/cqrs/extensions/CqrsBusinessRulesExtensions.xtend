package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRules
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance

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
	
}
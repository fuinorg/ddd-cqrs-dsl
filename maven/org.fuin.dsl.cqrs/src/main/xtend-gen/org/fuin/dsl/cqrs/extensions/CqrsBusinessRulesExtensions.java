package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRules;

@SuppressWarnings("all")
public class CqrsBusinessRulesExtensions {
  /**
   * Returns a non-null constraint list.
   * 
   * @param businessRules Container with constraints.
   * 
   * @return List of constraints that is never <code<null</code>.
   */
  public static List<BusinessRuleInstance> nullSafe(final BusinessRules businessRules) {
    if (((businessRules == null) || (businessRules.getBusinessRuleInstances() == null))) {
      return new ArrayList<BusinessRuleInstance>();
    }
    return businessRules.getBusinessRuleInstances();
  }
}

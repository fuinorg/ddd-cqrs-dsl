package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance;
import org.fuin.dsl.cqrs.cqrsDsl.Method;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;

/**
 * Provides extension methods for Method.
 */
@SuppressWarnings("all")
public class CqrsMethodExtensions {
  public static List<BusinessRule> allBusinessRules(final Method method) {
    final List<BusinessRule> list = new ArrayList<BusinessRule>();
    if (((method.getRefMethod() != null) && (!Objects.equals(method, method.getRefMethod())))) {
      list.addAll(CqrsMethodExtensions.allBusinessRules(method.getRefMethod()));
    }
    List<BusinessRuleInstance> _nullSafe = CqrsBusinessRulesExtensions.nullSafe(method.getBusinessRules());
    for (final BusinessRuleInstance ci : _nullSafe) {
      list.add(ci.getBusinessRule());
    }
    return list;
  }

  public static List<org.fuin.dsl.cqrs.cqrsDsl.Exception> allExceptions(final Method method) {
    List<org.fuin.dsl.cqrs.cqrsDsl.Exception> list = new ArrayList<org.fuin.dsl.cqrs.cqrsDsl.Exception>();
    if (((method.getRefMethod() != null) && (!Objects.equals(method, method.getRefMethod())))) {
      list.addAll(CqrsMethodExtensions.allExceptions(method.getRefMethod()));
    }
    List<BusinessRuleInstance> _nullSafe = CqrsBusinessRulesExtensions.nullSafe(method.getBusinessRules());
    for (final BusinessRuleInstance ci : _nullSafe) {
      list.add(ci.getBusinessRule().getException());
    }
    return list;
  }

  public static List<Parameter> allParameters(final Method method) {
    List<Parameter> list = new ArrayList<Parameter>();
    list.addAll(method.getParameters());
    if (((method.getRefMethod() != null) && (!Objects.equals(method, method.getRefMethod())))) {
      list.addAll(CqrsMethodExtensions.allParameters(method.getRefMethod()));
    }
    return list;
  }
}

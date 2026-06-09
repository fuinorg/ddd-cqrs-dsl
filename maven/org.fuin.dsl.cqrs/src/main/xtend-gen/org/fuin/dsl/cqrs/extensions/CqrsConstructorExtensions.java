package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance;
import org.fuin.dsl.cqrs.cqrsDsl.Constructor;

/**
 * Provides extension methods for Constructor.
 */
@SuppressWarnings("all")
public class CqrsConstructorExtensions {
  public static List<org.fuin.dsl.cqrs.cqrsDsl.Exception> allExceptions(final Constructor constructor) {
    List<org.fuin.dsl.cqrs.cqrsDsl.Exception> list = new ArrayList<org.fuin.dsl.cqrs.cqrsDsl.Exception>();
    List<BusinessRuleInstance> _nullSafe = CqrsBusinessRulesExtensions.nullSafe(constructor.getBusinessRules());
    for (final BusinessRuleInstance cc : _nullSafe) {
      list.add(cc.getBusinessRule().getException());
    }
    return list;
  }
}

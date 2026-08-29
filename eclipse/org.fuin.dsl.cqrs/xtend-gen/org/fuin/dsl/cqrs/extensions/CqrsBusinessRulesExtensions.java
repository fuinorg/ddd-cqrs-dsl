package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.EcoreUtil2;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractBusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRules;
import org.fuin.dsl.cqrs.cqrsDsl.CarrierAttributeArgument;
import org.fuin.dsl.cqrs.cqrsDsl.EntityPathArgument;
import org.fuin.dsl.cqrs.cqrsDsl.IdentityArgument;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;
import org.fuin.dsl.cqrs.cqrsDsl.RuleArgument;
import org.fuin.dsl.cqrs.cqrsDsl.RuleExpr;
import org.fuin.dsl.cqrs.cqrsDsl.Variable;
import org.fuin.dsl.cqrs.cqrsDsl.VariableArgument;

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
  public static BusinessRule declaredRule(final BusinessRuleInstance instance) {
    AbstractBusinessRule _businessRule = null;
    if (instance!=null) {
      _businessRule=instance.getBusinessRule();
    }
    final AbstractBusinessRule rule = _businessRule;
    BusinessRule _xifexpression = null;
    if ((rule instanceof BusinessRule)) {
      _xifexpression = ((BusinessRule)rule);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }

  public static boolean clientAnswerable(final BusinessRuleInstance instance) {
    if ((instance == null)) {
      return false;
    }
    final AbstractBusinessRule rule = instance.getBusinessRule();
    if ((!(rule instanceof BusinessRule))) {
      return false;
    }
    final BusinessRule declaring = ((BusinessRule) rule);
    RuleExpr _requires = declaring.getRequires();
    boolean _tripleEquals = (_requires == null);
    if (_tripleEquals) {
      return false;
    }
    final AbstractMethod operation = EcoreUtil2.<AbstractMethod>getContainerOfType(instance, AbstractMethod.class);
    if ((operation == null)) {
      return false;
    }
    int _size = CqrsCollectionExtensions.<Attribute>nullSafe(declaring.getAttributes()).size();
    int _size_1 = CqrsCollectionExtensions.<RuleArgument>nullSafe(instance.getParams()).size();
    boolean _tripleNotEquals = (_size != _size_1);
    if (_tripleNotEquals) {
      return false;
    }
    List<RuleArgument> _nullSafe = CqrsCollectionExtensions.<RuleArgument>nullSafe(instance.getParams());
    for (final RuleArgument actual : _nullSafe) {
      boolean _matched = false;
      if (actual instanceof IdentityArgument) {
        _matched=true;
      }
      if (!_matched) {
        if (actual instanceof EntityPathArgument) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (actual instanceof CarrierAttributeArgument) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (actual instanceof VariableArgument) {
          _matched=true;
          boolean _declares = CqrsBusinessRulesExtensions.declares(operation, ((VariableArgument)actual).getVariable());
          if (_declares) {
            return false;
          }
        }
      }
      if (!_matched) {
        return false;
      }
    }
    return true;
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
  public static List<String> carrierAttributesRead(final BusinessRuleInstance instance) {
    final ArrayList<String> out = new ArrayList<String>();
    boolean _clientAnswerable = CqrsBusinessRulesExtensions.clientAnswerable(instance);
    boolean _not = (!_clientAnswerable);
    if (_not) {
      return out;
    }
    List<RuleArgument> _nullSafe = CqrsCollectionExtensions.<RuleArgument>nullSafe(instance.getParams());
    for (final RuleArgument actual : _nullSafe) {
      boolean _matched = false;
      if (actual instanceof CarrierAttributeArgument) {
        _matched=true;
        out.add(((CarrierAttributeArgument)actual).getAttribute().getName());
      }
      if (!_matched) {
        if (actual instanceof VariableArgument) {
          _matched=true;
          out.add(((VariableArgument)actual).getVariable().getName());
        }
      }
    }
    return out;
  }

  /**
   * Whether the operation declares the given name as one of its own parameters.
   */
  private static boolean declares(final AbstractMethod operation, final Variable variable) {
    List<Parameter> _nullSafe = CqrsCollectionExtensions.<Parameter>nullSafe(operation.getParameters());
    for (final Parameter parameter : _nullSafe) {
      if ((parameter == variable)) {
        return true;
      }
    }
    return false;
  }
}

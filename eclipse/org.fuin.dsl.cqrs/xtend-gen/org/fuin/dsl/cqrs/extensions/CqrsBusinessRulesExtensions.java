package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.EcoreUtil2;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRules;
import org.fuin.dsl.cqrs.cqrsDsl.CarrierAttributeArgument;
import org.fuin.dsl.cqrs.cqrsDsl.IdentityArgument;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;
import org.fuin.dsl.cqrs.cqrsDsl.RuleArgument;
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
  public static boolean clientAnswerable(final BusinessRuleInstance instance) {
    if ((instance == null)) {
      return false;
    }
    final BusinessRule rule = instance.getBusinessRule();
    if (((rule == null) || (rule.getRequires() == null))) {
      return false;
    }
    final AbstractMethod operation = EcoreUtil2.<AbstractMethod>getContainerOfType(instance, AbstractMethod.class);
    if ((operation == null)) {
      return false;
    }
    int _size = CqrsCollectionExtensions.<Attribute>nullSafe(rule.getAttributes()).size();
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

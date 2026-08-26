package org.fuin.dsl.cqrs.scoping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject;
import org.fuin.dsl.cqrs.cqrsDsl.InternalType;
import org.fuin.dsl.cqrs.cqrsDsl.RuleAttrRef;
import org.fuin.dsl.cqrs.cqrsDsl.RuleComparison;
import org.fuin.dsl.cqrs.cqrsDsl.Service;
import org.fuin.dsl.cqrs.cqrsDsl.Type;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;

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
@SuppressWarnings("all")
public class CqrsDslScopeProvider extends AbstractCqrsDslScopeProvider {
  /**
   * Answers the references that are local to the element they are written in, and hands everything
   * else - which is every reference to a type - to the import aware delegate.
   */
  @Override
  public IScope getScope(final EObject context, final EReference reference) {
    IScope _switchResult = null;
    boolean _matched = false;
    if (Objects.equals(reference, CqrsDslPackage.Literals.VALUE_OBJECT__IDENTIFIED_BY)) {
      _matched=true;
      _switchResult = Scopes.scopeFor(EcoreUtil2.<ValueObject>getContainerOfType(context, ValueObject.class).getAttributes());
    }
    if (!_matched) {
      if (Objects.equals(reference, CqrsDslPackage.Literals.KEY__ATTRIBUTES)) {
        _matched=true;
        _switchResult = Scopes.scopeFor(this.declaringAttributes(context));
      }
    }
    if (!_matched) {
      if (Objects.equals(reference, CqrsDslPackage.Literals.RULE_ATTR_REF__ATTRIBUTE)) {
        _matched=true;
        _switchResult = Scopes.scopeFor(this.ruleAttributes(context));
      }
    }
    if (!_matched) {
      if (Objects.equals(reference, CqrsDslPackage.Literals.RULE_REF_OPERAND__TARGET)) {
        _matched=true;
        IScope _xblockexpression = null;
        {
          List<Attribute> _ruleAttributes = this.ruleAttributes(context);
          final ArrayList<EObject> candidates = new ArrayList<EObject>(_ruleAttributes);
          candidates.addAll(this.enumValues(this.leftAttribute(context)));
          _xblockexpression = Scopes.scopeFor(candidates);
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      if (Objects.equals(reference, CqrsDslPackage.Literals.VARIABLE_ARGUMENT__VARIABLE)) {
        _matched=true;
        _switchResult = Scopes.scopeFor(this.carrierVariables(context));
      }
    }
    if (!_matched) {
      if (Objects.equals(reference, CqrsDslPackage.Literals.CARRIER_ATTRIBUTE_ARGUMENT__ATTRIBUTE)) {
        _matched=true;
        _switchResult = Scopes.scopeFor(this.declaringAttributes(context));
      }
    }
    if (!_matched) {
      if (Objects.equals(reference, CqrsDslPackage.Literals.SERVICE_CALL_ARGUMENT__METHOD)) {
        _matched=true;
        _switchResult = Scopes.scopeFor(this.contextMethods(context));
      }
    }
    if (!_matched) {
      _switchResult = super.getScope(context, reference);
    }
    return _switchResult;
  }

  /**
   * Attributes of the aggregate, entity or value object a key is declared in.
   */
  private List<Attribute> declaringAttributes(final EObject obj) {
    List<Attribute> _xblockexpression = null;
    {
      final InternalType type = EcoreUtil2.<InternalType>getContainerOfType(obj, InternalType.class);
      List<Attribute> _xifexpression = null;
      if ((type == null)) {
        _xifexpression = CollectionLiterals.<Attribute>emptyList();
      } else {
        _xifexpression = type.getAttributes();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  /**
   * Attributes the enclosing business rule declares for itself.
   */
  private List<Attribute> ruleAttributes(final EObject obj) {
    List<Attribute> _xblockexpression = null;
    {
      final BusinessRule rule = EcoreUtil2.<BusinessRule>getContainerOfType(obj, BusinessRule.class);
      List<Attribute> _xifexpression = null;
      if ((rule == null)) {
        _xifexpression = CollectionLiterals.<Attribute>emptyList();
      } else {
        _xifexpression = rule.getAttributes();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  /**
   * The attribute a comparison is written about, which the enclosing comparison holds.
   */
  private Attribute leftAttribute(final EObject obj) {
    Attribute _xblockexpression = null;
    {
      final EObject container = obj.eContainer();
      RuleAttrRef _xifexpression = null;
      if ((container instanceof RuleComparison)) {
        _xifexpression = ((RuleComparison)container).getLeft();
      } else {
        _xifexpression = null;
      }
      final RuleAttrRef left = _xifexpression;
      Attribute _xifexpression_1 = null;
      if ((left instanceof RuleAttrRef)) {
        _xifexpression_1 = left.getAttribute();
      } else {
        _xifexpression_1 = null;
      }
      _xblockexpression = _xifexpression_1;
    }
    return _xblockexpression;
  }

  /**
   * The values of the enumeration an attribute is typed with, or nothing when it is not one.
   */
  private List<? extends EObject> enumValues(final Attribute attribute) {
    List<? extends EObject> _xblockexpression = null;
    {
      if ((attribute == null)) {
        return CollectionLiterals.<EObject>emptyList();
      }
      final Type type = attribute.getType();
      List<? extends EObject> _xifexpression = null;
      if ((type instanceof EnumObject)) {
        _xifexpression = ((EnumObject)type).getInstances();
      } else {
        _xifexpression = CollectionLiterals.<EObject>emptyList();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  /**
   * What the operation carrying the rule can hand it: its own parameters, and the state of the type
   * it belongs to. The two together are why one rule can be carried by operations that agree on
   * nothing - the same value is a parameter on one and a field on another.
   */
  private List<EObject> carrierVariables(final EObject obj) {
    ArrayList<EObject> _xblockexpression = null;
    {
      final ArrayList<EObject> result = new ArrayList<EObject>();
      final AbstractMethod method = EcoreUtil2.<AbstractMethod>getContainerOfType(obj, AbstractMethod.class);
      if ((method != null)) {
        result.addAll(method.getParameters());
      }
      final InternalType type = EcoreUtil2.<InternalType>getContainerOfType(obj, InternalType.class);
      if ((type != null)) {
        result.addAll(type.getAttributes());
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }

  /**
   * Methods of the operation context, plus those of any service the operation declares inline.
   */
  private List<EObject> contextMethods(final EObject obj) {
    ArrayList<EObject> _xblockexpression = null;
    {
      final ArrayList<EObject> result = new ArrayList<EObject>();
      final AbstractMethod method = EcoreUtil2.<AbstractMethod>getContainerOfType(obj, AbstractMethod.class);
      if ((method != null)) {
        final Service context = method.getOperationContext();
        if ((context != null)) {
          result.addAll(context.getMethods());
        }
        EList<Service> _services = method.getServices();
        for (final Service service : _services) {
          result.addAll(service.getMethods());
        }
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
}

package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.CollisionStrategy;
import org.fuin.dsl.cqrs.cqrsDsl.Key;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;
import org.fuin.dsl.cqrs.cqrsDsl.Type;

/**
 * The vocabulary a business key derives, in one place.
 * 
 * <p>A key states a natural key; what it derives is a uniqueness rule, the service method that answers
 * whether the key is taken, and the actuals binding the two at each operation that checks it. Those
 * names are generated rather than written, so they are settled here and nowhere else - a name computed
 * twice is a name that can disagree with itself.</p>
 */
@SuppressWarnings("all")
public class CqrsKeyExtensions {
  /**
   * The name of the derived rule's first attribute: whether something already holds the key.
   * 
   * @param key Key that derives it.
   * 
   * @return Attribute name, first letter lower case.
   */
  public static String answerName(final Key key) {
    String _firstLower = StringExtensions.toFirstLower(key.getName());
    return (_firstLower + "Taken");
  }

  /**
   * The name of the derived uniqueness rule.
   * 
   * @param key Key that derives it.
   * 
   * @return Rule name.
   */
  public static String ruleName(final Key key) {
    String _firstUpper = StringExtensions.toFirstUpper(key.getName());
    return (_firstUpper + "MustBeUnique");
  }

  /**
   * The name of the service method that answers whether the key is taken.
   * 
   * @param key Key that derives it.
   * 
   * @return Method name.
   */
  public static String serviceMethodName(final Key key) {
    String _firstUpper = StringExtensions.toFirstUpper(key.getName());
    return ("exists" + _firstUpper);
  }

  /**
   * How many values the derived rule decides from: the answer, plus every attribute the key is made
   * of, because a refusal names the key it refused.
   * 
   * @param key Key that derives it.
   * 
   * @return Number of actuals an explicit usage has to supply.
   */
  public static int derivedAttributeCount(final Key key) {
    int _size = key.getKeyAttributes().size();
    return (1 + _size);
  }

  /**
   * The operation's parameter a key attribute binds to, or <code>null</code> where it binds to what
   * the carrier already holds.
   * 
   * <p><b>By type, not by name.</b> An operation that edits commonly names its parameter after the
   * change rather than the field - <code>rename</code> takes a <code>newName</code> and the key is on
   * <code>name</code> - so a name match would bind nothing on exactly the operations where a
   * uniqueness check matters most. The type is what the two agree on.</p>
   * 
   * <p>Nothing is bound where the pairing is not one to one; see {@link #ambiguousAttributes}, which
   * is what reports it.</p>
   * 
   * @param key Key the attribute belongs to.
   * @param keyAttribute Attribute the key is made of.
   * @param operation Operation checking the key.
   * 
   * @return Parameter, or <code>null</code> where the operation has none of that type.
   */
  public static Parameter boundParameter(final Key key, final Attribute keyAttribute, final AbstractMethod operation) {
    boolean _isEmpty = CqrsKeyExtensions.ambiguousAttributes(key, operation).isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      return null;
    }
    final Function1<Parameter, Boolean> _function = (Parameter it) -> {
      Type _type = it.getType();
      Type _type_1 = keyAttribute.getType();
      return Boolean.valueOf((_type == _type_1));
    };
    final List<Parameter> matching = IterableExtensions.<Parameter>toList(IterableExtensions.<Parameter>filter(operation.getParameters(), _function));
    Parameter _xifexpression = null;
    int _size = matching.size();
    boolean _equals = (_size == 1);
    if (_equals) {
      _xifexpression = IterableExtensions.<Parameter>head(matching);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }

  /**
   * The key attributes an operation binds to more than one of its parameters.
   * 
   * <p>Two parameters of the key attribute's type and nothing to choose between them. Guessing here
   * would silently check uniqueness against the wrong value, which is the defect class this construct
   * exists to remove, so it is refused and the model says the actuals instead.</p>
   * 
   * @param key Key being checked.
   * @param operation Operation checking it.
   * 
   * @return Attributes that cannot be bound, empty where all of them can.
   */
  public static List<Attribute> ambiguousAttributes(final Key key, final AbstractMethod operation) {
    final ArrayList<Attribute> out = new ArrayList<Attribute>();
    EList<Attribute> _keyAttributes = key.getKeyAttributes();
    for (final Attribute attribute : _keyAttributes) {
      {
        final Function1<Parameter, Boolean> _function = (Parameter it) -> {
          Type _type = it.getType();
          Type _type_1 = attribute.getType();
          return Boolean.valueOf((_type == _type_1));
        };
        final int parameters = IterableExtensions.size(IterableExtensions.<Parameter>filter(operation.getParameters(), _function));
        final Function1<Attribute, Boolean> _function_1 = (Attribute it) -> {
          Type _type = it.getType();
          Type _type_1 = attribute.getType();
          return Boolean.valueOf((_type == _type_1));
        };
        final int siblings = IterableExtensions.size(IterableExtensions.<Attribute>filter(key.getKeyAttributes(), _function_1));
        if (((parameters > 1) || ((siblings > 1) && (parameters > 0)))) {
          out.add(attribute);
        }
      }
    }
    return out;
  }

  /**
   * Whether the key generates a rule at all. Only a key that refuses does: 'overwrite' and 'skip'
   * are what the handler does with the second occurrence, not something an operation is refused for.
   * 
   * @param key Key to check.
   * 
   * @return <code>true</code> when a collision is a refusal.
   */
  public static boolean refuses(final Key key) {
    CollisionStrategy _onCollision = key.getOnCollision();
    return Objects.equals(_onCollision, CollisionStrategy.REFUSE);
  }
}

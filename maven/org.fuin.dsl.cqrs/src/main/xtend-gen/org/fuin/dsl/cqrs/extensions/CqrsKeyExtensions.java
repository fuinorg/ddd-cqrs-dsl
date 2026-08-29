package org.fuin.dsl.cqrs.extensions;

import java.util.Objects;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.CollisionStrategy;
import org.fuin.dsl.cqrs.cqrsDsl.Key;

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

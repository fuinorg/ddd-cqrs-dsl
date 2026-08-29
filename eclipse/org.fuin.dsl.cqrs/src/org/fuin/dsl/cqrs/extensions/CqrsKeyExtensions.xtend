package org.fuin.dsl.cqrs.extensions

import org.fuin.dsl.cqrs.cqrsDsl.CollisionStrategy
import org.fuin.dsl.cqrs.cqrsDsl.Key

/**
 * The vocabulary a business key derives, in one place.
 *
 * <p>A key states a natural key; what it derives is a uniqueness rule, the service method that answers
 * whether the key is taken, and the actuals binding the two at each operation that checks it. Those
 * names are generated rather than written, so they are settled here and nowhere else - a name computed
 * twice is a name that can disagree with itself.</p>
 */
class CqrsKeyExtensions {

	/**
	 * The name of the derived rule's first attribute: whether something already holds the key.
	 *
	 * @param key Key that derives it.
	 *
	 * @return Attribute name, first letter lower case.
	 */
	static def String answerName(Key key) {
		return key.name.toFirstLower + "Taken"
	}

	/**
	 * The name of the derived uniqueness rule.
	 *
	 * @param key Key that derives it.
	 *
	 * @return Rule name.
	 */
	static def String ruleName(Key key) {
		return key.name.toFirstUpper + "MustBeUnique"
	}

	/**
	 * The name of the service method that answers whether the key is taken.
	 *
	 * @param key Key that derives it.
	 *
	 * @return Method name.
	 */
	static def String serviceMethodName(Key key) {
		return "exists" + key.name.toFirstUpper
	}

	/**
	 * How many values the derived rule decides from: the answer, plus every attribute the key is made
	 * of, because a refusal names the key it refused.
	 *
	 * @param key Key that derives it.
	 *
	 * @return Number of actuals an explicit usage has to supply.
	 */
	static def int derivedAttributeCount(Key key) {
		return 1 + key.keyAttributes.size
	}

	/**
	 * Whether the key generates a rule at all. Only a key that refuses does: 'overwrite' and 'skip'
	 * are what the handler does with the second occurrence, not something an operation is refused for.
	 *
	 * @param key Key to check.
	 *
	 * @return <code>true</code> when a collision is a refusal.
	 */
	static def boolean refuses(Key key) {
		return key.onCollision == CollisionStrategy.REFUSE
	}

}

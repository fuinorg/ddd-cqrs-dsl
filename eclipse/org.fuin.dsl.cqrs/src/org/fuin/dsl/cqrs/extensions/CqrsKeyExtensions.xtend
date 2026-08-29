package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.CollisionStrategy
import org.fuin.dsl.cqrs.cqrsDsl.Key
import org.fuin.dsl.cqrs.cqrsDsl.Parameter

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
	static def Parameter boundParameter(Key key, Attribute keyAttribute, AbstractMethod operation) {
		if (!key.ambiguousAttributes(operation).empty) {
			return null
		}
		val matching = operation.parameters.filter[type === keyAttribute.type].toList
		return if (matching.size == 1) matching.head else null
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
	static def List<Attribute> ambiguousAttributes(Key key, AbstractMethod operation) {
		val out = new ArrayList<Attribute>()
		for (attribute : key.keyAttributes) {
			val parameters = operation.parameters.filter[type === attribute.type].size
			val siblings = key.keyAttributes.filter[type === attribute.type].size
			// Two parameters of the type, or two of the key's own attributes sharing it while the
			// operation takes one - either way the pairing is a guess, and a uniqueness check made
			// against the wrong half of a composite key still compiles and still passes.
			if (parameters > 1 || (siblings > 1 && parameters > 0)) {
				out.add(attribute)
			}
		}
		return out
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

package org.fuin.dsl.cqrs.extensions

import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement


import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static org.fuin.dsl.cqrs.extensions.CqrsExtensionUtils.*

/**
 * Provides extension methods for AbstractElement.
 */
class CqrsAbstractElementExtensions {

	/**
	 * Returns the unique name .
	 * 
	 * @param el Element to return a unique name for.
	 * 
	 * @return Unique name in the context/namespace.
	 */
	def static String uniqueName(AbstractElement el) {
		if (el === null) {
			throw new IllegalArgumentException("argument 'el' cannot be null")
		}
		if (el.context === null) {
			throw new IllegalArgumentException("argument 'el.context' cannot be null: " + el.path)
		}
		if (el.namespace === null) {
			throw new IllegalArgumentException("argument 'el.namespace' cannot be null: " + el.path)
		}
		return separated(".", el.context.name, el.namespace.name, el.name)
	}

	/**
	 * Returns the abstract unique name.
	 * 
	 * @param el Element to return an abstract unique name for.
	 * 
	 * @return Abstract unique name in the context/namespace.
	 */
	def static String uniqueAbstractName(AbstractElement el) {
		if (el === null) {
			throw new IllegalArgumentException("argument 'el' cannot be null")
		}
		if (el.context === null) {
			throw new IllegalArgumentException("argument 'el.context' cannot be null")
		}
		if (el.namespace === null) {
			throw new IllegalArgumentException("argument 'el.namespace' cannot be null")
		}
		return separated(".", el.context.name, el.namespace.name, el.abstractName)		
	}


	/**
	 * Returns the unique name .
	 * 
	 * @param el Element to return a unique name for.
	 * 
	 * @return Unique name in the context/namespace.
	 */
	def static String abstractName(AbstractElement el) {
		if (el === null) {
			throw new IllegalArgumentException("argument 'el' cannot be null")
		}
		return "Abstract" + el.name
	}

	/**
	 * Compares two abstract elements by their unique name.
	 * 
	 * @param a1 Element 1.
	 * @param a2 Element 2.
	 * 
	 * @return TRUE if both elements have the same unique name (context/namespace/name).
	 */
	def static boolean same(AbstractElement a1, AbstractElement a2) {
		if (a1 === null) {
			if (a2 === null) {
				return true
			}
			return false
		} else {
			if (a2 === null) {
				return false
			}
			return a1.uniqueName.equals(a2.uniqueName)
		}
	}


}

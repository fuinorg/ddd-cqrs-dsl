package org.fuin.dsl.cqrs.extensions

import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory
import org.fuin.dsl.cqrs.cqrsDsl.Type

class CqrsDslFactoryExtensions {

	/**
	 * Creates a parameter with a name.
	 * 
	 * @param factory Factory.
	 * @param name Name.
	 */
	def static createParameter(CqrsDslFactory factory, String name) {
		createParameter(factory, name, false)
	}

	/**
	 * Creates a parameter with a name and "nullable" information.
	 * 
	 * @param factory Factory.
	 * @param name Name.
	 * @param nullable TRUE if nullable, else false.
	 */
	def static createParameter(CqrsDslFactory factory, String name, boolean nullable) {
		var v = factory.createParameter
		v.setName(name)
		if (nullable) {
			v.setNullable("nullable")
		}
		return v
	}

	/**
	 * Creates a parameter with type, name and "nullable" information.
	 * 
	 * @param factory Factory.
	 * @param type Type.
	 * @param name Name.
	 * @param nullable TRUE if nullable, else false.
	 */
	def static createParameter(CqrsDslFactory factory, Type type, String name, boolean nullable) {
		var v = factory.createParameter
		v.setName(name)
		if (nullable) {
			v.setNullable("nullable")
		}
		v.setType(type)
		return v
	}

	/**
	 * Creates a parameter with type, name and "nullable" information.
	 * 
	 * @param factory Factory.
	 * @param doc Documentation.
	 * @param type Type.
	 * @param name Name.
	 * @param nullable TRUE if nullable, else false.
	 */
	def static createParameter(CqrsDslFactory factory, String doc, Type type, String name, boolean nullable) {
		var v = factory.createParameter
		v.setDoc(doc)
		v.setName(name)
		if (nullable) {
			v.setNullable("nullable")
		}
		v.setType(type)
		return v
	}
	/**
	 * Creates an attribute with a name.
	 * 
	 * @param factory Factory.
	 * @param name Name.
	 */
	def static createAttribute(CqrsDslFactory factory, String name) {
		createAttribute(factory, name, false)
	}

	/**
	 * Creates an attribute with a name and "nullable" information.
	 * 
	 * @param factory Factory.
	 * @param name Name.
	 * @param nullable TRUE if nullable, else false.
	 */
	def static createAttribute(CqrsDslFactory factory, String name, boolean nullable) {
		var v = factory.createAttribute
		v.setName(name)
		if (nullable) {
			v.setNullable("nullable")
		}
		return v
	}

	/**
	 * Creates an attribute with type, name and "nullable" information.
	 * 
	 * @param factory Factory.
	 * @param type Type.
	 * @param name Name.
	 * @param nullable TRUE if nullable, else false.
	 */
	def static createAttribute(CqrsDslFactory factory, Type type, String name, boolean nullable) {
		var v = factory.createAttribute
		v.setName(name)
		if (nullable) {
			v.setNullable("nullable")
		}
		v.setType(type)
		return v
	}

	/**
	 * Creates an attribute with type, name and "nullable" information.
	 * 
	 * @param factory Factory.
	 * @param doc Documentation.
	 * @param type Type.
	 * @param name Name.
	 * @param nullable TRUE if nullable, else false.
	 */
	def static createAttribute(CqrsDslFactory factory, String doc, Type type, String name, boolean nullable) {
		var v = factory.createAttribute
		v.setDoc(doc)
		v.setName(name)
		if (nullable) {
			v.setNullable("nullable")
		}
		v.setType(type)
		return v
	}

}

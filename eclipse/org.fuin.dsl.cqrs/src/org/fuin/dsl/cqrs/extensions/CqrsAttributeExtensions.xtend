package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory
import org.fuin.dsl.cqrs.cqrsDsl.Parameter

import static extension org.fuin.dsl.cqrs.extensions.CqrsInvariantsExtensions.*

/**
 * Provides extension methods for Attributes.
 */
class CqrsAttributeExtensions {

	/**
	 * Copies the attribute and assigns a new name to the copy.
	 * CAUTION: This is a shallow copy (no deep copy).
	 * 
	 * @param attr Attribute to copy
	 * @param name New name.
	 * 
	 * @return Attribute copy with new name.
	 */
	static def Attribute copyWithNewName(Attribute attr, String name) {
		var Attribute newAttr = CqrsDslFactory.eINSTANCE.createAttribute();
		newAttr.name = name;
		newAttr.doc = attr.doc;
		newAttr.nullable = attr.nullable;
		newAttr.type = attr.type;
		if (attr.generics !== null && attr.generics.args !== null) {
			val generics = CqrsDslFactory.eINSTANCE.createGenericArgs
			generics.args.addAll(attr.generics.args)
			newAttr.generics = generics
		}
		newAttr.invariants = attr.invariants;
		newAttr.overridden = attr.overridden;
		return newAttr;
	}
	
	/**
	 * Converts the attribute into a parameter.
	 * CAUTION: This is a shallow copy (no deep copy).
	 *
	 * @param attribute Attribute to convert.
	 * 
	 * @return Parameter.
	 */
	static def Parameter asParameter(Attribute attr) {
		if (attr === null) {
			return null
		}
		val param = CqrsDslFactory.eINSTANCE.createParameter
		param.preconditions = CqrsDslFactory.eINSTANCE.createPreconditions
		param.preconditions.constraintInstances.addAll(attr.invariants.nullSafe)
		param.doc = attr.doc
		param.nullable = attr.nullable
		param.type = attr.type
		if (attr.generics !== null && attr.generics.args !== null) {
			val generics = CqrsDslFactory.eINSTANCE.createGenericArgs
			generics.args.addAll(attr.generics.args)
			param.generics = generics
		}
		param.name = attr.name
		param.overridden = attr.overridden
		return param
	}
	
	/**
	 * Converts an attribute list into a list of parameters.
	 * 
	 * @param attributes List of attributes.
	 * 
	 * @return Parameter list.
	 */
	def static List<Parameter> asParameters(List<Attribute> attributes) {
		if (attributes === null) {
			return null
		}
		val list = new ArrayList<Parameter>()
		for (attr : attributes) {
			list.add(attr.asParameter)
		}
		return list
	}
	
	/**
	 * Returns a list of names from all attributes.
	 * 
	 * @param attributes Attribute list.
	 * 
	 * @return List with names in the same order as the attributes.
	 */
	def static List<String> asNames(List<Attribute> attributes) {
		if (attributes === null) {
			return null
		}
		val List<String> result = new ArrayList<String>()
		if (attributes !== null) {
			for (attribute : attributes) {
				result.add(attribute.name)
			}
		}
		return result
	}

	
	
}

package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Constraint
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.InternalType
import org.fuin.dsl.cqrs.cqrsDsl.Type

import static org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory.eINSTANCE

import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDslFactoryExtensions.*

class CqrsConstraintExtension {

	def static List<Attribute> allAllowedVariables(Constraint constr) {
		var List<Attribute> list = new ArrayList<Attribute>();
		if (constr.attributes !== null) {
			list.addAll(constr.attributes);
		}
		if (constr.input !== null && constr.input.size > 0) {
			val Type first = constr.input.get(0)
			if (constr.input.size === 1) {
				// For a single target type it's possible to also use 
				// attributes of sub type with "input." prefix as variable 
				list.addAll(attributesOf(first))
			} else {
				// If there are multiple target types, it's only possible
				// to use the type itself as variable "input"
				if (first instanceof ExternalType) {
					list.add(createInputAttribute(first))			
				}
			}
		}
		return list;
	}

	def static List<Attribute> attributesOf(Type target)  {
		var List<Attribute> list = new ArrayList<Attribute>();
		if (target instanceof ExternalType) {
			list.add(createInputAttribute(target))
		} else if (target instanceof InternalType) {
			list.add(createInputAttribute(target))
			if (target.attributes !== null) {
				for (attr : target.attributes) {
					var Attribute newAttr = attr.copyWithNewName("input." + attr.name);					
					list.add(newAttr);
				}
			}
		}		
		return list;
	}
	
	def static createInputAttribute(Type type) {
		return eINSTANCE.createAttribute("/** The validated value. */", type, "input", true)
	}


}

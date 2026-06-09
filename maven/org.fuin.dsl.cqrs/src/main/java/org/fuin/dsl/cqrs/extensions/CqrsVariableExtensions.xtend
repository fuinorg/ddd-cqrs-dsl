package org.fuin.dsl.cqrs.extensions

import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory
import org.fuin.dsl.cqrs.cqrsDsl.Literal
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo
import org.fuin.dsl.cqrs.cqrsDsl.Variable

import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsTypeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import java.util.List
import java.util.ArrayList

/**
 * Provides extension methods for Variable.
 */
class CqrsVariableExtensions {

	/**
	 * Returns the doc text from the variable or the type.
	 * 
	 * @param variable Variable with doc text to read.
	 * 
	 * @return Variable or type doc.
	 */
	def static String superDoc(Variable variable) {
		if (variable.doc === null) {
			variable.type.doc.text
		} else {
			return variable.doc.text
		}
	}

	/**
	 * Returns the corresponding Java primitive type.
	 * 
	 * @param variable Variable 
	 * 
	 * @return Primitive type or original type name. 
	 */
	def static String asJavaPrimitive(Variable variable) {
		return variable.type.asJavaPrimitive;
	}

	/**
	 * Returns the overridden meta info for the variable.
	 * This is a null safe shortcut for <code>variable.overridden.metaInfo</code>. 
	 * 
	 * @param variable Variable.
	 * 
	 * @return Meta info or null.
	 */
	def static TypeMetaInfo overriddenMeta(Variable variable) {
		if (variable.overridden === null) {
			return null
		}
		return variable.overridden.metaInfo
	}
	
	/**
	 * Returns the meta info for the variable if available.
	 * 
	 * @param variable Variable.
	 * 
	 * @return Meta info or null.
	 */
	def static TypeMetaInfo meta(Variable variable) {

		val TypeMetaInfo metaInfo = CqrsDslFactory.eINSTANCE.createTypeMetaInfo

		val TypeMetaInfo typeMeta = variable.type.meta
		if (typeMeta !== null) {
			metaInfo.slabel = typeMeta.slabel
			metaInfo.label = typeMeta.label
			metaInfo.tooltip = typeMeta.tooltip
			metaInfo.prompt = typeMeta.prompt
			metaInfo.examples.addAll(typeMeta.examples)
		}

		if ((variable.overridden !== null) && (variable.overridden.metaInfo !== null)) {
			val TypeMetaInfo varMeta = variable.overridden.metaInfo
			if (varMeta.slabel !== null) {
				metaInfo.slabel = varMeta.slabel
			}
			if (varMeta.label !== null) {
				metaInfo.label = varMeta.label
			}
			if (varMeta.tooltip !== null) {
				metaInfo.tooltip = varMeta.tooltip
			}
			if (varMeta.prompt !== null) {
				metaInfo.prompt = varMeta.prompt
			}
			if (varMeta.examples.size > 0) {
				metaInfo.examples.clear
				metaInfo.examples.addAll(varMeta.examples)
			}

		}

		return metaInfo

	}

	/**
	 * Returns the first example from the meta info for the variable if available.
	 * 
	 * @param variable Variable.
	 * 
	 * @return Example literal or null.
	 */
	def static Literal firstExample(Variable variable) {
		val TypeMetaInfo metaInfo = variable.meta
		return metaInfo.examples.first
	}

	/**
	 * Converts a variable list into a list of parameters.
	 *
	 * @param variables List of variables.
	 *
	 * @return Parameter list.
	 */
	def static List<Parameter> asParameters(List<? extends Variable> variables) {
		if (variables === null) {
			return null
		}
		val list = new ArrayList<Parameter>()
		for (attr : variables) {
			list.add(attr.asParameter)
		}
		return list
	}

	/**
	 * Converts the variable into a parameter.
	 *
	 * @param variable Variable to convert.
	 *
	 * @return Parameter.
	 */
	static def Parameter asParameter(Variable variable) {
		if (variable === null) {
			return null
		}
		if (variable instanceof Parameter) {
			return variable
		}
		if (variable instanceof Attribute) {
			return variable.asParameter
		}
		throw new IllegalStateException("Unknown variable type: " + variable.class.name)
	}

}

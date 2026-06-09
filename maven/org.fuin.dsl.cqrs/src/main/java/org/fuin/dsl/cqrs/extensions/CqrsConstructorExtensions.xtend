package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.Exception

import static extension org.fuin.dsl.cqrs.extensions.CqrsBusinessRulesExtensions.*

/**
 * Provides extension methods for Constructor.
 */
class CqrsConstructorExtensions {

	def static List<Exception> allExceptions(Constructor constructor) {
		var List<Exception> list = new ArrayList<Exception>()
		for (cc : constructor.businessRules.nullSafe) {
			list.add(cc.businessRule.exception)
		}
		return list
	}

}

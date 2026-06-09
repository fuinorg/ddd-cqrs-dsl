package org.fuin.dsl.cqrs.extensions

import org.fuin.dsl.cqrs.cqrsDsl.AbstractVO
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject

/**
 * Provides extension methods for AbstractVO.
 */
class CqrsAbstractVOExtensions {

	/**
	 * Returns the base type for a value object.
	 * 
	 * @param vo Value object.
	 * 
	 * @return Base type or null.
	 */	
	def static ExternalType baseType(AbstractVO vo) {
		if (vo instanceof AggregateId) {
			return vo.base
		}
		if (vo instanceof EntityId) {
			return vo.base
		}
		if (vo instanceof ValueObject) {
			return vo.base
		}
		return null
	}
	
	
	/**
	 * Returns the base type name for a value object.
	 * 
	 * @param vo Value object.
	 * 
	 * @return Name or null.
	 */	
	def static String baseTypeName(AbstractVO vo) {
		if (vo.baseType === null) {
			return null
		} else {
			return vo.baseType.name
		}
	}

}

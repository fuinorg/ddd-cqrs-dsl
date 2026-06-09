package org.fuin.dsl.cqrs.extensions

import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate

/**
 * Provides extension methods for AggregateId.
 */
class CqrsAggregateIdExtensions {

	/**
	 * Returns the aggregate type regardless if it's defined as parent 
	 * of the aggregate identifier or somewhere else.
	 * 
	 * @param aggregateId Aggregate ID to return the aggregate type for.
	 * 
	 * @return Aggregate type.
	 */
	def static Aggregate getAggregateNullsafe(AggregateId aggregateId) {
		if (aggregateId.aggregate === null) {
			return aggregateId.eContainer as Aggregate
		}
		return aggregateId.aggregate
	}

}

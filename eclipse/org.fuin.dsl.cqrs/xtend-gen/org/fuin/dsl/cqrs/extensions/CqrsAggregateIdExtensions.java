package org.fuin.dsl.cqrs.extensions;

import org.eclipse.emf.ecore.EObject;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId;

/**
 * Provides extension methods for AggregateId.
 */
@SuppressWarnings("all")
public class CqrsAggregateIdExtensions {
  /**
   * Returns the aggregate type regardless if it's defined as parent
   * of the aggregate identifier or somewhere else.
   * 
   * @param aggregateId Aggregate ID to return the aggregate type for.
   * 
   * @return Aggregate type.
   */
  public static Aggregate getAggregateNullsafe(final AggregateId aggregateId) {
    Aggregate _aggregate = aggregateId.getAggregate();
    boolean _tripleEquals = (_aggregate == null);
    if (_tripleEquals) {
      EObject _eContainer = aggregateId.eContainer();
      return ((Aggregate) _eContainer);
    }
    return aggregateId.getAggregate();
  }
}

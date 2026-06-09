package org.fuin.dsl.cqrs.extensions;

import com.google.common.collect.Iterables;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId;

/**
 * Provides extension methods for Aggregate.
 */
@SuppressWarnings("all")
public class CqrsAggregateExtensions {
  /**
   * Returns the aggregate identifier type regardless if it's
   * defined inside the aggregate or somewhere outside.
   * 
   * @param aggregate Aggregate to return the identifier type for.
   * 
   * @return Aggregate identifier type.
   */
  public static AggregateId getIdTypeNullsafe(final Aggregate aggregate) {
    AggregateId _idType = aggregate.getIdType();
    boolean _tripleEquals = (_idType == null);
    if (_tripleEquals) {
      return CqrsAggregateExtensions.getAggregateId(aggregate);
    }
    return aggregate.getIdType();
  }

  /**
   * Returns the aggregate identifier that may be defined inside the aggregate.
   * 
   * @param aggregate Aggregate to return the identifier for.
   * 
   * @return Identifier or NULL if no such type is defined inside the aggregate.
   */
  public static AggregateId getAggregateId(final Aggregate aggregate) {
    final Iterable<AggregateId> types = Iterables.<AggregateId>filter(CqrsCollectionExtensions.<AbstractElement>nullSafe(aggregate.getElements()), AggregateId.class);
    int _length = ((Object[])Conversions.unwrapArray(types, Object.class)).length;
    boolean _equals = (_length == 0);
    if (_equals) {
      return null;
    }
    return ((AggregateId[])Conversions.unwrapArray(types, AggregateId.class))[0];
  }
}

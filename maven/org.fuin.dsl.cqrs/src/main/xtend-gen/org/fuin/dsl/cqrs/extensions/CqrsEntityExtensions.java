package org.fuin.dsl.cqrs.extensions;

import com.google.common.collect.Iterables;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.Entity;
import org.fuin.dsl.cqrs.cqrsDsl.EntityId;

/**
 * Provides extension methods for Entity.
 */
@SuppressWarnings("all")
public class CqrsEntityExtensions {
  /**
   * Returns the entity identifier type regardless if it's
   * defined inside the entity or somewhere outside.
   * 
   * @param entity Entity to return the identifier type for.
   * 
   * @return Entity identifier type.
   */
  public static EntityId getIdTypeNullsafe(final Entity entity) {
    EntityId _idType = entity.getIdType();
    boolean _tripleEquals = (_idType == null);
    if (_tripleEquals) {
      return CqrsEntityExtensions.getEntityId(entity);
    }
    return entity.getIdType();
  }

  /**
   * Returns the entity identifier that may be defined inside the entity.
   * 
   * @param entity Entity to return the identifier for.
   * 
   * @return Identifier or NULL if no such type is defined inside the entity.
   */
  public static EntityId getEntityId(final Entity entity) {
    final Iterable<EntityId> types = Iterables.<EntityId>filter(CqrsCollectionExtensions.<AbstractElement>nullSafe(entity.getElements()), EntityId.class);
    int _length = ((Object[])Conversions.unwrapArray(types, Object.class)).length;
    boolean _equals = (_length == 0);
    if (_equals) {
      return null;
    }
    return ((EntityId[])Conversions.unwrapArray(types, EntityId.class))[0];
  }

  /**
   * Returns the aggregate the entity belongs to. It does not matter
   * if the aggregate is referenced by using the 'root' expression
   * or the entity is declared inside an aggregate.
   * 
   * @param entity The entity to return the aggregate for.
   * 
   * @return Aggregate of the entity.
   */
  public static Aggregate getRootNullsafe(final Entity entity) {
    Aggregate _root = entity.getRoot();
    boolean _tripleEquals = (_root == null);
    if (_tripleEquals) {
      entity.eContainer();
    }
    return entity.getRoot();
  }
}

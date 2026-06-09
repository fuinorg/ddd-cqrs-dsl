package org.fuin.dsl.cqrs.extensions;

import org.eclipse.emf.ecore.EObject;
import org.fuin.dsl.cqrs.cqrsDsl.Entity;
import org.fuin.dsl.cqrs.cqrsDsl.EntityId;

/**
 * Provides extension methods for EntityId.
 */
@SuppressWarnings("all")
public class CqrsEntityIdExtensions {
  /**
   * Returns the entity type regardless if it's defined as parent
   * of the entity indentifier or somewhere else.
   * 
   * @param entityId Entity ID to return the entity type for.
   * 
   * @return Entity type.
   */
  public static Entity getEntityNullsafe(final EntityId entityId) {
    Entity _entity = entityId.getEntity();
    boolean _tripleEquals = (_entity == null);
    if (_tripleEquals) {
      EObject _eContainer = entityId.eContainer();
      return ((Entity) _eContainer);
    }
    return entityId.getEntity();
  }
}

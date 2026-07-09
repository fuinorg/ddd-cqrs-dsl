package org.fuin.dsl.cqrs.extensions;

import org.eclipse.emf.ecore.EObject;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.Event;
import org.fuin.dsl.cqrs.cqrsDsl.Namespace;

/**
 * Provides extension methods for Event.
 */
@SuppressWarnings("all")
public class CqrsEventExtensions {
  /**
   * Returns the unique name of the event.
   * 
   * @param el Event to return a unique name for.
   * 
   * @return Unique name in the context/namespace.
   */
  public static String uniqueName(final Event event) {
    if ((event == null)) {
      throw new IllegalArgumentException("argument \'event\' cannot be null");
    }
    Context _context = CqrsEObjectExtensions.getContext(event);
    boolean _tripleEquals = (_context == null);
    if (_tripleEquals) {
      throw new IllegalArgumentException("argument \'event.context\' cannot be null");
    }
    Namespace _namespace = CqrsEObjectExtensions.getNamespace(event);
    boolean _tripleEquals_1 = (_namespace == null);
    if (_tripleEquals_1) {
      throw new IllegalArgumentException("argument \'event.namespace\' cannot be null");
    }
    return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getProject(event).getName(), CqrsEObjectExtensions.getContext(event).getName(), CqrsEObjectExtensions.getNamespace(event).getName(), event.getName());
  }

  /**
   * Returns the aggregate or entity for an event if it is a domain event.
   * 
   * @param event Event to return the parent entity for.
   * 
   * @return Aggregate or Entity or null if the event is not inside one.
   */
  public static AbstractEntity getEntity(final Event event) {
    return CqrsEventExtensions.getAbstractEntity(event);
  }

  private static AbstractEntity getAbstractEntity(final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    if ((obj instanceof AbstractEntity)) {
      return ((AbstractEntity)obj);
    }
    return CqrsEventExtensions.getAbstractEntity(obj.eContainer());
  }
}

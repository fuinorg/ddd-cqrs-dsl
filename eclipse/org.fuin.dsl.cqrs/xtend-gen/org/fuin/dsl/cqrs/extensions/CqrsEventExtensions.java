package org.fuin.dsl.cqrs.extensions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod;
import org.fuin.dsl.cqrs.cqrsDsl.Constructor;
import org.fuin.dsl.cqrs.cqrsDsl.Event;
import org.fuin.dsl.cqrs.cqrsDsl.Method;

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
   * @return Unique name in the context/module.
   */
  public static String uniqueName(final Event event) {
    if ((event == null)) {
      throw new IllegalArgumentException("argument \'event\' cannot be null");
    }
    org.fuin.dsl.cqrs.cqrsDsl.Module _module = CqrsEObjectExtensions.getModule(event);
    boolean _tripleEquals = (_module == null);
    if (_tripleEquals) {
      throw new IllegalArgumentException("argument \'event.module\' cannot be null");
    }
    return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getContext(event).getName(), CqrsEObjectExtensions.getModule(event).getName(), event.getName());
  }

  /**
   * Returns the aggregate or entity an event belongs to, which is what makes it a domain event
   * rather than a plain one. Resolved in three steps, the first hit winning:
   * <ol>
   * <li>the containers, for an event declared inside an aggregate or entity;</li>
   * <li>the "copies-attributes-of" origin, whose container is the owner (a cross reference, which
   * the container walk cannot follow);</li>
   * <li>the constructor or method declaring "fires &lt;event&gt;", for an event declared beside
   * the aggregate rather than inside it.</li>
   * </ol>
   * 
   * @param event Event to return the parent entity for.
   * 
   * @return Aggregate or Entity or null if the event belongs to none.
   */
  public static AbstractEntity getEntity(final Event event) {
    if ((event == null)) {
      return null;
    }
    final AbstractEntity fromContainer = CqrsEventExtensions.getAbstractEntity(event);
    if ((fromContainer != null)) {
      return fromContainer;
    }
    AbstractMethod _origin = event.getOrigin();
    boolean _tripleNotEquals = (_origin != null);
    if (_tripleNotEquals) {
      final AbstractEntity fromOrigin = CqrsEventExtensions.getAbstractEntity(event.getOrigin());
      if ((fromOrigin != null)) {
        return fromOrigin;
      }
    }
    return CqrsEventExtensions.getFiringEntity(event);
  }

  /**
   * Returns the single aggregate or entity that declares "fires &lt;event&gt;" for the given event.
   * When more than one does, null is returned: binding the event to either of them would be a
   * guess, so it stays a plain event instead of silently belonging to the wrong aggregate.
   * 
   * @param event Event to find the firing aggregate or entity for.
   * 
   * @return Aggregate or Entity, or null if none or more than one fires the event.
   */
  private static AbstractEntity getFiringEntity(final Event event) {
    AbstractEntity found = null;
    final TreeIterator<EObject> iter = CqrsEObjectExtensions.getRoot(event).eAllContents();
    while (iter.hasNext()) {
      {
        final EObject obj = iter.next();
        if ((obj instanceof AbstractEntity)) {
          boolean _fires = CqrsEventExtensions.fires(((AbstractEntity)obj), event);
          if (_fires) {
            if (((found != null) && (found != obj))) {
              return null;
            }
            found = ((AbstractEntity)obj);
          }
        }
      }
    }
    return found;
  }

  /**
   * Returns TRUE if any of the entity's constructors or methods declares "fires &lt;event&gt;".
   */
  private static boolean fires(final AbstractEntity entity, final Event event) {
    EList<Constructor> _constructors = entity.getConstructors();
    for (final Constructor constructor : _constructors) {
      boolean _contains = constructor.getFiredEvents().contains(event);
      if (_contains) {
        return true;
      }
    }
    EList<Method> _methods = entity.getMethods();
    for (final Method method : _methods) {
      boolean _contains_1 = method.getFiredEvents().contains(event);
      if (_contains_1) {
        return true;
      }
    }
    return false;
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

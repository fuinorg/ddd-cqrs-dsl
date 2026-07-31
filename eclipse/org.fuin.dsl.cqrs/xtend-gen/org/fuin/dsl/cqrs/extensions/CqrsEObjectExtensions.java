package org.fuin.dsl.cqrs.extensions;

import java.lang.reflect.Method;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.Command;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.Entity;

/**
 * Provides extension methods for EObject.
 */
@SuppressWarnings("all")
public class CqrsEObjectExtensions {
  /**
   * Returns the module for an object.
   * 
   * @param obj Object to return the module for.
   * 
   * @return Module or null if the object is not inside one.
   */
  public static org.fuin.dsl.cqrs.cqrsDsl.Module getModule(final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    if ((obj instanceof org.fuin.dsl.cqrs.cqrsDsl.Module)) {
      return ((org.fuin.dsl.cqrs.cqrsDsl.Module)obj);
    }
    return CqrsEObjectExtensions.getModule(obj.eContainer());
  }

  /**
   * Returns the context an object belongs to.
   * 
   * @param obj Object to return the context for.
   * 
   * @return Context or null if the object is not inside one.
   */
  public static Context getContext(final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    if ((obj instanceof Context)) {
      return ((Context)obj);
    }
    return CqrsEObjectExtensions.getContext(obj.eContainer());
  }

  /**
   * Returns the path in the model to the object.
   * 
   * @param obj Object to return the path for.
   * 
   * @return Path or empty string if the object is not inside one.
   */
  public static String getPath(final EObject obj) {
    if ((obj == null)) {
      return "";
    }
    String _path = CqrsEObjectExtensions.getPath(obj.eContainer());
    String _plus = (_path + "/");
    String _name = CqrsEObjectExtensions.getName(obj);
    return (_plus + _name);
  }

  /**
   * Returns the name of an object if it has one.
   * 
   * @param obj Object to return the name for.
   * 
   * @return Name or text returned by the object's toString() method.
   */
  public static String getName(final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    if ((obj instanceof AbstractElement)) {
      return ((AbstractElement)obj).getName();
    }
    return CqrsEObjectExtensions.reflectName(obj);
  }

  /**
   * Returns the parent aggregate for an object.
   * 
   * @param obj Object to return the parent aggregate for.
   * 
   * @return Context or null if the object is not inside one.
   */
  public static Aggregate getAggregate(final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    if ((obj instanceof Entity)) {
      return ((Entity)obj).getRoot();
    }
    if ((obj instanceof Aggregate)) {
      return ((Aggregate)obj);
    }
    return CqrsEObjectExtensions.getAggregate(obj.eContainer());
  }

  /**
   * Returns the aggregate a command belongs to. A command nested inside an aggregate is found by
   * walking its containers, exactly like any other object. A command declared beside the aggregate
   * (directly in the context) has no such container, so the aggregate is taken from the method the
   * command targets - the target is a cross reference, which the container walk cannot follow.
   * 
   * @param command Command to return the aggregate for.
   * 
   * @return Aggregate or null if the command neither sits inside one nor targets one.
   */
  public static Aggregate getAggregate(final Command command) {
    if ((command == null)) {
      return null;
    }
    final Aggregate aggregate = CqrsEObjectExtensions.getAggregate(command.eContainer());
    if ((aggregate != null)) {
      return aggregate;
    }
    AbstractMethod _target = command.getTarget();
    boolean _tripleEquals = (_target == null);
    if (_tripleEquals) {
      return null;
    }
    return CqrsEObjectExtensions.getAggregate(command.getTarget());
  }

  /**
   * Returns the parent entity for an object.
   * 
   * @param obj Object to return the parent entity for.
   * 
   * @return Context or null if the object is not inside one.
   */
  public static AbstractEntity getEntity(final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    if ((obj instanceof Entity)) {
      return ((AbstractEntity)obj);
    }
    if ((obj instanceof Aggregate)) {
      return ((AbstractEntity)obj);
    }
    return CqrsEObjectExtensions.getEntity(obj.eContainer());
  }

  /**
   * Returns the entity a command belongs to, following the same two step resolution as
   * {@link #getAggregate(Command)}: the containers first, then the targeted method. This is the
   * aggregate root for a command addressing the root itself, and the child entity for a command
   * addressing one.
   * 
   * @param command Command to return the entity for.
   * 
   * @return Entity or null if the command neither sits inside one nor targets one.
   */
  public static AbstractEntity getEntity(final Command command) {
    if ((command == null)) {
      return null;
    }
    final AbstractEntity entity = CqrsEObjectExtensions.getEntity(command.eContainer());
    if ((entity != null)) {
      return entity;
    }
    AbstractMethod _target = command.getTarget();
    boolean _tripleEquals = (_target == null);
    if (_tripleEquals) {
      return null;
    }
    return CqrsEObjectExtensions.getEntity(command.getTarget());
  }

  /**
   * Returns the root container for a given object.
   * 
   * @param obj Object to return the top container for.
   * 
   * @return Root container.
   */
  public static EObject getRoot(final EObject obj) {
    EObject _eContainer = obj.eContainer();
    boolean _tripleEquals = (_eContainer == null);
    if (_tripleEquals) {
      return obj;
    }
    return CqrsEObjectExtensions.getRoot(obj.eContainer());
  }

  /**
   * Returns the first parent with a given type for an object.
   * 
   * @param obj Object to return the parent for.
   * 
   * @return Parent or null if the object is not inside the requested type.
   */
  public static <T extends Object> T getParent(final Class<T> clasz, final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    boolean _isAssignableFrom = clasz.isAssignableFrom(obj.getClass());
    if (_isAssignableFrom) {
      return ((T) obj);
    }
    return CqrsEObjectExtensions.<T>getParent(clasz, obj.eContainer());
  }

  public static String reflectName(final Object obj) {
    try {
      if ((obj == null)) {
        return null;
      }
      try {
        final Method method = obj.getClass().getMethod("getName", null);
        Object _invoke = method.invoke(obj);
        return ((String) _invoke);
      } catch (final Throwable _t) {
        if (_t instanceof NoSuchMethodException) {
          return obj.toString();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}

package org.fuin.dsl.cqrs.extensions;

import java.lang.reflect.Method;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.Entity;
import org.fuin.dsl.cqrs.cqrsDsl.Namespace;

/**
 * Provides extension methods for EObject.
 */
@SuppressWarnings("all")
public class CqrsEObjectExtensions {
  /**
   * Returns the namespace for an object.
   * 
   * @param obj Object to return the namespace for.
   * 
   * @return Namespace or null if the object is not inside one.
   */
  public static Namespace getNamespace(final EObject obj) {
    if ((obj == null)) {
      return null;
    }
    if ((obj instanceof Namespace)) {
      return ((Namespace)obj);
    }
    return CqrsEObjectExtensions.getNamespace(obj.eContainer());
  }

  /**
   * Returns the context for an object.
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

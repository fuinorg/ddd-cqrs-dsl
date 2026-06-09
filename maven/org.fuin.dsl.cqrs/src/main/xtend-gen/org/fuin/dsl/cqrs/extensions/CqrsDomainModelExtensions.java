package org.fuin.dsl.cqrs.extensions;

import com.google.common.collect.Iterators;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;

@SuppressWarnings("all")
public class CqrsDomainModelExtensions {
  /**
   * Returns an abstract element of a given type by it's name. The object
   * must have a public instance method <code>String getName()</code>.
   * 
   * Throws an exception if the element is not found.
   * 
   * @param model Domain model.
   * @param type Type to find.
   * @param name Name that is unique within the type.
   * 
   * @return Element.
   */
  public static <T extends EObject> T find(final DomainModel model, final Class<T> type, final String name) {
    final Iterator<T> iter = Iterators.<T>filter(model.eAllContents(), type);
    while (iter.hasNext()) {
      {
        final T el = iter.next();
        boolean _equals = name.equals(CqrsDomainModelExtensions.getName(el));
        if (_equals) {
          return el;
        }
      }
    }
    String _simpleName = type.getSimpleName();
    String _plus = ("No element of type \'" + _simpleName);
    String _plus_1 = (_plus + "\' found with name \'");
    String _plus_2 = (_plus_1 + name);
    String _plus_3 = (_plus_2 + "\'");
    throw new IllegalArgumentException(_plus_3);
  }

  private static String getName(final Object obj) {
    if ((obj == null)) {
      return null;
    }
    try {
      final Method method = obj.getClass().getMethod("getName", null);
      final Object result = method.invoke(obj, null);
      if ((result instanceof String)) {
        return ((String)result);
      }
      return null;
    } catch (final Throwable _t) {
      if (_t instanceof SecurityException) {
        final SecurityException ex = (SecurityException)_t;
        throw new RuntimeException(ex);
      } else if (_t instanceof NoSuchMethodException) {
        return null;
      } else if (_t instanceof IllegalArgumentException) {
        final IllegalArgumentException ex_2 = (IllegalArgumentException)_t;
        throw new RuntimeException(ex_2);
      } else if (_t instanceof IllegalAccessException) {
        final IllegalAccessException ex_3 = (IllegalAccessException)_t;
        throw new RuntimeException(ex_3);
      } else if (_t instanceof InvocationTargetException) {
        final InvocationTargetException ex_4 = (InvocationTargetException)_t;
        throw new RuntimeException(ex_4);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
}

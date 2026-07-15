package org.fuin.dsl.cqrs.extensions;

import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.Namespace;

/**
 * Provides extension methods for AbstractElement.
 */
@SuppressWarnings("all")
public class CqrsAbstractElementExtensions {
  /**
   * Returns the unique name .
   * 
   * @param el Element to return a unique name for.
   * 
   * @return Unique name in the context/namespace.
   */
  public static String uniqueName(final AbstractElement el) {
    if ((el == null)) {
      throw new IllegalArgumentException("argument \'el\' cannot be null");
    }
    Context _context = CqrsEObjectExtensions.getContext(el);
    boolean _tripleEquals = (_context == null);
    if (_tripleEquals) {
      String _path = CqrsEObjectExtensions.getPath(el);
      String _plus = ("argument \'el.context\' cannot be null: " + _path);
      throw new IllegalArgumentException(_plus);
    }
    Namespace _namespace = CqrsEObjectExtensions.getNamespace(el);
    boolean _tripleEquals_1 = (_namespace == null);
    if (_tripleEquals_1) {
      return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getProject(el).getName(), CqrsEObjectExtensions.getContext(el).getName(), el.getName());
    }
    return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getProject(el).getName(), CqrsEObjectExtensions.getContext(el).getName(), CqrsEObjectExtensions.getNamespace(el).getName(), el.getName());
  }

  /**
   * Returns the abstract unique name.
   * 
   * @param el Element to return an abstract unique name for.
   * 
   * @return Abstract unique name in the context/namespace.
   */
  public static String uniqueAbstractName(final AbstractElement el) {
    if ((el == null)) {
      throw new IllegalArgumentException("argument \'el\' cannot be null");
    }
    Context _context = CqrsEObjectExtensions.getContext(el);
    boolean _tripleEquals = (_context == null);
    if (_tripleEquals) {
      throw new IllegalArgumentException("argument \'el.context\' cannot be null");
    }
    Namespace _namespace = CqrsEObjectExtensions.getNamespace(el);
    boolean _tripleEquals_1 = (_namespace == null);
    if (_tripleEquals_1) {
      return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getProject(el).getName(), CqrsEObjectExtensions.getContext(el).getName(), CqrsAbstractElementExtensions.abstractName(el));
    }
    return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getProject(el).getName(), CqrsEObjectExtensions.getContext(el).getName(), CqrsEObjectExtensions.getNamespace(el).getName(), CqrsAbstractElementExtensions.abstractName(el));
  }

  /**
   * Returns the unique name .
   * 
   * @param el Element to return a unique name for.
   * 
   * @return Unique name in the context/namespace.
   */
  public static String abstractName(final AbstractElement el) {
    if ((el == null)) {
      throw new IllegalArgumentException("argument \'el\' cannot be null");
    }
    String _name = el.getName();
    return ("Abstract" + _name);
  }

  /**
   * Compares two abstract elements by their unique name.
   * 
   * @param a1 Element 1.
   * @param a2 Element 2.
   * 
   * @return TRUE if both elements have the same unique name (context/namespace/name).
   */
  public static boolean same(final AbstractElement a1, final AbstractElement a2) {
    if ((a1 == null)) {
      if ((a2 == null)) {
        return true;
      }
      return false;
    } else {
      if ((a2 == null)) {
        return false;
      }
      return CqrsAbstractElementExtensions.uniqueName(a1).equals(CqrsAbstractElementExtensions.uniqueName(a2));
    }
  }
}

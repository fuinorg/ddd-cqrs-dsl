package org.fuin.dsl.cqrs.extensions;

import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;

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
   * @return Unique name in the context/module.
   */
  public static String uniqueName(final AbstractElement el) {
    if ((el == null)) {
      throw new IllegalArgumentException("argument \'el\' cannot be null");
    }
    org.fuin.dsl.cqrs.cqrsDsl.Module _module = CqrsEObjectExtensions.getModule(el);
    boolean _tripleEquals = (_module == null);
    if (_tripleEquals) {
      String _path = CqrsEObjectExtensions.getPath(el);
      String _plus = ("argument \'el.module\' cannot be null: " + _path);
      throw new IllegalArgumentException(_plus);
    }
    return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getContext(el).getName(), CqrsEObjectExtensions.getModule(el).getName(), el.getName());
  }

  /**
   * Returns the abstract unique name.
   * 
   * @param el Element to return an abstract unique name for.
   * 
   * @return Abstract unique name in the context/module.
   */
  public static String uniqueAbstractName(final AbstractElement el) {
    if ((el == null)) {
      throw new IllegalArgumentException("argument \'el\' cannot be null");
    }
    org.fuin.dsl.cqrs.cqrsDsl.Module _module = CqrsEObjectExtensions.getModule(el);
    boolean _tripleEquals = (_module == null);
    if (_tripleEquals) {
      throw new IllegalArgumentException("argument \'el.module\' cannot be null");
    }
    return CqrsExtensionUtils.separated(".", CqrsEObjectExtensions.getContext(el).getName(), CqrsEObjectExtensions.getModule(el).getName(), CqrsAbstractElementExtensions.abstractName(el));
  }

  /**
   * Returns the unique name .
   * 
   * @param el Element to return a unique name for.
   * 
   * @return Unique name in the context/module.
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
   * @return TRUE if both elements have the same unique name (context/module/name).
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

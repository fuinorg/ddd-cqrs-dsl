package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Provides extension methods for collections.
 */
@SuppressWarnings("all")
public class CqrsCollectionExtensions {
  /**
   * Returns a null safe version of the list.
   * 
   * @return Original or empty list.
   */
  public static <T extends Object> List<T> nullSafe(final List<T> list) {
    if ((list == null)) {
      return Collections.<T>emptyList();
    }
    return list;
  }

  /**
   * Returns the first element of the list.
   * 
   * @param list List to return the first element.
   * 
   * @return First element or null if the list is null or empty.
   */
  public static <T extends Object> T first(final List<T> list) {
    if (((list == null) || (list.size() == 0))) {
      return null;
    }
    return list.get(0);
  }

  /**
   * Returns the list without the first element.
   * 
   * @param list List to return the rest for.
   * 
   * @return Elements without the first one.
   */
  public static <T extends Object> List<T> rest(final List<T> list) {
    if ((list == null)) {
      return null;
    }
    List<T> rest = new ArrayList<T>();
    if (((list != null) && (list.size() > 0))) {
      int count = 0;
      for (final T v : list) {
        {
          if ((count > 0)) {
            rest.add(v);
          }
          count = (count + 1);
        }
      }
    }
    return rest;
  }

  /**
   * Returns a null safe version of the map.
   * 
   * @return Original or empty map.
   */
  public static <K extends Object, V extends Object> Map<K, V> nullSafe(final Map<K, V> map) {
    if ((map == null)) {
      return Collections.<K, V>emptyMap();
    }
    return map;
  }
}

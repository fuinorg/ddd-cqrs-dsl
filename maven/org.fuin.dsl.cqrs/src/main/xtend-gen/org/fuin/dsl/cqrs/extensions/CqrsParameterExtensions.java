package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;

/**
 * Provides extension methods for Parameters.
 */
@SuppressWarnings("all")
public class CqrsParameterExtensions {
  /**
   * Returns a list of names from all parameters.
   * 
   * @param parameters Parameter list.
   * 
   * @return List with names in the same order as the parameters.
   */
  public static List<String> asNames(final List<Parameter> parameters) {
    if ((parameters == null)) {
      return null;
    }
    final List<String> result = new ArrayList<String>();
    if ((parameters != null)) {
      for (final Parameter parameter : parameters) {
        result.add(parameter.getName());
      }
    }
    return result;
  }
}

package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.Literal;
import org.fuin.dsl.cqrs.cqrsDsl.StringLiteral;

/**
 * Provides extension methods for Literal.
 */
@SuppressWarnings("all")
public class CqrsLiteralExtensions {
  /**
   * Returns the value of the literal with leading and trailing double quote.
   * 
   * @param literal Literal to enhance with double quotes.
   * 
   * @return Original value or string with double quotes.
   */
  public static String str(final Literal literal) {
    if ((literal == null)) {
      return null;
    }
    if ((literal instanceof StringLiteral)) {
      String _value = ((StringLiteral)literal).getValue();
      String _plus = ("\"" + _value);
      return (_plus + "\"");
    }
    return literal.getValue();
  }

  /**
   * Returns a list of names from all variables.
   * 
   * @param vars Variable list.
   * 
   * @return List with names in the same order as the variables.
   */
  public static List<String> litNames(final List<Literal> literals) {
    if ((literals == null)) {
      return null;
    }
    final List<String> result = new ArrayList<String>();
    if ((literals != null)) {
      for (final Literal literal : literals) {
        result.add(literal.getValue());
      }
    }
    return result;
  }
}

package org.fuin.dsl.cqrs.extensions;

import java.util.StringTokenizer;
import org.eclipse.xtext.xbase.lib.IntegerRange;

/**
 * Provides extension methods for String.
 */
@SuppressWarnings("all")
public class CqrsStringExtensions {
  /**
   * Returns the pure doc message without slashes and stars in one line.
   * 
   * @param str JavaDoc comment.
   * 
   * @return Plain single line text.
   */
  public static String text(final String str) {
    if ((str == null)) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    StringTokenizer tok = new StringTokenizer(str, "\r\n");
    while (tok.hasMoreTokens()) {
      {
        String line = tok.nextToken();
        line = line.replace("/**", "");
        line = line.replace(" * ", "");
        line = line.replace("*/", "");
        sb.append(line);
        sb.append(" ");
      }
    }
    String result = sb.toString().replaceAll("\\s+", " ").trim();
    return result;
  }

  public static String toXmlName(final String name) {
    return name.replaceAll("(.)(\\p{Upper})", "$1-$2").toLowerCase();
  }

  public static String toSqlUpper(final String name) {
    return name.replaceAll("(.)(\\p{Upper})", "$1_$2").toUpperCase();
  }

  public static String toSqlLower(final String name) {
    return name.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
  }

  public static String toSqlInitials(final String name) {
    if (((name == null) || (name.length() == 0))) {
      return name;
    }
    final StringBuilder sb = new StringBuilder();
    final String lname = CqrsStringExtensions.toSqlLower(name);
    int _length = lname.length();
    int _minus = (_length - 1);
    IntegerRange _upTo = new IntegerRange(0, _minus);
    for (final Integer i : _upTo) {
      {
        final char ch = lname.charAt((i).intValue());
        if (((i).intValue() == 0)) {
          sb.append(ch);
        } else {
          if (((Character.valueOf(ch).compareTo(Character.valueOf('_')) == 0) && ((i).intValue() < (lname.length() - 1)))) {
            sb.append("_");
            sb.append(lname.charAt(((i).intValue() + 1)));
          }
        }
      }
    }
    return sb.toString();
  }

  /**
   * Returns the package without the class name.
   * 
   * @param packageAndClassName Package and name (like 'a.b.c.MyClass')
   * 
   * @return Package without class (like 'MyClass')
   */
  public static String onlyPackage(final String packageAndClassName) {
    final int p = packageAndClassName.lastIndexOf(".");
    if ((p == (-1))) {
      return packageAndClassName;
    }
    return packageAndClassName.substring(0, p);
  }
}

package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractVO;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject;
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType;
import org.fuin.dsl.cqrs.cqrsDsl.InternalType;
import org.fuin.dsl.cqrs.cqrsDsl.Literal;
import org.fuin.dsl.cqrs.cqrsDsl.Type;
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo;

/**
 * Provides extension methods for Type.
 */
@SuppressWarnings("all")
public class CqrsTypeExtensions {
  /**
   * Returns the doc text from the type.
   * 
   * @param type Type with doc text to read.
   * 
   * @return Type doc text.
   */
  public static String doc(final Type type) {
    if ((type instanceof AbstractEntity)) {
      return ((AbstractEntity)type).getDoc();
    } else {
      if ((type instanceof AbstractVO)) {
        return ((AbstractVO)type).getDoc();
      }
    }
    return type.getName();
  }

  /**
   * Returns the base type if available. External types as argument
   * will return the external type itself.
   * 
   * @param variable Type with base to return.
   * 
   * @return Base type or null.
   */
  public static ExternalType base(final Type type) {
    if ((type instanceof AbstractVO)) {
      return ((AbstractVO)type).getBase();
    } else {
      if ((type instanceof EnumObject)) {
        return ((EnumObject)type).getBase();
      } else {
        if ((type instanceof ExternalType)) {
          return ((ExternalType)type);
        }
      }
    }
    return null;
  }

  /**
   * Returns the meta info if available.
   * 
   * @param type Type with meta info to return.
   * 
   * @return Meta info or null.
   */
  public static TypeMetaInfo meta(final Type type) {
    if ((type instanceof InternalType)) {
      return ((InternalType)type).getMetaInfo();
    }
    return null;
  }

  /**
   * Returns the first example from the meta info for the variable if available.
   * 
   * @param variable Variable.
   * 
   * @return Example literal or null.
   */
  public static Literal firstExample(final Type type) {
    final TypeMetaInfo metaInfo = CqrsTypeExtensions.meta(type);
    if ((metaInfo == null)) {
      return null;
    }
    return metaInfo.getExamples().getFirst();
  }

  /**
   * Returns the corresponding Java primitive type if one exists.
   * 
   * @param type Type
   * 
   * @return Java primitive or original type name.
   */
  public static String asJavaPrimitive(final Type type) {
    String name = type.getName();
    if (name != null) {
      switch (name) {
        case "Byte":
          name = "byte";
          break;
        case "Short":
          name = "short";
          break;
        case "Integer":
          name = "int";
          break;
        case "Long":
          name = "long";
          break;
        case "Float":
          name = "float";
          break;
        case "Double":
          name = "double";
          break;
        case "Boolean":
          name = "boolean";
          break;
        case "Character":
          name = "char";
          break;
      }
    }
    return name;
  }

  /**
   * Returns the attributes of a type.
   * 
   * @param type Type to return a list of attributes for.
   * 
   * @return Attributes - Never null.
   */
  public static List<Attribute> getAttributes(final Type type) {
    if ((type == null)) {
      return new ArrayList<Attribute>();
    }
    if ((type instanceof InternalType)) {
      return ((InternalType)type).getAttributes();
    } else {
      return new ArrayList<Attribute>();
    }
  }
}

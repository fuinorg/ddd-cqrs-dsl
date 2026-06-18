package org.fuin.dsl.cqrs.extensions;

import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;
import org.fuin.dsl.cqrs.cqrsDsl.Type;

@SuppressWarnings("all")
public class CqrsDslFactoryExtensions {
  /**
   * Creates a parameter with a name.
   * 
   * @param factory Factory.
   * @param name Name.
   */
  public static Parameter createParameter(final CqrsDslFactory factory, final String name) {
    return CqrsDslFactoryExtensions.createParameter(factory, name, false);
  }

  /**
   * Creates a parameter with a name and "optional" information.
   * 
   * @param factory Factory.
   * @param name Name.
   * @param optional TRUE if optional, else false.
   */
  public static Parameter createParameter(final CqrsDslFactory factory, final String name, final boolean optional) {
    Parameter v = factory.createParameter();
    v.setName(name);
    if (optional) {
      v.setOptional("optional");
    }
    return v;
  }

  /**
   * Creates a parameter with type, name and "optional" information.
   * 
   * @param factory Factory.
   * @param type Type.
   * @param name Name.
   * @param optional TRUE if optional, else false.
   */
  public static Parameter createParameter(final CqrsDslFactory factory, final Type type, final String name, final boolean optional) {
    Parameter v = factory.createParameter();
    v.setName(name);
    if (optional) {
      v.setOptional("optional");
    }
    v.setType(type);
    return v;
  }

  /**
   * Creates a parameter with type, name and "optional" information.
   * 
   * @param factory Factory.
   * @param doc Documentation.
   * @param type Type.
   * @param name Name.
   * @param optional TRUE if optional, else false.
   */
  public static Parameter createParameter(final CqrsDslFactory factory, final String doc, final Type type, final String name, final boolean optional) {
    Parameter v = factory.createParameter();
    v.setDoc(doc);
    v.setName(name);
    if (optional) {
      v.setOptional("optional");
    }
    v.setType(type);
    return v;
  }

  /**
   * Creates an attribute with a name.
   * 
   * @param factory Factory.
   * @param name Name.
   */
  public static Attribute createAttribute(final CqrsDslFactory factory, final String name) {
    return CqrsDslFactoryExtensions.createAttribute(factory, name, false);
  }

  /**
   * Creates an attribute with a name and "optional" information.
   * 
   * @param factory Factory.
   * @param name Name.
   * @param optional TRUE if optional, else false.
   */
  public static Attribute createAttribute(final CqrsDslFactory factory, final String name, final boolean optional) {
    Attribute v = factory.createAttribute();
    v.setName(name);
    if (optional) {
      v.setOptional("optional");
    }
    return v;
  }

  /**
   * Creates an attribute with type, name and "optional" information.
   * 
   * @param factory Factory.
   * @param type Type.
   * @param name Name.
   * @param optional TRUE if optional, else false.
   */
  public static Attribute createAttribute(final CqrsDslFactory factory, final Type type, final String name, final boolean optional) {
    Attribute v = factory.createAttribute();
    v.setName(name);
    if (optional) {
      v.setOptional("optional");
    }
    v.setType(type);
    return v;
  }

  /**
   * Creates an attribute with type, name and "optional" information.
   * 
   * @param factory Factory.
   * @param doc Documentation.
   * @param type Type.
   * @param name Name.
   * @param optional TRUE if optional, else false.
   */
  public static Attribute createAttribute(final CqrsDslFactory factory, final String doc, final Type type, final String name, final boolean optional) {
    Attribute v = factory.createAttribute();
    v.setDoc(doc);
    v.setName(name);
    if (optional) {
      v.setOptional("optional");
    }
    v.setType(type);
    return v;
  }
}

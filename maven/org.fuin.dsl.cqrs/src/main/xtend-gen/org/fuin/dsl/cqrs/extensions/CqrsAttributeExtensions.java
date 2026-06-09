package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory;
import org.fuin.dsl.cqrs.cqrsDsl.GenericArgs;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;

/**
 * Provides extension methods for Attributes.
 */
@SuppressWarnings("all")
public class CqrsAttributeExtensions {
  /**
   * Copies the attribute and assigns a new name to the copy.
   * CAUTION: This is a shallow copy (no deep copy).
   * 
   * @param attr Attribute to copy
   * @param name New name.
   * 
   * @return Attribute copy with new name.
   */
  public static Attribute copyWithNewName(final Attribute attr, final String name) {
    Attribute newAttr = CqrsDslFactory.eINSTANCE.createAttribute();
    newAttr.setName(name);
    newAttr.setDoc(attr.getDoc());
    newAttr.setNullable(attr.getNullable());
    newAttr.setType(attr.getType());
    if (((attr.getGenerics() != null) && (attr.getGenerics().getArgs() != null))) {
      final GenericArgs generics = CqrsDslFactory.eINSTANCE.createGenericArgs();
      generics.getArgs().addAll(attr.getGenerics().getArgs());
      newAttr.setGenerics(generics);
    }
    newAttr.setInvariants(attr.getInvariants());
    newAttr.setOverridden(attr.getOverridden());
    return newAttr;
  }

  /**
   * Converts the attribute into a parameter.
   * CAUTION: This is a shallow copy (no deep copy).
   * 
   * @param attribute Attribute to convert.
   * 
   * @return Parameter.
   */
  public static Parameter asParameter(final Attribute attr) {
    if ((attr == null)) {
      return null;
    }
    final Parameter param = CqrsDslFactory.eINSTANCE.createParameter();
    param.setPreconditions(CqrsDslFactory.eINSTANCE.createPreconditions());
    param.getPreconditions().getConstraintInstances().addAll(CqrsInvariantsExtensions.nullSafe(attr.getInvariants()));
    param.setDoc(attr.getDoc());
    param.setNullable(attr.getNullable());
    param.setType(attr.getType());
    if (((attr.getGenerics() != null) && (attr.getGenerics().getArgs() != null))) {
      final GenericArgs generics = CqrsDslFactory.eINSTANCE.createGenericArgs();
      generics.getArgs().addAll(attr.getGenerics().getArgs());
      param.setGenerics(generics);
    }
    param.setName(attr.getName());
    param.setOverridden(attr.getOverridden());
    return param;
  }

  /**
   * Converts an attribute list into a list of parameters.
   * 
   * @param attributes List of attributes.
   * 
   * @return Parameter list.
   */
  public static List<Parameter> asParameters(final List<Attribute> attributes) {
    if ((attributes == null)) {
      return null;
    }
    final ArrayList<Parameter> list = new ArrayList<Parameter>();
    for (final Attribute attr : attributes) {
      list.add(CqrsAttributeExtensions.asParameter(attr));
    }
    return list;
  }

  /**
   * Returns a list of names from all attributes.
   * 
   * @param attributes Attribute list.
   * 
   * @return List with names in the same order as the attributes.
   */
  public static List<String> asNames(final List<Attribute> attributes) {
    if ((attributes == null)) {
      return null;
    }
    final List<String> result = new ArrayList<String>();
    if ((attributes != null)) {
      for (final Attribute attribute : attributes) {
        result.add(attribute.getName());
      }
    }
    return result;
  }
}

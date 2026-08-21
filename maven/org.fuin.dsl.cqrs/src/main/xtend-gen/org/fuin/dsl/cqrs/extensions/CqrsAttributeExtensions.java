package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory;
import org.fuin.dsl.cqrs.cqrsDsl.GenericArgs;
import org.fuin.dsl.cqrs.cqrsDsl.Invariants;
import org.fuin.dsl.cqrs.cqrsDsl.OverriddenTypeMetaInfo;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;
import org.fuin.dsl.cqrs.cqrsDsl.Type;

/**
 * Provides extension methods for Attributes.
 * 
 * <p><b>Anything containment-typed is copied here, never assigned.</b> EMF lets an object have one
 * container, so assigning an attribute's <code>overridden</code> wording or its <code>invariants</code>
 * to a newly built parameter does not share them - it <em>moves</em> them, and the attribute in the
 * parsed model is left with nothing. That model is not this code's to spend: a generator reads it, and
 * every generator that runs afterwards reads the same objects. A single caller building a parameter
 * from an attribute would otherwise silently strip the wording off the model for everybody behind it.
 */
@SuppressWarnings("all")
public class CqrsAttributeExtensions {
  /**
   * Copies the generic arguments.
   * The arguments are cross references, and EMF keeps a list of those unique: adding them with
   * "addAll" would silently drop the second argument of a "Map&lt;String, String&gt;", because it
   * is the same object as the first one.
   * 
   * @param generics Arguments to copy.
   * 
   * @return Copy that has all arguments, including the repeated ones.
   */
  private static GenericArgs copyOf(final GenericArgs generics) {
    final GenericArgs copy = CqrsDslFactory.eINSTANCE.createGenericArgs();
    EList<Type> _args = copy.getArgs();
    final InternalEList<Type> args = ((InternalEList<Type>) _args);
    args.addAllUnique(generics.getArgs());
    return copy;
  }

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
    newAttr.setOptional(attr.getOptional());
    newAttr.setType(attr.getType());
    if (((attr.getGenerics() != null) && (attr.getGenerics().getArgs() != null))) {
      newAttr.setGenerics(CqrsAttributeExtensions.copyOf(attr.getGenerics()));
    }
    newAttr.setInvariants(EcoreUtil.<Invariants>copy(attr.getInvariants()));
    newAttr.setOverridden(EcoreUtil.<OverriddenTypeMetaInfo>copy(attr.getOverridden()));
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
    List<ConstraintInstance> _nullSafe = CqrsInvariantsExtensions.nullSafe(attr.getInvariants());
    for (final ConstraintInstance invariant : _nullSafe) {
      param.getPreconditions().getConstraintInstances().add(EcoreUtil.<ConstraintInstance>copy(invariant));
    }
    param.setDoc(attr.getDoc());
    param.setOptional(attr.getOptional());
    param.setType(attr.getType());
    if (((attr.getGenerics() != null) && (attr.getGenerics().getArgs() != null))) {
      param.setGenerics(CqrsAttributeExtensions.copyOf(attr.getGenerics()));
    }
    param.setName(attr.getName());
    param.setOverridden(EcoreUtil.<OverriddenTypeMetaInfo>copy(attr.getOverridden()));
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

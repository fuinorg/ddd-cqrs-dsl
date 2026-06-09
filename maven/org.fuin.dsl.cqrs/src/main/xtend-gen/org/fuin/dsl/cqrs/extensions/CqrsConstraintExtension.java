package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.Constraint;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory;
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType;
import org.fuin.dsl.cqrs.cqrsDsl.InternalType;
import org.fuin.dsl.cqrs.cqrsDsl.Type;

@SuppressWarnings("all")
public class CqrsConstraintExtension {
  public static List<Attribute> allAllowedVariables(final Constraint constr) {
    List<Attribute> list = new ArrayList<Attribute>();
    EList<Attribute> _attributes = constr.getAttributes();
    boolean _tripleNotEquals = (_attributes != null);
    if (_tripleNotEquals) {
      list.addAll(constr.getAttributes());
    }
    if (((constr.getInput() != null) && (constr.getInput().size() > 0))) {
      final Type first = constr.getInput().get(0);
      int _size = constr.getInput().size();
      boolean _tripleEquals = (_size == 1);
      if (_tripleEquals) {
        list.addAll(CqrsConstraintExtension.attributesOf(first));
      } else {
        if ((first instanceof ExternalType)) {
          list.add(CqrsConstraintExtension.createInputAttribute(first));
        }
      }
    }
    return list;
  }

  public static List<Attribute> attributesOf(final Type target) {
    List<Attribute> list = new ArrayList<Attribute>();
    if ((target instanceof ExternalType)) {
      list.add(CqrsConstraintExtension.createInputAttribute(target));
    } else {
      if ((target instanceof InternalType)) {
        list.add(CqrsConstraintExtension.createInputAttribute(target));
        EList<Attribute> _attributes = ((InternalType)target).getAttributes();
        boolean _tripleNotEquals = (_attributes != null);
        if (_tripleNotEquals) {
          EList<Attribute> _attributes_1 = ((InternalType)target).getAttributes();
          for (final Attribute attr : _attributes_1) {
            {
              String _name = attr.getName();
              String _plus = ("input." + _name);
              Attribute newAttr = CqrsAttributeExtensions.copyWithNewName(attr, _plus);
              list.add(newAttr);
            }
          }
        }
      }
    }
    return list;
  }

  public static Attribute createInputAttribute(final Type type) {
    return CqrsDslFactoryExtensions.createAttribute(CqrsDslFactory.eINSTANCE, "/** The validated value. */", type, "input", true);
  }
}

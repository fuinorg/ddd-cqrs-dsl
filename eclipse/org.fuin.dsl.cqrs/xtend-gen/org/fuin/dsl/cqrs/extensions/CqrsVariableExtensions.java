package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory;
import org.fuin.dsl.cqrs.cqrsDsl.Literal;
import org.fuin.dsl.cqrs.cqrsDsl.OverriddenTypeMetaInfo;
import org.fuin.dsl.cqrs.cqrsDsl.Parameter;
import org.fuin.dsl.cqrs.cqrsDsl.TypeMetaInfo;
import org.fuin.dsl.cqrs.cqrsDsl.Variable;

/**
 * Provides extension methods for Variable.
 */
@SuppressWarnings("all")
public class CqrsVariableExtensions {
  /**
   * Returns the doc text from the variable or the type.
   * 
   * @param variable Variable with doc text to read.
   * 
   * @return Variable or type doc.
   */
  public static String superDoc(final Variable variable) {
    String _xifexpression = null;
    String _doc = variable.getDoc();
    boolean _tripleEquals = (_doc == null);
    if (_tripleEquals) {
      _xifexpression = CqrsStringExtensions.text(variable.getType().getDoc());
    } else {
      return CqrsStringExtensions.text(variable.getDoc());
    }
    return _xifexpression;
  }

  /**
   * Returns the corresponding Java primitive type.
   * 
   * @param variable Variable
   * 
   * @return Primitive type or original type name.
   */
  public static String asJavaPrimitive(final Variable variable) {
    return CqrsTypeExtensions.asJavaPrimitive(variable.getType());
  }

  /**
   * Returns the overridden meta info for the variable.
   * This is a null safe shortcut for <code>variable.overridden.metaInfo</code>.
   * 
   * @param variable Variable.
   * 
   * @return Meta info or null.
   */
  public static TypeMetaInfo overriddenMeta(final Variable variable) {
    OverriddenTypeMetaInfo _overridden = variable.getOverridden();
    boolean _tripleEquals = (_overridden == null);
    if (_tripleEquals) {
      return null;
    }
    return variable.getOverridden().getMetaInfo();
  }

  /**
   * Returns the meta info for the variable if available.
   * 
   * @param variable Variable.
   * 
   * @return Meta info or null.
   */
  public static TypeMetaInfo meta(final Variable variable) {
    final TypeMetaInfo metaInfo = CqrsDslFactory.eINSTANCE.createTypeMetaInfo();
    final TypeMetaInfo typeMeta = CqrsTypeExtensions.meta(variable.getType());
    if ((typeMeta != null)) {
      metaInfo.setSlabel(typeMeta.getSlabel());
      metaInfo.setLabel(typeMeta.getLabel());
      metaInfo.setTooltip(typeMeta.getTooltip());
      metaInfo.setPrompt(typeMeta.getPrompt());
      metaInfo.getExamples().addAll(typeMeta.getExamples());
    }
    if (((variable.getOverridden() != null) && (variable.getOverridden().getMetaInfo() != null))) {
      final TypeMetaInfo varMeta = variable.getOverridden().getMetaInfo();
      String _slabel = varMeta.getSlabel();
      boolean _tripleNotEquals = (_slabel != null);
      if (_tripleNotEquals) {
        metaInfo.setSlabel(varMeta.getSlabel());
      }
      String _label = varMeta.getLabel();
      boolean _tripleNotEquals_1 = (_label != null);
      if (_tripleNotEquals_1) {
        metaInfo.setLabel(varMeta.getLabel());
      }
      String _tooltip = varMeta.getTooltip();
      boolean _tripleNotEquals_2 = (_tooltip != null);
      if (_tripleNotEquals_2) {
        metaInfo.setTooltip(varMeta.getTooltip());
      }
      String _prompt = varMeta.getPrompt();
      boolean _tripleNotEquals_3 = (_prompt != null);
      if (_tripleNotEquals_3) {
        metaInfo.setPrompt(varMeta.getPrompt());
      }
      int _size = varMeta.getExamples().size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        metaInfo.getExamples().clear();
        metaInfo.getExamples().addAll(varMeta.getExamples());
      }
    }
    return metaInfo;
  }

  /**
   * Returns the first example from the meta info for the variable if available.
   * 
   * @param variable Variable.
   * 
   * @return Example literal or null.
   */
  public static Literal firstExample(final Variable variable) {
    final TypeMetaInfo metaInfo = CqrsVariableExtensions.meta(variable);
    return metaInfo.getExamples().getFirst();
  }

  /**
   * Converts a variable list into a list of parameters.
   * 
   * @param variables List of variables.
   * 
   * @return Parameter list.
   */
  public static List<Parameter> asParameters(final List<? extends Variable> variables) {
    if ((variables == null)) {
      return null;
    }
    final ArrayList<Parameter> list = new ArrayList<Parameter>();
    for (final Variable attr : variables) {
      list.add(CqrsVariableExtensions.asParameter(attr));
    }
    return list;
  }

  /**
   * Converts the variable into a parameter.
   * 
   * @param variable Variable to convert.
   * 
   * @return Parameter.
   */
  public static Parameter asParameter(final Variable variable) {
    if ((variable == null)) {
      return null;
    }
    if ((variable instanceof Parameter)) {
      return ((Parameter)variable);
    }
    if ((variable instanceof Attribute)) {
      return CqrsAttributeExtensions.asParameter(((Attribute)variable));
    }
    String _name = variable.getClass().getName();
    String _plus = ("Unknown variable type: " + _name);
    throw new IllegalStateException(_plus);
  }
}

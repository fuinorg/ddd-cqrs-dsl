package org.fuin.dsl.cqrs.extensions;

import org.fuin.dsl.cqrs.cqrsDsl.AbstractVO;
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId;
import org.fuin.dsl.cqrs.cqrsDsl.EntityId;
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;

/**
 * Provides extension methods for AbstractVO.
 */
@SuppressWarnings("all")
public class CqrsAbstractVOExtensions {
  /**
   * Returns the base type for a value object.
   * 
   * @param vo Value object.
   * 
   * @return Base type or null.
   */
  public static ExternalType baseType(final AbstractVO vo) {
    if ((vo instanceof AggregateId)) {
      return ((AggregateId)vo).getBase();
    }
    if ((vo instanceof EntityId)) {
      return ((EntityId)vo).getBase();
    }
    if ((vo instanceof ValueObject)) {
      return ((ValueObject)vo).getBase();
    }
    return null;
  }

  /**
   * Returns the base type name for a value object.
   * 
   * @param vo Value object.
   * 
   * @return Name or null.
   */
  public static String baseTypeName(final AbstractVO vo) {
    ExternalType _baseType = CqrsAbstractVOExtensions.baseType(vo);
    boolean _tripleEquals = (_baseType == null);
    if (_tripleEquals) {
      return null;
    } else {
      return CqrsAbstractVOExtensions.baseType(vo).getName();
    }
  }
}

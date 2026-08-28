package org.fuin.dsl.cqrs.extensions;

import org.eclipse.emf.common.util.EList;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractVO;
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.EntityId;
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType;
import org.fuin.dsl.cqrs.cqrsDsl.Type;
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

  /**
   * The attribute that identifies a row, whether the row says so or the type does.
   * 
   * <p>A declared <code>identified-by</code> wins, because it is a cross reference and the model has
   * already guaranteed it names an attribute of this row. Without one, only the type can say, and a
   * single attribute typed as an aggregate id or an entity id is unambiguous - which is how most rows
   * in a corpus end up identified without anyone writing the clause.
   * 
   * <p>A path is <em>not</em> inferred: rows carry paths to other things far more often than as their
   * own identity, so an undeclared one stays a column rather than silently becoming the identity. A
   * declared one is fine, because somebody said so.
   * 
   * <p><b>This has to agree with what the Flutter target does</b> - see the role of a generated row's
   * attributes - because a check that disagrees with the generator reports on a screen that does not
   * exist, or stays quiet about one that does.
   * 
   * @param vo Value object, which is a row when this answers something.
   * 
   * @return The identifying attribute, or <code>null</code> when the value object identifies nothing.
   */
  public static Attribute rowIdentity(final ValueObject vo) {
    if ((vo == null)) {
      return null;
    }
    Attribute _identifiedBy = vo.getIdentifiedBy();
    boolean _tripleNotEquals = (_identifiedBy != null);
    if (_tripleNotEquals) {
      return vo.getIdentifiedBy();
    }
    Attribute found = null;
    EList<Attribute> _attributes = vo.getAttributes();
    for (final Attribute attribute : _attributes) {
      {
        final Type type = attribute.getType();
        if (((type instanceof AggregateId) || (type instanceof EntityId))) {
          if ((found != null)) {
            return null;
          }
          found = attribute;
        }
      }
    }
    return found;
  }
}

package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance;
import org.fuin.dsl.cqrs.cqrsDsl.Preconditions;

/**
 * Provides extension methods for Preconditions.
 */
@SuppressWarnings("all")
public class CqrsPreconditionsExtensions {
  /**
   * Returns a non-null constraint list.
   * 
   * @param preconditions Container with constraints.
   * 
   * @return List of constraints that is never <code<null</code>.
   */
  public static List<ConstraintInstance> nullSafe(final Preconditions preconditions) {
    if (((preconditions == null) || (preconditions.getConstraintInstances() == null))) {
      return new ArrayList<ConstraintInstance>();
    }
    return preconditions.getConstraintInstances();
  }
}

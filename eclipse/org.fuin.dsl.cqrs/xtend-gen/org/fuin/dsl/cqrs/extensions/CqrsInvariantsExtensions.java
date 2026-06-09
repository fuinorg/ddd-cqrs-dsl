package org.fuin.dsl.cqrs.extensions;

import java.util.ArrayList;
import java.util.List;
import org.fuin.dsl.cqrs.cqrsDsl.ConstraintInstance;
import org.fuin.dsl.cqrs.cqrsDsl.Invariants;

/**
 * Provides extension methods for Invariants.
 */
@SuppressWarnings("all")
public class CqrsInvariantsExtensions {
  /**
   * Returns a non-null constraint list.
   * 
   * @param invariants Container with constraints.
   * 
   * @return List of constraints that is never <code<null</code>.
   */
  public static List<ConstraintInstance> nullSafe(final Invariants invariants) {
    if (((invariants == null) || (invariants.getConstraintInstances() == null))) {
      return new ArrayList<ConstraintInstance>();
    }
    return invariants.getConstraintInstances();
  }
}

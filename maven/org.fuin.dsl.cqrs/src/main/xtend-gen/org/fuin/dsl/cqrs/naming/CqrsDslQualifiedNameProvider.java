package org.fuin.dsl.cqrs.naming;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.fuin.dsl.cqrs.cqrsDsl.Project;

/**
 * Keeps DSL qualified names based on <code>context.namespace.element</code> by excluding the
 * enclosing {@link Project} from the name hierarchy. A project groups contexts and carries generator
 * hints; it is an organizational / code generation unit, not part of the reference namespace, so
 * imports and cross references keep resolving against context/namespace names regardless of the
 * enclosing project.
 */
@SuppressWarnings("all")
public class CqrsDslQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {
  /**
   * Returns <code>null</code> for a {@link Project} so it contributes no segment: the name-attribute
   * walk-up then skips it and a child's qualified name starts at its context. Returning null from a
   * <code>qualifiedName(Project)</code> dispatch method would NOT work - the framework treats that as
   * "no custom name" and falls back to the project's name attribute.
   */
  @Override
  protected QualifiedName computeFullyQualifiedName(final EObject obj) {
    QualifiedName _xblockexpression = null;
    {
      if ((obj instanceof Project)) {
        return null;
      }
      _xblockexpression = super.computeFullyQualifiedName(obj);
    }
    return _xblockexpression;
  }
}

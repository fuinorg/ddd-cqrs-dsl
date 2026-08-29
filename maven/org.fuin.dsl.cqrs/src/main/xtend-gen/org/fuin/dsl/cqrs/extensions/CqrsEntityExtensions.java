package org.fuin.dsl.cqrs.extensions;

import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId;
import org.fuin.dsl.cqrs.cqrsDsl.Entity;
import org.fuin.dsl.cqrs.cqrsDsl.EntityId;
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType;
import org.fuin.dsl.cqrs.cqrsDsl.PathSegment;

/**
 * Provides extension methods for Entity.
 */
@SuppressWarnings("all")
public class CqrsEntityExtensions {
  /**
   * Returns the declared path that addresses the given entity from its root, or <code>null</code> if
   * the model declares none.
   * 
   * <p>Matched on the chain rather than on a name, so a model that spells the declaration differently
   * still resolves it. The first match wins: a second declaration addressing the same chain is a
   * duplicate rather than a choice, and saying which one to prefer would be inventing an answer the
   * model does not give.</p>
   * 
   * <p>Searched over the whole resource set rather than the entity's own file. A module may be declared
   * in more than one file - a context whose aggregates are split off has a block of the same name in
   * both halves - and the path is commonly declared in the half the entity is not in.</p>
   * 
   * @param entity Entity to find the path type for.
   * 
   * @return Declared path type, or NULL if the model has none for this chain.
   */
  public static EntityIdPathType getPathTypeNullable(final Entity entity) {
    Aggregate _rootNullsafe = null;
    if (entity!=null) {
      _rootNullsafe=CqrsEntityExtensions.getRootNullsafe(entity);
    }
    final Aggregate root = _rootNullsafe;
    if ((root == null)) {
      return null;
    }
    Resource _eResource = entity.eResource();
    ResourceSet _resourceSet = null;
    if (_eResource!=null) {
      _resourceSet=_eResource.getResourceSet();
    }
    final ResourceSet resourceSet = _resourceSet;
    List<EObject> _xifexpression = null;
    if ((resourceSet == null)) {
      EObject _rootContainer = EcoreUtil2.getRootContainer(entity);
      _xifexpression = Collections.<EObject>unmodifiableList(CollectionLiterals.<EObject>newArrayList(_rootContainer));
    } else {
      final Function1<Resource, EList<EObject>> _function = (Resource it) -> {
        return it.getContents();
      };
      _xifexpression = IterableExtensions.<EObject>toList(Iterables.<EObject>concat(ListExtensions.<Resource, EList<EObject>>map(resourceSet.getResources(), _function)));
    }
    final List<EObject> roots = _xifexpression;
    for (final EObject container : roots) {
      List<EntityIdPathType> _allContentsOfType = EcoreUtil2.<EntityIdPathType>getAllContentsOfType(container, EntityIdPathType.class);
      for (final EntityIdPathType path : _allContentsOfType) {
        {
          final List<PathSegment> segments = IterableExtensions.<PathSegment>toList(CqrsCollectionExtensions.<PathSegment>nullSafe(path.getSegments()));
          int _size = segments.size();
          boolean _equals = (_size == 2);
          if (_equals) {
            final AbstractEntityId first = segments.get(0).getType();
            final AbstractEntityId last = segments.get(1).getType();
            if (((((first instanceof AggregateId) && (last instanceof EntityId)) && (((AggregateId) first).getAggregate() == root)) && (((EntityId) last).getEntity() == entity))) {
              return path;
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Returns the entity identifier type regardless if it's
   * defined inside the entity or somewhere outside.
   * 
   * @param entity Entity to return the identifier type for.
   * 
   * @return Entity identifier type.
   */
  public static EntityId getIdTypeNullsafe(final Entity entity) {
    EntityId _idType = entity.getIdType();
    boolean _tripleEquals = (_idType == null);
    if (_tripleEquals) {
      return CqrsEntityExtensions.getEntityId(entity);
    }
    return entity.getIdType();
  }

  /**
   * Returns the entity identifier that may be defined inside the entity.
   * 
   * @param entity Entity to return the identifier for.
   * 
   * @return Identifier or NULL if no such type is defined inside the entity.
   */
  public static EntityId getEntityId(final Entity entity) {
    final Iterable<EntityId> types = Iterables.<EntityId>filter(CqrsCollectionExtensions.<AbstractElement>nullSafe(entity.getElements()), EntityId.class);
    int _length = ((Object[])Conversions.unwrapArray(types, Object.class)).length;
    boolean _equals = (_length == 0);
    if (_equals) {
      return null;
    }
    return ((EntityId[])Conversions.unwrapArray(types, EntityId.class))[0];
  }

  /**
   * Returns the aggregate the entity belongs to. It does not matter
   * if the aggregate is referenced by using the 'root' expression
   * or the entity is declared inside an aggregate.
   * 
   * @param entity The entity to return the aggregate for.
   * 
   * @return Aggregate of the entity.
   */
  public static Aggregate getRootNullsafe(final Entity entity) {
    Aggregate _root = entity.getRoot();
    boolean _tripleEquals = (_root == null);
    if (_tripleEquals) {
      entity.eContainer();
    }
    return entity.getRoot();
  }
}

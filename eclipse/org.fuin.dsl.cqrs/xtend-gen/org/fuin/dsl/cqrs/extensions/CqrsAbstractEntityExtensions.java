package org.fuin.dsl.cqrs.extensions;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.Entity;
import org.fuin.dsl.cqrs.cqrsDsl.Event;
import org.fuin.dsl.cqrs.cqrsDsl.Service;
import org.fuin.dsl.cqrs.cqrsDsl.Type;

/**
 * Provides extension methods for AbstractEntity.
 */
@SuppressWarnings("all")
public class CqrsAbstractEntityExtensions {
  /**
   * Returns a list of all constructors and methods.
   * 
   * @param entity Entity to return all constructors and methods for.
   * 
   * @return List of all constructors and methods.
   */
  public static List<AbstractMethod> constructorsAndMethods(final AbstractEntity entity) {
    final List<AbstractMethod> methods = new ArrayList<AbstractMethod>();
    methods.addAll(entity.getConstructors());
    methods.addAll(entity.getMethods());
    return methods;
  }

  /**
   * Returns a list of all constructors and methods.
   * 
   * @param entity Entity to return all constructors and methods for.
   * 
   * @return List of all constructors and methods.
   */
  public static List<Service> services(final AbstractEntity entity) {
    final List<Service> services = new ArrayList<Service>();
    final List<AbstractMethod> methods = CqrsAbstractEntityExtensions.constructorsAndMethods(entity);
    for (final AbstractMethod method : methods) {
      services.addAll(CqrsCollectionExtensions.<Service>nullSafe(method.getServices()));
    }
    return services;
  }

  /**
   * Returns a list of all direct child entities for an entity.
   * 
   * @param parent Direct parent with references to entities.
   * 
   * @return List of directly referenced child entities.
   */
  public static Set<Entity> childEntities(final AbstractEntity parent) {
    Set<Entity> childs = new HashSet<Entity>();
    EList<Attribute> _attributes = parent.getAttributes();
    for (final Attribute v : _attributes) {
      Type _type = v.getType();
      if ((_type instanceof Entity)) {
        Type _type_1 = v.getType();
        childs.add(((Entity) _type_1));
      }
    }
    return childs;
  }

  /**
   * Returns a list of all events for an entity.
   * 
   * @param entity Entity to return the events for.
   * 
   * @return List of events declared in the entity or in one of it's methods.
   */
  public static List<Event> allEvents(final AbstractEntity entity) {
    List<Event> events = new ArrayList<Event>();
    List<AbstractMethod> _constructorsAndMethods = CqrsAbstractEntityExtensions.constructorsAndMethods(entity);
    for (final AbstractMethod m : _constructorsAndMethods) {
      events.addAll(CqrsCollectionExtensions.<Event>nullSafe(m.getEvents()));
    }
    List<AbstractElement> _nullSafe = CqrsCollectionExtensions.<AbstractElement>nullSafe(entity.getElements());
    for (final AbstractElement element : _nullSafe) {
      if ((element instanceof Event)) {
        events.add(((Event)element));
      }
    }
    return events;
  }

  /**
   * Returns the abstract entity identifier that may be defined inside the abstractEntity.
   * 
   * @param abstractEntity Abstract entity to return the identifier for.
   * 
   * @return Identifier or NULL if no such type is defined inside the abstract entity.
   */
  public static AbstractEntityId getAbstractEntityId(final AbstractEntity abstractEntity) {
    final Iterable<AbstractEntityId> types = Iterables.<AbstractEntityId>filter(CqrsCollectionExtensions.<AbstractElement>nullSafe(abstractEntity.getElements()), AbstractEntityId.class);
    int _length = ((Object[])Conversions.unwrapArray(types, Object.class)).length;
    boolean _equals = (_length == 0);
    if (_equals) {
      return null;
    }
    return ((AbstractEntityId[])Conversions.unwrapArray(types, AbstractEntityId.class))[0];
  }

  /**
   * Returns the type of the identifier for the abstract entity.
   * 
   * @param abstractEntity Abstract entity to return the identifier type for.
   * 
   * @return Identifier or NULL if no such type is defined inside the abstract entity.
   */
  public static AbstractEntityId getIdType(final AbstractEntity abstractEntity) {
    if ((abstractEntity instanceof Aggregate)) {
      return ((Aggregate)abstractEntity).getIdType();
    }
    if ((abstractEntity instanceof Entity)) {
      return ((Entity)abstractEntity).getIdType();
    }
    Class<? extends AbstractEntity> _class = abstractEntity.getClass();
    String _plus = ("Expected \'Aggregate\' or \'Entity\', but was: " + _class);
    throw new IllegalStateException(_plus);
  }
}

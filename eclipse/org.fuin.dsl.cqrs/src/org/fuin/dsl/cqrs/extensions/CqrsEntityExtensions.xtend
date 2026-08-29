package org.fuin.dsl.cqrs.extensions

import org.eclipse.xtext.EcoreUtil2
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.Entity
import org.fuin.dsl.cqrs.cqrsDsl.EntityId
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Provides extension methods for Entity.
 */
class CqrsEntityExtensions {

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
	def static EntityIdPathType getPathTypeNullable(Entity entity) {
		val root = entity?.rootNullsafe
		if (root === null) {
			return null
		}
		val resourceSet = entity.eResource?.resourceSet
		val roots = if (resourceSet === null) {
				#[EcoreUtil2.getRootContainer(entity)]
			} else {
				resourceSet.resources.map[contents].flatten.toList
			}
		for (container : roots) {
			for (path : EcoreUtil2.getAllContentsOfType(container, EntityIdPathType)) {
				val segments = path.segments.nullSafe.toList
				if (segments.size == 2) {
					val first = segments.get(0).type
					val last = segments.get(1).type
					if (first instanceof AggregateId && last instanceof EntityId
						&& (first as AggregateId).aggregate === root && (last as EntityId).entity === entity) {
						return path
					}
				}
			}
		}
		return null
	}

	/**
	 * Returns the entity identifier type regardless if it's 
	 * defined inside the entity or somewhere outside.
	 * 
	 * @param entity Entity to return the identifier type for.
	 * 
	 * @return Entity identifier type.
	 */
	def static EntityId getIdTypeNullsafe(Entity entity) {
		if (entity.idType === null) {
			return entity.entityId
		}
		return entity.idType
	}

	/**
	 * Returns the entity identifier that may be defined inside the entity.
	 * 
	 * @param entity Entity to return the identifier for.
	 * 
	 * @return Identifier or NULL if no such type is defined inside the entity.
	 */
	def static EntityId getEntityId(Entity entity) {
		val types = entity.elements.nullSafe.filter(typeof(EntityId))
		if (types.length == 0) {
			return null
		}
		return types.get(0)
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
	def static Aggregate getRootNullsafe(Entity entity) {
		if (entity.root === null) {
			entity.eContainer as Aggregate
		}
		return entity.root
	}

}

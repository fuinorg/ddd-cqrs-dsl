package org.fuin.dsl.cqrs.extensions

import java.util.ArrayList
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.fuin.dsl.cqrs.cqrsDsl.Event


import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static org.fuin.dsl.cqrs.extensions.CqrsExtensionUtils.*

/**
 * Provides extension methods for Event.
 */
class CqrsEventExtensions {
	
	/**
	 * Returns the unique name of the event.
	 * 
	 * @param el Event to return a unique name for.
	 * 
	 * @return Unique name in the context/module.
	 */
	def static String uniqueName(Event event) {
		if (event === null) {
			throw new IllegalArgumentException("argument 'event' cannot be null")
		}
		if (event.module === null) {
			throw new IllegalArgumentException("argument 'event.module' cannot be null")
		}
		return separated(".", event.context.name, event.module.name, event.name)
	}


	/**
	 * Returns the aggregate or entity an event belongs to, which is what makes it a domain event
	 * rather than a plain one. Resolved in three steps, the first hit winning:
	 * <ol>
	 * <li>the containers, for an event declared inside an aggregate or entity;</li>
	 * <li>the "copies-attributes-of" origin, whose container is the owner (a cross reference, which
	 * the container walk cannot follow);</li>
	 * <li>the constructor or method declaring "fires &lt;event&gt;", for an event declared beside
	 * the aggregate rather than inside it.</li>
	 * </ol>
	 *
	 * @param event Event to return the parent entity for.
	 *
	 * @return Aggregate or Entity or null if the event belongs to none.
	 */
	def static AbstractEntity getEntity(Event event) {
		if (event === null) {
			return null
		}
		val fromContainer = getAbstractEntity(event)
		if (fromContainer !== null) {
			return fromContainer
		}
		if (event.origin !== null) {
			val fromOrigin = getAbstractEntity(event.origin)
			if (fromOrigin !== null) {
				return fromOrigin
			}
		}
		return getFiringEntity(event)
	}

	/**
	 * Returns the single aggregate or entity that declares "fires &lt;event&gt;" for the given event.
	 * When more than one does, null is returned: binding the event to either of them would be a
	 * guess, so it stays a plain event instead of silently belonging to the wrong aggregate.
	 *
	 * @param event Event to find the firing aggregate or entity for.
	 *
	 * @return Aggregate or Entity, or null if none or more than one fires the event.
	 */
	private def static AbstractEntity getFiringEntity(Event event) {
		var AbstractEntity found = null
		// Every model read so far is searched, not only the one the event lives in: a module may be
		// split across files - a model that publishes only part of itself has to be - so the aggregate
		// firing an event is not necessarily beside it. A copy is iterated because comparing the fired
		// events resolves cross references, which may load further resources into the very set being
		// iterated.
		for (resource : event.resources) {
			val iter = resource.allContents
			while (iter.hasNext) {
				val obj = iter.next
				if (obj instanceof AbstractEntity) {
					if (obj.fires(event)) {
						if (found !== null && found !== obj) {
							return null
						}
						found = obj
					}
				}
			}
		}
		return found
	}

	/** Every model to search for the given element, its own included. */
	private def static List<Resource> getResources(EObject obj) {
		val resource = obj.eResource
		if (resource === null) {
			return emptyList
		}
		val resourceSet = resource.resourceSet
		if (resourceSet === null) {
			return #[resource]
		}
		return new ArrayList(resourceSet.resources)
	}

	/** Returns TRUE if any of the entity's constructors or methods declares "fires &lt;event&gt;". */
	private def static boolean fires(AbstractEntity entity, Event event) {
		for (constructor : entity.constructors) {
			if (constructor.firedEvents.contains(event)) {
				return true
			}
		}
		for (method : entity.methods) {
			if (method.firedEvents.contains(event)) {
				return true
			}
		}
		return false
	}

	private def static AbstractEntity getAbstractEntity(EObject obj) {
		if (obj === null) {
			return null
		}
		if (obj instanceof AbstractEntity) {
			return obj
		}
		return getAbstractEntity(obj.eContainer)
	}

}

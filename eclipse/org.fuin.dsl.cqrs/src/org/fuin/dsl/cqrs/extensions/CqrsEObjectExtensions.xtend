package org.fuin.dsl.cqrs.extensions

import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.Context
import org.fuin.dsl.cqrs.cqrsDsl.Entity
import org.fuin.dsl.cqrs.cqrsDsl.Namespace
import org.fuin.dsl.cqrs.cqrsDsl.Project
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement
import java.lang.reflect.Method
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity

/**
 * Provides extension methods for EObject.
 */
class CqrsEObjectExtensions {

	/**
	 * Returns the namespace for an object.
	 * 
	 * @param obj Object to return the namespace for.
	 * 
	 * @return Namespace or null if the object is not inside one.
	 */
	def static Namespace getNamespace(EObject obj) {
		if (obj === null) {
			return null
		}
		if (obj instanceof Namespace) {
			return obj
		}
		return getNamespace(obj.eContainer)
	}

	/**
	 * Returns the context for an object.
	 * 
	 * @param obj Object to return the context for.
	 * 
	 * @return Context or null if the object is not inside one.
	 */
	def static Context getContext(EObject obj) {
		if (obj === null) {
			return null
		}
		if (obj instanceof Context) {
			return obj
		}
		return getContext(obj.eContainer)
	}

	/**
	 * Returns the project an object belongs to.
	 *
	 * @param obj Object to return the project for.
	 *
	 * @return Project or null if the object is not inside one.
	 */
	def static Project getProject(EObject obj) {
		if (obj === null) {
			return null
		}
		if (obj instanceof Project) {
			return obj
		}
		return getProject(obj.eContainer)
	}


	/**
	 * Returns the path in the model to the object.
	 * 
	 * @param obj Object to return the path for.
	 * 
	 * @return Path or empty string if the object is not inside one.
	 */
	def static String getPath(EObject obj) {
		if (obj === null) {
			return ""
		}		
		return getPath(obj.eContainer) + "/" + obj.name
	}

	/**
	 * Returns the name of an object if it has one.
	 * 
	 * @param obj Object to return the name for.
	 * 
	 * @return Name or text returned by the object's toString() method.
	 */
	def static String getName(EObject obj) {
		if (obj === null) {
			return null
		}		
		if (obj instanceof AbstractElement) {
			return obj.name
		}
		return obj.reflectName
	}

	/**
	 * Returns the parent aggregate for an object.
	 * 
	 * @param obj Object to return the parent aggregate for.
	 * 
	 * @return Context or null if the object is not inside one.
	 */
	def static Aggregate getAggregate(EObject obj) {
		if (obj === null) {
			return null
		}
		if (obj instanceof Entity) {
			return obj.root
		}
		if (obj instanceof Aggregate) {
			return obj
		}
		return getAggregate(obj.eContainer)
	}

	/**
	 * Returns the aggregate a command belongs to. A command nested inside an aggregate is found by
	 * walking its containers, exactly like any other object. A command declared beside the aggregate
	 * (directly in the context) has no such container, so the aggregate is taken from the method the
	 * command targets - the target is a cross reference, which the container walk cannot follow.
	 *
	 * @param command Command to return the aggregate for.
	 *
	 * @return Aggregate or null if the command neither sits inside one nor targets one.
	 */
	def static Aggregate getAggregate(Command command) {
		if (command === null) {
			return null
		}
		val aggregate = getAggregate(command.eContainer)
		if (aggregate !== null) {
			return aggregate
		}
		if (command.target === null) {
			return null
		}
		return getAggregate(command.target)
	}


	/**
	 * Returns the parent entity for an object.
	 *
	 * @param obj Object to return the parent entity for.
	 *
	 * @return Context or null if the object is not inside one.
	 */
	def static AbstractEntity getEntity(EObject obj) {
		if (obj === null) {
			return null
		}
		if (obj instanceof Entity) {
			return obj
		}
		if (obj instanceof Aggregate) {
			return obj
		}
		return getEntity(obj.eContainer)
	}

	/**
	 * Returns the entity a command belongs to, following the same two step resolution as
	 * {@link #getAggregate(Command)}: the containers first, then the targeted method. This is the
	 * aggregate root for a command addressing the root itself, and the child entity for a command
	 * addressing one.
	 *
	 * @param command Command to return the entity for.
	 *
	 * @return Entity or null if the command neither sits inside one nor targets one.
	 */
	def static AbstractEntity getEntity(Command command) {
		if (command === null) {
			return null
		}
		val entity = getEntity(command.eContainer)
		if (entity !== null) {
			return entity
		}
		if (command.target === null) {
			return null
		}
		return getEntity(command.target)
	}

	/**
	 * Returns the root container for a given object.
	 * 
	 * @param obj Object to return the top container for.
	 * 
	 * @return Root container.
	 */
	static def EObject getRoot(EObject obj) {
		if (obj.eContainer === null) {
			return obj
		}
		return getRoot(obj.eContainer)
	}

	/**
	 * Returns the first parent with a given type for an object.
	 * 
	 * @param obj Object to return the parent for.
	 * 
	 * @return Parent or null if the object is not inside the requested type.
	 */
	def static <T> T getParent(Class<T> clasz, EObject obj) {
		if (obj === null) {
			return null
		}
		if (clasz.isAssignableFrom(obj.class)) {
			return (obj as T)
		}
		return getParent(clasz, obj.eContainer)
	}


	def static String reflectName(Object obj) {
		if (obj === null) {
			return null
		}
		try {
			val Method method = obj.class.getMethod("getName", null)
			return method.invoke(obj) as String
		} catch (NoSuchMethodException ex) {
			// Fallback
			return obj.toString
		}
	}

}

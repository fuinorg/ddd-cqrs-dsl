package org.fuin.dsl.ddd.gen.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.dsl.cqrs.cqrsDsl.Service

import static org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory.eINSTANCE

import static extension org.fuin.dsl.cqrs.extensions.CqrsDslFactoryExtensions.*

/**
 * Provides extension methods for the service a constructor or method uses.
 * <p>
 * A constructor and a method may <i>reference</i> exactly one service - the SPI the operation needs to
 * verify a business rule or fetch data it cannot know itself (a read model check, a cross-aggregate
 * lookup, an import adapter). That is the singular reference in the grammar, written as a bare type
 * name in the body; it is deliberately not a modelled parameter, because an active collaborator has
 * no place in a serializable command.
 * <p>
 * The generated operation still has to receive it, though: an aggregate is a plain object that no
 * container injects into, so the referenced service is appended to the signature as a trailing
 * parameter, after the modelled domain arguments, and the command handler resolves the implementation
 * and hands it in.
 * <p>
 * Not to be confused with the services a constructor or method <i>declares</i> inline
 * (<code>services</code>): those are type declarations, generated as nested interfaces of the
 * aggregate or entity, and never a parameter on their own. A method typically declares the interface
 * it needs inline and references it here, but referencing a service declared elsewhere works just as
 * well - a <code>Service</code> is a <code>Type</code>, so the declaration, the JavaDoc and the
 * required import all fall out of the normal parameter machinery either way.
 */
class ServiceExtensions {

    /**
     * Returns the parameter carrying the service a method uses.
     *
     * @param method Method to return the service parameter for.
     *
     * @return Single-element list, or empty if the method references no service.
     */
    def static List<Parameter> serviceParameters(Method method) {
        return method.service.asServiceParameters
    }

    /**
     * Returns the parameter carrying the service a constructor uses.
     *
     * @param constructor Constructor to return the service parameter for.
     *
     * @return Single-element list, or empty if the constructor references no service.
     */
    def static List<Parameter> serviceParameters(Constructor constructor) {
        return constructor.service.asServiceParameters
    }

    /**
     * Wraps the service in a parameter named after it.
     *
     * @param service Service to wrap, may be <code>null</code>.
     *
     * @return Single-element list, or empty if there is no service.
     */
    private def static List<Parameter> asServiceParameters(Service service) {
        val List<Parameter> list = new ArrayList<Parameter>()
        if (service !== null) {
            // The convention is one service per operation, named "<Operation>Service", so the type's
            // own name in lower camel case is both unique and self-explaining: "createService".
            list.add(eINSTANCE.createParameter(service.doc, service, service.name.toFirstLower, false))
        }
        return list
    }

}

package org.fuin.dsl.ddd.gen.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.cqrsDsl.Variable

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Provides extension methods for Event.
 */
class EventExtensions {

    /**
     * Returns the variables the generated event actually carries - its fields, its constructor
     * arguments and its builder setters.
     * <p>
     * For an ordinary event those are its own attributes. An event declared
     * <code>copies-attributes-of</code> an operation instead takes that operation's parameters, so
     * the fact carries exactly what the operation was given; attributes declared on such an event are
     * <b>not</b> added to them.
     *
     * @param event Event to return the variables for.
     *
     * @return Attributes or, for a "copies-attributes-of" event, the origin's parameters.
     */
    def static List<Variable> eventVariables(Event event) {
        val List<Variable> list = new ArrayList<Variable>()
        if (event.origin === null) {
            list.addAll(event.attributes.nullSafe)
        } else {
            list.addAll(event.origin.parameters.nullSafe)
        }
        return list
    }

}

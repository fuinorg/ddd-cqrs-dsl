package org.fuin.dsl.ddd.gen.extensions

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.cqrsDsl.Variable

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Provides extension methods for Event and Command.
 */
class EventExtensions {

    /**
     * Returns the variables the generated event actually carries - its fields, its constructor
     * arguments and its builder setters.
     * <p>
     * For an ordinary event those are its own attributes. An event declared
     * <code>copies-attributes-of</code> an operation takes that operation's parameters <b>and</b> its
     * own declared attributes, in that order: the fact carries what the operation was given plus
     * whatever context the model added to it - the previous name on a rename, for example.
     * <p>
     * The two were mutually exclusive until it turned out that the attributes were being dropped
     * silently while the event's <code>message</code> template still referred to them, so the message
     * was rendered with a value nobody had supplied. A name declared on both sides is not an error:
     * the operation's parameter wins, because that is the one the caller passes.
     *
     * @param event Event to return the variables for.
     *
     * @return The origin's parameters, if any, followed by the event's own attributes.
     */
    def static List<Variable> eventVariables(Event event) {
        val List<Variable> list = new ArrayList<Variable>()
        if (event.origin !== null) {
            list.addAll(event.origin.parameters.nullSafe)
        }
        addAllNotAlreadyNamed(list, event.attributes.nullSafe)
        return list
    }

    /**
     * Adds every variable whose name is not taken yet, so a declared attribute cannot shadow the
     * operation parameter it shares a name with.
     *
     * @param list List to add to.
     * @param candidates Variables to add.
     */
    private def static void addAllNotAlreadyNamed(List<Variable> list, Iterable<? extends Variable> candidates) {
        for (candidate : candidates) {
            if (!list.exists[it.name == candidate.name]) {
                list.add(candidate)
            }
        }
    }

    /**
     * Returns the variables the generated command actually carries, on the same terms as
     * {@link #eventVariables(Event)}: the targeted operation's parameters, if it targets one,
     * followed by the command's own declared attributes.
     *
     * @param command Command to return the variables for.
     *
     * @return The target's parameters, if any, followed by the command's own attributes.
     */
    def static List<Variable> commandVariables(Command command) {
        val List<Variable> list = new ArrayList<Variable>()
        if (command.target !== null) {
            list.addAll(command.target.parameters.nullSafe)
        }
        addAllNotAlreadyNamed(list, command.attributes.nullSafe)
        return list
    }

}

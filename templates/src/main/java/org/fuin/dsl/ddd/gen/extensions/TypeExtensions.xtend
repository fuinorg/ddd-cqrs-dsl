package org.fuin.dsl.ddd.gen.extensions

import org.fuin.dsl.cqrs.cqrsDsl.Type
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.dsl.ddd.gen.base.TypeKeys

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*

/**
 * Provides extension methods for Type.
 */
class TypeExtensions {

    /**
     * Returns the last part of the name.
     * 
     * @param type Type.
     * @param ctx Context.
     * 
     * @return Simple name (Name without package).
     */
    def static String simpleName(Type type, CodeSnippetContext ctx) {
        val String name = ctx.getReference(TypeKeys.refKey(type))
        val int p = name.lastIndexOf('.')
        if (p == -1) {
            return name
        }
        return name.substring(p + 1)
    }

    /**
     * Converts an aggregate or entity name into the constant used as its {@code EntityType}, by splitting
     * on the camel-case boundaries and upper-casing: {@code Tenant} becomes {@code TENANT} and
     * {@code JournalEntry} becomes {@code JOURNAL_ENTRY}.
     * <p>
     * The upper case is what makes the type readable as a type: written the same way as the class, there
     * is no telling at a glance whether {@code Tenant} means the aggregate or its identifier.
     * <p>
     * <b>The result is part of the storage and wire contract, not a display name.</b> It becomes the
     * event store's stream name ({@code AggregateStreamId.getName()} returns it) and the
     * {@code entity-id-path} carried by every command and event, so changing this convention - or
     * renaming an aggregate - renames its streams and invalidates every stored path.
     *
     * @param name Name of the aggregate or entity.
     *
     * @return Upper case constant with underscores between the words.
     */
    def static String asEntityTypeConstant(String name) {
        return name.replaceAll("(?<!^)(?=[A-Z])", "_").toUpperCase
    }

}

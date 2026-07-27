package org.fuin.dsl.ddd.gen.event

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractEntityExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEventExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.EventExtensions.*
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId
import org.fuin.dsl.ddd.gen.base.SrcBuilderSetters

/**
 * Creates a builder snippet for a event.
 */
class SrcEventBuilder implements CodeSnippet {
	
	val CodeSnippetContext ctx
	val GenerateOptions options
    val Event event

    new(CodeSnippetContext ctx, GenerateOptions options, Event event) {
    	this.ctx = ctx
    	this.options = options
        this.event = event
        if (options.jsonb) {
            ctx.requiresImport("org.fuin.ddd4j.jsonb.AbstractDomainEvent")        
        }
        if (options.jaxb) {
            ctx.requiresImport("org.fuin.ddd4j.jaxb.AbstractDomainEvent")        
        }
        if (options.jackson) {
            ctx.requiresImport("org.fuin.ddd4j.jackson.AbstractDomainEvent")        
        }
        
        ctx.requiresReference(event.entityIdType.uniqueName)
        
    }

    /**
     * Returns the id type the builder is parameterized with. This must be the type the event class
     * itself extends "AbstractDomainEvent" with, so it is the id of the owning <em>entity</em> - for
     * an event fired by a child entity that is not the aggregate root's id.
     */
    def AbstractEntityId getEntityIdType(Event event) {
        if (event.entity === null) {
            return null
        }
        return event.entity.idType
    }

    override toString() {
        var variables = event.eventVariables
        '''
        /**
         * Builds an instance of the outer class.
         */
        public static final class Builder extends AbstractDomainEvent.Builder<«event.entityIdType.name», «event.name», Builder> {

            private «event.name» delegate;

            private Builder() {
                super(new «event.name»());
                delegate = delegate();
            }

            «new SrcBuilderSetters(ctx, options, variables)»

            /**
             * Creates the event and clears the builder.
             *
             * @return New instance.
             */
            public «event.name» build() {
                ensureBuildableAbstractDomainEvent();
                if (delegate.getEventId() == null) {
                    this.eventId(new EventId());
                }
                if (delegate.getEventTimestamp() == null) {
                    this.timestamp(ZonedDateTime.now());
                }
                
            	«FOR variable : variables»
            	«IF variable.optional === null»ensureNotNull("«variable.name»", delegate.«variable.name»);«ENDIF»
            	«ENDFOR»
                
                final «event.name» result = delegate;
                delegate = new «event.name»();
                resetAbstractDomainEvent(delegate);
                return result;
            }

        }
        '''
    }
	
}
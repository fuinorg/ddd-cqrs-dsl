/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package p.command.core.domain.x.aggregates;

import org.fuin.ddd4j.core.AbstractAggregateRoot;
import org.fuin.ddd4j.core.EntityType;
import p.x.aggregates.AggregateFChangedEvent;
import p.x.aggregates.AggregateFCreatedEvent;
import p.x.aggregates.AggregateFId;
import p.x.aggregates.AnyConstraintViolatedException;
import p.x.aggregates.SharedService;

/**
 * Aggregate F - References the service declared outside the operation. It becomes the same kind of trailing parameter as an inline one, but is imported as the top-level type it is.
 */
public abstract class AbstractAggregateF extends AbstractAggregateRoot<AggregateFId> {

    @SuppressWarnings("NullAway.Init")
    private AggregateFId id;

    @Override
    public final EntityType getType() {
        return AggregateFId.TYPE;
    }

    @Override
    public final AggregateFId getId() {
        return id;
    }

    /**
     * Sets the aggregate identifier. Called from the event handler that brings the
     * aggregate into existence, which also runs when it is replayed from past events,
     * so this must never throw.
     *
     * @param id Unique aggregate identifier.
     */
    protected final void setId(final AggregateFId id) {
        this.id = id;
    }

    /**
     * Handles: AggregateFCreatedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleAggregateFCreatedEvent(final AggregateFCreatedEvent event);
    
    /**
     * Handles: AggregateFChangedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleAggregateFChangedEvent(final AggregateFChangedEvent event);
    
    /**
     * Changes something.
     *
     * @param a Variable A.
     * @param sharedService A service declared outside any operation, so it becomes a top-level interface of its own - unlike an inline one, which is nested in the aggregate that declares it.
     *
     * @throws AnyConstraintViolatedException The constraint was violated.
     */
    public abstract void change(final String a, final SharedService sharedService) throws AnyConstraintViolatedException;
    
}

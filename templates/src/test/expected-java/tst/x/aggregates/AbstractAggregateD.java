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
import p.x.aggregates.AggregateDChangedEvent;
import p.x.aggregates.AggregateDId;
import p.x.aggregates.AggregateDRenamedEvent;

/**
 * Aggregate D - Fires an event declared outside the method.
 */
public abstract class AbstractAggregateD extends AbstractAggregateRoot<AggregateDId> {

    @SuppressWarnings("NullAway.Init")
    private AggregateDId id;

    @Override
    public final EntityType getType() {
        return AggregateDId.TYPE;
    }

    @Override
    public final AggregateDId getId() {
        return id;
    }

    /**
     * Sets the aggregate identifier. Called from the event handler that brings the
     * aggregate into existence, which also runs when it is replayed from past events,
     * so this must never throw.
     *
     * @param id Unique aggregate identifier.
     */
    protected final void setId(final AggregateDId id) {
        this.id = id;
    }

    /**
     * Handles: AggregateDChangedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleAggregateDChangedEvent(final AggregateDChangedEvent event);
    
    /**
     * Handles: AggregateDRenamedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleAggregateDRenamedEvent(final AggregateDRenamedEvent event);
    
    /**
     * Changes something.
     *
     * @param a Variable A.
     */
    public abstract void change(final String a);
    
    /**
     * Renames something.
     *
     * @param newName The new name.
     */
    public abstract void rename(final String newName);
    
}

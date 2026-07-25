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

import org.fuin.ddd4j.core.ApplyEvent;
import org.fuin.objects4j.common.Contract;
import p.x.aggregates.AbstractAggregateD;
import p.x.aggregates.AggregateDChangedEvent;
import p.x.aggregates.AggregateDRenamedEvent;

/**
 * Aggregate D - Fires an event declared outside the method.
 */
public final class AggregateD extends AbstractAggregateD {

    /**
     * Default constructor for loading the aggregate root from history. 
     */
    public AggregateD() {
        super();
    }

    /**
     * Changes something.
     *
     * @param a Variable A.
     */
    public final void change(final String a) {
        // Check preconditions
        Contract.requireArgNotNull("a", a);
        
        // Verify business constraints
        // None declared for this operation.
        
        // Apply events
        // TODO apply(AggregateDChangedEvent.builder()
        //     .entityIdPath(getId())
        //     .aggregateVersion(getNextApplyVersion())
        //     .a(a)
        //     .build());
    }
    
    /**
     * Renames something.
     *
     * @param newName The new name.
     */
    public final void rename(final String newName) {
        // Check preconditions
        Contract.requireArgNotNull("newName", newName);
        
        // Verify business constraints
        // None declared for this operation.
        
        // Apply events
        // TODO apply(AggregateDRenamedEvent.builder()
        //     .entityIdPath(getId())
        //     .aggregateVersion(getNextApplyVersion())
        //     .newName(newName)
        //     .build());
    }
    
    /**
     * Handles: AggregateDChangedEvent.
     *
     * @param event Event to handle.
     */
    @Override
    @ApplyEvent
    protected final void handleAggregateDChangedEvent(final AggregateDChangedEvent event) {
        // TODO Handle event!
    }
    
    /**
     * Handles: AggregateDRenamedEvent.
     *
     * @param event Event to handle.
     */
    @Override
    @ApplyEvent
    protected final void handleAggregateDRenamedEvent(final AggregateDRenamedEvent event) {
        // TODO Handle event!
    }
    
}

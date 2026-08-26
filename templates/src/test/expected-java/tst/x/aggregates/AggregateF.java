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
import p.shared.domain.x.aggregates.AggregateFChangedEvent;
import p.shared.domain.x.aggregates.AggregateFCreatedEvent;
import p.shared.domain.x.aggregates.AggregateFId;
import p.shared.domain.x.aggregates.AnyConstraintViolatedException;

/**
 * Aggregate F - References the service declared outside the operation. It becomes the same kind of trailing parameter as an inline one, but is imported as the top-level type it is.
 */
public final class AggregateF extends AbstractAggregateF {

    /**
     * Default constructor for loading the aggregate root from history. 
     */
    public AggregateF() {
        super();
    }

    /**
     * Creates the entity.
     *
     * @param id Unique aggregate identifier, as sent by the command.
     * @param a Variable A.
     * @param sharedService A service declared outside any operation, so it becomes a top-level interface of its own - unlike an inline one, which is nested in the aggregate that declares it.
     *
     * @throws AnyConstraintViolatedException The constraint was violated.
     */
    public AggregateF(final AggregateFId id, final String a, final SharedService sharedService) throws AnyConstraintViolatedException {
        super(id);

        // Check preconditions
        Contract.requireArgNotNull("a", a);
        
        // Verify business constraints
        // TODO Verify "AnyConstraint" and throw AnyConstraintViolatedException if it is violated.
        
        // Apply events
        // TODO apply(AggregateFCreatedEvent.builder()
        //     .entityIdPath(getId())
        //     .aggregateVersion(getNextApplyVersion())
        //     .a(a)
        //     .build());
    }

    /**
     * Changes something.
     *
     * @param a Variable A.
     * @param sharedService A service declared outside any operation, so it becomes a top-level interface of its own - unlike an inline one, which is nested in the aggregate that declares it.
     *
     * @throws AnyConstraintViolatedException The constraint was violated.
     */
    public final void change(final String a, final SharedService sharedService) throws AnyConstraintViolatedException {
        // Check preconditions
        Contract.requireArgNotNull("a", a);
        
        // Verify business constraints
        // TODO Verify "AnyConstraint" and throw AnyConstraintViolatedException if it is violated.
        
        // Apply events
        // TODO apply(AggregateFChangedEvent.builder()
        //     .entityIdPath(getId())
        //     .aggregateVersion(getNextApplyVersion())
        //     .a(a)
        //     .build());
    }
    
    /**
     * Handles: AggregateFCreatedEvent.
     *
     * @param event Event to handle.
     */
    @Override
    @ApplyEvent
    protected final void handleAggregateFCreatedEvent(final AggregateFCreatedEvent event) {
        // TODO Handle event!
    }
    
    /**
     * Handles: AggregateFChangedEvent.
     *
     * @param event Event to handle.
     */
    @Override
    @ApplyEvent
    protected final void handleAggregateFChangedEvent(final AggregateFChangedEvent event) {
        // TODO Handle event!
    }
    
}

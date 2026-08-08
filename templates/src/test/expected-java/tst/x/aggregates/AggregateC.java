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
import p.shared.domain.x.aggregates.AggregateCCreatedEvent;
import p.shared.domain.x.aggregates.AggregateCId;
import p.shared.domain.x.aggregates.AnyConstraintViolatedException;

/**
 * Aggregate C - With constructor, constraint and event.
 */
public final class AggregateC extends AbstractAggregateC {

    /**
     * Default constructor for loading the aggregate root from history. 
     */
    public AggregateC() {
        super();
    }

    /**
     * Creates the entity.
     *
     * @param id Unique aggregate identifier, as sent by the command.
     * @param a Variable A.
     * @param b Variable B.
     *
     * @throws AnyConstraintViolatedException The constraint was violated.
     */
    public AggregateC(final AggregateCId id, final String a, final int b) throws AnyConstraintViolatedException {
        super(id);

        // Check preconditions
        Contract.requireArgNotNull("a", a);
        Contract.requireArgNotNull("b", b);
        
        // Verify business constraints
        // TODO Verify "AnyConstraint" and throw AnyConstraintViolatedException if it is violated.
        
        // Apply events
        // TODO apply(AggregateCCreatedEvent.builder()
        //     .entityIdPath(getId())
        //     .aggregateVersion(getNextApplyVersion())
        //     .a(a)
        //     .b(b)
        //     .build());
    }

    /**
     * Handles: AggregateCCreatedEvent.
     *
     * @param event Event to handle.
     */
    @Override
    @ApplyEvent
    protected final void handleAggregateCCreatedEvent(final AggregateCCreatedEvent event) {
        // TODO Handle event!
    }
    
}

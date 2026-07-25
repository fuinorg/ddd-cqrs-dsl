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
import p.x.aggregates.AbstractAggregateE;
import p.x.aggregates.AggregateEChangedEvent;
import p.x.aggregates.AggregateECreatedEvent;
import p.x.aggregates.AggregateEId;
import p.x.aggregates.AnyConstraintViolatedException;

/**
 * Aggregate E - A constructor and a method that each reference a service. The referenced service is the collaborator the operation needs to verify its rule, and becomes a trailing parameter; the inline declaration itself is only the nested interface.
 */
public final class AggregateE extends AbstractAggregateE {

    /**
     * Default constructor for loading the aggregate root from history. 
     */
    public AggregateE() {
        super();
    }

    /**
     * Creates the entity.
     *
     * @param id Unique aggregate identifier, as sent by the command.
     * @param a Variable A.
     * @param createService Verifies the value is still free.
     *
     * @throws AnyConstraintViolatedException The constraint was violated.
     */
    public AggregateE(final AggregateEId id, final String a, final CreateService createService) throws AnyConstraintViolatedException {
        super(id);

        // Check preconditions
        Contract.requireArgNotNull("a", a);
        
        // Verify business constraints
        // TODO Verify "AnyConstraint" and throw AnyConstraintViolatedException if it is violated.
        
        // Apply events
        // TODO apply(AggregateECreatedEvent.builder()
        //     .entityIdPath(getId())
        //     .aggregateVersion(getNextApplyVersion())
        //     .a(a)
        //     .build());
    }

    /**
     * Changes something.
     *
     * @param a Variable A.
     * @param changeService Verifies the new value is still free.
     *
     * @throws AnyConstraintViolatedException The constraint was violated.
     */
    public final void change(final String a, final ChangeService changeService) throws AnyConstraintViolatedException {
        // Check preconditions
        Contract.requireArgNotNull("a", a);
        
        // Verify business constraints
        // TODO Verify "AnyConstraint" and throw AnyConstraintViolatedException if it is violated.
        
        // Apply events
        // TODO apply(AggregateEChangedEvent.builder()
        //     .entityIdPath(getId())
        //     .aggregateVersion(getNextApplyVersion())
        //     .a(a)
        //     .build());
    }
    
    /**
     * Handles: AggregateECreatedEvent.
     *
     * @param event Event to handle.
     */
    @Override
    @ApplyEvent
    protected final void handleAggregateECreatedEvent(final AggregateECreatedEvent event) {
        // TODO Handle event!
    }
    
    /**
     * Handles: AggregateEChangedEvent.
     *
     * @param event Event to handle.
     */
    @Override
    @ApplyEvent
    protected final void handleAggregateEChangedEvent(final AggregateEChangedEvent event) {
        // TODO Handle event!
    }
    
}

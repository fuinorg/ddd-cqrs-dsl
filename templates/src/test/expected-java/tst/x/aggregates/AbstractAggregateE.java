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
import org.fuin.objects4j.common.Contract;
import p.shared.domain.x.aggregates.AggregateEChangedEvent;
import p.shared.domain.x.aggregates.AggregateECreatedEvent;
import p.shared.domain.x.aggregates.AggregateEId;
import p.shared.domain.x.aggregates.AnyConstraintViolatedException;

/**
 * Aggregate E - A constructor and a method that each reference a service. The referenced service is the collaborator the operation needs to verify its rule, and becomes a trailing parameter; the inline declaration itself is only the nested interface.
 */
public abstract class AbstractAggregateE extends AbstractAggregateRoot<AggregateEId> {

    @SuppressWarnings("NullAway.Init")
    private AggregateEId id;

    /**
     * Default constructor for loading the aggregate from its history. The identity comes
     * from the event that created it (see setId below).
     */
    @SuppressWarnings("NullAway.Init")
    protected AbstractAggregateE() {
        super();
    }

    /**
     * Constructor with the identity, used when the aggregate is created. Having it up front
     * means every operation of the final class can rely on getId(), including the
     * constructor that is still applying the event which brings the aggregate into being.
     *
     * @param id Unique aggregate identifier.
     */
    protected AbstractAggregateE(final AggregateEId id) {
        super();
        // Checked here because a "super(id)" has to be the first statement of the creating
        // constructor, leaving it no place to check the identity it passes on.
        Contract.requireArgNotNull("id", id);
        this.id = id;
    }

    @Override
    public final EntityType getType() {
        return AggregateEId.TYPE;
    }

    @Override
    public final AggregateEId getId() {
        return id;
    }

    /**
     * Sets the aggregate identifier. Called from the event handler that brings the
     * aggregate into existence, which also runs when it is replayed from past events,
     * so this must never throw.
     *
     * @param id Unique aggregate identifier.
     */
    protected final void setId(final AggregateEId id) {
        this.id = id;
    }

    /**
     * Handles: AggregateECreatedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleAggregateECreatedEvent(final AggregateECreatedEvent event);
    
    /**
     * Handles: AggregateEChangedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleAggregateEChangedEvent(final AggregateEChangedEvent event);
    
    /**
     * Verifies the value is still free.
     */
    public interface CreateService {
        
        /**
         * Returns how many others already use the value.
         *
         * @param a Variable A.
         *
         * @return Number of other users.
         */
        public Integer countUsages(final String a);
        
    }
    
    /**
     * Verifies the new value is still free.
     */
    public interface ChangeService {
        
        /**
         * Returns how many others already use the value.
         *
         * @param a Variable A.
         *
         * @return Number of other users.
         */
        public Integer countUsages(final String a);
        
    }
    
    /**
     * Changes something.
     *
     * @param a Variable A.
     * @param changeService Verifies the new value is still free.
     *
     * @throws AnyConstraintViolatedException The constraint was violated.
     */
    public abstract void change(final String a, final ChangeService changeService) throws AnyConstraintViolatedException;
    
}

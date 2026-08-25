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
import p.shared.domain.x.aggregates.AggregateCCreatedEvent;
import p.shared.domain.x.aggregates.AggregateCId;

/**
 * Aggregate C - With constructor, constraint and event.
 */
// Everything the model declares is left unset by the constructors on purpose: an
// aggregate's state comes from the event that created it, applied a moment later and again
// on every replay, so there is nothing for a constructor to put there.
@SuppressWarnings("NullAway.Init")
public abstract class AbstractAggregateC extends AbstractAggregateRoot<AggregateCId> {

    @SuppressWarnings("NullAway.Init")
    private AggregateCId id;

    private String a;
    
    private int b;
    
    /**
     * Default constructor for loading the aggregate from its history. The identity comes
     * from the event that created it (see setId below).
     */
    @SuppressWarnings("NullAway.Init")
    protected AbstractAggregateC() {
        super();
    }

    /**
     * Constructor with the identity, used when the aggregate is created. Having it up front
     * means every operation of the final class can rely on getId(), including the
     * constructor that is still applying the event which brings the aggregate into being.
     *
     * @param id Unique aggregate identifier.
     */
    protected AbstractAggregateC(final AggregateCId id) {
        super();
        // Checked here because a "super(id)" has to be the first statement of the creating
        // constructor, leaving it no place to check the identity it passes on.
        Contract.requireArgNotNull("id", id);
        this.id = id;
    }

    @Override
    public final EntityType getType() {
        return AggregateCId.TYPE;
    }

    @Override
    public final AggregateCId getId() {
        return id;
    }

    /**
     * Sets the aggregate identifier. Called from the event handler that brings the
     * aggregate into existence, which also runs when it is replayed from past events,
     * so this must never throw.
     *
     * @param id Unique aggregate identifier.
     */
    protected final void setId(final AggregateCId id) {
        this.id = id;
    }

    /**
     * Returns: Variable A.
     *
     * @return Current value.
     */
    public final String getA() {
        return a;
    }
    
    /**
     * Returns: Variable B.
     *
     * @return Current value.
     */
    public final int getB() {
        return b;
    }
    
    /**
     * Sets: Variable A.
     *
     * @param a Value to set.
     */
    protected final void setA(final String a) {
        Contract.requireArgNotNull("a", a);
        this.a = a;
    }
    
    /**
     * Sets: Variable B.
     *
     * @param b Value to set.
     */
    protected final void setB(final int b) {
        this.b = b;
    }
    
    /**
     * Handles: AggregateCCreatedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleAggregateCCreatedEvent(final AggregateCCreatedEvent event);
    
}

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
import p.shared.domain.x.aggregates.AggregateAId;

/**
 * Aggregate A.
 */
public abstract class AbstractAggregateA extends AbstractAggregateRoot<AggregateAId> {

    @SuppressWarnings("NullAway.Init")
    private AggregateAId id;

    /**
     * Default constructor for loading the aggregate from its history. The identity comes
     * from the event that created it (see setId below).
     */
    @SuppressWarnings("NullAway.Init")
    protected AbstractAggregateA() {
        super();
    }

    /**
     * Constructor with the identity, used when the aggregate is created. Having it up front
     * means every operation of the final class can rely on getId(), including the
     * constructor that is still applying the event which brings the aggregate into being.
     *
     * @param id Unique aggregate identifier.
     */
    protected AbstractAggregateA(final AggregateAId id) {
        super();
        // Checked here because a "super(id)" has to be the first statement of the creating
        // constructor, leaving it no place to check the identity it passes on.
        Contract.requireArgNotNull("id", id);
        this.id = id;
    }

    @Override
    public final EntityType getType() {
        return AggregateAId.TYPE;
    }

    @Override
    public final AggregateAId getId() {
        return id;
    }

    /**
     * Sets the aggregate identifier. Called from the event handler that brings the
     * aggregate into existence, which also runs when it is replayed from past events,
     * so this must never throw.
     *
     * @param id Unique aggregate identifier.
     */
    protected final void setId(final AggregateAId id) {
        this.id = id;
    }

}

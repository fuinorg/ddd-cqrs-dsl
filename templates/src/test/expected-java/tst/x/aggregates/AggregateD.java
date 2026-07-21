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
import p.x.aggregates.AbstractAggregateD;
import p.x.aggregates.AggregateDChangedEvent;

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
        // TODO Implement!
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
    
}

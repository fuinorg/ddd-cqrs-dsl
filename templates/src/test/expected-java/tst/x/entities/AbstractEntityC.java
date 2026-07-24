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
package p.command.core.domain.x.entities;

import org.fuin.ddd4j.core.AbstractEntity;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.objects4j.common.Contract;
import p.x.entities.AggregateX;
import p.x.entities.AggregateXId;
import p.x.entities.EntityCId;

/**
 * Entity C - With constructor, constraint and event.
 */
public abstract class AbstractEntityC extends AbstractEntity<AggregateXId, AggregateX, EntityCId> {

    private EntityCId id;

    /**
     * Constructor with mandatory data.
     *
     * @param rootAggregate The root aggregate of this entity.
     * @param id Unique entity identifier.
     */
    protected AbstractEntityC(final AggregateX rootAggregate, final EntityCId id) {
        super(rootAggregate);
        Contract.requireArgNotNull("id", id);
        
        this.id = id;
    }
    
    @Override
    public final EntityType getType() {
        return EntityCId.TYPE;
    }

    @Override
    public final EntityCId getId() {
        return id;
    }

    /**
     * Handles: EntityCCreatedEvent.
     *
     * @param event Event to handle.
     */
    protected abstract void handleEntityCCreatedEvent(final EntityCCreatedEvent event);
    
}

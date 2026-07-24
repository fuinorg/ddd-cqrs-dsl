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
import p.x.entities.EntityBId;

/**
 * Entity B - With variables.
 */
public abstract class AbstractEntityB extends AbstractEntity<AggregateXId, AggregateX, EntityBId> {

    private EntityBId id;

    /**
     * Constructor with mandatory data.
     *
     * @param rootAggregate The root aggregate of this entity.
     * @param id Unique entity identifier.
     */
    protected AbstractEntityB(final AggregateX rootAggregate, final EntityBId id) {
        super(rootAggregate);
        Contract.requireArgNotNull("id", id);
        
        this.id = id;
    }
    
    @Override
    public final EntityType getType() {
        return EntityBId.TYPE;
    }

    @Override
    public final EntityBId getId() {
        return id;
    }

}

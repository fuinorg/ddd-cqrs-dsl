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
package p.shared.domain.x.entityid;

import java.io.Serial;
import org.fuin.ddd4j.core.EntityId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ValueObject;
import org.fuin.objects4j.core.AbstractStringValueObject;

/**
 * Entity ID single attribute and base.
 */
public abstract class AbstractMyEntityId extends AbstractStringValueObject implements EntityId, ValueObject {

    @Serial
    private static final long serialVersionUID = 1000L;
    
    private String value;
    
    /**
     * Default constructor.
     */
    @SuppressWarnings("NullAway.Init")
    protected AbstractMyEntityId() {
        super();
    }
    
    /**
     * Constructor with all data.
     *
     * @param value Persistent value.
     */
    public AbstractMyEntityId(final String value) {
        super();
        Contract.requireArgNotNull("value", value);
        
        this.value = value;
    }
    
    /**
     * Returns: Persistent value.
     *
     * @return Current value.
     */
    public final String getValue() {
        return value;
    }
    
    /** Name that identifies the entity uniquely within the context. */    
    public static final EntityType TYPE = new StringBasedEntityType("MY_ENTITY");
    
    @Override
    public final EntityType getType() {
        return TYPE;
    }
    
    @Override
    public final String asTypedString() {
        return TYPE + " " + asString();
    }
    
}

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
package p.x.ev;

import java.io.Serial;
import java.util.UUID;


import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.AbstractUuidValueObject;
import org.fuin.objects4j.core.UUIDStr;
import org.fuin.objects4j.core.UUIDStrValidator;

public final class CustomerId extends AbstractUuidValueObject implements AggregateRootId {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Name that identifies the entity uniquely within the context. */
    public static final EntityType TYPE = new StringBasedEntityType(
            "CustomerId");

    private final UUID uuid;
    
    /**
     * Default constructor.
     */
    public CustomerId() {
        super();
        uuid = UUID.randomUUID();
    }

    /**
     * Constructor with all data.
     * 
     * @param value
     *            Persistent value.
     */
    public CustomerId(final UUID value) {
        super();
        Contract.requireArgNotNull("value", value);
        this.uuid = value;        
    }

    /**
     * Constructor with all data.
     * 
     * @param strValue
     *            String value.
     */
    public CustomerId(@UUIDStr final String strValue) {
        super();
        Contract.requireArgNotNull("strValue", strValue);
        if (!UUIDStrValidator.isValid(strValue)) {
            throw new ConstraintViolationException("The argument 'strValue' is not a valid UUID");
        }
        this.uuid = UUID.fromString(strValue);
    }
    
    @Override
    public final EntityType getType() {
        return TYPE;
    }

    @Override
    public final String asTypedString() {
        return TYPE + " " + asString();
    }
    
    /**
     * The id as it reads, which is what an exception message interpolating it wants. Without this
     * the base class leaves Object's version in place and a refusal names the thing it refused as
     * "SomethingId@1f3a2b" - a value object of the same shape already carries one.
     */
    @Override
    public final String toString() {
        return asString();
    }

    @Override
    public final UUID asBaseType() {
        return uuid;
    }
    
    @Override
    public String asString() {
        return uuid.toString();
    }
    
}

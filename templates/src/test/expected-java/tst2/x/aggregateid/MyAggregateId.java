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
package p.shared.domain.x.aggregateid;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import javax.annotation.concurrent.Immutable;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ValueObject;
import org.fuin.objects4j.core.AbstractStringValueObject;
import org.jspecify.annotations.Nullable;

/**
 * Aggregate ID single attribute and base.
 */
@Immutable
@XmlJavaTypeAdapter(MyAggregateIdConverter.class)
public final class MyAggregateId extends AbstractStringValueObject implements AggregateRootId, ValueObject {

@Serial
private static final long serialVersionUID = 1000L;

    private String value;
    
    /**
     * Default constructor.
     */
    @SuppressWarnings("NullAway.Init")
    protected MyAggregateId() {
        super();
    }
    
    /**
     * Constructor with all data.
     *
     * @param value Persistent value.
     */
    public MyAggregateId(final String value) {
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
    public static final EntityType TYPE = new StringBasedEntityType("MY_AGGREGATE");
    
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
    public final String asBaseType() {
        return getValue();
    }
    
    /**
     * Returns the information if a given string can be converted into
     * an instance of MyAggregateId. A <code>null</code> value returns <code>true</code>.
     * 
     * @param value
     *            Value to check.
     * 
     * @return TRUE if it's a valid string, else FALSE.
     */
    public static boolean isValid(@Nullable final String value) {
        if (value == null) {
            return true;
        }
        // TODO Verify the value is valid!
        return true;
    }
    
    /**
     * Parses a given string and returns a new instance of MyAggregateId.
     * 
     * @param value
     *            Value to convert. A <code>null</code> value returns
     *            <code>null</code>.
     * 
     * @return Converted value.
     */
    @Nullable
    public static MyAggregateId valueOf(@Nullable final String value) {
        if (value == null) {
            return null;
        }
        // TODO Parse string value and return new instance! 
        // return new MyAggregateId(value);
        return null;
    }
    
}

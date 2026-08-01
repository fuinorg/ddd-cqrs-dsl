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
package p.shared.domain.x.valueobject;

import java.io.Serial;
import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

/**
 * Value object with a decimal base, which has no abstract base class in objects4j.
 */
public final class MyDecimalValueObject extends AbstractMyDecimalValueObject {

    @Serial
    private static final long serialVersionUID = 1000L;
    
    /**
     * Default constructor.
     */
    @SuppressWarnings("NullAway.Init")
    protected MyDecimalValueObject() {
        super();
    }
    
    /**
     * Constructor with all data.
     *
     * @param value Persistent value.
     */
    public MyDecimalValueObject(final BigDecimal value) {
        super(value);
    }
    
    @Override
    public final BigDecimal asBaseType() {
        return getValue();
    }
    
    @Override
    public final Class<BigDecimal> getBaseType() {
        return BigDecimal.class;
    }
    
    @Override
    public final int hashCode() {
        return asBaseType().hashCode();
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MyDecimalValueObject other = (MyDecimalValueObject) obj;
        return asBaseType().equals(other.asBaseType());
    }
    
    /**
     * Returns the information if a given BigDecimal can be converted into
     * an instance of MyDecimalValueObject. A <code>null</code> value returns <code>true</code>.
     *
     * @param value
     *            Value to check.
     *
     * @return TRUE if it's a valid BigDecimal, else FALSE.
     */
    public static boolean isValid(@Nullable final BigDecimal value) {
        // Unlike a string, a BigDecimal instance cannot be malformed - it was already parsed
        // when it was created. The method exists so a value object with a decimal base offers
        // the same contract as one with a string or an integral base.
        return true;
    }
    
    /**
     * Parses a given BigDecimal and returns a new instance of MyDecimalValueObject.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns
     *            <code>null</code>.
     *
     * @return Converted value.
     */
    @Nullable
    public static MyDecimalValueObject valueOf(@Nullable final BigDecimal value) {
        if (value == null) {
            return null;
        }
        return new MyDecimalValueObject(value);
    }
    
    /**
     * Parses a given String and returns a new instance of MyDecimalValueObject.
     *
     * @param value
     *            Value to convert. A <code>null</code> value returns
     *            <code>null</code>.
     *
     * @return Converted value.
     *
     * @throws NumberFormatException The value is not a valid decimal number.
     */
    @Nullable
    public static MyDecimalValueObject valueOf(@Nullable final String value) {
        if (value == null) {
            return null;
        }
        return new MyDecimalValueObject(new BigDecimal(value));
    }
    
}

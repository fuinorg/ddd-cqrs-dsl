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
import java.util.regex.Pattern;
import javax.annotation.concurrent.Immutable;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.jspecify.annotations.Nullable;
import p.x.entityid.AbstractMy4EntityId;

/**
 * Entity ID multiple attribute and without base.
 */
@Immutable
@HasEntityTypeConstant
@HasPublicStaticIsValidMethod
@HasPublicStaticValueOfMethod
public final class MyEntity4Id extends AbstractMyEntity4Id {

    @Serial
    private static final long serialVersionUID = 1000L;
    /** Separates the parts in the string form of this identifier. */
    public static final String SEPARATOR = "-";
    
    /**
     * Default constructor.
     */
    @SuppressWarnings("NullAway.Init")
    protected MyEntity4Id() {
        super();
    }
    
    /**
     * Constructor with all data.
     *
     * @param a Persistent value A.
     * @param b Persistent value B.
     */
    public MyEntity4Id(final String a, final String b) {
        super(a, b);
    }
    
    @Override
    public final String asString() {
        // TODO Implement!
        return null;
    }

    /**
     * Converts the string form produced by {@link #asString()} back into an identifier.
     *
     * @param value String to convert. A {@literal null} value returns {@literal null}.
     *
     * @return Converted value.
     *
     * @throws IllegalArgumentException The value is no valid MyEntity4Id. Refusing loudly is the point:
     *                                  a converter that answered null would turn a bad string into a
     *                                  {@code NullPointerException} somewhere else entirely.
     */
    @Nullable
    public static MyEntity4Id valueOf(@Nullable final String value) {
        if (value == null) {
            return null;
        }
        final String[] parts = value.split(Pattern.quote(SEPARATOR), 2);
        if (!validParts(parts)) {
            throw new IllegalArgumentException("Not a valid MyEntity4Id: " + value);
        }
        // Every part was checked above, so none of these conversions can fail.
        return new MyEntity4Id(parts[0], parts[1]);
    }
    
    /**
     * Verifies that a given string can be converted into the type.
     *
     * @param value Value to validate.
     *
     * @return Returns {@literal true} if it's a valid type else {@literal false}.
     */
    public static boolean isValid(@Nullable final String value) {
        if (value == null) {
            return true;
        }
        return validParts(value.split(Pattern.quote(SEPARATOR), 2));
    }
    
    /**
     * Says whether the parts of a string form can all be converted. Every check is non-throwing, which
     * is what lets {@link #isValid(String)} answer without catching anything.
     *
     * @param parts Parts to check.
     *
     * @return TRUE if the parts make up a valid MyEntity4Id.
     */
    private static boolean validParts(final String[] parts) {
        if (parts.length != 2) {
            return false;
        }
        return parts[0] != null
            && parts[1] != null;
    }
}

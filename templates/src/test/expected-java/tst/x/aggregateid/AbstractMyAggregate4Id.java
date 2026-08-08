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

import java.io.Serial;
import java.util.Objects;
import java.util.regex.Pattern;
import org.fuin.ddd4j.core.AggregateRootId;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.ValueObject;
import org.jspecify.annotations.Nullable;

/**
 * Aggregate ID multiple attribute and without base.
 */
public abstract class AbstractMyAggregate4Id implements AggregateRootId, ValueObject {

    @Serial
    private static final long serialVersionUID = 1000L;
    
    private String a;
    
    private String b;
    
    /**
     * Default constructor.
     */
    @SuppressWarnings("NullAway.Init")
    protected AbstractMyAggregate4Id() {
        super();
    }
    
    /**
     * Constructor with all data.
     *
     * @param a Persistent value A.
     * @param b Persistent value B.
     */
    public AbstractMyAggregate4Id(final String a, final String b) {
        super();
        Contract.requireArgNotNull("a", a);
        Contract.requireArgNotNull("b", b);
        
        this.a = a;
        this.b = b;
    }
    
    /**
     * Returns: Persistent value A.
     *
     * @return Current value.
     */
    public final String getA() {
        return a;
    }
    
    /**
     * Returns: Persistent value B.
     *
     * @return Current value.
     */
    public final String getB() {
        return b;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hash(a, b);
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
        final AbstractMyAggregate4Id other = (AbstractMyAggregate4Id) obj;
        return Objects.equals(a, other.a)
            && Objects.equals(b, other.b);
    }
    
    /**
     * Creates the identifier from its parts - the concrete class passes its own constructor, which is
     * what lets the reading below live here instead of being repeated in every identifier.
     *
     * @param <T> Type that is created.
     */
    @FunctionalInterface
    public interface Factory<T> {
    
        /**
         * Creates the identifier.
         *
         * @param a Part of the identifier.
         * @param b Part of the identifier.
         *
         * @return New instance.
         */
        T create(String a, String b);
    
    }
    
    /**
     * Converts a string form back into an identifier.
     *
     * @param value String to convert. A {@literal null} value returns {@literal null}.
     * @param separator Separator the parts are joined with.
     * @param factory Creates the identifier from the converted parts.
     * @param <T> Type that is created.
     *
     * @return Converted value.
     *
     * @throws IllegalArgumentException The value is no valid MyAggregate4Id. Refusing loudly is the point:
     *                                  a converter that answered null would turn a bad string into a
     *                                  {@code NullPointerException} somewhere else entirely.
     */
    @Nullable
    public static <T> T valueOf(@Nullable final String value, final String separator,
            final Factory<T> factory) {
        if (value == null) {
            return null;
        }
        final String[] parts = split(value, separator);
        if (!validParts(parts)) {
            throw new IllegalArgumentException("Not a valid MyAggregate4Id: " + value);
        }
        // Every part was checked above, so none of these conversions can fail.
        return factory.create(parts[0], parts[1]);
    }
    
    /**
     * Verifies that a given string can be converted into the type.
     *
     * @param value Value to validate.
     * @param separator Separator the parts are joined with.
     *
     * @return Returns {@literal true} if it's a valid type else {@literal false}.
     */
    public static boolean isValid(@Nullable final String value, final String separator) {
        return value == null || validParts(split(value, separator));
    }
    
    /**
     * Splits a string form into its parts. Limited to the number of parts and applied left to right, so
     * only the last one may contain the separator itself - which is what makes a trailing date work.
     *
     * @param value Value to split.
     * @param separator Separator the parts are joined with.
     *
     * @return Parts, as many as the split found.
     */
    private static String[] split(final String value, final String separator) {
        return value.split(Pattern.quote(separator), 2);
    }
    
    /**
     * Says whether the parts can all be converted. Every check is non-throwing, which is what lets
     * {@code isValid} answer without catching anything.
     *
     * @param parts Parts to check.
     *
     * @return TRUE if the parts make up a valid MyAggregate4Id.
     */
    private static boolean validParts(final String[] parts) {
        if (parts.length != 2) {
            return false;
        }
        return !parts[0].isEmpty()
            && !parts[1].isEmpty();
    }
    /** Name that identifies the entity uniquely within the context. */    
    public static final EntityType TYPE = new StringBasedEntityType("MY_AGGREGATE4");
    
    @Override
    public final EntityType getType() {
        return TYPE;
    }
    
    @Override
    public final String asTypedString() {
        return TYPE + " " + asString();
    }
    
}

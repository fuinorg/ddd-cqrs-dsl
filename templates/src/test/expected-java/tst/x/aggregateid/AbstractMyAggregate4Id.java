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
    
    /** Separates the parts in the string form of this identifier. */
    public static final String SEPARATOR = "-";
    
    /** The separator as it appears inside a part, where it stands for itself. */
    private static final String SEPARATOR_ESCAPED = "%2D";
    
    /** The escape character, escaped, so that a part may contain one. */
    private static final String ESCAPE_ESCAPED = "%25";
    
    /** What {@link #unescape(String)} puts back. */
    private static final Pattern ESCAPED = Pattern.compile("%(25|2D)");
    
    /**
     * Returns the parts joined by {@link #SEPARATOR}, which is the form the identifier travels in and
     * the form {@link #valueOf(String, Factory)} reads back.
     *
     * <p>Every part but the last is escaped first. Without that the form is ambiguous whenever a part
     * contains the separator: the split below would cut in the wrong place and hand back a different
     * identifier that reports itself valid. The last part is left alone because the split already
     * lets it carry separators, which is what keeps the form of an identifier ending in a date or a
     * UUID exactly as it has always been.
     *
     * @return String form of this identifier.
     */
    @Override
    public final String asString() {
        return escape(String.valueOf(getA())) + SEPARATOR + getB();
    }
    
    /**
     * Escapes one part so that it cannot be mistaken for two.
     *
     * <p>The escape character goes first, or escaping the separator would then have its own escape
     * escaped in turn.
     *
     * @param value Part to escape.
     *
     * @return The part, with the escape character and the separator percent-escaped.
     */
    private static String escape(final String value) {
        return value.replace("%", ESCAPE_ESCAPED).replace(SEPARATOR, SEPARATOR_ESCAPED);
    }
    
    /**
     * Undoes {@link #escape(String)}, in one pass so that an escaped escape cannot be read twice.
     *
     * @param value Part to unescape.
     *
     * @return The part as it was before escaping.
     */
    private static String unescape(final String value) {
        return ESCAPED.matcher(value)
                .replaceAll(match -> "25".equals(match.group(1)) ? "%" : SEPARATOR);
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
    public static <T> T valueOf(@Nullable final String value, final Factory<T> factory) {
        if (value == null) {
            return null;
        }
        final String[] parts = split(value);
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
     *
     * @return Returns {@literal true} if it's a valid type else {@literal false}.
     */
    public static boolean isValid(@Nullable final String value) {
        return value == null || validParts(split(value));
    }
    
    /**
     * Splits a string form into its parts, undoing the escaping as it goes.
     *
     * <p>Limited to the number of parts and applied left to right, so only the last one may contain a
     * bare separator - which is what makes a trailing date work. Every earlier part was escaped by
     * {@code asString}, so it is unescaped here and every caller below sees the real value.
     *
     * @param value Value to split.
     *
     * @return Parts, as many as the split found.
     */
    private static String[] split(final String value) {
        final String[] parts = value.split(Pattern.quote(SEPARATOR), 2);
        for (int i = 0; i < parts.length - 1; i++) {
            parts[i] = unescape(parts[i]);
        }
        return parts;
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
    
    /**
     * The id as it reads, which is what an exception message interpolating it wants. Without this
     * the base class leaves Object's version in place and a refusal names the thing it refused as
     * "SomethingId@1f3a2b" - a value object of the same shape already carries one.
     */
    @Override
    public final String toString() {
        return asString();
    }
    
}

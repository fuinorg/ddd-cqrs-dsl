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
import javax.annotation.concurrent.Immutable;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.jspecify.annotations.Nullable;

/**
 * Aggregate ID multiple attribute and without base.
 */
@Immutable
@HasEntityTypeConstant
@HasPublicStaticIsValidMethod
@HasPublicStaticValueOfMethod
public final class MyAggregate4Id extends AbstractMyAggregate4Id {

    @Serial
    private static final long serialVersionUID = 1000L;
    
    /**
     * Default constructor.
     */
    @SuppressWarnings("NullAway.Init")
    protected MyAggregate4Id() {
        super();
    }
    
    /**
     * Constructor with all data.
     *
     * @param a Persistent value A.
     * @param b Persistent value B.
     */
    public MyAggregate4Id(final String a, final String b) {
        super(a, b);
    }
    
    /**
     * Converts the string form produced by {@link #asString()} back into an identifier.
     *
     * @param value String to convert. A {@literal null} value returns {@literal null}.
     *
     * @return Converted value.
     *
     * @throws IllegalArgumentException The value is no valid MyAggregate4Id.
     */
    @Nullable
    public static MyAggregate4Id valueOf(@Nullable final String value) {
        return AbstractMyAggregate4Id.valueOf(value, MyAggregate4Id::new);
    }
    
    /**
     * Verifies that a given string can be converted into the type.
     *
     * @param value Value to validate.
     *
     * @return Returns {@literal true} if it's a valid type else {@literal false}.
     */
    public static boolean isValid(@Nullable final String value) {
        return AbstractMyAggregate4Id.isValid(value);
    }
}

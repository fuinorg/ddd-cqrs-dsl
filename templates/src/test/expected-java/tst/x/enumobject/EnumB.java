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
package p.shared.domain.x.enumobject;

import java.util.List;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.ShortLabel;
import org.jspecify.annotations.Nullable;

/** Enumeration type B - With variables. */
public final class EnumB extends AbstractEnumB {
    
    /** First. */
    @ShortLabel(bundle = "Enumobject", key = "EnumB.A.slabel", value = "1st")
    @Label(bundle = "Enumobject", key = "EnumB.A.label", value = "First one")
    public static final EnumB A = new EnumB(1, "a", "First");
    
    /** Second. */
    public static final EnumB B = new EnumB(2, "b", "Second");
    
    /** Third. */
    public static final EnumB C = new EnumB(3, "c", "Third");
    
    /** All instances. */
    public static final List<EnumB> ALL = List.of(
        A, B, C
    );
    
    /** Valid instances. */
    public static final List<EnumB> VALID = List.of(
        A, B, C
    );
    
    /** Deprecated instances. */
    public static final List<EnumB> DEPRECATED = List.of(
    );
    
    private EnumB(final int id, final String shortName, final String longName) {
        super(id, shortName, longName);
    }
    
}

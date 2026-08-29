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
package p.shared.domain.x.except;

import java.io.Serial;
import java.util.Objects;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.KeyValue;
import org.fuin.objects4j.core.KeyValueEL;

/**
 * Exception D - Multiple variables
 */
public final class ExceptionD extends Exception {

    @Serial
    private static final long serialVersionUID = 1000L;

    private String a;
    
    private int b;
    
    /**
     * Constructs a new instance of the exception.
     *
     * @param a A.
     * @param b B.
     */
    public ExceptionD(final String a, final int b) {
        super(Objects.requireNonNull(KeyValueEL.replace("Exception D: ${a} / ${b}",  new KeyValue("a", a), new KeyValue("b", b))));
        Contract.requireArgNotNull("a", a);
        Contract.requireArgNotNull("b", b);
        
        this.a = a;
        this.b = b;
    }

    /**
     * Returns: A.
     *
     * @return Current value.
     */
    public final String getA() {
        return a;
    }
    
    /**
     * Returns: B.
     *
     * @return Current value.
     */
    public final int getB() {
        return b;
    }
    
}

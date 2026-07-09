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
package p.core.domain.x.aggregates;

import org.fuin.ddd4j.core.AbstractAggregateRoot;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.objects4j.common.Contract;
import p.x.aggregates.AggregateBId;

/**
 * Aggregate B - With variables.
 */
public abstract class AbstractAggregateB extends AbstractAggregateRoot<AggregateBId> {

    private AggregateBId id;

    private String a;
    
    private Integer b;
    
    @Override
    public final EntityType getType() {
        return AggregateBId.TYPE;
    }

    @Override
    public final AggregateBId getId() {
        return id;
    }

    /**
     * Sets the aggregate identifier.
     * 
     * @param id Unique aggregate identifier.
     */
    protected final void setId(final AggregateBId id) {
        Contract.requireArgNotNull("id", id);
        this.id = id;
    }
    
    /**
     * Returns: Variable A.
     *
     * @return Current value.
     */
    protected final String getA() {
        return a;
    }
    
    /**
     * Returns: Variable B.
     *
     * @return Current value.
     */
    protected final Integer getB() {
        return b;
    }
    
    /**
     * Sets: Variable A.
     *
     * @param a Value to set.
     */
    protected final void setA(final String a) {
        Contract.requireArgNotNull("a", a);
        this.a = a;
    }
    
    /**
     * Sets: Variable B.
     *
     * @param b Value to set.
     */
    protected final void setB(final Integer b) {
        Contract.requireArgNotNull("b", b);
        this.b = b;
    }
    
}

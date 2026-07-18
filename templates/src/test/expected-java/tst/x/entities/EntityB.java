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
package p.command.core.domain.x.entities;

import org.fuin.ddd4j.core.AbstractEntity;
import org.fuin.objects4j.common.Contract;
import p.x.entities.AbstractEntityB;
import p.x.entities.AggregateX;
import p.x.entities.AggregateXId;
import p.x.entities.EntityBId;

/**
 * Entity B - With variables.
 */
public final class EntityB extends AbstractEntityB {

    /**
     * Constructor with mandatory data.
     *
     * @param rootAggregate The root aggregate of this entity.
     * @param id Unique entity identifier.
     */
    public EntityB(final AggregateX rootAggregate, final EntityBId id) {
        super(rootAggregate, id);
    }
    

    /**
     * Creates a new builder instance.
     *
     * @return New builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds an instance of the outer class.
     */
    public static final class Builder extends AbstractEntity.Builder<AggregateXId, AggregateX, EntityBId, EntityB, Builder> {
    
        private String a;
        
        private Integer b;
        
        private Builder() {
            super();
        }
    
        /**
         * Sets: Variable A.
         *
         * @param a Value to set.
         * @return This builder.
         */
        public Builder a(final String a) {
            Contract.requireArgNotNull("a", a);
            this.a = a;
            return this;
        }
    
        /**
         * Sets: Variable B.
         *
         * @param b Value to set.
         * @return This builder.
         */
        public Builder b(final Integer b) {
            Contract.requireArgNotNull("b", b);
            this.b = b;
            return this;
        }
    
        /**
         * Creates the entity and clears the builder.
         *
         * @return New instance.
         */
        @Override
        public EntityB build() {
            ensureBuildableAbstractEntity();
            ensureNotNull("a", a);
            ensureNotNull("b", b);
    
            final EntityB result = new EntityB(getRootAggregate(), getEntityId());
            result.setA(a);
            result.setB(b);
    
            resetAbstractEntity();
            this.a = null;
            this.b = null;
            return result;
        }
    
    }
}

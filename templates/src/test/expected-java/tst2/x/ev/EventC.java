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
package p.shared.domain.x.ev;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
import org.fuin.ddd4j.jsonb.AbstractDomainEvent;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.KeyValue;
import org.fuin.objects4j.core.KeyValueEL;
import org.fuin.objects4j.ui.Examples;

/**
 * Aggregate event C.
 */
@HasSerializedDataTypeConstant
public final class EventC extends AbstractDomainEvent<CustomerId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name used to store the event. */
    public static final EventType EVENT_TYPE = new EventType("EventC");
    
    /**
     * Type used to look up the serializer and deserializer. The event store registry is built
     * by scanning for the annotation above, so without this constant the type is unknown at
     * runtime and neither storing nor reading it back works.
     */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(EVENT_TYPE.asBaseType());
    
    @NotNull
    @JsonbProperty("a")
    @Examples(value = { "Abc" })
    @SuppressWarnings("NullAway.Init")
    private String a;
    
    @JsonbProperty("b")
    @Examples(value = { "123" })
    private int b;
    

    /**
     * Protected default constructor for deserialization and the builder.
     */
    @SuppressWarnings("NullAway.Init")
    protected EventC() {
        super();
    }
    
    @Override
    public EventType getEventType() {
        return EVENT_TYPE;
    }

    /**
     * Returns: A.
     *
     * @return Current value.
     */
    public String getA() {
        return a;
    }
    
    /**
     * Returns: B.
     *
     * @return Current value.
     */
    public int getB() {
        return b;
    }
    

    @Override
    public String toString() {
        return Objects.requireNonNull(KeyValueEL.replace("Event C: ${a} / ${b} [${entityIdPath}]",
            new KeyValue("entityIdPath", getEntityIdPath())
            , new KeyValue("a", a)
            , new KeyValue("b", b)
        ));
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
    public static final class Builder extends AbstractDomainEvent.Builder<CustomerId, EventC, Builder> {
    
        private EventC delegate;
    
        private Builder() {
            super(new EventC());
            delegate = delegate();
        }
    
        /**
         * Sets: A.
         *
         * @param a Value to set.
         * @return This builder.
         */
        public Builder a(final String a) {
            Contract.requireArgNotNull("a", a);
            delegate.a = a;
            return this;
        }
        
        /**
         * Sets: B.
         *
         * @param b Value to set.
         * @return This builder.
         */
        public Builder b(final int b) {
            Contract.requireArgNotNull("b", b);
            delegate.b = b;
            return this;
        }
        
    
        /**
         * Creates the event and clears the builder.
         *
         * @return New instance.
         */
        public EventC build() {
            ensureBuildableAbstractDomainEvent();
            if (delegate.getEventId() == null) {
                this.eventId(new EventId());
            }
            if (delegate.getEventTimestamp() == null) {
                this.timestamp(ZonedDateTime.now());
            }
            
        	ensureNotNull("a", delegate.a);
        	ensureNotNull("b", delegate.b);
            
            final EventC result = delegate;
            delegate = new EventC();
            resetAbstractDomainEvent(delegate);
            return result;
        }
    
    }
}


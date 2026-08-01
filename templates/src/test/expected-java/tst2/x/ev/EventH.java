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

/**
 * Event H - Copies the operation's parameters and declares an attribute of its own on top. The generated event has to carry both: the parameters first, then the extra attribute. Its message renders one of each, so an attribute silently dropped here would leave a placeholder nothing supplies.
 */
@HasSerializedDataTypeConstant
public final class EventH extends AbstractDomainEvent<CustomerId> {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name used to store the event. */
    public static final EventType EVENT_TYPE = new EventType("EventH");
    
    /**
     * Type used to look up the serializer and deserializer. The event store registry is built
     * by scanning for the annotation above, so without this constant the type is unknown at
     * runtime and neither storing nor reading it back works.
     */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(EVENT_TYPE.asBaseType());
    
    @NotNull
    @JsonbProperty("new-name")
    @SuppressWarnings("NullAway.Init")
    private MyString newName;
    
    @NotNull
    @JsonbProperty("old-name")
    @SuppressWarnings("NullAway.Init")
    private MyString oldName;
    

    /**
     * Protected default constructor for deserialization and the builder.
     */
    @SuppressWarnings("NullAway.Init")
    protected EventH() {
        super();
    }
    
    @Override
    public EventType getEventType() {
        return EVENT_TYPE;
    }

    /**
     * Returns: The new name.
     *
     * @return Current value.
     */
    public MyString getNewName() {
        return newName;
    }
    
    /**
     * Returns: The name before the change.
     *
     * @return Current value.
     */
    public MyString getOldName() {
        return oldName;
    }
    

    @Override
    public String toString() {
        return Objects.requireNonNull(KeyValueEL.replace("Renamed from ${oldName} to ${newName}",
            new KeyValue("entityIdPath", getEntityIdPath())
            , new KeyValue("newName", newName)
            , new KeyValue("oldName", oldName)
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
    public static final class Builder extends AbstractDomainEvent.Builder<CustomerId, EventH, Builder> {
    
        private EventH delegate;
    
        private Builder() {
            super(new EventH());
            delegate = delegate();
        }
    
        /**
         * Sets: The new name.
         *
         * @param newName Value to set.
         * @return This builder.
         */
        public Builder newName(final MyString newName) {
            Contract.requireArgNotNull("newName", newName);
            delegate.newName = newName;
            return this;
        }
        
        /**
         * Sets: The name before the change.
         *
         * @param oldName Value to set.
         * @return This builder.
         */
        public Builder oldName(final MyString oldName) {
            Contract.requireArgNotNull("oldName", oldName);
            delegate.oldName = oldName;
            return this;
        }
        
    
        /**
         * Creates the event and clears the builder.
         *
         * @return New instance.
         */
        public EventH build() {
            ensureBuildableAbstractDomainEvent();
            if (delegate.getEventId() == null) {
                this.eventId(new EventId());
            }
            if (delegate.getEventTimestamp() == null) {
                this.timestamp(ZonedDateTime.now());
            }
            
        	ensureNotNull("newName", delegate.newName);
        	ensureNotNull("oldName", delegate.oldName);
            
            final EventH result = delegate;
            delegate = new EventH();
            resetAbstractDomainEvent(delegate);
            return result;
        }
    
    }
}


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
package p.command.api.x.cmd;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.fuin.cqrs4j.jsonb.AbstractAggregateCommand;
import org.fuin.ddd4j.core.EventId;
import org.fuin.ddd4j.core.EventType;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.KeyValue;
import org.fuin.objects4j.core.KeyValueEL;
import p.x.cmd.CustomerId;

/**
 * Command E - Targets an aggregate method, so its attributes are the method's parameters rather than its own. That is a different path through the generator.
 */
@HasSerializedDataTypeConstant
public final class CommandE extends AbstractAggregateCommand<CustomerId, CustomerId> {

	@Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name used to store the command. */
    public static final EventType EVENT_TYPE = new EventType("CommandE");
    
    /**
     * Type used to look up the serializer and deserializer. The registry is built by scanning
     * for the annotation above, so without this constant the command cannot be deserialized
     * when it arrives at the command endpoint.
     */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(EVENT_TYPE.asBaseType());
    
    @NotNull
    @JsonbProperty("new-name")
    @SuppressWarnings("NullAway.Init")
    private String newName;
    

    /**
     * Protected default constructor for deserialization and the builder.
     */
    @SuppressWarnings("NullAway.Init")
    protected CommandE() {
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
    public String getNewName() {
        return newName;
    }
    

    @Override
    public String toString() {
        return Objects.requireNonNull(KeyValueEL.replace("Command E",
            new KeyValue("entityIdPath", getEntityIdPath())
            , new KeyValue("newName", newName)
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
    public static final class Builder extends AbstractAggregateCommand.Builder<CustomerId, CustomerId, CommandE, Builder> {
    
        private CommandE delegate;
    
        private Builder() {
            super(new CommandE());
            delegate = delegate();
        }
    
        /**
         * Sets: The new name.
         *
         * @param newName Value to set.
         * @return This builder.
         */
        public Builder newName(final String newName) {
            Contract.requireArgNotNull("newName", newName);
            delegate.newName = newName;
            return this;
        }
        
    
        /**
         * Creates the command and clears the builder.
         *
         * @return New instance.
         */
        public CommandE build() {
            ensureBuildableAbstractAggregateCommand();
            if (delegate.getEventId() == null) {
                this.eventId(new EventId());
            }
            if (delegate.getEventTimestamp() == null) {
                this.timestamp(ZonedDateTime.now());
            }
            
        	ensureNotNull("newName", delegate.newName);
            
            final CommandE result = delegate;
            delegate = new CommandE();
            resetAbstractAggregateCommand(delegate);
            return result;
        }
    
    }
}

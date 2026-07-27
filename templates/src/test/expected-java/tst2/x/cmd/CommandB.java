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
import java.util.Objects;
import org.fuin.cqrs4j.jsonb.AbstractCommand;
import org.fuin.ddd4j.core.EventType;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.core.KeyValue;
import org.fuin.objects4j.core.KeyValueEL;
import org.fuin.objects4j.ui.Examples;

/**
 * Command B - One mandatory attribute, which has to carry @NotNull.
 */
@HasSerializedDataTypeConstant
public final class CommandB extends AbstractCommand {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name used to store the command. */
    public static final EventType EVENT_TYPE = new EventType("CommandB");
    
    /**
     * Type used to look up the serializer and deserializer. The registry is built by scanning
     * for the annotation above, so without this constant the command cannot be deserialized
     * when it arrives at the command endpoint.
     */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(EVENT_TYPE.asBaseType());
    
    @NotNull
    @JsonbProperty("a")
    @Examples(value = { "Abc" })
    private String a;
    

    /**
     * Protected default constructor for deserialization.
     */
    @SuppressWarnings("NullAway.Init")
    protected CommandB() {
        super();
    }
    
    /**
     * Command B - One mandatory attribute, which has to carry @NotNull.
     *
    * @param a A. 
    */
    public CommandB(final String a) {
        super();
        Contract.requireArgNotNull("a", a);
        
        this.a = a;
    }

    @Override
    public final EventType getEventType() {
        return EVENT_TYPE;
    }

    /**
     * Returns: A.
     *
     * @return Current value.
     */
    public final String getA() {
        return a;
    }
    

    @Override
    public final String toString() {
        return Objects.requireNonNull(KeyValueEL.replace("Command B: ${a}"
        , new KeyValue("a", a)
        ));
    }
    
}

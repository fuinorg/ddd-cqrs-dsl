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
import org.jspecify.annotations.Nullable;

/**
 * Command C - A mandatory and an optional attribute side by side, so the two annotations can be told apart: mandatory gets @NotNull, optional gets @Nullable and neither gets both.
 */
@HasSerializedDataTypeConstant
public final class CommandC extends AbstractCommand {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name used to store the command. */
    public static final EventType EVENT_TYPE = new EventType("CommandC");
    
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
    
    @Nullable
    @JsonbProperty("b")
    @Examples(value = { "123" })
    private Integer b;
    

    /**
     * Protected default constructor for deserialization.
     */
    @SuppressWarnings("NullAway.Init")
    protected CommandC() {
        super();
    }
    
    /**
     * Command C - A mandatory and an optional attribute side by side, so the two annotations can be told apart: mandatory gets @NotNull, optional gets @Nullable and neither gets both.
     *
    * @param a A. 
    * @param b B. 
    */
    public CommandC(final String a, @Nullable final Integer b) {
        super();
        Contract.requireArgNotNull("a", a);
        
        this.a = a;
        this.b = b;
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
    
    /**
     * Returns: B.
     *
     * @return Current value.
     */
    @Nullable
    public final Integer getB() {
        return b;
    }
    

    @Override
    public final String toString() {
        return Objects.requireNonNull(KeyValueEL.replace("Command C: ${a} / ${b}"
        , new KeyValue("a", a)
        , new KeyValue("b", b)
        ));
    }
    
}

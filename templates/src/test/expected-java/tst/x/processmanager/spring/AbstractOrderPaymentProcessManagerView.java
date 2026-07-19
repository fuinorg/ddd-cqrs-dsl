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
package p.process.core.x.m;

import com.example.UserCreatedEvent;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.fuin.cqrs4j.core.CommandOutbox;
import org.fuin.cqrs4j.core.ProcessManagerView;
import org.fuin.cqrs4j.core.View;
import org.fuin.ddd4j.core.Event;
import org.fuin.ddd4j.core.EventType;

/**
 * Base class for the "OrderPayment" process manager. Implements {@link ProcessManagerView};
 * the concrete OrderPaymentProcessManagerView adds the runtime-specific bean annotations and the reaction
 * bodies. Regenerated on every build.
 */
public abstract class AbstractOrderPaymentProcessManagerView implements ProcessManagerView {

    /** Unique name of the process manager / projection. */
    public static final String NAME = "OrderPaymentProcessManager";

    /** Name of the bean. */
    public static final String BEAN_NAME = "OrderPaymentProcessManagerView";

    /** Outbox used to enqueue commands produced by this process manager. */
    protected final CommandOutbox outbox;

    /** Entity manager used to store the process state. */
    protected final EntityManager em;

    /** Reaction dispatch by exact event class, populated in the constructor. */
    private final Map<Class<? extends Event>, Consumer<Event>> reactions = new HashMap<>();

    /**
     * Constructor with the injected collaborators.
     *
     * @param outbox Command outbox (same transaction as the state update).
     * @param em Entity manager used to store the process state.
     */
    protected AbstractOrderPaymentProcessManagerView(final CommandOutbox outbox, final EntityManager em) {
        this.outbox = outbox;
        this.em = em;
        reactions.put(UserCreatedEvent.class, event -> onUserCreatedEvent((UserCreatedEvent) event));
    }

    @Override
    public CommandOutbox getCommandOutboxService() {
        return outbox;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getBeanName() {
        return BEAN_NAME;
    }

    @Override
    public Class<? extends View> getBeanClass() {
        return OrderPaymentProcessManagerView.class;
    }

    @Override
    public Set<EventType> getEventTypes() {
        return Set.of(UserCreatedEvent.EVENT_TYPE);
    }

    @Override
    public String getCron() {
        return "* * * * * *";
    }

    @Override
    public void handleEvents(final List<Event> events) {
        for (final Event event : events) {
            final Consumer<Event> reaction = reactions.get(event.getClass());
            if (reaction == null) {
                throw new IllegalStateException("Cannot handle event: " + event);
            }
            reaction.accept(event);
        }
    }

    /**
     * Reacts to a {@link UserCreatedEvent}: correlate to a running process, update the state,
     * enqueue commands via {@code send(...)} and arm/cancel timeouts.
     *
     * @param event Event that arrived.
     */
    protected abstract void onUserCreatedEvent(UserCreatedEvent event);

}

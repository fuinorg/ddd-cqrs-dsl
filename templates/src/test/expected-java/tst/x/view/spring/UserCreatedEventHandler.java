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
package p.query.core.view.x.m;

import com.example.UserCreatedEvent;
import jakarta.persistence.EntityManager;
import org.fuin.cqrs4j.core.EventHandler;
import org.fuin.ddd4j.core.EventType;

/**
 * Handles the {@link UserCreatedEvent} by updating the read model. TODO Implement the update.
 */
public class UserCreatedEventHandler implements EventHandler<UserCreatedEvent> {

    @Override
    public EventType getEventType() {
        return UserCreatedEvent.TYPE;
    }

    @Override
    public void handle(final EntityManager em, final UserCreatedEvent event) {
        // TODO Update the read model for this event.
    }

}

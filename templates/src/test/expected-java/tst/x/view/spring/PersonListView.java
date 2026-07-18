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
import java.util.List;
import java.util.Set;
import org.fuin.cqrs4j.core.View;
import org.fuin.cqrs4j.esc.JpaEventDispatcher;
import org.fuin.cqrs4j.esc.SimpleJpaEventDispatcher;
import org.fuin.ddd4j.core.Event;
import org.fuin.ddd4j.core.EventType;
import org.fuin.objects4j.common.ThreadSafe;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * View with the list of PersonList. Implements {@link View} and is discovered by the query
 * runtime as a bean. Fully generated - regenerated on every build.
 */
@ThreadSafe
@Component(PersonListView.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PersonListView implements View {

    /** Unique name of the view / projection. */
    public static final String NAME = "spring-qry-personlist";

    /** Name of the bean. */
    public static final String BEAN_NAME = "PersonListView";

    private final EntityManager em;

    private final JpaEventDispatcher eventDispatcher;

    /**
     * Constructor with the injected entity manager.
     *
     * @param em Entity manager used to store the read model.
     */
    public PersonListView(final EntityManager em) {
        this.em = em;
        this.eventDispatcher = new SimpleJpaEventDispatcher(
            new UserCreatedEventHandler()
        );
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
        return PersonListView.class;
    }

    @Override
    public Set<EventType> getEventTypes() {
        return Set.of(UserCreatedEvent.TYPE);
    }

    @Override
    public String getCron() {
        return "* * * * * *";
    }

    @Override
    public void handleEvents(final List<Event> events) {
        eventDispatcher.dispatchEvents(em, events);
    }

}

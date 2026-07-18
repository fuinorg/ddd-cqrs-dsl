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
import org.fuin.cqrs4j.core.CommandOutbox;
import org.fuin.objects4j.common.ThreadSafe;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The "OrderPaymentProcess" process manager. Register it with the process-manager runtime
 * (Spring, importing {@code ProcessManagerConfig}).
 */
@ThreadSafe
@Component(OrderPaymentProcessManagerView.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderPaymentProcessManagerView extends AbstractOrderPaymentProcessManagerView {

    /**
     * Constructor with the injected collaborators.
     *
     * @param outbox Command outbox (same transaction as the state update).
     * @param em Entity manager used to store the process state.
     */
    public OrderPaymentProcessManagerView(final CommandOutbox outbox, final EntityManager em) {
        super(outbox, em);
    }

    @Override
    protected void onUserCreatedEvent(final UserCreatedEvent event) {
        // TODO Correlate to a running process, update the state, send commands and
        // arm/cancel timeouts.
    }

}

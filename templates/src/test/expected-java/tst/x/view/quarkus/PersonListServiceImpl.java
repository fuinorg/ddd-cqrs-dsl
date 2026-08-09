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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import p.query.api.view.x.m.PersonListService;
import p.shared.domain.x.m.PersonListItem;
import p.shared.domain.x.m.UserId;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.String;

/**
 * Answers the PersonList read model's queries against the database. Implements
 * {@link PersonListService}, which the generated PersonListResource exposes over REST.
 *
 * <p>This is a generate-once stub and yours from here on - the generator will not overwrite
 * it. TODO implement the queries against your read model.
 */
@ApplicationScoped
@Transactional
public class PersonListServiceImpl implements PersonListService {

    private final EntityManager em;

    /**
     * Constructor with all mandatory dependencies. A single constructor is injected
     * implicitly, and it is what lets a test drive this service against an in-memory
     * database without starting a container.
     *
     * @param em Entity manager of the read model.
     */
    public PersonListServiceImpl(final EntityManager em) {
        this.em = Objects.requireNonNull(em, "em==null");
    }

    @Override
    public List<PersonListItem> listPersons(@Nullable final String search) {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement listPersons()");
    }

    @Override
    public Optional<PersonListItem> findPerson(final UserId id) {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement findPerson()");
    }

    @Override
    public int countPersons() {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement countPersons()");
    }

}

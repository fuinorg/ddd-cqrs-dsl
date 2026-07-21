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

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import p.query.api.view.x.m.PersonListResourceApi;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.PersonListItem;
import p.x.m.String;
import p.x.m.UserId;

/**
 * REST resource providing the PersonList read model. Implements {@link PersonListResourceApi}; the
 * class-level {@code @Path} is re-declared because JAX-RS does not inherit it from the
 * interface. This is a generate-once stub - TODO implement the queries against your read model.
 */
@Path("/persons")
public class PersonListResource implements PersonListResourceApi {

    @Inject
    EntityManager em;

    @Override
    public List<PersonListItem> listPersons(@QueryParam("search") final String search) {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement listPersons()");
    }

    @Override
    public PersonListItem findPerson(@PathParam("id") final UserId id) {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement findPerson()");
    }

    @Override
    public Integer countPersons() {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement countPersons()");
    }

}

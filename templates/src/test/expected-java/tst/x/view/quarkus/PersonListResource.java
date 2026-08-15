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
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import java.util.Objects;
import org.fuin.cqrs4j.core.QueryAuthorization;
import org.fuin.cqrs4j.core.QueryAuthorizer;
import org.fuin.cqrs4j.core.QueryExecutionContextProvider;
import p.query.api.view.x.m.PersonListResourceApi;
import p.query.api.view.x.m.PersonListService;
import p.shared.domain.x.m.PersonListItem;
import p.shared.domain.x.m.UserId;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.String;

/**
 * Exposes the PersonList read model over REST by forwarding to {@link PersonListService}.
 * Implements {@link PersonListResourceApi}; the class-level {@code @Path} is re-declared because
 * JAX-RS does not inherit it from the interface. Holds no logic of its own and is
 * regenerated on every build - implement the queries in PersonListServiceImpl.
 */
@ApplicationScoped
@Path("/persons")
public class PersonListResource implements PersonListResourceApi {

    private final PersonListService service;

    private final QueryAuthorizer authorizer;

    private final QueryExecutionContextProvider contextProvider;

    /**
     * Constructor with all mandatory dependencies.
     *
     * @param service Read model this resource exposes.
     * @param authorizer Decides whether the caller may invoke an operation.
     * @param contextProvider Says who is calling.
     */
    public PersonListResource(final PersonListService service, final QueryAuthorizer authorizer,
            final QueryExecutionContextProvider contextProvider) {
        this.service = Objects.requireNonNull(service, "service==null");
        this.authorizer = Objects.requireNonNull(authorizer, "authorizer==null");
        this.contextProvider = Objects.requireNonNull(contextProvider, "contextProvider==null");
    }

    @Override
    public List<PersonListItem> listPersons(@QueryParam("search") final String search) {
        QueryAuthorization.require(authorizer, "PersonListView.listPersons", contextProvider.current());
        return service.listPersons(search);
    }

    @Override
    public PersonListItem findPerson(@PathParam("id") final UserId id) {
        QueryAuthorization.require(authorizer, "PersonListView.findPerson", contextProvider.current());
        return service.findPerson(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Integer countPersons() {
        QueryAuthorization.require(authorizer, "PersonListView.countPersons", contextProvider.current());
        return service.countPersons();
    }

}

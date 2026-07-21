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
package p.query.api.view.x.m;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.PersonListItem;
import p.x.m.String;
import p.x.m.UserId;

/**
 * REST contract for the "PersonList" view: usable as a MicroProfile REST client and
 * implemented by the PersonListResource server class. Declares the operations that MUST be
 * provided - no implementation and no persistence assumptions. Regenerated on every build.
 *
 * <p>JAX-RS does not inherit class-level annotations, so the server class re-declares
 * {@code @Path}; the method annotations below are inherited by the implementation.
 */
@Path("/persons")
public interface PersonListResourceApi {

    /**
     * Returns the persons matching the optional filter.
     *
     * @param search 
     *
     * @return 
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    List<PersonListItem> listPersons(@QueryParam("search") final String search);

    /**
     * Returns a single person.
     *
     * @param id 
     *
     * @return 
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    PersonListItem findPerson(@PathParam("id") final UserId id);

    /**
     * Returns the number of persons - no explicit path, so the method name is used.
     *
     * @return 
     */
    @GET
    @Path("/countpersons")
    @Produces(MediaType.APPLICATION_JSON)
    Integer countPersons();

}

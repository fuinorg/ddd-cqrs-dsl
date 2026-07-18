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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
     * Returns all entries of the read model.
     *
     * @return JSON response with the list of entries.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getAll();

    /**
     * Returns a single entry by its id.
     *
     * @param id Read-model id.
     *
     * @return JSON response with the entry, or 404 if unknown.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getById(@PathParam("id") String id);
}

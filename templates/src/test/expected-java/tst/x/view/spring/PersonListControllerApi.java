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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * REST contract for the "PersonList" view: an {@code @HttpExchange} interface usable by an
 * HTTP-interface client and implemented by the PersonListController server class (which adds
 * {@code @RestController}). Declares the operations that MUST be provided - no implementation
 * and no persistence assumptions. Regenerated on every build.
 */
@HttpExchange("/persons")
public interface PersonListControllerApi {

    /**
     * Returns all entries of the read model.
     *
     * @return response with the list of entries.
     */
    @GetExchange
    ResponseEntity<?> getAll();

    /**
     * Returns a single entry by its id.
     *
     * @param id Read-model id.
     *
     * @return response with the entry, or 404 if unknown.
     */
    @GetExchange("/{id}")
    ResponseEntity<?> getById(@PathVariable("id") String id);
}

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import p.shared.domain.x.m.PersonListItem;
import p.shared.domain.x.m.UserId;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.String;

/**
 * REST contract for the "PersonList" view: an {@code @HttpExchange} interface usable by an
 * HTTP-interface client and implemented by the PersonListController server class (which adds
 * {@code @RestController}). Declares the operations that MUST be provided - no implementation
 * and no persistence assumptions. Regenerated on every build.
 *
 * <p>Spring flavour - requires {@code org.springframework:spring-web}, which the module owning
 * this interface declares as an <em>optional</em> dependency. Add that dependency to whatever
 * uses this interface. The Quarkus flavour {@link PersonListResourceApi} is generated alongside
 * it and declares the same operations; use one or the other, not both.
 */
@HttpExchange("/persons")
public interface PersonListControllerApi {

    /**
     * Returns the persons matching the optional filter.
     *
     * @param search 
     *
     * @return 
     */
    @GetExchange("")
    ResponseEntity<List<PersonListItem>> listPersons(@RequestParam(value = "search", required = false) final String search);

    /**
     * Returns a single person.
     *
     * @param id 
     *
     * @return 
     */
    @GetExchange("/{id}")
    ResponseEntity<PersonListItem> findPerson(@PathVariable("id") final UserId id);

    /**
     * Returns the number of persons - no explicit path, so the method name is used.
     *
     * @return 
     */
    @GetExchange("/count-persons")
    ResponseEntity<Integer> countPersons();

}

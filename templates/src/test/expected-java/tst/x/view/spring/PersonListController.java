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

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import p.query.api.view.x.m.PersonListControllerApi;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.PersonListItem;
import p.x.m.String;
import p.x.m.UserId;

/**
 * REST controller providing the PersonList read model. Implements {@link PersonListControllerApi} and adds
 * {@code @RestController} (required - not inherited from the interface). This is a generate-once
 * stub - TODO implement the queries against your read model.
 */
@RestController
@Transactional(readOnly = true)
public class PersonListController implements PersonListControllerApi {

    @Autowired
    private EntityManager em;

    @Override
    public ResponseEntity<List<PersonListItem>> listPersons(@RequestParam(value = "search", required = false) final String search) {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement listPersons()");
    }

    @Override
    public ResponseEntity<PersonListItem> findPerson(@PathVariable("id") final UserId id) {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement findPerson()");
    }

    @Override
    public ResponseEntity<Integer> countPersons() {
        // TODO Implement: query the read model and return the result.
        throw new UnsupportedOperationException("TODO: implement countPersons()");
    }

}

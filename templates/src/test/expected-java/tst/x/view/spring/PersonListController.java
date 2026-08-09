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

import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import p.query.api.view.x.m.PersonListControllerApi;
import p.query.api.view.x.m.PersonListService;
import p.shared.domain.x.m.PersonListItem;
import p.shared.domain.x.m.UserId;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.String;

/**
 * Exposes the PersonList read model over REST by forwarding to {@link PersonListService}.
 * Implements {@link PersonListControllerApi} and adds {@code @RestController} (required - not inherited
 * from the interface). Holds no logic of its own and is regenerated on every build -
 * implement the queries in PersonListServiceImpl.
 */
@RestController
public class PersonListController implements PersonListControllerApi {

    private final PersonListService service;

    /**
     * Constructor with all mandatory dependencies. A single constructor is autowired
     * implicitly.
     *
     * @param service Read model this controller exposes.
     */
    public PersonListController(final PersonListService service) {
        this.service = Objects.requireNonNull(service, "service==null");
    }

    @Override
    public ResponseEntity<List<PersonListItem>> listPersons(@RequestParam(value = "search", required = false) final String search) {
        return ResponseEntity.ok(service.listPersons(search));
    }

    @Override
    public ResponseEntity<PersonListItem> findPerson(@PathVariable("id") final UserId id) {
        return service.findPerson(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Integer> countPersons() {
        return ResponseEntity.ok(service.countPersons());
    }

}

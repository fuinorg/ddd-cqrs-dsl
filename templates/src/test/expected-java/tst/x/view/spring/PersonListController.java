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
import org.springframework.web.bind.annotation.RestController;
import p.query.api.view.x.m.PersonListControllerApi;

/**
 * REST controller providing the PersonList read model. Implements {@link PersonListControllerApi} and adds
 * {@code @RestController} (required - not inherited from the interface). TODO Adjust the
 * queries / response mapping to your read model.
 */
@RestController
@Transactional(readOnly = true)
public class PersonListController implements PersonListControllerApi {

    @Autowired
    private EntityManager em;

    @Override
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(em.createNamedQuery(PersonListEntry.FIND_ALL, PersonListEntry.class).getResultList());
    }

    @Override
    public ResponseEntity<?> getById(final String id) {
        final PersonListEntry entry = em.find(PersonListEntry.class, id);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entry);
    }

}

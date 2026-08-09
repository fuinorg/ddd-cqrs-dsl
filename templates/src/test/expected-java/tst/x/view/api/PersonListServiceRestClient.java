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

import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import p.shared.domain.x.m.PersonListItem;
import p.shared.domain.x.m.UserId;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.String;

/**
 * Satisfies {@link PersonListService} over HTTP, by wrapping the generated
 * {@code @HttpExchange} proxy for {@link PersonListControllerApi} and unwrapping its
 * {@link ResponseEntity}. Regenerated on every build.
 *
 * <p>A 404 is an answer here, not a failure: for an operation the model declares
 * {@code optional} it becomes an empty result, which is the same thing the service reports
 * when it runs in this JVM. Both a thrown {@code HttpClientErrorException.NotFound} and a
 * returned 404 status are handled, because which of the two a caller sees depends on how
 * the underlying client was configured.
 *
 * <p>Carries no bean-defining annotation on purpose: it is wired explicitly by whatever
 * application needs it, and must stay inert on a classpath that is scanned for beans.
 */
public class PersonListServiceRestClient implements PersonListService {

    private final PersonListControllerApi api;

    /**
     * Constructor with all mandatory dependencies.
     *
     * @param api Proxy for the read model of another process.
     */
    public PersonListServiceRestClient(final PersonListControllerApi api) {
        this.api = Objects.requireNonNull(api, "api==null");
    }

    @Override
    public List<PersonListItem> listPersons(@Nullable final String search) {
        final ResponseEntity<List<PersonListItem>> response = api.listPersons(search);
        final List<PersonListItem> body = response.getBody();
        if (body == null) {
            throw new IllegalStateException(
                    "The query side answered 'listPersons' with an empty body");
        }
        return body;
    }

    @Override
    public Optional<PersonListItem> findPerson(final UserId id) {
        try {
            final ResponseEntity<PersonListItem> response = api.findPerson(id);
            if (response.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            return Optional.ofNullable(response.getBody());
        } catch (final HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }

    @Override
    public int countPersons() {
        final ResponseEntity<Integer> response = api.countPersons();
        final Integer body = response.getBody();
        if (body == null) {
            throw new IllegalStateException(
                    "The query side answered 'countPersons' with an empty body");
        }
        return body;
    }

}

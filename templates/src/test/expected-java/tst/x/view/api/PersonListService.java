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

import java.util.Optional;
import org.jspecify.annotations.Nullable;
import p.shared.domain.x.m.PersonListItem;
import p.shared.domain.x.m.UserId;
import p.x.m.Integer;
import p.x.m.List;
import p.x.m.String;

/**
 * The "PersonList" read model as plain Java: the operations that MUST be provided, with no
 * implementation, no persistence assumptions and no framework types. Regenerated on every
 * build.
 *
 * <p>This is the contract to depend on from inside the same application. The REST contracts
 * {@link PersonListControllerApi} and {@link PersonListResourceApi} declare the same
 * operations for a caller in another process; the generated REST classes implementing them
 * do nothing but delegate here, so going through HTTP inside one JVM would buy nothing.
 *
 * <p>An operation the model declares {@code optional} returns an {@link java.util.Optional}.
 * Over HTTP that same absence is a 404, which the generated delegates translate in both
 * directions.
 */
public interface PersonListService {
    
    /**
     * Returns the persons matching the optional filter.
     *
     * @param search 
     *
     * @return 
     */
    public List<PersonListItem> listPersons(@Nullable final String search);
    
    /**
     * Returns a single person.
     *
     * @param id 
     *
     * @return 
     */
    public Optional<PersonListItem> findPerson(final UserId id);
    
    /**
     * Returns the number of persons - no explicit path, so the method name is used.
     *
     * @return 
     */
    public int countPersons();
    
}

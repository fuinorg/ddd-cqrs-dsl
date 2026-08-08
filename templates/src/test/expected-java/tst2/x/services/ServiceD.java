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
package p.command.core.domain.x.services;

import java.util.List;
import java.util.Optional;

/**
 * Service D - Optional return type.
 */
public interface ServiceD {
    
    /**
     * Finds something that may not exist.
     *
     * @param a Key.
     *
     * @return Value, if there is one.
     */
    public Optional<String> find(final int a);
    
    /**
     * Lists something that may not exist.
     *
     * @param a Key.
     *
     * @return Values, if there are any.
     */
    public Optional<List<String>> list(final int a);
    
}

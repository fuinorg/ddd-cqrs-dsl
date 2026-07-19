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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA entity for the ORDERS table.
 */
@Table(name = "ORDERS")
@Entity
public class Order {

    @Id
    @Column(name = "ID", nullable = false)
    private UUID id;
    
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true, cascade = { CascadeType.ALL })
    private List<OrderLine> lines = new ArrayList<>();
    
    /**
     * Protected default constructor only required for JPA.
     */
    @SuppressWarnings("NullAway.Init")
    protected Order() {
        super();
    }

    public UUID getId() {
        return id;
    }
    
    public void setId(final UUID id) {
        this.id = id;
    }
    
    public List<OrderLine> getLines() {
        return lines;
    }
    
}

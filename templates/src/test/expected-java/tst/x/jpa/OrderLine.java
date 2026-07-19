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

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA entity for the ORDER_LINES table.
 */
@Table(name = "ORDER_LINES")
@Entity
public class OrderLine {

    @Id
    @Column(name = "ID", nullable = false)
    private UUID id;
    
    @Column(name = "AMOUNT", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Order order;
    
    /**
     * Protected default constructor only required for JPA.
     */
    @SuppressWarnings("NullAway.Init")
    protected OrderLine() {
        super();
    }

    public UUID getId() {
        return id;
    }
    
    public void setId(final UUID id) {
        this.id = id;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(final Order order) {
        this.order = order;
    }
    
}

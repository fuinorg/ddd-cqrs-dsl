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
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA entity for the CUSTOMER table.
 */
@Table(name = "CUSTOMER", schema = "SALES", uniqueConstraints = { @UniqueConstraint(name = "UQ_EMAIL", columnNames = { "EMAIL" }) }, indexes = { @Index(name = "IX_NAME", columnList = "LAST_NAME", unique = false) })
@Entity
public class Customer {

    @Id
    @Column(name = "ID", nullable = false)
    private UUID id;
    
    @Column(name = "AMOUNT", nullable = false, precision = 12, scale = 2)
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal amount;
    
    @Column(name = "LAST_NAME", length = 100)
    private String name;
    
    /**
     * Protected default constructor only required for JPA.
     */
    @SuppressWarnings("NullAway.Init")
    protected Customer() {
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
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
}

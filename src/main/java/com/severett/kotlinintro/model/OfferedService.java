package com.severett.kotlinintro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OfferedService {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;
    private BigDecimal price;
    private BigDecimal discount;

    @PrePersist
    public void prePersist() {
        if (discount == null) discount = BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferedService that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

package com.severett.kotlinintro.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Horse extends Pet {
    private static final int HASHCODE = Horse.class.hashCode();

    private double hoofWidth;

    public Horse(int id, String name, double hoofWidth) {
        super(id, name);
        this.hoofWidth = hoofWidth;
    }

    @Override
    public String getSound() {
        return "Neighs";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Horse)) return false;
        return getId() == ((Horse) o).getId();
    }

    @Override
    public int hashCode() {
        return HASHCODE;
    }
}

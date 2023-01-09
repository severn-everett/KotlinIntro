package com.severett.kotlinintro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Horse extends Pet {
    private static final int HASHCODE = Horse.class.hashCode();

    private double hoofSize;

    public Horse(int id, String name, double hoofSize) {
        super(id, name);
        this.hoofSize = hoofSize;
    }

    public String neigh() {
        return "Neighs";
    }

    @Transient
    @JsonIgnore
    @Override
    public PetType getType() {
        return PetType.horse;
    }

    @Override
    public int hashCode() {
        return HASHCODE;
    }
}

package com.severett.kotlinintro.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Dog extends Pet {
    private static final int HASHCODE = Dog.class.hashCode();

    private double tailLength;

    public Dog(int id, String name, double tailLength) {
        super(id, name);
        this.tailLength = tailLength;
    }

    @Override
    public String getSound() {
        return "Barks";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dog)) return false;
        return getId() == ((Dog) o).getId();
    }

    @Override
    public int hashCode() {
        return HASHCODE;
    }
}

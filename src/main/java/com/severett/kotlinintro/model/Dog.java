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
public class Dog extends Pet {
    private static final int HASHCODE = Dog.class.hashCode();

    private double tailLength;

    public Dog(int id, String name, double tailLength) {
        super(id, name);
        this.tailLength = tailLength;
    }

    public String bark() {
        return "Barks";
    }

    @Transient
    @JsonIgnore
    @Override
    public PetType getType() {
        return PetType.dog;
    }

    @Override
    public int hashCode() {
        return HASHCODE;
    }
}

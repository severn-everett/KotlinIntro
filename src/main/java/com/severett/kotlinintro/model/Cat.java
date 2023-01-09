package com.severett.kotlinintro.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Cat extends Pet {
    private static final int HASHCODE = Cat.class.hashCode();

    private double clawLength;

    public Cat(int id, String name, double clawLength) {
        super(id, name);
        this.clawLength = clawLength;
    }

    @Override
    public String getSound() {
        return "Meows";
    }

    @Override
    public int hashCode() {
        return HASHCODE;
    }
}

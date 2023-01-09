package com.severett.kotlinintro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
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

    public String meow() {
        return "Meows";
    }

    @Transient
    @JsonIgnore
    @Override
    public PetType getType() {
        return PetType.cat;
    }

    @Override
    public int hashCode() {
        return HASHCODE;
    }
}

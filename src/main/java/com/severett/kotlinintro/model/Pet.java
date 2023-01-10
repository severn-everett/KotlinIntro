package com.severett.kotlinintro.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Pet {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private String name;

    @SuppressWarnings("unused")
    public abstract String getSound();
}

package com.severett.kotlinintro.model;

public enum PetType {
    cat("Cat"), dog("Dog"), horse("Horse");

    private final String displayName;

    PetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

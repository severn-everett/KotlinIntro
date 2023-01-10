package com.severett.kotlinintro.model

@Suppress("EnumEntryName")
enum class PetType(val displayName: String) {
    cat("Cat"), dog("Dog"), horse("Horse")
}

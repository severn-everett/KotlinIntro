package com.severett.kotlinintro.model

import jakarta.persistence.Entity

private val HASHCODE = Dog::javaClass.hashCode()

@Entity
class Dog(id: Int = 0, name: String = "", var tailLength: Double = 0.0) : Pet(id, name) {
    override val sound: String
        get() = "Barks"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Dog) return false
        return id == other.id
    }

    override fun hashCode() = HASHCODE
}

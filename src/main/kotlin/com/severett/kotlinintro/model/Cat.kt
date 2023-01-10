package com.severett.kotlinintro.model

import jakarta.persistence.Entity

private val HASHCODE: Int = Cat::javaClass.hashCode()

@Entity
class Cat(id: Int = 0, name: String = "", var clawLength: Double = 0.0) : Pet(id, name) {
    override val sound: String
        get() = "Meows"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cat) return false
        return id == other.id
    }

    override fun hashCode() = HASHCODE
}

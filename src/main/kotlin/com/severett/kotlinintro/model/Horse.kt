package com.severett.kotlinintro.model

import jakarta.persistence.Entity

private val HASHCODE = Horse::javaClass.hashCode()

@Entity
class Horse(id: Int = 0, name: String = "", var hoofWidth: Double = 0.0) : Pet(id, name) {
    override val sound: String
        get() = "Neighs"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Horse) return false
        return id == other.id
    }

    override fun hashCode() = HASHCODE
}

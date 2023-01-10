package com.severett.kotlinintro.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
data class OfferedService(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val discount: BigDecimal = BigDecimal.ZERO
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OfferedService) return false

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

package com.severett.kotlinintro.service

import com.severett.kotlinintro.model.OfferedService
import com.severett.kotlinintro.repo.OfferedServiceRepo
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
open class OfferedServiceService(private val offeredServiceRepo: OfferedServiceRepo) {
    open fun getOfferedService(id: Int) = offeredServiceRepo.findByIdOrNull(id)

    @Transactional
    open fun createOfferedService(request: CreateOfferedServiceRequest): OfferedService {
        val offeredService = OfferedService(name = request.name, price = request.price)
        return offeredServiceRepo.save(offeredService)
    }

    @Transactional
    open fun setDiscount(request: SetDiscountRequest): OfferedService {
        val newOfferedService = offeredServiceRepo.findByIdOrNull(request.id)
            ?.copy(discount = request.discount)
            ?: throw EntityNotFoundException("No offered service found with id ${request.id}")
        return offeredServiceRepo.save(newOfferedService)
    }

    data class CreateOfferedServiceRequest(val name: String, val price: BigDecimal)
    data class SetDiscountRequest(val id: Int, val discount: BigDecimal)
}

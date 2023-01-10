package com.severett.kotlinintro.service

import com.severett.kotlinintro.model.OfferedService
import com.severett.kotlinintro.model.OfferedServiceConstants.OFFERED_SERVICE_ONE
import com.severett.kotlinintro.model.Pet
import com.severett.kotlinintro.repo.OfferedServiceRepo
import com.severett.kotlinintro.service.OfferedServiceService.CreateOfferedServiceRequest
import com.severett.kotlinintro.service.OfferedServiceService.SetDiscountRequest
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import java.math.BigDecimal
import java.util.*

class OfferedServiceServiceTest {
    private val offeredServiceRepo: OfferedServiceRepo = mock()
    private lateinit var offeredServiceService: OfferedServiceService

    @BeforeEach
    fun setup() {
        offeredServiceService = OfferedServiceService(offeredServiceRepo)
        `when`(offeredServiceRepo.findById(any())).thenReturn(Optional.empty())
        `when`(offeredServiceRepo.findById(1)).thenReturn(Optional.of(OFFERED_SERVICE_ONE))
        `when`(offeredServiceRepo.save(any())).then(returnsFirstArg<Pet>())
    }

    @Test
    fun getOfferedServiceTest() {
        val offeredService = offeredServiceService.getOfferedService(1).get()
        assertEquals(OFFERED_SERVICE_ONE, offeredService)
    }

    @Test
    fun offeredServiceNotFoundTest() {
        val offeredService = offeredServiceService.getOfferedService(5)
        assertEquals(Optional.empty<OfferedService>(), offeredService)
    }

    @Test
    fun createOfferedServiceTest() {
        val givenName = "Vaccines"
        val givenPrice = BigDecimal.valueOf(15)
        val (_, name, price) = offeredServiceService.createOfferedService(
            CreateOfferedServiceRequest(givenName, givenPrice)
        )
        assertAll(
            { assertEquals(givenName, name) },
            { assertEquals(givenPrice, price) }
        )
    }

    @Test
    fun setDiscountTest() {
        val discount = BigDecimal.valueOf(5.5)
        val request = SetDiscountRequest(OFFERED_SERVICE_ONE.id, discount)
        val savedOfferedService = offeredServiceService.setDiscount(request)
        val expectedOfferedService = OFFERED_SERVICE_ONE.copy(discount = discount)
        assertEquals(expectedOfferedService, savedOfferedService)
    }

    @Test
    fun setDiscountFailTest() {
        val unknownId = 10
        val exception = Assertions.assertThrows(EntityNotFoundException::class.java) {
            offeredServiceService.setDiscount(
                SetDiscountRequest(
                    unknownId,
                    BigDecimal.ONE
                )
            )
        }
        assertEquals("No offered service found with id $unknownId", exception.message)
    }
}

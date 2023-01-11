package com.severett.kotlinintro.controller

import com.severett.kotlinintro.model.OfferedServiceConstants.OFFERED_SERVICE_ONE
import com.severett.kotlinintro.service.OfferedServiceService
import com.severett.kotlinintro.service.OfferedServiceService.CreateOfferedServiceRequest
import com.severett.kotlinintro.service.OfferedServiceService.SetDiscountRequest
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.util.*
import java.util.stream.*

private val RUNTIME_EXCEPTION = RuntimeException("Something bad!")

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OfferedServiceControllerTest {
    private val offeredServiceService: OfferedServiceService = mock()
    private lateinit var offeredServiceController: OfferedServiceController

    @BeforeEach
    fun setup() {
        offeredServiceController = OfferedServiceController(offeredServiceService)
    }

    @AfterEach
    fun shutdown() {
        reset(offeredServiceService)
    }

    @Test
    fun getOfferedServiceTest() {
        val id = OFFERED_SERVICE_ONE.id
        `when`(offeredServiceService.getOfferedService(id)).thenReturn(OFFERED_SERVICE_ONE)
        val offeredServiceResponse = offeredServiceController.getOfferedService(id)
        assertAll(
            { assertEquals(HttpStatus.OK, offeredServiceResponse.statusCode) },
            { assertEquals(OFFERED_SERVICE_ONE, offeredServiceResponse.body) },
        )
    }

    @Test
    fun getNonExistentOfferedServiceTest() {
        val id = OFFERED_SERVICE_ONE.id
        `when`(offeredServiceService.getOfferedService(any())).thenReturn(null)
        val offeredServiceResponse = offeredServiceController.getOfferedService(id)
        assertAll(
            { assertEquals(HttpStatus.NOT_FOUND, offeredServiceResponse.statusCode) },
            { assertFalse(offeredServiceResponse.hasBody()) },
        )
    }

    @Test
    fun createOfferedServiceTest() {
        val request = CreateOfferedServiceRequest(OFFERED_SERVICE_ONE.name, OFFERED_SERVICE_ONE.price)
        `when`(offeredServiceService.createOfferedService(request)).thenReturn(OFFERED_SERVICE_ONE)
        val offeredServiceResponse = offeredServiceController.createOfferedService(request)
        assertAll(
            { assertEquals(HttpStatus.CREATED, offeredServiceResponse.statusCode) },
            { assertEquals(OFFERED_SERVICE_ONE, offeredServiceResponse.body) },
        )
    }

    @Test
    fun setDiscountTest() {
        val id = 1
        val discount = BigDecimal.valueOf(5.5)
        val expectedOfferedService = OFFERED_SERVICE_ONE.copy(discount = discount)
        `when`(offeredServiceService.setDiscount(SetDiscountRequest(id, discount)))
            .thenReturn(expectedOfferedService)

        val offeredServiceResponse = offeredServiceController.setDiscount(id, discount)
        assertAll(
            { assertEquals(HttpStatus.OK, offeredServiceResponse.statusCode) },
            { assertEquals(expectedOfferedService, offeredServiceResponse.body) },
        )
    }

    @ParameterizedTest
    @MethodSource
    fun setDiscountExceptionTest(thrownException: Throwable, expectedStatus: HttpStatus) {
        val id = 1
        val discount = BigDecimal.valueOf(5.5)
        `when`(offeredServiceService.setDiscount(SetDiscountRequest(id, discount)))
            .thenThrow(thrownException)
        val offeredServiceResponse = offeredServiceController.setDiscount(id, discount)
        assertAll(
            { assertEquals(expectedStatus, offeredServiceResponse.statusCode) },
            { assertFalse(offeredServiceResponse.hasBody()) },
        )
    }

    private fun setDiscountExceptionTest(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(EntityNotFoundException("Not found!"), HttpStatus.NOT_FOUND),
            Arguments.of(RUNTIME_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR)
        )
    }
}

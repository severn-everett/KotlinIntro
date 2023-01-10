package com.severett.kotlinintro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.severett.kotlinintro.model.OfferedService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import java.math.BigDecimal

private const val CHECKUP_NAME = "Checkup"
private val CHECKUP_PRICE = BigDecimal("25.50")

@SpringBootTest
@AutoConfigureMockMvc
class OfferedServiceControllerIT(@Autowired private val mvc: MockMvc) {
    private val objectMapper = ObjectMapper()

    @Test
    fun getOfferedServiceTest() {
        val response = mvc.perform(get("/offered_services/1")).andReturn().response
        assertEquals(HttpStatus.OK.value(), response.status)
        val body = objectMapper.readValue(response.contentAsString, OfferedService::class.java)
        checkOfferedService(body, CHECKUP_NAME, CHECKUP_PRICE, BigDecimal("0.00"))
    }

    @Test
    fun getOfferedServiceNotFoundTest() {
        val response = mvc.perform(get("/offered_services/2000")).andReturn().response
        assertAll(
            { assertEquals(HttpStatus.NOT_FOUND.value(), response.status) },
            { assertEquals(0, response.contentLength) },
        )
    }

    @Test
    @Transactional
    fun createOfferedServiceTest() {
        val offeredServiceName = "Microchip Injection"
        val price = BigDecimal("15.00")
        val bodyMap = buildMap {
            put("name", offeredServiceName)
            put("price", price)
        }
        val response = mvc.perform(
            post("/offered_services")
                .content(objectMapper.writeValueAsBytes(bodyMap))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response
        assertEquals(HttpStatus.CREATED.value(), response.status)
        val body = objectMapper.readValue(response.contentAsString, OfferedService::class.java)
        checkOfferedService(body, offeredServiceName, price, BigDecimal.ZERO)
    }

    @Test
    @Transactional
    fun setDiscountTest() {
        val discount = BigDecimal("5.50")
        val response = mvc.perform(put("/offered_services/1/setDiscount?discount=$discount"))
            .andReturn().response
        assertEquals(HttpStatus.OK.value(), response.status)
        val body = objectMapper.readValue(response.contentAsString, OfferedService::class.java)
        checkOfferedService(body, CHECKUP_NAME, CHECKUP_PRICE, discount)
    }

    @Test
    @Transactional
    fun setDiscountNotFoundTest() {
        val response = mvc.perform(put("/offered_services/2000/setDiscount?discount=5.50"))
            .andReturn().response
        assertAll(
            { assertEquals(HttpStatus.NOT_FOUND.value(), response.status) },
            { assertEquals(0, response.contentLength) },
        )
    }

    private fun checkOfferedService(
        offeredService: OfferedService,
        name: String,
        price: BigDecimal,
        discount: BigDecimal
    ) {
        assertAll(
            { assertEquals(name, offeredService.name) },
            { assertEquals(price, offeredService.price) },
            { assertEquals(discount, offeredService.discount) },
        )
    }
}

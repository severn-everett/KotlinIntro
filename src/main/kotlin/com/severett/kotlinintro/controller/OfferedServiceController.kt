package com.severett.kotlinintro.controller

import com.severett.kotlinintro.model.OfferedService
import com.severett.kotlinintro.service.OfferedServiceService
import com.severett.kotlinintro.service.OfferedServiceService.CreateOfferedServiceRequest
import com.severett.kotlinintro.service.OfferedServiceService.SetDiscountRequest
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping(value = ["/offered_services"], produces = [APPLICATION_JSON_VALUE])
open class OfferedServiceController(private val offeredServiceService: OfferedServiceService) {
    private val log = LoggerFactory.getLogger(OfferedServiceController::class.java)

    @GetMapping("/{id}")
    open fun getOfferedService(@PathVariable id: Int): ResponseEntity<OfferedService> {
        return offeredServiceService.getOfferedService(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    open fun createOfferedService(
        @RequestBody request: CreateOfferedServiceRequest
    ): ResponseEntity<OfferedService> {
        return try {
            ResponseEntity<OfferedService>(offeredServiceService.createOfferedService(request), HttpStatus.CREATED)
        } catch (e: Exception) {
            log.error("Unable to persist {}", request, e)
            ResponseEntity.internalServerError().build()
        }
    }

    @PutMapping("/{id}/setDiscount")
    open fun setDiscount(@PathVariable id: Int, @RequestParam discount: BigDecimal): ResponseEntity<OfferedService> {
        return try {
            val request = SetDiscountRequest(id, discount)
            ResponseEntity.ok(offeredServiceService.setDiscount(request))
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: java.lang.Exception) {
            log.error("Unable to set discount for offered service #{}", id, e)
            ResponseEntity.internalServerError().build()
        }
    }
}

package com.severett.kotlinintro.controller

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerAdvisor {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(enfe: EntityNotFoundException, webRequest: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.notFound().build()
    }
}

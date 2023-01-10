package com.severett.kotlinintro.controller

import com.severett.kotlinintro.model.PetType
import com.severett.kotlinintro.service.PetService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = ["/pets"], produces = [MediaType.APPLICATION_JSON_VALUE])
open class PetsController(private val petService: PetService) {
    @GetMapping("/{type}/{id}")
    open fun get(@PathVariable type: PetType, @PathVariable id: Int) = petService.get(type, id)

    @GetMapping
    open fun getAll() = petService.getAll()

    @PostMapping("/{type}")
    @ResponseBody
    open fun create(@PathVariable type: PetType, @RequestBody data: Map<String, Any>) = petService.create(type, data)

    @DeleteMapping("/{type}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open fun delete(@PathVariable type: PetType, @PathVariable id: Int) = petService.delete(type, id)
}

package com.severett.kotlinintro.config

import com.severett.kotlinintro.repo.PetRepo
import com.severett.kotlinintro.service.PetService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class PetsConfiguration(@param:Value("\${serviceName}") private val serviceName: String) {
    @Bean
    open fun petService(petRepo: PetRepo) = PetService(serviceName, petRepo)
}

package com.severett.kotlinintro.service

import com.severett.kotlinintro.exception.InternalException
import com.severett.kotlinintro.model.Cat
import com.severett.kotlinintro.model.Dog
import com.severett.kotlinintro.model.Horse
import com.severett.kotlinintro.model.Pet
import com.severett.kotlinintro.model.PetType
import com.severett.kotlinintro.repo.PetRepo
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory

private const val NAME_FIELD = "name"
private const val CLAW_LENGTH_FIELD = "clawLength"
private const val TAIL_LENGTH_FIELD = "tailLength"
private const val HOOF_WIDTH_FIELD = "hoofWidth"

open class PetService(private val serviceName: String, private val petRepo: PetRepo) {
    private val log = LoggerFactory.getLogger(PetService::class.java)

    @PostConstruct
    open fun postConstruct() {
        log.info("$serviceName has been constructed!")
    }

    open fun get(type: PetType, id: Int) = petRepo.get(type, id)

    open fun getAll() = petRepo.getAll()

    @Transactional
    open fun create(type: PetType, data: Map<String, Any>): Pet {
        val petToCreate = when (type) {
            PetType.cat -> Cat(name = data[NAME_FIELD] as String, clawLength = data[CLAW_LENGTH_FIELD] as Double)
            PetType.dog -> Dog(name = data[NAME_FIELD] as String, tailLength = data[TAIL_LENGTH_FIELD] as Double)
            PetType.horse -> Horse(name = data[NAME_FIELD] as String, hoofWidth = data[HOOF_WIDTH_FIELD] as Double)
        }
        return petRepo.save(petToCreate)
    }

    @Transactional
    open fun delete(type: PetType, id: Int) = petRepo.delete(type, id)
}

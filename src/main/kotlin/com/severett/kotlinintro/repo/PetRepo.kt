package com.severett.kotlinintro.repo

import com.severett.kotlinintro.exception.InternalException
import com.severett.kotlinintro.model.Cat
import com.severett.kotlinintro.model.Dog
import com.severett.kotlinintro.model.Horse
import com.severett.kotlinintro.model.Pet
import com.severett.kotlinintro.model.PetType
import jakarta.persistence.EntityNotFoundException
import org.apache.commons.collections4.IterableUtils
import org.springframework.stereotype.Repository

@Repository
open class PetRepo(private val catRepo: CatRepo, private val dogRepo: DogRepo, private val horseRepo: HorseRepo) {
    open fun get(type: PetType, id: Int): Pet {
        val pet = findRepo(type).findById(id)
        return pet.orElseThrow { EntityNotFoundException("No pet of type '$type' with id $id found") }
    }

    open fun getAll() = LinkedHashMap<String, List<Pet>>().apply {
        put(PetType.cat.displayName, IterableUtils.toList(catRepo.findAll()))
        put(PetType.dog.displayName, IterableUtils.toList(dogRepo.findAll()))
        put(PetType.horse.displayName, IterableUtils.toList(horseRepo.findAll()))
    }

    open fun save(pet: Pet): Pet {
        return when(pet) {
            is Cat -> catRepo.save(pet)
            is Dog -> dogRepo.save(pet)
            is Horse -> horseRepo.save(pet)
            else -> throw InternalException("No repository found for class '${Pet::class.java}'")
        }
    }

    open fun delete(type: PetType, id: Int) {
        findRepo(type).deleteById(id)
    }

    private fun findRepo(type: PetType) = when(type) {
        PetType.cat -> catRepo
        PetType.dog -> dogRepo
        PetType.horse -> horseRepo
    }
}

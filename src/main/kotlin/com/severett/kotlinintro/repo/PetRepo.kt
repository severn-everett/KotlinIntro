package com.severett.kotlinintro.repo

import com.severett.kotlinintro.model.Cat
import com.severett.kotlinintro.model.Dog
import com.severett.kotlinintro.model.Horse
import com.severett.kotlinintro.model.Pet
import com.severett.kotlinintro.model.PetType
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
open class PetRepo(private val catRepo: CatRepo, private val dogRepo: DogRepo, private val horseRepo: HorseRepo) {
    open fun get(type: PetType, id: Int): Pet {
        val pet = useRepo(type) { it.findByIdOrNull(id) }
        return pet ?: throw EntityNotFoundException("No pet of type '$type' with id $id found")
    }

    open fun getAll() = mapOf(
        PetType.cat.displayName to catRepo.findAll().toList(),
        PetType.dog.displayName to dogRepo.findAll().toList(),
        PetType.horse.displayName to horseRepo.findAll().toList(),
    )

    open fun save(pet: Pet): Pet {
        return when(pet) {
            is Cat -> catRepo.save(pet)
            is Dog -> dogRepo.save(pet)
            is Horse -> horseRepo.save(pet)
        }
    }

    open fun delete(type: PetType, id: Int) {
        useRepo(type) { it.deleteById(id) }
    }

    private inline fun <T> useRepo(type: PetType, block: (CrudRepository<out Pet, Int>) -> T): T {
        val targetRepo = when(type) {
            PetType.cat -> catRepo
            PetType.dog -> dogRepo
            PetType.horse -> horseRepo
        }
        return block.invoke(targetRepo)
    }
}

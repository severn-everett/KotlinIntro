package com.severett.kotlinintro.repo

import com.severett.kotlinintro.model.Dog
import org.springframework.data.repository.CrudRepository

interface DogRepo : CrudRepository<Dog, Int>

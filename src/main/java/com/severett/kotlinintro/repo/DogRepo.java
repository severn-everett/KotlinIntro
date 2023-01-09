package com.severett.kotlinintro.repo;

import com.severett.kotlinintro.model.Dog;
import org.springframework.data.repository.CrudRepository;

public interface DogRepo extends CrudRepository<Dog, Integer> {
}

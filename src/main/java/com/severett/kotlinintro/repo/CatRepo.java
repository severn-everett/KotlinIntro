package com.severett.kotlinintro.repo;

import com.severett.kotlinintro.model.Cat;
import org.springframework.data.repository.CrudRepository;

public interface CatRepo extends CrudRepository<Cat, Integer> {
}

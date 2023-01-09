package com.severett.kotlinintro.repo;

import com.severett.kotlinintro.model.Horse;
import org.springframework.data.repository.CrudRepository;

public interface HorseRepo extends CrudRepository<Horse, Integer> {
}

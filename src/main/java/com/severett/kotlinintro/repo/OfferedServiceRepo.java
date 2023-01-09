package com.severett.kotlinintro.repo;

import com.severett.kotlinintro.model.OfferedService;
import org.springframework.data.repository.CrudRepository;

public interface OfferedServiceRepo extends CrudRepository<OfferedService, Integer> {
}

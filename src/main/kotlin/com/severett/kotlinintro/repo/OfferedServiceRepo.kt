package com.severett.kotlinintro.repo

import com.severett.kotlinintro.model.OfferedService
import org.springframework.data.repository.CrudRepository

interface OfferedServiceRepo : CrudRepository<OfferedService, Int>

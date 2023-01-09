package com.severett.kotlinintro.config;

import com.severett.kotlinintro.repo.PetRepo;
import com.severett.kotlinintro.service.PetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PetsConfiguration {
    private final String serviceName;

    public PetsConfiguration(@Value("${serviceName}") String serviceName) {
        this.serviceName = serviceName;
    }

    @Bean
    public PetService petService(PetRepo petRepo) {
        return new PetService(serviceName, petRepo);
    }
}

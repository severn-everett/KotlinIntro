package com.severett.kotlinintro.service;

import com.severett.kotlinintro.exception.EntityNotFoundException;
import com.severett.kotlinintro.model.Pet;
import com.severett.kotlinintro.model.PetType;
import com.severett.kotlinintro.repo.PetRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PetService {
    private final PetRepo petRepo;

    public Pet get(PetType type, int id) throws EntityNotFoundException {
        return petRepo.get(type, id);
    }

    public Map<String, List<? extends Pet>> getAll() {
        return petRepo.getAll();
    }

    public void delete(PetType type, int id) {
        petRepo.delete(type, id);
    }
}

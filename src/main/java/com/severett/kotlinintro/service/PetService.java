package com.severett.kotlinintro.service;

import com.severett.kotlinintro.exception.EntityNotFoundException;
import com.severett.kotlinintro.exception.InternalException;
import com.severett.kotlinintro.model.Cat;
import com.severett.kotlinintro.model.Dog;
import com.severett.kotlinintro.model.Horse;
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
    private static final String NAME_FIELD = "name";
    private static final String CLAW_LENGTH_FIELD = "clawLength";
    private static final String TAIL_LENGTH_FIELD = "tailLength";
    private static final String HOOF_WIDTH_FIELD = "hoofWidth";

    private final PetRepo petRepo;

    public Pet get(PetType type, int id) throws EntityNotFoundException {
        return petRepo.get(type, id);
    }

    public Map<String, List<? extends Pet>> getAll() {
        return petRepo.getAll();
    }

    public void create(PetType type, Map<String, Object> data) throws InternalException {
        Pet petToCreate = switch (type) {
            case cat -> new Cat(0, (String) data.get(NAME_FIELD), (Double) data.get(CLAW_LENGTH_FIELD));
            case dog -> new Dog(0, (String) data.get(NAME_FIELD), (Double) data.get(TAIL_LENGTH_FIELD));
            case horse -> new Horse(0, (String) data.get(NAME_FIELD), (Double) data.get(HOOF_WIDTH_FIELD));
        };
        petRepo.save(petToCreate);
    }

    public void delete(PetType type, int id) {
        petRepo.delete(type, id);
    }
}

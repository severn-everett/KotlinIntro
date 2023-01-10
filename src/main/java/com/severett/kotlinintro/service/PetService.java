package com.severett.kotlinintro.service;

import com.severett.kotlinintro.exception.InternalException;
import com.severett.kotlinintro.model.Cat;
import com.severett.kotlinintro.model.Dog;
import com.severett.kotlinintro.model.Horse;
import com.severett.kotlinintro.model.Pet;
import com.severett.kotlinintro.model.PetType;
import com.severett.kotlinintro.repo.PetRepo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class PetService {
    private static final String NAME_FIELD = "name";
    private static final String CLAW_LENGTH_FIELD = "clawLength";
    private static final String TAIL_LENGTH_FIELD = "tailLength";
    private static final String HOOF_WIDTH_FIELD = "hoofWidth";

    private final String serviceName;
    private final PetRepo petRepo;

    @PostConstruct
    public void postConstruct() {
        log.info(serviceName + " has been constructed!");
    }

    public Pet get(PetType type, int id) throws InternalException {
        return petRepo.get(type, id);
    }

    public Map<String, List<? extends Pet>> getAll() {
        return petRepo.getAll();
    }

    @Transactional
    public Pet create(PetType type, Map<String, Object> data) throws InternalException {
        Pet petToCreate;
        switch (type) {
            case cat: petToCreate = new Cat(0, (String) data.get(NAME_FIELD), (Double) data.get(CLAW_LENGTH_FIELD)); break;
            case dog: petToCreate = new Dog(0, (String) data.get(NAME_FIELD), (Double) data.get(TAIL_LENGTH_FIELD)); break;
            case horse: petToCreate = new Horse(0, (String) data.get(NAME_FIELD), (Double) data.get(HOOF_WIDTH_FIELD)); break;
            default: throw new InternalException("Unrecognized type '" + type + "'");
        }
        return petRepo.save(petToCreate);
    }

    @Transactional
    public void delete(PetType type, int id) throws InternalException {
        petRepo.delete(type, id);
    }
}

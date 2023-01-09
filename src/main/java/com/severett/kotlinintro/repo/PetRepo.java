package com.severett.kotlinintro.repo;

import com.severett.kotlinintro.exception.InternalException;
import com.severett.kotlinintro.model.Cat;
import com.severett.kotlinintro.model.Dog;
import com.severett.kotlinintro.model.Horse;
import com.severett.kotlinintro.model.Pet;
import com.severett.kotlinintro.model.PetType;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class PetRepo {
    private final CatRepo catRepo;
    private final DogRepo dogRepo;
    private final HorseRepo horseRepo;

    public Pet get(PetType type, int id) {
        var pet = findRepo(type).findById(id);

        return pet.orElseThrow(() -> new EntityNotFoundException("No pet of type '" + type + "' with id " + id + " found"));
    }

    public Map<String, List<? extends Pet>> getAll() {
        var pets = new LinkedHashMap<String, List<? extends Pet>>();
        pets.put(PetType.cat.getDisplayName(), IterableUtils.toList(catRepo.findAll()));
        pets.put(PetType.dog.getDisplayName(), IterableUtils.toList(dogRepo.findAll()));
        pets.put(PetType.horse.getDisplayName(), IterableUtils.toList(horseRepo.findAll()));
        return pets;
    }

    public Pet save(Pet pet) throws InternalException {
        if (pet instanceof Dog dog) {
            return dogRepo.save(dog);
        } else if (pet instanceof Cat cat) {
            return catRepo.save(cat);
        } else if (pet instanceof Horse horse) {
            return horseRepo.save(horse);
        } else {
            throw new InternalException("No repository found for class '" + Pet.class + "'");
        }
    }

    public void delete(PetType type, int id) {
        findRepo(type).deleteById(id);
    }

    private CrudRepository<? extends Pet, Integer> findRepo(PetType type) {
        return switch (type) {
            case dog -> dogRepo;
            case cat -> catRepo;
            case horse -> horseRepo;
        };
    }
}

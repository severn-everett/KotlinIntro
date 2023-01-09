package com.severett.kotlinintro.controller;

import com.severett.kotlinintro.exception.EntityNotFoundException;
import com.severett.kotlinintro.exception.InternalException;
import com.severett.kotlinintro.model.Pet;
import com.severett.kotlinintro.model.PetType;
import com.severett.kotlinintro.service.PetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/pets", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class PetsController {

    private final PetService petService;

    @GetMapping("/{type}/{id}")
    public Pet get(@PathVariable PetType type, @PathVariable int id) throws EntityNotFoundException {
        return petService.get(type, id);
    }

    @GetMapping()
    public Map<String, List<? extends Pet>> getAll() {
        return petService.getAll();
    }

    @PostMapping("/{type}")
    public void create(@PathVariable PetType type, @RequestBody Map<String, Object> data) throws InternalException {
        petService.create(type, data);
    }

    @DeleteMapping("/{type}/{id}")
    public void delete(@PathVariable PetType type, @PathVariable int id) {
        petService.delete(type, id);
    }
}

package com.severett.kotlinintro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.severett.kotlinintro.model.PetType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetsControllerIT {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvc mvc;

    public PetsControllerIT(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @ParameterizedTest
    @MethodSource
    public void getPetsTest(String url, String expectedFile) throws Exception {
        checkResponse(mvc.perform(get(url)).andReturn().getResponse(), expectedFile);
    }

    @Test
    public void getUnrecognizedTypeTest() throws Exception {
        mvc.perform(get("/pets/UNRECOGNIZED_TYPE/1")).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @EnumSource(PetType.class)
    public void getNotFoundTest(PetType type) throws Exception {
        mvc.perform(get("/pets/" + type + "/9999")).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource
    @Transactional
    public void createPetTest(String inputFile, String url, String expectedFile) throws Exception {
        var input = getFileContent(inputFile);
        checkResponse(
                mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input)).andReturn().getResponse(),
                expectedFile
        );
    }

    @Test
    public void createUnrecognizedTypeTest() throws Exception {
        var input = getFileContent("/request/unrecognized_type.json");
        mvc.perform(post("/pets/UNRECOGNIZED_TYPE").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource
    @Transactional
    public void removePetTest(String url, String expectedFile) throws Exception {
        mvc.perform(delete(url)).andExpect(status().isNoContent());
        checkResponse(mvc.perform(get("/pets")).andReturn().getResponse(), expectedFile);
    }

    @Test
    public void removeUnrecognizedTypeTest() throws Exception {
        mvc.perform(delete("/pets/UNRECOGNIZED_TYPE/1")).andExpect(status().isBadRequest());
        checkResponse(mvc.perform(get("/pets")).andReturn().getResponse(), "/expected/existing/all_pets.json");
    }

    private void checkResponse(MockHttpServletResponse response, String expectedFile) throws Exception {
        var expectedContent = objectMapper.readTree(getFileContent(expectedFile));
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(expectedContent, objectMapper.readTree(response.getContentAsString()))
        );
    }

    private Stream<Arguments> getPetsTest() {
        return Stream.of(
                Arguments.of("/pets/cat/1", "/expected/existing/cat.json"),
                Arguments.of("/pets/dog/1", "/expected/existing/dog.json"),
                Arguments.of("/pets/horse/1", "/expected/existing/horse.json"),
                Arguments.of("/pets", "/expected/existing/all_pets.json")
        );
    }

    private Stream<Arguments> createPetTest() {
        return Stream.of(
                Arguments.of("/request/cat.json", "/pets/cat", "/expected/new/cat.json"),
                Arguments.of("/request/dog.json", "/pets/dog", "/expected/new/dog.json"),
                Arguments.of("/request/horse.json", "/pets/horse", "/expected/new/horse.json")
        );
    }

    private Stream<Arguments> removePetTest() {
        return Stream.of(
                Arguments.of("/pets/cat/1", "/expected/removed/cat.json"),
                Arguments.of("/pets/dog/1", "/expected/removed/dog.json"),
                Arguments.of("/pets/horse/1", "/expected/removed/horse.json")
        );
    }

    private String getFileContent(String filePath) throws Exception {
        //noinspection ConstantConditions
        return String.join(
                "",
                Files.readAllLines(
                        Paths.get(this.getClass().getResource(filePath).toURI()),
                        Charset.defaultCharset()
                )
        );
    }
}

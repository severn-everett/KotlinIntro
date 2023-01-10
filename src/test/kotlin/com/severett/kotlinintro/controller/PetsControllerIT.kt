package com.severett.kotlinintro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.severett.kotlinintro.model.PetType
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.*

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetsControllerIT(@Autowired private val mvc: MockMvc) {
    private val objectMapper = ObjectMapper()

    @ParameterizedTest
    @MethodSource
    fun getPetsTest(url: String, expectedFile: String) {
        checkResponse(mvc.perform(get(url)).andReturn().response, expectedFile)
    }

    @Test
    fun getUnrecognizedTypeTest() {
        mvc.perform(get("/pets/UNRECOGNIZED_TYPE/1")).andExpect(status().isBadRequest)
    }

    @ParameterizedTest
    @EnumSource(PetType::class)
    fun getNotFoundTest(type: PetType) {
        mvc.perform(get("/pets/$type/9999")).andExpect(status().isNotFound)
    }

    @ParameterizedTest
    @MethodSource
    @Transactional
    fun createPetTest(inputFile: String, url: String, expectedFile: String) {
        val input = getFileContent(inputFile)
        checkResponse(
            mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(input)).andReturn().response,
            expectedFile
        )
    }

    @Test
    fun createUnrecognizedTypeTest() {
        val input = getFileContent("/request/unrecognized_type.json")
        mvc.perform(
            post("/pets/UNRECOGNIZED_TYPE").contentType(MediaType.APPLICATION_JSON)
                .content(input)
        ).andExpect(status().isBadRequest)
    }

    @ParameterizedTest
    @MethodSource
    @Transactional
    fun removePetTest(url: String, expectedFile: String) {
        mvc.perform(delete(url)).andExpect(status().isNoContent)
        checkResponse(mvc.perform(get("/pets")).andReturn().response, expectedFile)
    }

    @Test
    fun removeUnrecognizedTypeTest() {
        mvc.perform(delete("/pets/UNRECOGNIZED_TYPE/1"))
            .andExpect(status().isBadRequest)
        checkResponse(
            mvc.perform(get("/pets")).andReturn().response,
            "/expected/existing/all_pets.json"
        )
    }

    private fun checkResponse(response: MockHttpServletResponse, expectedFile: String) {
        val expectedContent = objectMapper.readTree(getFileContent(expectedFile))
        assertAll(
            { assertEquals(HttpStatus.OK.value(), response.status) },
            { assertEquals(expectedContent, objectMapper.readTree(response.contentAsString)) }
        )
    }

    private fun getPetsTest(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("/pets/cat/1", "/expected/existing/cat.json"),
            Arguments.of("/pets/dog/1", "/expected/existing/dog.json"),
            Arguments.of("/pets/horse/1", "/expected/existing/horse.json"),
            Arguments.of("/pets", "/expected/existing/all_pets.json")
        )
    }

    private fun createPetTest(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("/request/cat.json", "/pets/cat", "/expected/new/cat.json"),
            Arguments.of("/request/dog.json", "/pets/dog", "/expected/new/dog.json"),
            Arguments.of("/request/horse.json", "/pets/horse", "/expected/new/horse.json")
        )
    }

    private fun removePetTest(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("/pets/cat/1", "/expected/removed/cat.json"),
            Arguments.of("/pets/dog/1", "/expected/removed/dog.json"),
            Arguments.of("/pets/horse/1", "/expected/removed/horse.json")
        )
    }

    private fun getFileContent(filePath: String): String {
        return java.lang.String.join(
            "",
            Files.readAllLines(
                Paths.get(this.javaClass.getResource(filePath)!!.toURI()),
                Charset.defaultCharset()
            )
        )
    }
}

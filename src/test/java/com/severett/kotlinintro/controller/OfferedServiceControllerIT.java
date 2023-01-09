package com.severett.kotlinintro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.severett.kotlinintro.model.OfferedService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferedServiceControllerIT {
    private static final String CHECKUP_NAME = "Checkup";
    private static final BigDecimal CHECKUP_PRICE = new BigDecimal("25.50");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvc mvc;

    public OfferedServiceControllerIT(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void getOfferedServiceTest() throws Exception {
        var response = mvc.perform(get("/offered_services/1")).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        var body = objectMapper.readValue(response.getContentAsString(), OfferedService.class);
        checkDish(body, CHECKUP_NAME, CHECKUP_PRICE, new BigDecimal("0.00"));
    }

    @Test
    public void getOfferedServiceNotFoundTest() throws Exception {
        var response = mvc.perform(get("/offered_services/2000")).andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals(0, response.getContentLength())
        );
    }

    @Test
    @Transactional
    public void createOfferedServiceTest() throws Exception {
        var offeredServiceName = "Microchip Injection";
        var price = new BigDecimal("15.00");
        var bodyMap = new HashMap<String, Object>();
        bodyMap.put("name", offeredServiceName);
        bodyMap.put("price", price);

        var response = mvc.perform(
                post("/offered_services")
                        .content(objectMapper.writeValueAsBytes(bodyMap))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        var body = objectMapper.readValue(response.getContentAsString(), OfferedService.class);
        checkDish(body, offeredServiceName, price, BigDecimal.ZERO);
    }

    @Test
    @Transactional
    public void setDiscountTest() throws Exception {
        var discount = new BigDecimal("5.50");

        var response = mvc.perform(
                put("/offered_services/1/setDiscount?discount=" + discount)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        var body = objectMapper.readValue(response.getContentAsString(), OfferedService.class);
        checkDish(body, CHECKUP_NAME, CHECKUP_PRICE, discount);
    }

    @Test
    @Transactional
    public void setDiscountNotFoundTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                put("/offered_services/2000/setDiscount?discount=5.50")
        ).andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals(0, response.getContentLength())
        );
    }

    private void checkDish(OfferedService offeredService, String name, BigDecimal price, BigDecimal discount) {
        assertAll(
                () -> assertEquals(name, offeredService.getName()),
                () -> assertEquals(price, offeredService.getPrice()),
                () -> assertEquals(discount, offeredService.getDiscount())
        );
    }
}

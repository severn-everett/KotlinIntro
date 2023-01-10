package com.severett.kotlinintro.controller;

import com.severett.kotlinintro.service.OfferedServiceService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static com.severett.kotlinintro.model.OfferedServiceConstants.OFFERED_SERVICE_ONE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OfferedServiceControllerTest {
    private static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException("Something bad!");
    private final OfferedServiceService offeredServiceService = mock(OfferedServiceService.class);
    private OfferedServiceController offeredServiceController;

    @BeforeEach
    public void setUp() {
        offeredServiceController = new OfferedServiceController(offeredServiceService);
    }

    @AfterEach
    public void shutdown() {
        reset(offeredServiceService);
    }

    @Test
    public void getOfferedServiceTest() {
        int id = OFFERED_SERVICE_ONE.getId();
        when(offeredServiceService.getOfferedService(id)).thenReturn(Optional.of(OFFERED_SERVICE_ONE));

        var offeredServiceResponse = offeredServiceController.getOfferedService(id);
        assertAll(
                () -> assertEquals(HttpStatus.OK, offeredServiceResponse.getStatusCode()),
                () -> assertEquals(OFFERED_SERVICE_ONE, offeredServiceResponse.getBody())
        );
    }

    @Test
    public void getNonExistentOfferedServiceTest() {
        int id = OFFERED_SERVICE_ONE.getId();
        when(offeredServiceService.getOfferedService(anyInt())).thenReturn(Optional.empty());

        var offeredServiceResponse = offeredServiceController.getOfferedService(id);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, offeredServiceResponse.getStatusCode()),
                () -> assertFalse(offeredServiceResponse.hasBody())
        );
    }

    @Test
    public void createOfferedServiceTest() {
        var request = new OfferedServiceService.CreateOfferedServiceRequest(
                OFFERED_SERVICE_ONE.getName(),
                OFFERED_SERVICE_ONE.getPrice()
        );
        when(offeredServiceService.createOfferedService(request)).thenReturn(OFFERED_SERVICE_ONE);

        var offeredServiceResponse = offeredServiceController.createOfferedService(request);
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, offeredServiceResponse.getStatusCode()),
                () -> assertEquals(OFFERED_SERVICE_ONE, offeredServiceResponse.getBody())
        );
    }

    @Test
    public void setDiscountTest() {
        var id = 1;
        var discount = BigDecimal.valueOf(5.5);
        var expectedOfferedService = OFFERED_SERVICE_ONE.toBuilder().discount(discount).build();
        when(offeredServiceService.setDiscount(new OfferedServiceService.SetDiscountRequest(id, discount)))
                .thenReturn(expectedOfferedService);

        var offeredServiceResponse = offeredServiceController.setDiscount(id, discount);
        assertAll(
                () -> assertEquals(HttpStatus.OK, offeredServiceResponse.getStatusCode()),
                () -> assertEquals(expectedOfferedService, offeredServiceResponse.getBody())
        );
    }

    @ParameterizedTest
    @MethodSource
    public void setDiscountExceptionTest(Throwable thrownException, HttpStatus expectedStatus) {
        var id = 1;
        var discount = BigDecimal.valueOf(5.5);
        when(offeredServiceService.setDiscount(new OfferedServiceService.SetDiscountRequest(id, discount)))
                .thenThrow(thrownException);

        var offeredServiceResponse = offeredServiceController.setDiscount(id, discount);
        assertAll(
                () -> assertEquals(expectedStatus, offeredServiceResponse.getStatusCode()),
                () -> assertFalse(offeredServiceResponse.hasBody())
        );
    }

    private Stream<Arguments> setDiscountExceptionTest() {
        return Stream.of(
                Arguments.of(new EntityNotFoundException("Not found!"), HttpStatus.NOT_FOUND),
                Arguments.of(RUNTIME_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }
}

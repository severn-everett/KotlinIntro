package com.severett.kotlinintro.service;

import com.severett.kotlinintro.repo.OfferedServiceRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.severett.kotlinintro.model.OfferedServiceConstants.OFFERED_SERVICE_ONE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class OfferedServiceServiceTest {
    private final OfferedServiceRepo offeredServiceRepo = mock(OfferedServiceRepo.class);
    private OfferedServiceService offeredServiceService;

    @BeforeEach
    public void setUp() {
        offeredServiceService = new OfferedServiceService(offeredServiceRepo);
        when(offeredServiceRepo.findById(anyInt())).thenReturn(Optional.empty());
        when(offeredServiceRepo.findById(1)).thenReturn(Optional.of(OFFERED_SERVICE_ONE));
        when(offeredServiceRepo.save(any())).then(returnsFirstArg());
    }

    @Test
    public void getOfferedServiceTest() {
        var offeredService = offeredServiceService.getOfferedService(1).get();
        assertEquals(OFFERED_SERVICE_ONE, offeredService);
    }

    @Test
    public void offeredServiceNotFoundTest() {
        var offeredService = offeredServiceService.getOfferedService(5);
        assertEquals(Optional.empty(), offeredService);
    }

    @Test
    public void createOfferedServiceTest() {
        var name = "Vaccines";
        BigDecimal price = BigDecimal.valueOf(15);
        var savedOfferedService = offeredServiceService.createOfferedService(
                new OfferedServiceService.CreateOfferedServiceRequest(name, price)
        );
        assertAll(
                () -> assertEquals(name, savedOfferedService.getName()),
                () -> assertEquals(price, savedOfferedService.getPrice())
        );
    }

    @Test
    public void setDiscountTest() {
        var discount = BigDecimal.valueOf(5.5);
        var request = new OfferedServiceService.SetDiscountRequest(
                OFFERED_SERVICE_ONE.getId(),
                discount
        );
        var savedOfferedService = offeredServiceService.setDiscount(request);
        var expectedOfferedService = OFFERED_SERVICE_ONE.toBuilder().discount(discount).build();
        assertEquals(expectedOfferedService, savedOfferedService);
    }

    @Test
    public void setDiscountFailTest() {
        int unknownId = 10;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                offeredServiceService.setDiscount(
                        new OfferedServiceService.SetDiscountRequest(
                                unknownId,
                                BigDecimal.ONE
                        )
                )
        );
        assertEquals("No offered service found with id " + unknownId, exception.getMessage());
    }
}

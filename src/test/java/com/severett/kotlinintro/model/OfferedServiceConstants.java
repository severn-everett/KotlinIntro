package com.severett.kotlinintro.model;

import java.math.BigDecimal;

public class OfferedServiceConstants {
    private OfferedServiceConstants() {
    }

    public static final OfferedService OFFERED_SERVICE_ONE = OfferedService.builder()
            .id(1)
            .name("Check-Up")
            .price(BigDecimal.TEN)
            .discount(BigDecimal.ZERO)
            .build();
}

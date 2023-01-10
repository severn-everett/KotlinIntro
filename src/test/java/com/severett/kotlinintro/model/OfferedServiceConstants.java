package com.severett.kotlinintro.model;

import java.math.BigDecimal;

public class OfferedServiceConstants {
    private OfferedServiceConstants() {
    }

    public static final OfferedService OFFERED_SERVICE_ONE = new OfferedService(
            1,
            "Check-Up",
            BigDecimal.TEN,
            BigDecimal.ZERO
    );
}

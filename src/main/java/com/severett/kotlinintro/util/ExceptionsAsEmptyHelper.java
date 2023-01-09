package com.severett.kotlinintro.util;

import org.slf4j.Logger;

import java.util.function.Supplier;

public class ExceptionsAsEmptyHelper {
    private ExceptionsAsEmptyHelper() {}

    public static <T> T exceptionsAsEmpty(Logger log, Supplier<T> fnc) {
        try {
            return fnc.get();
        } catch (Exception e) {
            log.error("Ignored exception: ", e);
            return null;
        }
    }
}

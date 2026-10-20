package com.alex.payroll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

/** Money helpers for tests: BigDecimal is compared by value with compareTo, never with equals. */
public final class TestMoney {

    private TestMoney() {
    }

    public static BigDecimal money(String value) {
        return new BigDecimal(value);
    }

    public static void assertMoney(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual),
                     "expected " + expected + " but was " + actual);
    }
}

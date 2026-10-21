package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

final class Validations {
    private Validations() {}

    static String requireText(String value, String field) {
        if  (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be null or empty");
        }

        return value;
    }

    static BigDecimal requirePositive(BigDecimal value, String field) {
        if(Objects.requireNonNull(value, field + " must not be null")
                .compareTo(BigDecimal.ZERO) <= 0) {
            throw new  IllegalArgumentException("The " + field + " must be greater than zero");
        }

        return value;
    }

    static LocalDate requireDate(LocalDate date, String field) {
        return Objects.requireNonNull(date, field + " must not be null");
    }

    static BigDecimal requireNonNegative(BigDecimal value, String field) {
        if(Objects.requireNonNull(value, field + " must not be null")
                .compareTo(BigDecimal.ZERO) >= 0) {
            return value;
        }

        throw new IllegalArgumentException("The " + field + " must be greater than or equal to zero");
    }
}

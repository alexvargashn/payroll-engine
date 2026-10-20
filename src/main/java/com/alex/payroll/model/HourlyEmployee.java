package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Paid per hour. Hours above 160 in the month are paid at 1.5x.
 * Result rounded to 2 decimals with RoundingMode.HALF_EVEN.
 *
 * TODO: hourlyRate must be positive; hoursWorked can be zero but not negative.
 */
public class HourlyEmployee extends Employee {

    public HourlyEmployee(String id, String name, LocalDate hiredOn, BigDecimal hourlyRate, BigDecimal hoursWorked) {
        super(id, name, hiredOn);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public BigDecimal monthlyPay() {
        throw new UnsupportedOperationException("TODO");
    }
}

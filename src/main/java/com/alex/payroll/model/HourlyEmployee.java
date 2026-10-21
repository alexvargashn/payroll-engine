package com.alex.payroll.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Paid per hour. Hours above 160 in the month are paid at 1.5x.
 * Result rounded to 2 decimals with RoundingMode.HALF_EVEN.
 *
 * TODO: hourlyRate must be positive; hoursWorked can be zero but not negative.
 */
public final class HourlyEmployee extends Employee {

    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");
    private static final BigDecimal STANDARD_HOURS = new BigDecimal("160");

    private final BigDecimal hourlyRate;
    private final BigDecimal hoursWorked;


    public HourlyEmployee(String id, String name, LocalDate hiredOn, BigDecimal hourlyRate, BigDecimal hoursWorked) {
        super(id, name, hiredOn);
        this.hourlyRate = Validations.requirePositive(hourlyRate, "hourlyRate");
        this.hoursWorked = Validations.requireNonNegative(hoursWorked, "hoursWorked");
    }

    @Override
    public BigDecimal monthlyPay() {
        BigDecimal regularHours = hoursWorked.min(STANDARD_HOURS);
        BigDecimal overtimeHours = hoursWorked.subtract(STANDARD_HOURS).max(BigDecimal.ZERO);

        BigDecimal regularPay = regularHours.multiply(hourlyRate);
        BigDecimal overtimePay = overtimeHours.multiply(hourlyRate).multiply(OVERTIME_MULTIPLIER);

        return regularPay.add(overtimePay).setScale(2, RoundingMode.HALF_EVEN);
    }
}

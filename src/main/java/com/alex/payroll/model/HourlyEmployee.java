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

    private final BigDecimal HOURLY_ABOVE_RATE = new BigDecimal("1.5");
    private final BigDecimal HOURS_STANDARD_RATE = new BigDecimal("160");

    private final BigDecimal hourlyRate;
    private final BigDecimal hoursWorked;


    public HourlyEmployee(String id, String name, LocalDate hiredOn, BigDecimal hourlyRate, BigDecimal hoursWorked) {
        super(id, name, hiredOn);
        this.hourlyRate = Validations.requirePositive(hourlyRate, "hourlyRate");
        this.hoursWorked = Validations.requireNonNegative(hoursWorked, "hoursWorked");
    }

    @Override
    public BigDecimal monthlyPay() {
        BigDecimal hoursStandard =  hoursWorked;
        BigDecimal monthlyPay = BigDecimal.ZERO;
        BigDecimal hoursAbove = BigDecimal.ZERO;

        if(hoursWorked.subtract(HOURS_STANDARD_RATE).compareTo(BigDecimal.ZERO) > 0) {
            hoursAbove = hoursWorked.subtract(HOURS_STANDARD_RATE);
            hoursStandard = hoursWorked.subtract(hoursAbove);
        }

        if(hoursAbove.compareTo(BigDecimal.ZERO) > 0) {
            monthlyPay = hoursAbove.multiply(hourlyRate).multiply(HOURLY_ABOVE_RATE);
        }

        monthlyPay = monthlyPay.add(hoursStandard.multiply(hourlyRate));
        return monthlyPay.setScale(2, RoundingMode.HALF_EVEN);
    }
}

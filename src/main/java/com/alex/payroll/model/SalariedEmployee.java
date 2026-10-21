package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Fixed monthly salary. TODO: salary must be positive. Done ✅ */
public sealed class SalariedEmployee extends Employee permits Manager {

    private final BigDecimal monthlySalary;

    public SalariedEmployee(String id, String name, LocalDate hiredOn, BigDecimal monthlySalary) {
        super(id, name, hiredOn);
        this.monthlySalary = requiereAndBePositive(monthlySalary);
    }

    @Override
    public BigDecimal monthlyPay() {
        return this.monthlySalary;
    }

    public BigDecimal requiereAndBePositive(BigDecimal monthlySalary) {
        if(monthlySalary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new  IllegalArgumentException("The monthly salary must be greater than zero");
        }

        return monthlySalary;
    }
}

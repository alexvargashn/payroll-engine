package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Fixed monthly salary. TODO: salary must be positive. */
public sealed class SalariedEmployee extends Employee permits Manager {

    public SalariedEmployee(String id, String name, LocalDate hiredOn, BigDecimal monthlySalary) {
        super(id, name, hiredOn);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public BigDecimal monthlyPay() {
        throw new UnsupportedOperationException("TODO");
    }
}

package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Common state and behavior of every employee.
 *
 * TODO:
 *  - private final fields: id, name, hiredOn - Done✅
 *  - validate in the constructor: id and name not blank, hiredOn not null -> IllegalArgumentException / NPE - Done✅
 *  - payeeId() returns id; amountDue() reuses monthlyPay() (consider making both final)
 *  - equals/hashCode by id only (two objects with the same id are the same employee)
 */
public sealed abstract class Employee implements Payable permits SalariedEmployee, HourlyEmployee {

    private final String id;
    private final String name;
    private final LocalDate hiredOn;

    protected Employee(String id, String name, LocalDate hiredOn) {
       this.id = Validations.requireText(id, "id");
       this.name = Validations.requireText(name, "name");
       this.hiredOn = Validations.requireDate(hiredOn, "hiredOn");
    }

    /** Each kind of employee is paid differently. */
    public abstract BigDecimal monthlyPay();

    @Override
    public String payeeId() {
        return this.id;
    }

    @Override
    public BigDecimal amountDue() {
        return this.monthlyPay();
    }

    public String id() {
        throw new UnsupportedOperationException("TODO");
    }

    public String name() {
        throw new UnsupportedOperationException("TODO");
    }

    public LocalDate hiredOn() {
        throw new UnsupportedOperationException("TODO");
    }

    private String requiredTxt(String fieldName, String fieldValue) {
        if  (fieldValue == null || fieldValue.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }

        return fieldValue;
    }
}

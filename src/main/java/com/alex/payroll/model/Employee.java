package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Common state and behavior of every employee.
 *
 * TODO:
 *  - private final fields: id, name, hiredOn - Done✅
 *  - validate in the constructor: id and name not blank, hiredOn not null -> IllegalArgumentException / NPE - Done✅
 *  - payeeId() returns id; amountDue() reuses monthlyPay() (consider making both final) Done ✅
 *  - equals/hashCode by id only (two objects with the same id are the same employee)
 */
public abstract sealed class Employee implements Payable permits SalariedEmployee, HourlyEmployee {

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
    public final String payeeId() {
        return this.id;
    }

    @Override
    public final BigDecimal amountDue() {
        return this.monthlyPay();
    }

    public String id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public LocalDate hiredOn() {
        return this.hiredOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if(!(o instanceof Employee employee)) { return false; }

        return this.id().equals(employee.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}

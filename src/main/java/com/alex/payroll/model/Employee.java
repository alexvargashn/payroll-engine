package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Common state and behavior of every employee.
 *
 * TODO:
 *  - private final fields: id, name, hiredOn
 *  - validate in the constructor: id and name not blank, hiredOn not null -> IllegalArgumentException / NPE
 *  - payeeId() returns id; amountDue() reuses monthlyPay() (consider making both final)
 *  - equals/hashCode by id only (two objects with the same id are the same employee)
 */
public abstract class Employee implements Payable {

    protected Employee(String id, String name, LocalDate hiredOn) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Each kind of employee is paid differently. */
    public abstract BigDecimal monthlyPay();

    @Override
    public String payeeId() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public BigDecimal amountDue() {
        throw new UnsupportedOperationException("TODO");
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
}

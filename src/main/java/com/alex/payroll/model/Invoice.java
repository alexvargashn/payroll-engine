package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** TODO: validate (amount positive, supplier and dueDate required) and implement Payable. */
public record Invoice(String number, Supplier supplier, BigDecimal amount, LocalDate dueDate) implements Payable {

    @Override
    public String payeeId() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public BigDecimal amountDue() {
        throw new UnsupportedOperationException("TODO");
    }
}

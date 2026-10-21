package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** TODO: validate (amount positive, supplier and dueDate required) and implement Payable. */
public record Invoice(String number, Supplier supplier, BigDecimal amount, LocalDate dueDate) implements Payable {

    public Invoice(String number, Supplier supplier, BigDecimal amount, LocalDate dueDate) {
        this.number = number;
        this.supplier = Objects.requireNonNull(supplier, "The supplier must not be null");
        this.amount = Validations.requirePositive(amount, "amount");
        this.dueDate = Validations.requireDate(dueDate, "dueDate");
    }

    @Override
    public String payeeId() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public BigDecimal amountDue() {
        throw new UnsupportedOperationException("TODO");
    }
}

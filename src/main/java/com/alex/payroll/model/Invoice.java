package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** TODO: validate (amount positive, supplier and dueDate required) and implement Payable. */
public record Invoice(String number, Supplier supplier, BigDecimal amount, LocalDate dueDate) implements Payable {

    public Invoice {
        Validations.requireText(number, "number");
        Objects.requireNonNull(supplier, "The supplier must not be null");
        Validations.requirePositive(amount, "amount");
        Validations.requireDate(dueDate, "dueDate");
    }

    @Override
    public String payeeId() {
        return this.supplier.id();
    }

    @Override
    public BigDecimal amountDue() {
        return this.amount;
    }
}

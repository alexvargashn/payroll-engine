package com.alex.payroll.model;

/** A supplier is not an employee: it is paid through its invoices. TODO: validate id and name. */
public record Supplier(String id, String name, String taxId) {
    public Supplier(String id, String name, String taxId) {
        this.id = Validations.requireText(id, "id");
        this.name = Validations.requireText(name, "name");
        this.taxId = taxId;
    }
}

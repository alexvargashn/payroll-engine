package com.alex.payroll.model;

/** A supplier is not an employee: it is paid through its invoices. TODO: validate id and name. */
public record Supplier(String id, String name, String taxId) {
}

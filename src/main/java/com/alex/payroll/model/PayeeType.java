package com.alex.payroll.model;

public enum PayeeType {
    EMPLOYEE,
    SUPPLIER;

    /** TODO: pattern matching switch over Payable (exhaustive once Payable is sealed). */
    public static PayeeType of(Payable payable) {
        throw new UnsupportedOperationException("TODO");
    }
}

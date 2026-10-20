package com.alex.payroll.model;

import java.math.BigDecimal;

/**
 * Anything the company pays: employees and supplier invoices.
 *
 * TODO (Java 17): make it sealed so only Employee and Invoice can implement it.
 * That lets PayeeType.of(...) use an exhaustive switch with no default branch.
 */
public interface Payable {

    /** Who receives the money: employee id or supplier id. */
    String payeeId();

    /** How much to pay this period. */
    BigDecimal amountDue();
}

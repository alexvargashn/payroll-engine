package com.alex.payroll.bank;

import java.math.BigDecimal;

public final class Transfers {

    private Transfers() {
    }

    /**
     * Moves money atomically between two accounts.
     *
     * TODO:
     *  - A->B and B->A running at the same time must never deadlock (hint: lock order)
     *  - insufficient funds -> InsufficientFundsException and no balance changes
     *  - same account or non-positive amount -> IllegalArgumentException
     */
    public static void transfer(Account from, Account to, BigDecimal amount) {
        throw new UnsupportedOperationException("TODO");
    }
}

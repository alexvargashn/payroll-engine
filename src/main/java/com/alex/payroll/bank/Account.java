package com.alex.payroll.bank;

import java.math.BigDecimal;

/**
 * Company bank account shared by many threads.
 *
 * TODO:
 *  - thread-safe: balance reads and updates must be atomic (synchronized, ReentrantLock or AtomicReference)
 *  - tryWithdraw is check-then-act: the check and the subtraction must happen under the same lock
 *  - amounts must be positive; initial balance can be zero, not negative
 *  - Transfers will need to lock two accounts: think about what this class must expose for that
 */
public final class Account {

    public Account(String id, BigDecimal initialBalance) {
        throw new UnsupportedOperationException("TODO");
    }

    public String id() {
        throw new UnsupportedOperationException("TODO");
    }

    public BigDecimal balance() {
        throw new UnsupportedOperationException("TODO");
    }

    public void deposit(BigDecimal amount) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Withdraws and returns true only if the balance is enough; never leaves it negative. */
    public boolean tryWithdraw(BigDecimal amount) {
        throw new UnsupportedOperationException("TODO");
    }
}

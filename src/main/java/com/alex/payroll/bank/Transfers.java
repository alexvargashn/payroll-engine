package com.alex.payroll.bank;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public final class Transfers {
    private static final ReentrantLock lock = new ReentrantLock();

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
        requirePositive(amount, "amount must be positive");
        requireSameAccount(from, to, "from and to must be different accounts");
        lock.lock();
        try {
            if (!from.tryWithdraw(amount)) {
                throw new InsufficientFundsException();
            }
            to.deposit(amount);
        } finally {
            lock.unlock();
        }
    }

    private static BigDecimal requirePositive(BigDecimal amount, String message) {
        if(amount.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new IllegalArgumentException(message);
        }
        return amount;
    }

    private static void requireSameAccount(Account from, Account to, String message) {
        if(from.equals(to)) {
            throw new IllegalArgumentException(message);
        }
    }
}

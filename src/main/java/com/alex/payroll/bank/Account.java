package com.alex.payroll.bank;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

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

    private final String id;
    private BigDecimal balance;

    private final ReentrantLock lock = new ReentrantLock();

    public Account(String id, BigDecimal initialBalance) {
        this.id = requireNonEmpty(
            Objects.requireNonNull(id, "id must not be null"),
             "id must not be empty");
        this.balance = requirePositive(
            Objects.requireNonNull(initialBalance, "initialBalance must not be null"),
            "initialBalance must not be negative");
    }

    public BigDecimal balance() {
        return this.balance;
    }

    public void deposit(BigDecimal amount) {
        requirePositive(amount, "amount must be positive");
        lock.lock();
        try {
            this.balance = this.balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    /** Withdraws and returns true only if the balance is enough; never leaves it negative. */
    public boolean tryWithdraw(BigDecimal amount) {
        requirePositive(amount, "amount must be positive");
        lock.lock();
        try {
            if (this.balance.compareTo(amount) < 0) {
                return false;
            }
            this.balance = this.balance.subtract(amount);
            return true;
        } finally {
            lock.unlock();
        }
    }

    private static BigDecimal requirePositive(BigDecimal amount, String message) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(message);
        }
        return amount;
    }

    private static String requireNonEmpty(String value, String message) {
        if(value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}

package com.alex.payroll.bank;

import static com.alex.payroll.TestMoney.assertMoney;
import static com.alex.payroll.TestMoney.money;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/**
 * These tests use many threads released at the same time by a CountDownLatch.
 * An implementation without proper synchronization fails them most of the time, not always:
 * run them several times before trusting a green result.
 */
class AccountConcurrencyTest {

    private static final int THREADS = 16;

    @Test
    void basicOperations() {
        Account account = new Account("A-1", money("100.00"));

        account.deposit(money("50.00"));
        assertTrue(account.tryWithdraw(money("120.00")));
        assertFalse(account.tryWithdraw(money("30.01")));
        assertMoney("30.00", account.balance());

        assertThrows(IllegalArgumentException.class, () -> account.deposit(money("0")));
        assertThrows(IllegalArgumentException.class, () -> new Account("A-2", money("-1")));
    }

    @Test
    void concurrentDepositsAreNotLost() throws Exception {
        Account account = new Account("A-1", money("0"));
        int deposits = 2_000;

        runConcurrently(deposits, () -> account.deposit(money("1.00")));

        assertMoney("2000.00", account.balance());
    }

    @Test
    void concurrentWithdrawalsNeverOverdraw() throws Exception {
        Account account = new Account("A-1", money("100.00"));
        AtomicInteger successful = new AtomicInteger();

        runConcurrently(1_000, () -> {
            if (account.tryWithdraw(money("1.00"))) {
                successful.incrementAndGet();
            }
        });

        assertEquals(100, successful.get());
        assertMoney("0.00", account.balance());
    }

    @Test
    void transfersInOppositeDirectionsDoNotDeadlockAndConserveMoney() throws Exception {
        Account a = new Account("A-1", money("1000.00"));
        Account b = new Account("A-2", money("1000.00"));
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);

        for (int i = 0; i < 500; i++) {
            pool.submit(() -> {
                await(start);
                Transfers.transfer(a, b, money("1.00"));
            });
            pool.submit(() -> {
                await(start);
                Transfers.transfer(b, a, money("1.00"));
            });
        }
        start.countDown();
        pool.shutdown();

        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS), "transfers did not finish: possible deadlock");
        assertMoney("1000.00", a.balance());
        assertMoney("1000.00", b.balance());
    }

    @Test
    void transferFailsWithoutChangingBalancesWhenFundsAreShort() {
        Account a = new Account("A-1", money("10.00"));
        Account b = new Account("A-2", money("0"));

        assertThrows(InsufficientFundsException.class, () -> Transfers.transfer(a, b, money("10.01")));
        assertMoney("10.00", a.balance());
        assertMoney("0", b.balance());
        assertThrows(IllegalArgumentException.class, () -> Transfers.transfer(a, a, money("1.00")));
    }

    private static void runConcurrently(int tasks, Runnable action) throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < tasks; i++) {
            pool.submit(() -> {
                await(start);
                action.run();
            });
        }
        start.countDown();
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS), "tasks did not finish in time");
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

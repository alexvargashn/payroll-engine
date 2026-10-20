package com.alex.payroll.processing;

import com.alex.payroll.bank.Account;
import com.alex.payroll.model.Payable;
import java.util.List;

/**
 * Pays a list of payables concurrently from one account.
 *
 * Rules (the tests check each one):
 *  1. Every payable is processed in parallel (virtual threads are a good fit: the bank call is I/O).
 *  2. Withdraw first; if funds are short -> Rejected("INSUFFICIENT_FUNDS"), the bank is not called.
 *  3. At most maxConcurrentGatewayCalls calls to the bank at the same time (hint: Semaphore).
 *  4. If the bank throws -> refund the account and return Rejected("GATEWAY_ERROR").
 *  5. The report keeps the input order.
 *  6. maxConcurrentGatewayCalls < 1 -> IllegalArgumentException.
 */
public final class PayrollProcessor {

    public PayrollProcessor(BankGateway gateway, int maxConcurrentGatewayCalls) {
        throw new UnsupportedOperationException("TODO");
    }

    public PayrollReport process(Account source, List<? extends Payable> payables) {
        throw new UnsupportedOperationException("TODO");
    }
}

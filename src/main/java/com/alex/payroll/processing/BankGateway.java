package com.alex.payroll.processing;

import java.math.BigDecimal;

/** External bank: slow I/O that can fail with a RuntimeException. */
@FunctionalInterface
public interface BankGateway {

    void send(String payeeId, BigDecimal amount);
}

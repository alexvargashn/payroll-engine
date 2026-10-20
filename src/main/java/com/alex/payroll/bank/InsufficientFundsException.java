package com.alex.payroll.bank;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String accountId, BigDecimal amount) {
        super("Account " + accountId + " cannot pay " + amount.toPlainString());
    }
}

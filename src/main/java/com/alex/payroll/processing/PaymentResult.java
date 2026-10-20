package com.alex.payroll.processing;

import com.alex.payroll.model.PayeeType;
import java.math.BigDecimal;

/** TODO (optional): make it sealed. Reasons used by the tests: INSUFFICIENT_FUNDS, GATEWAY_ERROR. */
public interface PaymentResult {

    String payeeId();

    PayeeType type();

    BigDecimal amount();

    record Paid(String payeeId, PayeeType type, BigDecimal amount) implements PaymentResult {
    }

    record Rejected(String payeeId, PayeeType type, BigDecimal amount, String reason) implements PaymentResult {
    }
}

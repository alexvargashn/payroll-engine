package com.alex.payroll.processing;

import com.alex.payroll.model.PayeeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** Results in the same order as the input. TODO: implement with streams. */
public record PayrollReport(List<PaymentResult> results) {

    public BigDecimal totalPaid() {
        throw new UnsupportedOperationException("TODO");
    }

    public long paidCount() {
        throw new UnsupportedOperationException("TODO");
    }

    public long rejectedCount() {
        throw new UnsupportedOperationException("TODO");
    }

    public List<PaymentResult.Rejected> rejected() {
        throw new UnsupportedOperationException("TODO");
    }

    /** Total paid per payee type (hint: groupingBy into an EnumMap). */
    public Map<PayeeType, BigDecimal> paidByType() {
        throw new UnsupportedOperationException("TODO");
    }
}

package com.alex.payroll.analytics;

import com.alex.payroll.model.Employee;
import com.alex.payroll.model.Invoice;
import com.alex.payroll.model.Manager;
import com.alex.payroll.model.Payable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;

/** Reports with streams and collectors. No loops allowed: that is the point of the exercise. */
public final class PayrollAnalytics {

    private PayrollAnalytics() {
    }

    public static BigDecimal totalDue(Collection<? extends Payable> payables) {
        throw new UnsupportedOperationException("TODO");
    }

    public static Optional<Employee> highestPaid(Collection<? extends Employee> employees) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Year -> employee ids, years ascending, ids sorted inside each year. */
    public static SortedMap<Integer, List<String>> employeeIdsByHireYear(Collection<? extends Employee> employees) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Manager id -> manager pay + pay of every report, sorted by manager id. */
    public static SortedMap<String, BigDecimal> teamCostByManager(Collection<Manager> managers) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Due before today, oldest due date first; same date -> larger amount first. */
    public static List<Invoice> overdueInvoices(Collection<Invoice> invoices, LocalDate today) {
        throw new UnsupportedOperationException("TODO");
    }
}

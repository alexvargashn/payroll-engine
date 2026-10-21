package com.alex.payroll.analytics;

import com.alex.payroll.model.Employee;
import com.alex.payroll.model.Invoice;
import com.alex.payroll.model.Manager;
import com.alex.payroll.model.Payable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/** Reports with streams and collectors. No loops allowed: that is the point of the exercise. */
public final class PayrollAnalytics {

    private PayrollAnalytics() {
    }

    public static BigDecimal totalDue(Collection<? extends Payable> payables) {
        return payables.stream()
                .map(Payable::amountDue)                    // Stream<Payable> -> Stream<BigDecimal>
                .reduce(BigDecimal.ZERO, BigDecimal::add);  // now everything is BigDecimal
    }
    public static Optional<Employee> highestPaid(Collection<? extends Employee> employees) {
        return employees.stream()
                .max(Comparator.comparing(Employee::monthlyPay))
                .map(e -> e);
    }

    /** Year -> employee ids, years ascending, ids sorted inside each year. */
    public static SortedMap<Integer, List<String>> employeeIdsByHireYear(Collection<? extends Employee> employees) {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::id))
                .collect(Collectors.groupingBy(
                        e -> e.hiredOn().getYear(),
                        TreeMap::new,
                        Collectors.mapping(Employee::id, Collectors.toList())));
    }

    /** Manager id -> manager pay + pay of every report, sorted by manager id. */
    public static SortedMap<String, BigDecimal> teamCostByManager(Collection<Manager> managers) {
        return managers.stream()
                .collect(Collectors.toMap(
                        Manager::id,
                        m -> m.monthlyPay().add(totalDue(m.reports())),
                        (a,b) -> a,
                        TreeMap::new));
    }

    /** Due before today, oldest due date first; same date -> larger amount first. */
    public static List<Invoice> overdueInvoices(Collection<Invoice> invoices, LocalDate today) {
        return invoices.stream()
                .filter(i -> i.dueDate().isBefore(today))
                .sorted(Comparator.comparing(Invoice::dueDate)
                    .reversed()
                    .thenComparing(Invoice::amountDue).reversed())
                .toList();
    }
}

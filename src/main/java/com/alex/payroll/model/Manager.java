package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * A manager IS a salaried employee (inheritance) and HAS a team (composition).
 * Pay = salary + bonusPerReport x number of reports. Reuse the parent's calculation.
 *
 * TODO:
 *  - bonusPerReport can be zero, not negative
 *  - addReport rejects null, itself and duplicates with IllegalArgumentException
 *  - reports() must not expose the internal mutable list
 */
public final class Manager extends SalariedEmployee {

    public Manager(String id, String name, LocalDate hiredOn, BigDecimal monthlySalary, BigDecimal bonusPerReport) {
        super(id, name, hiredOn, monthlySalary);
        throw new UnsupportedOperationException("TODO");
    }

    public void addReport(Employee employee) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Employee> reports() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public BigDecimal monthlyPay() {
        throw new UnsupportedOperationException("TODO");
    }
}

package com.alex.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final BigDecimal bonusPerReport;
    private List<Employee> employees = new ArrayList<>();

    public Manager(String id, String name, LocalDate hiredOn, BigDecimal monthlySalary, BigDecimal bonusPerReport) {
        super(id, name, hiredOn, monthlySalary);
        this.bonusPerReport = Validations.requireNonNegative(bonusPerReport, "bonusPerReport");
    }

    public void addReport(Employee employee) {
        if(Objects.isNull(employee) || this.equals(employee) || this.employees.contains(employee)) {
            throw new IllegalArgumentException("Employee can't be null, itself or already exists in the list");
        }
        this.employees.add(employee);
    }

    public List<Employee> reports() {
        return List.copyOf(employees);
    }

    @Override
    public BigDecimal monthlyPay() {
        BigDecimal plus = new BigDecimal(this.employees.size());
        return this.monthlySalary.add(bonusPerReport.multiply(plus));
    }
}

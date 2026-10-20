package com.alex.payroll.model;

import static com.alex.payroll.TestMoney.assertMoney;
import static com.alex.payroll.TestMoney.money;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.alex.payroll.analytics.PayrollAnalytics;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    private static final LocalDate HIRED = LocalDate.of(2022, 3, 1);

    @Test
    void salariedEmployeeIsPaidTheMonthlySalary() {
        Employee ana = new SalariedEmployee("E-1", "Ana", HIRED, money("3000.00"));

        assertMoney("3000.00", ana.monthlyPay());
        assertMoney("3000.00", ana.amountDue());
        assertEquals("E-1", ana.payeeId());
    }

    @Test
    void hourlyEmployeeGetsOvertimeAboveOneHundredSixtyHours() {
        // 160 h x 10.00 + 10 h x 15.00 (1.5x) = 1750.00
        Employee luis = new HourlyEmployee("E-2", "Luis", HIRED, money("10.00"), money("170"));
        // Under the threshold there is no overtime
        Employee rosa = new HourlyEmployee("E-3", "Rosa", HIRED, money("12.50"), money("100"));

        assertMoney("1750.00", luis.monthlyPay());
        assertMoney("1250.00", rosa.monthlyPay());
    }

    @Test
    void managerEarnsSalaryPlusBonusPerReport() {
        Manager carla = new Manager("M-1", "Carla", HIRED, money("5000.00"), money("200.00"));
        carla.addReport(new SalariedEmployee("E-1", "Ana", HIRED, money("3000.00")));
        carla.addReport(new HourlyEmployee("E-2", "Luis", HIRED, money("10.00"), money("170")));

        assertMoney("5400.00", carla.monthlyPay());
        assertEquals(2, carla.reports().size());
    }

    @Test
    void managerRejectsSelfAndDuplicateReports() {
        Manager carla = new Manager("M-1", "Carla", HIRED, money("5000.00"), money("200.00"));
        Employee ana = new SalariedEmployee("E-1", "Ana", HIRED, money("3000.00"));
        carla.addReport(ana);

        assertThrows(IllegalArgumentException.class, () -> carla.addReport(carla));
        assertThrows(IllegalArgumentException.class, () -> carla.addReport(ana));
    }

    @Test
    void managerDoesNotExposeItsInternalList() {
        Manager carla = new Manager("M-1", "Carla", HIRED, money("5000.00"), money("200.00"));
        List<Employee> reports = carla.reports();

        assertThrows(UnsupportedOperationException.class,
                     () -> reports.add(new SalariedEmployee("E-9", "X", HIRED, money("1.00"))));
    }

    @Test
    void invalidDataIsRejectedInConstructors() {
        assertThrows(IllegalArgumentException.class,
                     () -> new SalariedEmployee(" ", "Ana", HIRED, money("3000.00")));
        assertThrows(IllegalArgumentException.class,
                     () -> new SalariedEmployee("E-1", "Ana", HIRED, money("0")));
        assertThrows(IllegalArgumentException.class,
                     () -> new HourlyEmployee("E-2", "Luis", HIRED, money("10.00"), money("-1")));
        assertThrows(IllegalArgumentException.class,
                     () -> new Invoice("F-1", new Supplier("S-1", "Acme", "123"), money("-5.00"), HIRED));
    }

    @Test
    void employeesAreEqualByIdRegardlessOfOtherFields() {
        Employee a = new SalariedEmployee("E-1", "Ana", HIRED, money("3000.00"));
        Employee sameId = new SalariedEmployee("E-1", "Ana Maria", HIRED, money("3100.00"));
        Employee other = new SalariedEmployee("E-2", "Ana", HIRED, money("3000.00"));

        assertEquals(a, sameId);
        assertEquals(a.hashCode(), sameId.hashCode());
        assertNotEquals(a, other);
    }

    @Test
    void invoicePaysTheSupplier() {
        Supplier acme = new Supplier("S-1", "Acme", "123");
        Invoice invoice = new Invoice("F-100", acme, money("880.00"), HIRED);

        assertEquals("S-1", invoice.payeeId());
        assertMoney("880.00", invoice.amountDue());
        assertEquals(PayeeType.SUPPLIER, PayeeType.of(invoice));
    }

    @Test
    void employeesAndInvoicesAreHandledPolymorphically() {
        List<Payable> payables = List.of(
                new SalariedEmployee("E-1", "Ana", HIRED, money("3000.00")),
                new HourlyEmployee("E-2", "Luis", HIRED, money("10.00"), money("170")),
                new Invoice("F-1", new Supplier("S-1", "Acme", "123"), money("250.00"), HIRED));

        assertMoney("5000.00", PayrollAnalytics.totalDue(payables));
        assertEquals(PayeeType.EMPLOYEE, PayeeType.of(payables.get(0)));
    }
}

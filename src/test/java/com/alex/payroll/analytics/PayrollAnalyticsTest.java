package com.alex.payroll.analytics;

import static com.alex.payroll.TestMoney.assertMoney;
import static com.alex.payroll.TestMoney.money;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alex.payroll.model.Employee;
import com.alex.payroll.model.HourlyEmployee;
import com.alex.payroll.model.Invoice;
import com.alex.payroll.model.Manager;
import com.alex.payroll.model.SalariedEmployee;
import com.alex.payroll.model.Supplier;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import org.junit.jupiter.api.Test;

class PayrollAnalyticsTest {

    private final SalariedEmployee ana = new SalariedEmployee("E-1", "Ana", LocalDate.of(2021, 5, 1), money("3000.00"));
    private final HourlyEmployee luis = new HourlyEmployee("E-2", "Luis", LocalDate.of(2023, 1, 9), money("10.00"), money("170"));
    private final SalariedEmployee beto = new SalariedEmployee("E-0", "Beto", LocalDate.of(2021, 11, 2), money("2800.00"));
    private final Manager carla = new Manager("M-1", "Carla", LocalDate.of(2020, 2, 3), money("5000.00"), money("200.00"));

    @Test
    void findsTheHighestPaidEmployee() {
        carla.addReport(ana);

        assertEquals("M-1", PayrollAnalytics.highestPaid(List.of(ana, luis, carla)).orElseThrow().id());
        assertTrue(PayrollAnalytics.highestPaid(List.<Employee>of()).isEmpty());
    }

    @Test
    void groupsEmployeeIdsByHireYearInOrder() {
        SortedMap<Integer, List<String>> byYear =
                PayrollAnalytics.employeeIdsByHireYear(List.of(ana, luis, beto, carla));

        assertEquals(List.of(2020, 2021, 2023), List.copyOf(byYear.keySet()));
        assertEquals(List.of("E-0", "E-1"), byYear.get(2021)); // ids sorted inside each year
    }

    @Test
    void computesTeamCostPerManager() {
        Manager dani = new Manager("M-2", "Dani", LocalDate.of(2019, 1, 1), money("4000.00"), money("100.00"));
        carla.addReport(ana);   // 3000
        carla.addReport(luis);  // 1750
        dani.addReport(beto);   // 2800

        Map<String, java.math.BigDecimal> cost = PayrollAnalytics.teamCostByManager(List.of(dani, carla));

        assertEquals(List.of("M-1", "M-2"), List.copyOf(cost.keySet()));
        assertMoney("10150.00", cost.get("M-1")); // 5400 + 3000 + 1750
        assertMoney("6900.00", cost.get("M-2"));  // 4100 + 2800
    }

    @Test
    void listsOverdueInvoicesOldestFirstThenLargestFirst() {
        Supplier acme = new Supplier("S-1", "Acme", "1");
        Invoice old = new Invoice("F-1", acme, money("100.00"), LocalDate.of(2026, 9, 1));
        Invoice sameDayBig = new Invoice("F-2", acme, money("900.00"), LocalDate.of(2026, 9, 15));
        Invoice sameDaySmall = new Invoice("F-3", acme, money("50.00"), LocalDate.of(2026, 9, 15));
        Invoice notDue = new Invoice("F-4", acme, money("70.00"), LocalDate.of(2026, 10, 14));

        List<Invoice> overdue = PayrollAnalytics.overdueInvoices(
                List.of(sameDaySmall, notDue, old, sameDayBig), LocalDate.of(2026, 10, 14));

        assertEquals(List.of("F-1", "F-2", "F-3"), overdue.stream().map(Invoice::number).toList());
    }
}

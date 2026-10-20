package com.alex.payroll.processing;

import static com.alex.payroll.TestMoney.assertMoney;
import static com.alex.payroll.TestMoney.money;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alex.payroll.bank.Account;
import com.alex.payroll.model.HourlyEmployee;
import com.alex.payroll.model.Invoice;
import com.alex.payroll.model.Manager;
import com.alex.payroll.model.PayeeType;
import com.alex.payroll.model.Payable;
import com.alex.payroll.model.SalariedEmployee;
import com.alex.payroll.model.Supplier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class PayrollProcessorTest {

    private static final LocalDate DAY = LocalDate.of(2026, 10, 30);

    /** Fake bank: slow like real I/O, records calls and the maximum number of concurrent calls. */
    static final class FakeGateway implements BankGateway {
        final Map<String, BigDecimal> sent = new ConcurrentHashMap<>();
        final AtomicInteger inFlight = new AtomicInteger();
        final AtomicInteger maxInFlight = new AtomicInteger();
        final Set<String> failingPayees;

        FakeGateway(Set<String> failingPayees) {
            this.failingPayees = failingPayees;
        }

        @Override
        public void send(String payeeId, BigDecimal amount) {
            int now = inFlight.incrementAndGet();
            maxInFlight.accumulateAndGet(now, Math::max);
            try {
                Thread.sleep(20);
                if (failingPayees.contains(payeeId)) {
                    throw new IllegalStateException("bank rejected " + payeeId);
                }
                sent.merge(payeeId, amount, BigDecimal::add);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            } finally {
                inFlight.decrementAndGet();
            }
        }
    }

    private static List<Invoice> invoices(int count, String amountEach) {
        List<Invoice> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Supplier supplier = new Supplier("S-" + i, "Supplier " + i, "TAX-" + i);
            result.add(new Invoice("F-" + i, supplier, money(amountEach), DAY));
        }
        return result;
    }

    @Test
    void paysEveryoneWhenFundsAreEnough() {
        Manager carla = new Manager("M-1", "Carla", DAY, money("5000.00"), money("200.00"));
        SalariedEmployee ana = new SalariedEmployee("E-1", "Ana", DAY, money("3000.00"));
        HourlyEmployee luis = new HourlyEmployee("E-2", "Luis", DAY, money("10.00"), money("170"));
        carla.addReport(ana);
        carla.addReport(luis);
        List<Payable> payables = new ArrayList<>(List.of(carla, ana, luis)); // 5400 + 3000 + 1750
        payables.addAll(invoices(2, "925.00"));                               // 1850

        Account company = new Account("COMPANY", money("20000.00"));
        FakeGateway gateway = new FakeGateway(Set.of());

        PayrollReport report = new PayrollProcessor(gateway, 4).process(company, payables);

        assertEquals(5, report.paidCount());
        assertEquals(0, report.rejectedCount());
        assertMoney("12000.00", report.totalPaid());
        assertMoney("8000.00", company.balance());
        assertMoney("10150.00", report.paidByType().get(PayeeType.EMPLOYEE));
        assertMoney("1850.00", report.paidByType().get(PayeeType.SUPPLIER));
        assertEquals(5, gateway.sent.size());
        // Results keep the input order, whatever order the threads finished in
        assertEquals("M-1", report.results().get(0).payeeId());
        assertEquals("S-1", report.results().get(4).payeeId());
    }

    @Test
    void neverOverdrawsWhenFundsAreShort() {
        Account company = new Account("COMPANY", money("2550.00"));
        FakeGateway gateway = new FakeGateway(Set.of());

        PayrollReport report = new PayrollProcessor(gateway, 8).process(company, invoices(50, "100.00"));

        // Exactly 25 invoices of 100.00 fit in 2550.00, whatever the order
        assertEquals(25, report.paidCount());
        assertEquals(25, report.rejectedCount());
        assertMoney("50.00", company.balance());
        assertTrue(report.rejected().stream().allMatch(r -> r.reason().equals("INSUFFICIENT_FUNDS")));
        assertEquals(25, gateway.sent.size());
    }

    @Test
    void limitsConcurrentCallsToTheBankButStillRunsInParallel() {
        Account company = new Account("COMPANY", money("100000.00"));
        FakeGateway gateway = new FakeGateway(Set.of());

        new PayrollProcessor(gateway, 5).process(company, invoices(40, "10.00"));

        assertTrue(gateway.maxInFlight.get() <= 5, "max concurrent calls was " + gateway.maxInFlight.get());
        assertTrue(gateway.maxInFlight.get() >= 2, "payments were not processed in parallel");
    }

    @Test
    void refundsTheAccountWhenTheBankRejectsAPayment() {
        Account company = new Account("COMPANY", money("1000.00"));
        FakeGateway gateway = new FakeGateway(Set.of("S-1"));

        PayrollReport report = new PayrollProcessor(gateway, 3).process(company, invoices(3, "100.00"));

        assertEquals(2, report.paidCount());
        assertEquals(1, report.rejected().size());
        assertEquals("GATEWAY_ERROR", report.rejected().get(0).reason());
        assertEquals("S-1", report.rejected().get(0).payeeId());
        assertMoney("800.00", company.balance());
    }

    @Test
    void rejectsInvalidConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> new PayrollProcessor((p, a) -> { }, 0));
    }
}

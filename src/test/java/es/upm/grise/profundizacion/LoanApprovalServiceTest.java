package es.upm.grise.profundizacion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import es.upm.grise.profundizacion.LoanApprovalService.Applicant;
import es.upm.grise.profundizacion.LoanApprovalService.Decision;

public class LoanApprovalServiceTest {
    
    private final LoanApprovalService service = new LoanApprovalService();

    @Test
    void camino1() {
        Applicant applicant = new Applicant(3000, 400, false, false);
        Decision decision = service.evaluateLoan(applicant, 10000, 36);
        assertEquals(Decision.REJECTED, decision);
    }
    
    @Test
    void camino2() {
        Applicant applicant = new Applicant(3000, 550, false, false);
        Decision decision = service.evaluateLoan(applicant, 10000, 36);
        assertEquals(Decision.MANUAL_REVIEW, decision);
    }

    @Test
    void camino3() {
        Applicant applicant = new Applicant(2000, 550, false, false);
        Decision decision = service.evaluateLoan(applicant, 10000, 36);
        assertEquals(Decision.REJECTED, decision);
    }

    @Test
    void camino4() {
        Applicant applicant = new Applicant(3000, 700, false, false);
        int amountRequested = 20000;
        Decision decision = service.evaluateLoan(applicant, amountRequested, 36);
        assertEquals(Decision.APPROVED, decision);
    }

    @Test
    void camino5() {
        Applicant applicant = new Applicant(3000, 700, false, false);
        int amountRequested = 30000;
        Decision decision = service.evaluateLoan(applicant, amountRequested, 36);
        assertEquals(Decision.MANUAL_REVIEW, decision);
    }

    @Test
    void camino6() {
        Applicant applicant = new Applicant(3000, 620, false, true);
        int amountRequested = 10000;
        Decision decision = service.evaluateLoan(applicant, amountRequested, 36);
        assertEquals(Decision.APPROVED, decision);
    }
    
    @Test
    void testScoreLower600() {
        Applicant applicant = new Applicant(3000, 580, false, true);
        int amountRequested = 10000;
        Decision decision = service.evaluateLoan(applicant, amountRequested, 36);
        assertEquals(Decision.MANUAL_REVIEW, decision);
    }

    @Test
    void testHasDefaults() {
        Applicant applicant = new Applicant(3000, 550, true, false);
        Decision decision = service.evaluateLoan(applicant, 10000, 36);
        assertEquals(Decision.REJECTED, decision);
    }

    @Test
    void testAmountRequestedException() {
        Applicant applicant = new Applicant(3000, 700, false, false);
        try {
            service.evaluateLoan(applicant, 0, 36);
        } catch (IllegalArgumentException e) {
            assertEquals("amountRequested must be > 0", e.getMessage());
        }
    }

    @Test
    void testTermMonthsException() {
        Applicant applicant = new Applicant(0, 700, false, false);
        try {
            service.evaluateLoan(applicant, 10000, 5);
        } catch (IllegalArgumentException e) {
            assertEquals("termMonths must be between 6 and 84", e.getMessage());
        }

        try {
            service.evaluateLoan(applicant, 10000, 85);
        } catch (IllegalArgumentException e) {
            assertEquals("termMonths must be between 6 and 84", e.getMessage());
        }
    }

    @Test
    void testMonthlyIncomeException() {
        Applicant applicant = new Applicant(-1000, 700, false, false);
        try {
            service.evaluateLoan(applicant, 10000, 36);
        } catch (IllegalArgumentException e) {
            assertEquals("monthlyIncome must be > 0", e.getMessage());
        }
    }

    @Test
    void testCreditScoreException() {
        Applicant applicant = new Applicant(3000, 1200, false, false);
        try {
            service.evaluateLoan(applicant, 10000, 36);
        } catch (IllegalArgumentException e) {
            assertEquals("creditScore must be between 0 and 850", e.getMessage());
        }
        Applicant applicant2 = new Applicant(3000, -1200, false, false);
        try {
            service.evaluateLoan(applicant2, 10000, 36);
        } catch (IllegalArgumentException e) {
            assertEquals("creditScore must be between 0 and 850", e.getMessage());
        }
    }
}

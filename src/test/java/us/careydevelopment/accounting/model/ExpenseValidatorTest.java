package us.careydevelopment.accounting.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import us.careydevelopment.accounting.harness.ExpenseHarness;
import us.careydevelopment.accounting.harness.RandomStringGenerator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNullDate() {
        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.setDate(null);

        final Set<ConstraintViolation<Expense>> violations = validator.validate(expense);
        assertEquals(1, violations.size());
    }

    @Test
    public void testNullPaymentMethod() {
        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.setPaymentMethod(null);

        final Set<ConstraintViolation<Expense>> violations = validator.validate(expense);
        assertEquals(1, violations.size());
    }

    @Test
    public void testReferenceNumberExceedsLength() {
        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.setReferenceNumber(RandomStringGenerator.generateStringOfLength(13));

        final Set<ConstraintViolation<Expense>> violations = validator.validate(expense);
        assertEquals(1, violations.size());
    }

    @Test
    public void testEmptyPaymentList() {
        final Expense expense = ExpenseHarness.getValidTelephoneExpense();
        expense.setPayments(new ArrayList<>());

        final Set<ConstraintViolation<Expense>> violations = validator.validate(expense);
        assertEquals(1, violations.size());
    }


}

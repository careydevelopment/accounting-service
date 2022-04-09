package us.careydevelopment.accounting.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import us.careydevelopment.accounting.harness.ExpenseHarness;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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

//    @Test
//    public void testEmptyDisplayName() {
//        final Business business = new Business();
//        business.setName("businessname");
//
//        final Set<ConstraintViolation<Business>> violations = validator.validate(business);
//        assertEquals(1, violations.size());
//    }
//
//    @Test
//    public void testInvalidEmailAddress() {
//        final Business business = new Business();
//        business.setName("businessname");
//        business.setDisplayName("displayname");
//        business.setEmail("joe");
//
//        final Set<ConstraintViolation<Business>> violations = validator.validate(business);
//        assertEquals(1, violations.size());
//    }
}

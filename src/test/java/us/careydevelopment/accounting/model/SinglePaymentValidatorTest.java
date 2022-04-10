package us.careydevelopment.accounting.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import us.careydevelopment.accounting.harness.AccountHarness;
import us.careydevelopment.accounting.harness.RandomStringGenerator;
import us.careydevelopment.accounting.harness.SinglePaymentHarness;
import us.careydevelopment.accounting.harness.UserLightweightHarness;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SinglePaymentValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testDescriptionExceedsLength() {
        final Account account = AccountHarness.getTelephoneExpenseAccount();
        final UserLightweight owner = UserLightweightHarness.getMrSmithUserLightweight();
        final SinglePayment singlePayment = SinglePaymentHarness.getSinglePayment(account, 32l, "37", owner);
        singlePayment.setDescription(RandomStringGenerator.generateStringOfLength(129));

        final Set<ConstraintViolation<SinglePayment>> violations = validator.validate(singlePayment);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidAccount() {
        final Account account = AccountHarness.getTelephoneExpenseAccount();
        account.setName("");

        final UserLightweight owner = UserLightweightHarness.getMrSmithUserLightweight();
        final SinglePayment singlePayment = SinglePaymentHarness.getSinglePayment(account, 32l, "37", owner);

        final Set<ConstraintViolation<SinglePayment>> violations = validator.validate(singlePayment);
        assertEquals(1, violations.size());
    }

    @Test
    public void testNullAccount() {
        final UserLightweight owner = UserLightweightHarness.getMrSmithUserLightweight();
        final SinglePayment singlePayment = SinglePaymentHarness.getSinglePayment(null, 32l, "37", owner);

        final Set<ConstraintViolation<SinglePayment>> violations = validator.validate(singlePayment);
        assertEquals(1, violations.size());
    }

    @Test
    public void testNonExpenseAccount() {
        final Account account = AccountHarness.getSalesRevenueAccount();
        final UserLightweight owner = UserLightweightHarness.getMrSmithUserLightweight();
        final SinglePayment singlePayment = SinglePaymentHarness.getSinglePayment(account, 32l, "37", owner);

        final Set<ConstraintViolation<SinglePayment>> violations = validator.validate(singlePayment);
        assertEquals(1, violations.size());
    }
}

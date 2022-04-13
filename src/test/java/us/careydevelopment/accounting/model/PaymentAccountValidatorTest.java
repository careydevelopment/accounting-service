package us.careydevelopment.accounting.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import us.careydevelopment.accounting.harness.PaymentAccountHarness;
import us.careydevelopment.accounting.harness.RandomStringGenerator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentAccountValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testEmptyId() {
        final PaymentAccount paymentAccount = PaymentAccountHarness.getValidBankPaymentAccount();
        paymentAccount.setId("");

        final Set<ConstraintViolation<PaymentAccount>> violations = validator.validate(paymentAccount);
        assertEquals(1, violations.size());
    }

    @Test
    public void testNameExceedsLength() {
        final PaymentAccount paymentAccount = PaymentAccountHarness.getValidBankPaymentAccount();
        paymentAccount.setName(RandomStringGenerator.generateStringOfLength(33));

        final Set<ConstraintViolation<PaymentAccount>> violations = validator.validate(paymentAccount);
        assertEquals(1, violations.size());
    }
}

package us.careydevelopment.ecosystem.business.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import us.careydevelopment.ecosystem.business.util.JsonLoader;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusinessValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        final JsonLoader loader = new JsonLoader();
        loader.load();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testEmptyBusinessName() {
        final Business business = new Business();
        business.setDisplayName("displayname");

        final Set<ConstraintViolation<Business>> violations = validator.validate(business);
        assertEquals(1, violations.size());
    }

    @Test
    public void testEmptyDisplayName() {
        final Business business = new Business();
        business.setName("businessname");

        final Set<ConstraintViolation<Business>> violations = validator.validate(business);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidEmailAddress() {
        final Business business = new Business();
        business.setName("businessname");
        business.setDisplayName("displayname");
        business.setEmail("joe");

        final Set<ConstraintViolation<Business>> violations = validator.validate(business);
        assertEquals(1, violations.size());
    }
}

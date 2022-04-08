package us.careydevelopment.ecosystem.business.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import us.careydevelopment.ecosystem.business.harness.RandomStringGenerator;
import us.careydevelopment.ecosystem.business.util.JsonLoader;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class AddressValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        final JsonLoader loader = new JsonLoader();
        loader.load();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUsState() {
        final Address address = new Address();
        address.setCountry("US");
        address.setState("AK");

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(0, violations.size());
    }

    @Test
    public void testUsStateWithThreeLetterCode() {
        final Address address = new Address();
        address.setCountry("US");
        address.setState("AKS");

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());
    }

    @Test
    public void testUsStateWithBogusCode() {
        final Address address = new Address();
        address.setCountry("US");
        address.setState("BB");

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());
    }

    @Test
    public void testValidCountry() {
        final Address address = new Address();
        address.setCountry("US");

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(0, violations.size());
    }

    @Test
    public void testCountryWithBogusCode() {
        final Address address = new Address();
        address.setCountry("CC");

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());
    }

    @Test
    public void testStreet2WithoutStreet1() {
        final Address address = new Address();
        address.setStreet2("Apt. B");

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());
    }

    @Test
    public void testStreet1MaxLengthExceeded() {
        final Address address = new Address();
        final String str = RandomStringGenerator.generateStringOfLength(81);
        address.setStreet1(str);

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());
    }

    @Test
    public void testStreet2MaxLengthExceeded() {
        final Address address = new Address();
        final String str = RandomStringGenerator.generateStringOfLength(81);
        address.setStreet1("123 Main Str.");
        address.setStreet2(str);

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());
    }

    @Test
    public void testCityMaxLengthExceeded() {
        final Address address = new Address();
        final String str = RandomStringGenerator.generateStringOfLength(51);
        address.setCity(str);

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());
    }

    @Test
    public void testCountryMaxLengthExceeded() {
        final Address address = new Address();
        address.setCountry("UBS");

        final Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(2, violations.size());
    }
}

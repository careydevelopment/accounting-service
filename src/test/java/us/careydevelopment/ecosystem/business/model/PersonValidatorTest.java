package us.careydevelopment.ecosystem.business.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import us.careydevelopment.ecosystem.business.harness.RandomStringGenerator;
import us.careydevelopment.ecosystem.business.util.JsonLoader;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        final JsonLoader loader = new JsonLoader();
        loader.load();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testTitleExceedsLength() {
        final Person person = new Person();
        final String str = RandomStringGenerator.generateStringOfLength(13);
        person.setTitle(str);

        final Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertEquals(1, violations.size());
    }

    @Test
    public void testFirstNameExceedsLength() {
        final Person person = new Person();
        final String str = RandomStringGenerator.generateStringOfLength(53);
        person.setFirstName(str);

        final Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertEquals(1, violations.size());
    }
}

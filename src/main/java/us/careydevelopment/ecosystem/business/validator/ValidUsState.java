package us.careydevelopment.ecosystem.business.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUsStateValidator.class)
@Documented
public @interface ValidUsState {

    String message() default "U.S. State must be a valid two-letter code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

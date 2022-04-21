package us.careydevelopment.accounting.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RevenueAccountValidator.class)
@Documented
public @interface RevenueAccount {

    String message() default "Revenue account required for transaction";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package us.careydevelopment.accounting.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExpenseAccountValidator.class)
@Documented
public @interface ExpenseAccount {

    String message() default "Expense account required for transaction";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

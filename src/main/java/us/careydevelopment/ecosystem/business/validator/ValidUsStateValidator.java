package us.careydevelopment.ecosystem.business.validator;

import org.apache.commons.lang3.StringUtils;
import us.careydevelopment.ecosystem.business.util.UsStates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUsStateValidator implements ConstraintValidator<ValidUsState, String> {

    @Override
    public void initialize(ValidUsState constraintAnnotation) {
    }

    @Override
    public boolean isValid(String state, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(state)) {
            return true;
        }

        if (state.length() != 2) {
            return false;
        }

        return UsStates.isValid(state);
    }
}

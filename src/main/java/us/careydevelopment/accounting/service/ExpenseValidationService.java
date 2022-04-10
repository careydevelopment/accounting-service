package us.careydevelopment.accounting.service;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.model.Expense;
import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

import java.util.List;

@Component
public class ExpenseValidationService {

    public void validateNew(Expense expense, BindingResult bindingResult) throws InvalidRequestException {
        List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);
        if (errors.size() > 0) {
            throw new InvalidRequestException("Expense is not valid", errors);
        }
    }
}

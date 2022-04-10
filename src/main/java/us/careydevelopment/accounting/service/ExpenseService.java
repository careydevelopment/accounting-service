package us.careydevelopment.accounting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.model.Expense;

@Component
public class ExpenseService {

    @Autowired
    private ExpenseValidationService validationService;

    public void postExpense(Expense expense, BindingResult bindingResult) throws InvalidRequestException {
        validationService.validateNew(expense, bindingResult);
    }
}

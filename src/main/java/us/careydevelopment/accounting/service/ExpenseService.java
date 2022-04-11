package us.careydevelopment.accounting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.model.Expense;
import us.careydevelopment.accounting.repository.ExpenseRepository;

@Component
public class ExpenseService {

    @Autowired
    private ExpenseValidationService validationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExpenseRepository expenseRepository;

    public void postExpense(Expense expense, BindingResult bindingResult) throws InvalidRequestException {
        validationService.validateNew(expense, bindingResult);

        postExpense(expense);
    }

    @Transactional
    private void postExpense(Expense expense) {
        transactionService.transact(expense);
        add(expense);
    }

    private void add(Expense expense) {
        expenseRepository.save(expense);
    }
}

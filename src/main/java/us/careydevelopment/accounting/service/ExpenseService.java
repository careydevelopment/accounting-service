package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Expense;
import us.careydevelopment.accounting.repository.ExpenseRepository;

@Component
public class ExpenseService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseService.class);

    @Autowired
    private ExpenseValidationService validationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Transactional
    public Expense postExpense(final Expense expense, final BindingResult bindingResult) throws InvalidRequestException {
        validationService.validateNew(expense, bindingResult);

        final Expense returnedExpense = postExpense(expense);
        return returnedExpense;
    }

    private Expense postExpense(final Expense expense) {
        try {
            transactionService.transact(expense);
            final Expense returnedExpense = add(expense);

            return returnedExpense;
        } catch (Exception e) {
            LOG.error("Problem posting expense!", e);
            throw new ServiceException(e.getMessage());
        }
    }

    private Expense add(final Expense expense) {
        Expense returnedExpense = expenseRepository.save(expense);
        return returnedExpense;
    }
}

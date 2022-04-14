package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
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
import us.careydevelopment.accounting.util.DateUtil;

@Component
public class ExpenseService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseService.class);

    @Autowired
    private ExpenseValidationService validationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Transactional
    public Expense postExpense(final Expense expense, final BindingResult bindingResult) throws InvalidRequestException {
        sanitizeData(expense);

        validationService.validateNew(expense, bindingResult);

        final Expense returnedExpense = postExpense(expense);
        return returnedExpense;
    }

    @VisibleForTesting
    void sanitizeData(final Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(DateUtil.currentTime());
        }

        if (expense.getPayments() != null) {
            expense.getPayments().forEach(payment -> {
                if (payment.getDate() == null) {
                    payment.setDate(expense.getDate());
                }

                if (payment.getAmount() == null) {
                    payment.setAmount(0l);
                }

                setOwner(payment);
            });
        }

        setOwner(expense);
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

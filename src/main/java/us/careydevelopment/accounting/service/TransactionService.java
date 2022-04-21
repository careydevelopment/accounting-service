package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.*;
import us.careydevelopment.accounting.repository.TransactionRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private Validator validator;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private void validate(Transaction transaction) {
        final Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

        if (violations.size() > 0) {
            //TODO: Throwing a ServiceException for now because we should never get here
            throw new ServiceException("Invalid transaction: " + transaction);
        }
    }

    public void transact(final Expense expense) {
        expense.getPayments().forEach(payment -> {
            transact(payment, expense.getPaymentAccount());
        });
    }

    public void transact(final SalesReceipt salesReceipt) {
        salesReceipt.getSales().forEach(sale -> {
            transact(sale, salesReceipt.getDepositTo());
        });
    }

    public void transact(final SingleSale sale, PaymentAccount paymentAccount) {
        Transaction transaction = new Transaction();

        transaction.setDebitAccount(paymentAccount);
        transaction.setDebitAmount(sale.getAmount());

        transaction.setCreditAccount(sale.getAccount());
        transaction.setCreditAmount(sale.getAmount());

        transact(transaction);
    }

    public void transact(SinglePayment payment, PaymentAccount paymentAccount) {
        Transaction transaction = new Transaction();

        transaction.setDebitAccount(payment.getAccount());
        transaction.setDebitAmount(payment.getAmount());

        transaction.setCreditAccount(paymentAccount);
        transaction.setCreditAmount(payment.getAmount());

        transact(transaction);
    }

    public void transact(Transaction transaction) {
        try {
            post(transaction);
            accountService.update(transaction);
        } catch (Exception e) {
            LOG.error("Failure when posting transaction!", e);
            throw new ServiceException(e.getMessage());
        }
    }

    private void post(Transaction transaction) {
        //going the extra mile with validation
        validate(transaction);

        //persist the transaction BEFORE changing account values
        transactionRepository.save(transaction);

        credit(transaction.getCreditAccount(), transaction.getCreditAmount());
        debit(transaction.getDebitAccount(), transaction.getDebitAmount());
    }

    private void credit(Account account, Long amount) {
        final AccountType type = account.getAccountType();

        if (type.equals(AccountType.EXPENSE) ||
            type.equals(AccountType.ASSET)) {

            account.setValue(account.getValue() - amount);
        } else {
            account.setValue(account.getValue() + amount);
        }
    }

    private void debit(Account account, Long amount) {
        final AccountType type = account.getAccountType();

        if (type.equals(AccountType.EXPENSE) ||
                type.equals(AccountType.ASSET)) {

            account.setValue(account.getValue() + amount);
        } else {
            account.setValue(account.getValue() - amount);
        }
    }
}

package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.*;
import us.careydevelopment.accounting.repository.TransactionRepository;
import us.careydevelopment.accounting.util.DateUtil;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Component
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private Validator validator;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionValidationService transactionValidationService;

    public TransactionService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

        transaction.setDate(sale.getDate());

        transact(transaction);
    }

    public void transact(SinglePayment payment, PaymentAccount paymentAccount) {
        Transaction transaction = new Transaction();

        transaction.setDebitAccount(payment.getAccount());
        transaction.setDebitAmount(payment.getAmount());

        transaction.setCreditAccount(paymentAccount);
        transaction.setCreditAmount(payment.getAmount());

        transaction.setDate(payment.getDate());

        transact(transaction);
    }

    private void sanitizeData(final Transaction transaction) {
        if (transaction.getDate() == null) {
            transaction.setDate(DateUtil.currentTime());
        }
    }

    public Transaction transact(final Transaction transaction) {
        try {
            sanitizeData(transaction);

            final Transaction returnedTransaction = post(transaction);
            accountService.update(transaction);

            return returnedTransaction;
        } catch (Exception e) {
            LOG.error("Failure when posting transaction!", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public Transaction transact(final Transaction transaction, final BindingResult bindingResult) {
        transactionValidationService.validateNew(transaction, bindingResult);

        final Transaction returnedTransaction = transact(transaction);

        return returnedTransaction;
    }

    private Transaction post(Transaction transaction) {
        //going the extra mile with validation
        transactionValidationService.validate(transaction);

        //persist the transaction BEFORE changing account values
        final Transaction returnedTransaction = transactionRepository.save(transaction);

        credit(transaction.getCreditAccount(), transaction.getCreditAmount());
        debit(transaction.getDebitAccount(), transaction.getDebitAmount());

        //returned transaction will include updated account values
        return returnedTransaction;
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

package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.Transaction;
import us.careydevelopment.accounting.repository.TransactionRepository;
import us.careydevelopment.accounting.util.SecurityUtil;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Component
public class TransactionValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionValidationService.class);

    private Validator validator;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AccountValidationService accountValidationService;

    @Autowired
    private AccountService accountService;

    public TransactionValidationService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void validate(final Transaction transaction) {
        final Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

        if (violations.size() > 0) {
            //TODO: Throwing a ServiceException for now because we should never get here
            throw new ServiceException("Invalid transaction: " + transaction);
        }
    }

    @VisibleForTesting
    void validate(final Account account) {
        final Set<ConstraintViolation<Account>> violations = validator.validate(account);

        if (violations.size() > 0) {
            //TODO: Throwing a ServiceException for now because we should never get here
            throw new ServiceException("Invalid account: " + account);
        }
    }

    @VisibleForTesting
    public void validateNew(final Transaction transaction, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidationForNew(transaction, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Account is not valid", errors);
        }
    }

    @VisibleForTesting
    public void validateExisting(final Transaction transaction, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        //handleCustomValidationForExisting(account, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Account is not valid", errors);
        }
    }

//    @VisibleForTesting
//    void handleCustomValidationForNewAndExisting(final Account account, final List<ValidationError> errors) {
//        if (StringUtils.isBlank(account.getName())) {
//            ValidationUtil.addError(errors, "Account name can't be empty",
//                    "name", null);
//        }
//
//        if (account.getAccountType() == null) {
//            ValidationUtil.addError(errors, "Account type required",
//                    "accountType", null);
//        }
//    }

    @VisibleForTesting
    void handleCustomValidationForNew(final Transaction transaction, final List<ValidationError> errors) {
        if (transaction != null) {
            if (StringUtils.isBlank(transaction.getCreditAccount().getId())) {
                ValidationUtil.addError(errors, "Credit account ID required",
                        "creditAccount.id", null);

                return;
            }

            if (StringUtils.isBlank(transaction.getDebitAccount().getId())) {
                ValidationUtil.addError(errors, "Debit account ID required",
                        "debitAccount.id", null);

                return;
            }

            if (!accountValidationService.accountExists(transaction.getCreditAccount().getId())) {
                ValidationUtil.addError(errors, "Credit account with ID " + transaction.getCreditAccount().getId() + " doesn't exist",
                        "creditAccount.id", null);

                return;
            }

            if (!accountValidationService.accountExists(transaction.getDebitAccount().getId())) {
                ValidationUtil.addError(errors, "Debit account with ID " + transaction.getDebitAccount().getId() + " doesn't exist",
                        "debitAccount.id", null);

                return;
            }

            setPersistedData(transaction);
        }
    }

    private void setPersistedData(final Transaction transaction) {
        final Account creditAccount = accountService.retrieve(transaction.getCreditAccount().getId());
        final Account debitAccount = accountService.retrieve(transaction.getDebitAccount().getId());

        transaction.setCreditAccount(creditAccount);
        transaction.setDebitAccount(debitAccount);
    }
}

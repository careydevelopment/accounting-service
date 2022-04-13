package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.Transaction;
import us.careydevelopment.accounting.repository.AccountRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private Validator validator;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountValidationService accountValidationService;

    public AccountService() {
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

    public Account create(final Account account, final BindingResult bindingResult) throws InvalidRequestException {
        accountValidationService.validateNew(account, bindingResult);

        final Account returnedAccount = accountRepository.save(account);
        return returnedAccount;
    }

    public void update(Account account) {
        accountValidationService.validate(account);

        //update account values AFTER transaction
        accountRepository.save(account);
    }

    public Transaction update(Transaction transaction) {
        validate(transaction);

        update(transaction.getCreditAccount());
        update(transaction.getDebitAccount());

        return transaction;
    }
}

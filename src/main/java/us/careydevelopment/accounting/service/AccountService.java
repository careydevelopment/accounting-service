package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.NotFoundException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.PaymentAccount;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.repository.PaymentAccountRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;

@Component
public class AccountService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private Validator validator;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @Autowired
    private AccountValidationService accountValidationService;

    public AccountService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Account retrieve(final String id) {
        final Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isEmpty()) throw new NotFoundException("Account with ID " + id + " doesn't exist");

        return accountOptional.get();
    }

    @Transactional
    public Account create(final Account account, final BindingResult bindingResult) {
        accountValidationService.validateNew(account, bindingResult);

        sanitizeData(account);

        final Account returnedAccount = accountRepository.save(account);
        return returnedAccount;
    }

    public PaymentAccount create(final PaymentAccount account, final BindingResult bindingResult) {
        accountValidationService.validateNewPaymentAccount(account, bindingResult);

        sanitizeData(account);

        final PaymentAccount returnedAccount = paymentAccountRepository.save(account);
        return returnedAccount;
    }

    @Transactional
    public Account update(final Account account, final BindingResult bindingResult) {
        accountValidationService.validateExisting(account, bindingResult);

        final Account returnedAccount = accountRepository.save(account);
        return returnedAccount;
    }

    public void update(Account account) {
        accountValidationService.validate(account);

        //update account values AFTER transaction
        accountRepository.save(account);
    }

    @VisibleForTesting
    void sanitizeData(final Account account) {
        setOwner(account);
    }
}

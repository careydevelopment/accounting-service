package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SecurityUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

@Component
public class AccountValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountValidationService.class);

    private Validator validator;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SecurityUtil securityUtil;

    public AccountValidationService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void validate(final Account account) {
        final Set<ConstraintViolation<Account>> violations = validator.validate(account);

        if (violations.size() > 0) {
            //TODO: Throwing a ServiceException for now because we should never get here
            throw new ServiceException("Invalid account: " + account);
        }
    }

    public boolean validateOwnership(final String accountId) {
        boolean isOwner = false;
        final Optional<Account> accountOpt = accountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            final Account account = accountOpt.get();
            final String accountOwner = account.getOwner().getUsername();

            isOwner = securityUtil.isAuthorizedByUserName(accountOwner);
        }

        return isOwner;
    }
}

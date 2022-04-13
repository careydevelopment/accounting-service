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
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SecurityUtil;
import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
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

    @VisibleForTesting
    void validate(final Account account) {
        final Set<ConstraintViolation<Account>> violations = validator.validate(account);

        if (violations.size() > 0) {
            //TODO: Throwing a ServiceException for now because we should never get here
            throw new ServiceException("Invalid account: " + account);
        }
    }

    @VisibleForTesting
    public void validateNew(final Account account, final BindingResult bindingResult) throws InvalidRequestException {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidationForNew(account, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Account is not valid", errors);
        }
    }

    @VisibleForTesting
    void handleCustomValidationForNew(final Account account, final List<ValidationError> errors) {
        if (account != null) {
            if (StringUtils.isBlank(account.getName())) {
                ValidationUtil.addError(errors, "Account name can't be empty",
                        "name", null);
            }

            if (!StringUtils.isBlank(account.getId())) {
                ValidationUtil.addError(errors, "Account id not allowed for new accounts",
                        "id", null);
            }

            if (account.getAccountType() == null) {
                ValidationUtil.addError(errors, "Account type required",
                        "accountType", null);
            }
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

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
import us.careydevelopment.accounting.model.*;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SecurityUtil;
import us.careydevelopment.accounting.util.SessionUtil;
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

    @Autowired
    private SessionUtil sessionUtil;

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
    public void validateNew(final Account account, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidationForNew(account, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Account is not valid", errors);
        }
    }

    @VisibleForTesting
    public void validateExisting(final Account account, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidationForExisting(account, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Account is not valid", errors);
        }
    }

    public boolean accountNameExists(final String accountName) {
        boolean exists = false;

        final User user = sessionUtil.getCurrentUser();

        if (user != null) {
            final Account account = accountRepository.findByNameAndOwnerUsername(accountName, user.getUsername());
            if (account != null) exists = true;
        } else {
            //if the user is null we don't want to create or update the account
            LOG.warn("Null user found when trying to retrieve account by name!");
            exists = true;
        }

        return exists;
    }

    public boolean accountExists(final String id) {
        boolean exists = false;

        final Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isPresent()) exists = true;

        return exists;
    }

    @VisibleForTesting
    void handleCustomValidationForNewAndExisting(final Account account, final List<ValidationError> errors) {
        if (StringUtils.isBlank(account.getName())) {
            ValidationUtil.addError(errors, "Account name can't be empty",
                    "name", null);
        }

        if (account.getAccountType() == null) {
            ValidationUtil.addError(errors, "Account type required",
                    "accountType", null);
        }
    }

    @VisibleForTesting
    void handleCustomValidationForNew(final Account account, final List<ValidationError> errors) {
        validateSpecificAccountType(account, errors);
        validateFieldsForNew(account, errors);
    }

    @VisibleForTesting
    void validateSpecificAccountType(final Account account, final List<ValidationError> errors) {
        if (account instanceof ExpenseAccount) {
            if (!AccountType.EXPENSE.equals(account.getAccountType())) {
                ValidationUtil.addError(errors, "Account must be of type EXPENSE",
                        "accountType", null);
            }
        }

        if (account instanceof EquityAccount) {
            if (!AccountType.EQUITY.equals(account.getAccountType())) {
                ValidationUtil.addError(errors, "Account must be of type EQUITY",
                        "accountType", null);
            }
        }

        if (account instanceof RevenueAccount) {
            if (!AccountType.REVENUE.equals(account.getAccountType())) {
                ValidationUtil.addError(errors, "Account must be of type REVENUE",
                        "accountType", null);
            }
        }

        if (account instanceof AssetAccount) {
            if (!AccountType.ASSET.equals(account.getAccountType())) {
                ValidationUtil.addError(errors, "Account must be of type ASSET",
                        "accountType", null);
            }
        }

        if (account instanceof LiabilityAccount) {
            if (!AccountType.LIABILITY.equals(account.getAccountType())) {
                ValidationUtil.addError(errors, "Account must be of type LIABILITY",
                        "accountType", null);
            }
        }
    }

    @VisibleForTesting
    void validateFieldsForNew(final Account account, final List<ValidationError> errors) {
        handleCustomValidationForNewAndExisting(account, errors);

        if (account.getValue() != null && account.getValue() != 0l) {
            ValidationUtil.addError(errors, "Account value must be 0 at creation time",
                    "value", null);
        }

        if (!StringUtils.isBlank(account.getId())) {
            ValidationUtil.addError(errors, "Account id not allowed for new accounts",
                    "id", null);
        }

        if (account.getOwner() != null) {
            ValidationUtil.addError(errors, "Owner will be assigned",
                    "owner", null);
        }

        if (accountNameExists(account.getName())) {
            ValidationUtil.addError(errors, "Account name already exists",
                    "name", null);
        }
    }

    @VisibleForTesting
    void handleCustomValidationForExisting(final Account account, final List<ValidationError> errors) {
        if (account != null) {
            handleCustomValidationForNewAndExisting(account, errors);

            if (StringUtils.isBlank(account.getId())) {
                ValidationUtil.addError(errors, "Account id required for updates",
                        "id", null);

                return;
            }

            handleValidationFromPersistedAccountForExisting(account, errors);
        }
    }

    void handleValidationFromPersistedAccountForExisting(final Account account, final List<ValidationError> errors) {
        final Optional<Account> accountOpt = accountRepository.findById(account.getId());
        final Account existingAccount = accountOpt.isPresent() ? accountOpt.get() : null;

        if (existingAccount == null) {
            ValidationUtil.addError(errors, "Account with ID " + account.getId() + " doesn't exist",
                    "id", null);

            return;
        }

        if (!existingAccount.getName().equals(account.getName())) {
            if (accountNameExists(account.getName())) {
                ValidationUtil.addError(errors, "Account with name " + account.getName() + " already exists",
                        "name", null);
            }
        }

        if (!existingAccount.getValue().equals(account.getValue())) {
            ValidationUtil.addError(errors, "You may not change the value of accounts - use a transaction instead",
                    "value", null);
        }

        if (!existingAccount.getAccountType().equals(account.getAccountType())) {
            ValidationUtil.addError(errors, "You may not change the account type",
                    "accountType", null);
        }

        //TODO: setting unmodifiable data in validation service - may need refactoring
        setUnmodifiableData(account, existingAccount);
    }

    private void setUnmodifiableData(final Account account, final Account existingAccount) {
        account.setOwner(existingAccount.getOwner());
        account.setParentAccount(existingAccount.getParentAccount());
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

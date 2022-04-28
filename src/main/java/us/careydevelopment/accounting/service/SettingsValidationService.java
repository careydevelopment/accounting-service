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
import us.careydevelopment.accounting.model.Settings;
import us.careydevelopment.accounting.model.User;
import us.careydevelopment.accounting.repository.SettingsRepository;
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
public class SettingsValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsValidationService.class);

    private Validator validator;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private SessionUtil sessionUtil;

    public SettingsValidationService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @VisibleForTesting
    void validate(final Settings settings) {
        final Set<ConstraintViolation<Settings>> violations = validator.validate(settings);

        if (violations.size() > 0) {
            //TODO: Throwing a ServiceException for now because we should never get here
            throw new ServiceException("Invalid settings: " + settings);
        }
    }

    @VisibleForTesting
    public void validateNew(final Settings settings, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidationForNew(settings, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Settings not valid", errors);
        }
    }

//    @VisibleForTesting
//    public void validateExisting(final Account account, final BindingResult bindingResult) {
//        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);
//
//        handleCustomValidationForExisting(account, errors);
//
//        if (errors.size() > 0) {
//            throw new InvalidRequestException("Account is not valid", errors);
//        }
//    }


    public boolean settingsExists() {
        boolean exists = false;

        final User user = sessionUtil.getCurrentUser();

        final Settings settings = settingsRepository.findByOwnerUsername(user.getUsername());
        if (settings != null) exists = true;

        return exists;
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
    void handleCustomValidationForNew(final Settings settings, final List<ValidationError> errors) {
        validateDoesntExist(settings, errors);
        validateFieldsForNew(settings, errors);
    }

    @VisibleForTesting
    void validateDoesntExist(final Settings settings, final List<ValidationError> errors) {
        final boolean exists = settingsExists();

        if (exists) {
            ValidationUtil.addError(errors, "You already have settings",
                    "id", null);
        }
    }

    @VisibleForTesting
    void validateFieldsForNew(final Settings settings, final List<ValidationError> errors) {
        if (StringUtils.isBlank(settings.getCompanyName())) {
            ValidationUtil.addError(errors, "Company name required",
                    "companyName", null);
        }

        if (StringUtils.isBlank(settings.getCompanyLegalName())) {
            ValidationUtil.addError(errors, "Company legal name required",
                    "companyLegalName", null);
        }

        if (!StringUtils.isBlank(settings.getId())) {
            ValidationUtil.addError(errors, "Settings id not allowed for new accounts",
                    "id", null);
        }

        if (settings.getOwner() != null) {
            ValidationUtil.addError(errors, "Owner will be assigned",
                    "owner", null);
        }
    }

//    @VisibleForTesting
//    void handleCustomValidationForExisting(final Account account, final List<ValidationError> errors) {
//        if (account != null) {
//            handleCustomValidationForNewAndExisting(account, errors);
//
//            if (StringUtils.isBlank(account.getId())) {
//                ValidationUtil.addError(errors, "Account id required for updates",
//                        "id", null);
//
//                return;
//            }
//
//            handleValidationFromPersistedAccountForExisting(account, errors);
//        }
//    }

//    void handleValidationFromPersistedAccountForExisting(final Account account, final List<ValidationError> errors) {
//        final Optional<Account> accountOpt = accountRepository.findById(account.getId());
//        final Account existingAccount = accountOpt.isPresent() ? accountOpt.get() : null;
//
//        if (existingAccount == null) {
//            ValidationUtil.addError(errors, "Account with ID " + account.getId() + " doesn't exist",
//                    "id", null);
//
//            return;
//        }
//
//        if (!existingAccount.getName().equals(account.getName())) {
//            if (accountNameExists(account.getName())) {
//                ValidationUtil.addError(errors, "Account with name " + account.getName() + " already exists",
//                        "name", null);
//            }
//        }
//
//        if (!existingAccount.getValue().equals(account.getValue())) {
//            ValidationUtil.addError(errors, "You may not change the value of accounts - use a transaction instead",
//                    "value", null);
//        }
//
//        if (!existingAccount.getAccountType().equals(account.getAccountType())) {
//            ValidationUtil.addError(errors, "You may not change the account type",
//                    "accountType", null);
//        }
//
//        //TODO: setting unmodifiable data in validation service - may need refactoring
//        setUnmodifiableData(account, existingAccount);
//    }

//    private void setUnmodifiableData(final Account account, final Account existingAccount) {
//        account.setOwner(existingAccount.getOwner());
//        account.setParentAccount(existingAccount.getParentAccount());
//    }

    public boolean validateOwnership(final String settingsId) {
        boolean isOwner = false;
        final Optional<Settings> settingsOpt = settingsRepository.findById(settingsId);

        if (settingsOpt.isPresent()) {
            final Settings settings = settingsOpt.get();
            final String owner = settings.getOwner().getUsername();

            isOwner = securityUtil.isAuthorizedByUserName(owner);
        }

        return isOwner;
    }
}

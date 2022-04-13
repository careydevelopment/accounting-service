package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.exception.NotFoundException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.*;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

import java.util.Date;
import java.util.List;

@Component
public class ExpenseValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseValidationService.class);

    @Autowired
    private BusinessService businessService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AccountValidationService accountValidationService;

    public void validateNew(final Expense expense, final BindingResult bindingResult) throws InvalidRequestException {
        sanitizeData(expense);

        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidation(expense, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Expense is not valid", errors);
        }
    }

    @VisibleForTesting
    void sanitizeData(final Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(new Date().getTime());
        }

        if (expense.getPayments() != null) {
            expense.getPayments().forEach(payment -> {
                if (payment.getDate() == null) {
                    payment.setDate(expense.getDate());
                }

                if (payment.getAmount() == null) {
                    payment.setAmount(0l);
                }
            });
        }
    }

    @VisibleForTesting
    void handleCustomValidation(final Expense expense, final List<ValidationError> errors) {
        validateBusiness(expense, errors);
        validateOwnership(expense, errors);
    }

    @VisibleForTesting
    void validateOwnership(final Expense expense, final List<ValidationError> errors) {
        validatePaymentAccountOwnership(expense.getPaymentAccount(), errors);
        validatePaymentsOwnership(expense.getPayments(), errors);
    }

    @VisibleForTesting
    void validatePaymentsOwnership(final List<SinglePayment> payments, final List<ValidationError> errors) {
        payments.forEach(payment -> {
            final Account account = payment.getAccount();
            final boolean authorized = accountValidationService.validateOwnership(account.getId());

            if (!authorized) {
                ValidationUtil.addError(errors, "You aren't the owner of the account: " + account.getName(),
                        "account", null);
            }
        });
    }

    @VisibleForTesting
    void validatePaymentAccountOwnership(final PaymentAccount paymentAccount, final List<ValidationError> errors) {
        final boolean authorized = accountValidationService.validateOwnership(paymentAccount.getId());

        if (!authorized) {
            ValidationUtil.addError(errors, "You aren't the owner of the payment account: " + paymentAccount.getName(),
                    "paymentAccount", null);
        }
    }

    @VisibleForTesting
    void validateBusiness(final Expense expense, final List<ValidationError> errors) {
        validateNameOrId(expense.getPayee(), errors);
        validateBusinessExists(expense, errors);
    }

    @VisibleForTesting
    void validateNameOrId(final BusinessLightweight business, final List<ValidationError> errors) {
        final String businessName = business.getName();
        final String businessId = business.getId();

        if (StringUtils.isBlank(businessName) && StringUtils.isBlank(businessId)) {
            ValidationUtil.addError(errors, "Either Business ID or Business Name is required", "payee.id", null);
        }
    }

    @VisibleForTesting
    void validateBusinessExists(final Expense expense, final List<ValidationError> errors) {
        final BusinessLightweight business = expense.getPayee();
        final String businessId = business.getId();

        if (!StringUtils.isBlank(businessId)) {
            try {
                BusinessLightweight lightweight = businessService.fetchBusiness(businessId);
                expense.setPayee(lightweight);
            } catch (NotFoundException nfe) {
                ValidationUtil.addError(errors, "Business ID is not valid", "payee.id", null);
            } catch (Exception e) {
                LOG.error("Problem trying to fetch busineess by ID " + businessId, e);
                throw new ServiceException("Problem retrieving business by ID " + businessId);
            }
        }
    }
}

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
import us.careydevelopment.accounting.repository.PaymentAccountRepository;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.validation.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    public void validateNew(final Expense expense, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidation(expense, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Expense is not valid", errors);
        }
    }

    @VisibleForTesting
    void handleCustomValidation(final Expense expense, final List<ValidationError> errors) {
        validateBusiness(expense, errors);
        validateAccounts(expense, errors);
    }

    @VisibleForTesting
    void validateAccounts(final Expense expense, final List<ValidationError> errors) {
        validatePaymentAccount(expense, errors);
        validatePayments(expense, errors);
    }

    @VisibleForTesting
    void validatePayments(final Expense expense, final List<ValidationError> errors) {
        final List<SinglePayment> payments = expense.getPayments();
        AtomicInteger counter = new AtomicInteger(0);

        payments.forEach(payment -> {
            final Account account = payment.getAccount();
            counter.getAndIncrement();

            if (!StringUtils.isBlank(account.getId())) {
                final Optional<Account> acct = accountRepository.findById(account.getId());

                if (acct.isPresent()) {
                    if (sessionUtil.getCurrentUser().equals(acct.get().getOwner())) {
                        payment.setAccount(acct.get());
                    } else {
                        ValidationUtil.addError(errors, "You aren't the owner of the account: " + account.getName(),
                                "account.owner", null);
                    }
                } else {
                    ValidationUtil.addError(errors, "Account with ID: " + account.getId() + " doesn't exist",
                            "account.id", null);
                }
            } else {
                ValidationUtil.addError(errors, "Payment " + counter.get() + " account doesn't have a valid ID",
                        "account.id", null);
            }
        });
    }

    @VisibleForTesting
    void validatePaymentAccount(final Expense expense, final List<ValidationError> errors) {
        final PaymentAccount paymentAccount = expense.getPaymentAccount();

        if (!StringUtils.isBlank(paymentAccount.getId())) {
            final Optional<PaymentAccount> retrievedAccount = paymentAccountRepository.findById(paymentAccount.getId());

            if (retrievedAccount.isPresent()) {
                final User user = sessionUtil.getCurrentUser();
                final boolean authorized = user.equals(retrievedAccount.get().getOwner());

                if (authorized) {
                    expense.setPaymentAccount(retrievedAccount.get());
                } else {
                    ValidationUtil.addError(errors, "You aren't the owner of the payment account: " + paymentAccount.getName(),
                            "paymentAccount.owner", null);
                }
            } else {
                ValidationUtil.addError(errors, "Payment account with ID: " + paymentAccount.getId() + " doesn't exist!",
                        "paymentAccount.id", null);
            }
        } else {
            ValidationUtil.addError(errors, "Payment account ID is required",
                    "paymentAccount.id", null);
        }
    }

    @VisibleForTesting
    void validateBusiness(final Expense expense, final List<ValidationError> errors) {
        validateNameOrId(expense.getPayee(), errors);
        validateBusinessExists(expense, errors);
    }

    @VisibleForTesting
    void validateNameOrId(final Business business, final List<ValidationError> errors) {
        final String businessName = business.getName();
        final String businessId = business.getId();

        if (StringUtils.isBlank(businessName) && StringUtils.isBlank(businessId)) {
            ValidationUtil.addError(errors, "Either Business ID or Business Name is required", "payee.id", null);
        }
    }

    @VisibleForTesting
    void validateBusinessExists(final Expense expense, final List<ValidationError> errors) {
        final Business business = expense.getPayee();
        final String businessId = business.getId();

        if (!StringUtils.isBlank(businessId)) {
            try {
                Business lightweight = businessService.fetchBusiness(businessId);
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

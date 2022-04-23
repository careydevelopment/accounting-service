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
public class SalesValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(SalesValidationService.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AccountValidationService accountValidationService;

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    public void validateNew(final SalesReceipt salesReceipt, final BindingResult bindingResult) {
        final List<ValidationError> errors = ValidationUtil.convertBindingResultToValidationErrors(bindingResult);

        handleCustomValidation(salesReceipt, errors);

        if (errors.size() > 0) {
            throw new InvalidRequestException("Sales receipt is not valid", errors);
        }
    }

    @VisibleForTesting
    void handleCustomValidation(final SalesReceipt salesReceipt, final List<ValidationError> errors) {
        validateCustomer(salesReceipt, errors);
        validateAccounts(salesReceipt, errors);
    }

    @VisibleForTesting
    void validateAccounts(final SalesReceipt salesReceipt, final List<ValidationError> errors) {
        validatePaymentAccount(salesReceipt, errors);
        validatePayments(salesReceipt, errors);
    }

    @VisibleForTesting
    void validatePayments(final SalesReceipt salesReceipt, final List<ValidationError> errors) {
        final List<SingleSale> sales = salesReceipt.getSales();
        AtomicInteger counter = new AtomicInteger(0);

        sales.forEach(sale -> {
            final Account account = sale.getAccount();
            counter.getAndIncrement();

            if (!StringUtils.isBlank(account.getId())) {
                final Optional<Account> acct = accountRepository.findById(account.getId());

                if (acct.isPresent()) {
                    if (sessionUtil.getCurrentUser().equals(acct.get().getOwner())) {
                        sale.setAccount(acct.get());
                    } else {
                        ValidationUtil.addError(errors, "You aren't the owner of the account: " + account.getName(),
                                "account.owner", null);
                    }
                } else {
                    ValidationUtil.addError(errors, "Account with ID: " + account.getId() + " doesn't exist",
                            "account.id", null);
                }
            } else {
                ValidationUtil.addError(errors, "Sale " + counter.get() + " account doesn't have a valid ID",
                        "account.id", null);
            }
        });
    }

    @VisibleForTesting
    void validatePaymentAccount(final SalesReceipt salesReceipt, final List<ValidationError> errors) {
        final PaymentAccount paymentAccount = salesReceipt.getDepositTo();

        if (!StringUtils.isBlank(paymentAccount.getId())) {
            final Optional<PaymentAccount> retrievedAccount = paymentAccountRepository.findById(paymentAccount.getId());

            if (retrievedAccount.isPresent()) {
                final User user = sessionUtil.getCurrentUser();
                final boolean authorized = user.equals(retrievedAccount.get().getOwner());

                if (authorized) {
                    salesReceipt.setDepositTo(retrievedAccount.get());
                } else {
                    ValidationUtil.addError(errors, "You aren't the owner of the deposit account: " + paymentAccount.getName(),
                            "depositTo.owner", null);
                }
            } else {
                ValidationUtil.addError(errors, "Deposit account with ID: " + paymentAccount.getId() + " doesn't exist!",
                        "depositTo.id", null);
            }
        } else {
            ValidationUtil.addError(errors, "Deposit account ID is required",
                    "depositTo.id", null);
        }
    }

    @VisibleForTesting
    void validateCustomer(final SalesReceipt salesReceipt, final List<ValidationError> errors) {
        validateCustomerExists(salesReceipt, errors);
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
    void validateCustomerExists(final SalesReceipt salesReceipt, final List<ValidationError> errors) {
        final Customer customer = salesReceipt.getCustomer();
        final String id = customer.getId();

        if (!StringUtils.isBlank(id)) {
            try {
                final Customer lightweight = customerService.fetchCustomer(id);
                salesReceipt.setCustomer(lightweight);
            } catch (NotFoundException nfe) {
                ValidationUtil.addError(errors, "Customer ID is not valid", "customer.id", null);
            } catch (Exception e) {
                LOG.error("Problem trying to fetch customer by ID " + id, e);
                throw new ServiceException("Problem retrieving customer by ID " + id);
            }
        }
    }
}

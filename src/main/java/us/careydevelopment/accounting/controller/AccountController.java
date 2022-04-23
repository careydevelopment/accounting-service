package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.PaymentAccount;
import us.careydevelopment.accounting.service.AccountService;
import us.careydevelopment.util.api.model.IRestResponse;
import us.careydevelopment.util.api.response.ResponseEntityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class AccountController {
	
    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<IRestResponse<Account>> createAccount(final HttpServletRequest request,
                                                                 @Valid @RequestBody final Account account,
                                                                 final BindingResult bindingResult) {
        LOG.debug("Adding account: " + account);

        final Account returnedAccount = accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

    @PostMapping("/payment-accounts")
    public ResponseEntity<IRestResponse<Account>> createPaymentAccount(final HttpServletRequest request,
                                                                @Valid @RequestBody final PaymentAccount account,
                                                                final BindingResult bindingResult) {

        LOG.debug("Adding payment account: " + account);

        final PaymentAccount returnedAccount = accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created payment account!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

    @PutMapping("/accounts")
    public ResponseEntity<IRestResponse<Account>> updateAccount(final HttpServletRequest request,
                                                                @Valid @RequestBody final Account account,
                                                                final BindingResult bindingResult) {
        LOG.debug("Updating account: " + account);

        final Account returnedAccount = accountService.update(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully updated!",
                HttpStatus.OK.value(),
                returnedAccount);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<IRestResponse<Account>> retrieveAccount(final HttpServletRequest request,
                                                                  final @PathVariable String id) {
        LOG.debug("Locating account by ID: " + id);

        final Account account = accountService.retrieve(id);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Found account",
                HttpStatus.OK.value(),
                account);
    }
}

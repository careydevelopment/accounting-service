package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.careydevelopment.accounting.model.*;
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

    @PostMapping("/accounts/expenses")
    public ResponseEntity<IRestResponse<ExpenseAccount>> createExpenseAccount(@Valid @RequestBody final ExpenseAccount account,
                                                                        final BindingResult bindingResult) {
        LOG.debug("Adding expense account: " + account);

        final ExpenseAccount returnedAccount = (ExpenseAccount)accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

    @PostMapping("/accounts/liabilities")
    public ResponseEntity<IRestResponse<LiabilityAccount>> createLiabilityAccount(@Valid @RequestBody final LiabilityAccount account,
                                                                              final BindingResult bindingResult) {
        LOG.debug("Adding liability account: " + account);

        final LiabilityAccount returnedAccount = (LiabilityAccount)accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

    @PostMapping("/accounts/revenue")
    public ResponseEntity<IRestResponse<RevenueAccount>> createRevenueAccount(@Valid @RequestBody final RevenueAccount account,
                                                                              final BindingResult bindingResult) {
        LOG.debug("Adding revenue account: " + account);

        final RevenueAccount returnedAccount = (RevenueAccount)accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

    @PostMapping("/accounts/assets")
    public ResponseEntity<IRestResponse<AssetAccount>> createEquityAccount(@Valid @RequestBody final AssetAccount account,
                                                                              final BindingResult bindingResult) {
        LOG.debug("Adding asset account: " + account);

        final AssetAccount returnedAccount = (AssetAccount)accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

    @PostMapping("/accounts/equity")
    public ResponseEntity<IRestResponse<EquityAccount>> createEquityAccount(@Valid @RequestBody final EquityAccount account,
                                                                            final BindingResult bindingResult) {
        LOG.debug("Adding equity account: " + account);

        final EquityAccount returnedAccount = (EquityAccount)accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

    @PostMapping("/accounts/assets/paymentAccounts")
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

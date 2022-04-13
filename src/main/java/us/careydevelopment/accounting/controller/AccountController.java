package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.model.Account;
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
    public ResponseEntity<IRestResponse<Account>> createBusiness(final HttpServletRequest request,
                                                                 @Valid @RequestBody final Account account,
                                                                 final BindingResult bindingResult)
                                                                    throws InvalidRequestException {
        LOG.debug("Adding account: " + account);

        final Account returnedAccount = accountService.create(account, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
                HttpStatus.CREATED.value(),
                returnedAccount);
    }

//    @PutMapping("/businesses")
//    public ResponseEntity<IRestResponse<Business>> updateBusiness(final HttpServletRequest request,
//                                                                  @Valid @RequestBody final Business business,
//                                                                  final BindingResult bindingResult)
//                                                                    throws InvalidRequestException,
//                                                                            NotFoundException, NotAuthorizedException {
//        LOG.debug("Updating business: " + business);
//
//        final Business returnedBusiness = businessService.update(business, bindingResult);
//
//        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully updated!",
//                HttpStatus.OK.value(),
//                returnedBusiness);
//    }
//
//    @GetMapping("/businesses/{id}")
//    public ResponseEntity<IRestResponse<Business>> retrieveBusiness(final HttpServletRequest request,
//                                                                  final @PathVariable String id) throws NotFoundException {
//        LOG.debug("Locating business by ID: " + id);
//
//        final Business business = businessService.retrieve(id);
//
//        return ResponseEntityUtil.createSuccessfulResponseEntity("Found business",
//                HttpStatus.OK.value(),
//                business);
//    }
}

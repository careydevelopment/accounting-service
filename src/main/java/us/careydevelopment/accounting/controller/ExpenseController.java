package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import us.careydevelopment.accounting.exception.InvalidRequestException;
import us.careydevelopment.accounting.model.Expense;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.IRestResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RestController
public class ExpenseController {
	
    private static final Logger LOG = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping("/expenses")
    public ResponseEntity<IRestResponse<List<Expense>>> retrieveExpenses(final HttpServletRequest request)
            throws InvalidRequestException {


        //sessionUtil.getCurrentUser();
        System.err.println("in here");

        sessionUtil.getCurrentUser();
        System.err.println(sessionUtil.getBearerToken());
        return null;
    }


    @PostMapping("/expenses")
    public ResponseEntity<IRestResponse<Expense>> postExpense(final HttpServletRequest request,
                                                                 @Valid @RequestBody final Expense expense,
                                                                 final BindingResult bindingResult)
                                                                    throws InvalidRequestException {
        LOG.debug("Posting expense: " + expense);



        return null;
//        final Business returnedBusiness = businessService.save(business, bindingResult);
//
//        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created!",
//                HttpStatus.CREATED.value(),
//                returnedBusiness);
    }
}

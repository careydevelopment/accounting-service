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
import us.careydevelopment.accounting.model.Transaction;
import us.careydevelopment.accounting.service.TransactionService;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.IRestResponse;
import us.careydevelopment.util.api.response.ResponseEntityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class TransactionController {
	
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private TransactionService transactionService;

//    @GetMapping("/expenses")
//    public ResponseEntity<IRestResponse<List<Expense>>> retrieveExpenses(final HttpServletRequest request)
//            throws InvalidRequestException {
//
//        return null;
//    }

    @PostMapping("/transactions")
    public ResponseEntity<IRestResponse<Transaction>> postTransaction(final HttpServletRequest request,
                                                                  @Valid @RequestBody final Transaction transaction,
                                                                  final BindingResult bindingResult) {
        LOG.debug("Posting transaction: " + transaction);

        final Transaction returnedTransaction = transactionService.transact(transaction, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully posted expense!",
                HttpStatus.CREATED.value(),
                returnedTransaction);
    }
}

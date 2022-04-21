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
import us.careydevelopment.accounting.model.SalesReceipt;
import us.careydevelopment.accounting.service.SalesService;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.IRestResponse;
import us.careydevelopment.util.api.response.ResponseEntityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class SalesController {
	
    private static final Logger LOG = LoggerFactory.getLogger(SalesController.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private SalesService salesService;

//    @GetMapping("/expenses")
//    public ResponseEntity<IRestResponse<List<Expense>>> retrieveExpenses(final HttpServletRequest request)
//            throws InvalidRequestException {
//
//        return null;
//    }

    @PostMapping("/salesReceipt")
    public ResponseEntity<IRestResponse<SalesReceipt>> postSales(final HttpServletRequest request,
                                                            @Valid @RequestBody final SalesReceipt salesReceipt,
                                                            final BindingResult bindingResult)
                                                                    throws InvalidRequestException {
        LOG.debug("Posting sales: " + salesReceipt);

        final SalesReceipt returnedReceipt = salesService.postSalesReceipt(salesReceipt, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully posted sales receipt!",
                HttpStatus.CREATED.value(),
                returnedReceipt);
    }
}

package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.careydevelopment.accounting.model.BalanceSheet;
import us.careydevelopment.accounting.model.IncomeStatement;
import us.careydevelopment.accounting.service.BalanceSheetService;
import us.careydevelopment.accounting.service.IncomeStatementService;
import us.careydevelopment.util.api.model.IRestResponse;
import us.careydevelopment.util.api.response.ResponseEntityUtil;


@RestController
@RequestMapping("/reports")
public class ReportsController {
	
    private static final Logger LOG = LoggerFactory.getLogger(ReportsController.class);

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private IncomeStatementService incomeStatementService;

    @GetMapping("/balanceSheet")
    public ResponseEntity<IRestResponse<BalanceSheet>> getBalanceSHeet() {
        LOG.debug("Getting balance sheet");

        final BalanceSheet balanceSheet = balanceSheetService.report();

        return ResponseEntityUtil.createSuccessfulResponseEntity("Success!",
                HttpStatus.OK.value(),
                balanceSheet);
    }

    @GetMapping("/incomeStatement")
    public ResponseEntity<IRestResponse<IncomeStatement>> getIncomeStatement() {
        LOG.debug("Getting income statement");

        final IncomeStatement incomeStatement = incomeStatementService.report();

        return ResponseEntityUtil.createSuccessfulResponseEntity("Success!",
                HttpStatus.OK.value(),
                incomeStatement);
    }
}

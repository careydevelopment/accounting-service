package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.careydevelopment.accounting.model.BalanceSheet;
import us.careydevelopment.accounting.model.CashFlowStatement;
import us.careydevelopment.accounting.model.IncomeStatement;
import us.careydevelopment.accounting.service.BalanceSheetService;
import us.careydevelopment.accounting.service.CashFlowStatementService;
import us.careydevelopment.accounting.service.IncomeStatementService;
import us.careydevelopment.accounting.util.DateUtil;
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

    @Autowired
    private CashFlowStatementService cashFlowStatementService;

    @GetMapping("/balanceSheet")
    public ResponseEntity<IRestResponse<BalanceSheet>> getBalanceSHeet(@RequestParam(required = false) Long asOf) {
        LOG.debug("Getting balance sheet");

        if (asOf == null) {
            asOf = DateUtil.currentTime();
        }

        final BalanceSheet balanceSheet = balanceSheetService.report(asOf);

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

    @GetMapping("/cashFlowStatement")
    public ResponseEntity<IRestResponse<CashFlowStatement>> getCashFlowStatement() {
        LOG.debug("Getting cash flow statement");

        final CashFlowStatement cashFlowStatement = cashFlowStatementService.report();

        return ResponseEntityUtil.createSuccessfulResponseEntity("Success!",
                HttpStatus.OK.value(),
                cashFlowStatement);
    }
}

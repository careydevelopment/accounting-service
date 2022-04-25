package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.CashFlowStatement;
import us.careydevelopment.accounting.model.User;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SessionUtil;

import java.util.List;

@Service
public class CashFlowStatementService {

    private static final Logger LOG = LoggerFactory.getLogger(CashFlowStatementService.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IncomeStatementService incomeStatementService;

    public CashFlowStatement report() {
        final User user = sessionUtil.getCurrentUser();
        LOG.debug("Getting cash flow statement for " + user);

        if (user != null) {
            return reportByUser(user);
        } else {
            throw new ServiceException("Could not identify current user!");
        }
    }

    public CashFlowStatement reportByUser(final User user) {
        final CashFlowStatement cashFlowStatement = new CashFlowStatement();
        cashFlowStatement.setUser(user);

        final List<Account> allAccounts = accountRepository.findByOwnerUsername(user.getUsername());

        return cashFlowStatement;
    }
}

package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;
import us.careydevelopment.accounting.model.IncomeStatement;
import us.careydevelopment.accounting.model.User;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SessionUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeStatementService {

    private static final Logger LOG = LoggerFactory.getLogger(IncomeStatementService.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private TransactionService transactionService;

    public IncomeStatement report() {
        final User user = sessionUtil.getCurrentUser();
        LOG.debug("Getting income statement for " + user);

        if (user != null) {
            return reportByUser(user);
        } else {
            throw new ServiceException("Could not identify current user!");
        }
    }

    public IncomeStatement reportByUser(final User user) {
        final IncomeStatement incomeStatement = new IncomeStatement();
        incomeStatement.setUser(user);

        final List<Account> allAccounts = accountRepository.findByOwnerUsername(user.getUsername());

        calculateRevenue(incomeStatement, allAccounts);
        calculateExpenses(incomeStatement, allAccounts);

        incomeStatement.setNetIncome(incomeStatement.getTotalRevenue() - incomeStatement.getTotalExpenses());
        LOG.debug("Net income is " + incomeStatement.getNetIncome());

        return incomeStatement;
    }

    private void calculateRevenue(final IncomeStatement incomeStatement, final List<Account> allAccounts) {
        final List<Account> revenueAccounts = allAccounts
                .stream()
                .filter(account -> account.getAccountType().equals(AccountType.REVENUE))
                .collect(Collectors.toList());

        final Long revenueValue = revenueAccounts
                .stream()
                .mapToLong(account -> account.getValue())
                .sum();

        LOG.debug("Revenue value is " + revenueValue);

        incomeStatement.setRevenue(revenueAccounts);
        incomeStatement.setTotalRevenue(revenueValue);
    }

    private void calculateExpenses(final IncomeStatement incomeStatement, final List<Account> allAccounts) {
        final List<Account> expenseAccounts = allAccounts
                .stream()
                .filter(account -> account.getAccountType().equals(AccountType.EXPENSE))
                .collect(Collectors.toList());

        final Long expensesValue = expenseAccounts
                .stream()
                .mapToLong(account -> account.getValue())
                .sum();

        LOG.debug("Expenses value is " + expensesValue);

        incomeStatement.setExpenses(expenseAccounts);
        incomeStatement.setTotalExpenses(expensesValue);
    }
}

package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SessionUtil;

import java.util.List;

@Service
public class NetIncomeService {

    private static final Logger LOG = LoggerFactory.getLogger(NetIncomeService.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AccountRepository accountRepository;

    public Account getNetIncomeAsAccount(final List<Account> accounts) {
        final Account account = new Account();

        account.setName("Net Income");
        account.setAccountType(AccountType.EQUITY);

        final Long netIncome = calculateNetIncome(accounts);
        account.setValue(netIncome);

        return account;
    }

    private Long calculateNetIncome(final List<Account> accounts) {
        LOG.debug("Calculating net income from provided accounts");

        final Long revenue = accounts
                .stream()
                .filter(account -> account.getAccountType().equals(AccountType.REVENUE))
                .mapToLong(account -> account.getValue())
                .sum();

        LOG.debug("Revenue is " + revenue);

        final Long expenses = accounts
                .stream()
                .filter(account -> account.getAccountType().equals(AccountType.EXPENSE))
                .mapToLong(account -> account.getValue())
                .sum();

        LOG.debug("Expenses is " + expenses);

        final Long netIncome = revenue - expenses;

        LOG.debug("Net income is " + netIncome);

        return netIncome;
    }
}

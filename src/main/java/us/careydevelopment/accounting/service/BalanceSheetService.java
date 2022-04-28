package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;
import us.careydevelopment.accounting.model.BalanceSheet;
import us.careydevelopment.accounting.model.User;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.util.SessionUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalanceSheetService {

    private static final Logger LOG = LoggerFactory.getLogger(BalanceSheetService.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IncomeStatementService incomeStatementService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TransactionService transactionService;

    public BalanceSheet report(final Long asOf) {
        final User user = sessionUtil.getCurrentUser();
        LOG.debug("Getting balance sheet for " + user);

        if (user != null) {
            return reportByUser(user, asOf);
        } else {
            throw new ServiceException("Could not identify current user!");
        }
    }

    public BalanceSheet reportByUser(final User user, final Long asOf) {
        final BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setUser(user);
        balanceSheet.setAsOf(asOf);

        final List<Account> allAccounts = accountRepository.findByOwnerUsername(user.getUsername());
        final List<Account> accountsAtTime = transactionService.getAccountsAsOf(asOf, allAccounts);

        calculateAssets(balanceSheet, accountsAtTime);
        calculateLiabilities(balanceSheet, accountsAtTime);
        calculateEquity(balanceSheet, accountsAtTime);

        return balanceSheet;
    }

    private void calculateAssets(final BalanceSheet balanceSheet, final List<Account> allAccounts) {
        final List<Account> assetAccounts = allAccounts
                .stream()
                .filter(account -> account.getAccountType().equals(AccountType.ASSET))
                .collect(Collectors.toList());

        final Long assetsValue = assetAccounts
                .stream()
                .mapToLong(account -> account.getValue())
                .sum();

        LOG.debug("Assets value is " + assetsValue);

        balanceSheet.setAssets(assetAccounts);
        balanceSheet.setAssetsValue(assetsValue);
    }

    private void calculateLiabilities(final BalanceSheet balanceSheet, final List<Account> allAccounts) {
        final List<Account> liabilityAccounts = allAccounts
                .stream()
                .filter(account -> account.getAccountType().equals(AccountType.LIABILITY))
                .collect(Collectors.toList());

        final Long liabilitiesValue = liabilityAccounts
                .stream()
                .mapToLong(account -> account.getValue())
                .sum();

        LOG.debug("Liabilities value is " + liabilitiesValue);

        balanceSheet.setLiabilities(liabilityAccounts);
        balanceSheet.setLiabilitiesValue(liabilitiesValue);
    }

    private void calculateEquity(final BalanceSheet balanceSheet, final List<Account> accountsAtTime) {
        calculateEquityFromPersistedAccounts(balanceSheet, accountsAtTime);
    }

    private void calculateEquityFromPersistedAccounts(final BalanceSheet balanceSheet, final List<Account> allAccounts) {
        final List<Account> equityAccounts = allAccounts
                .stream()
                .filter(account -> account.getAccountType().equals(AccountType.EQUITY))
                .collect(Collectors.toList());

        final Long equityValue = equityAccounts
                .stream()
                .mapToLong(account -> account.getValue())
                .sum();

        LOG.debug("Persisted equity value is " + equityValue);

        balanceSheet.setEquity(equityAccounts);
        balanceSheet.setEquityValue(equityValue);
    }
}

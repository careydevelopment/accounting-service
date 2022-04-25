package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public BalanceSheet report() {
        final User user = sessionUtil.getCurrentUser();
        LOG.debug("Getting balance sheet for " + user);

        if (user != null) {
            return reportByUser(user);
        } else {
            throw new ServiceException("Could not identify current user!");
        }
    }

    public BalanceSheet reportByUser(final User user) {
        final BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setUser(user);

        final List<Account> allAccounts = accountRepository.findByOwnerUsername(user.getUsername());

        calculateAssets(balanceSheet, allAccounts);
        calculateLiabilities(balanceSheet, allAccounts);
        calculateEquity(balanceSheet, allAccounts);

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

    private void calculateEquity(final BalanceSheet balanceSheet, final List<Account> allAccounts) {
        calculateEquityFromPersistedAccounts(balanceSheet, allAccounts);
        calculateEquityFromDerivedAccounts(balanceSheet, allAccounts);
    }

    private void calculateEquityFromDerivedAccounts(final BalanceSheet balanceSheet, final List<Account> allAccounts) {
        final Account netIncomeAccount = incomeStatementService.getNetIncomeAsAccount(allAccounts);
        balanceSheet.getEquity().add(netIncomeAccount);
        LOG.debug("Net income derived account is " + netIncomeAccount);

        final Long updatedEquityValue = balanceSheet.getEquityValue() + netIncomeAccount.getValue();
        LOG.debug("Updated equity value is " + updatedEquityValue);

        balanceSheet.setEquityValue(updatedEquityValue);
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

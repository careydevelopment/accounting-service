package us.careydevelopment.accounting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.*;
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

        calculateBeginningCash(cashFlowStatement, allAccounts);
        calculateNetIncome(cashFlowStatement, allAccounts);
        calculateOperatingActivities(cashFlowStatement, allAccounts);
        calculateInvestingActivities(cashFlowStatement, allAccounts);
        calculateFinancingActivities(cashFlowStatement, allAccounts);

        cashFlowStatement.setChangeInCash(cashFlowStatement.getCashFromOperatingActivities()
                + cashFlowStatement.getCashFromInvestingActivities()
                + cashFlowStatement.getCashFromFinancingActivities());

        cashFlowStatement.setCashAtEnd(cashFlowStatement.getCashAtBeginning() + cashFlowStatement.getChangeInCash());

        return cashFlowStatement;
    }

    private void calculateBeginningCash(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts) {
        final Long cashAtBeginning = allAccounts
                .stream()
                .filter(account -> AccountType.ASSET.equals(account.getAccountType()))
                .map(account -> (AssetAccount)account)
                .filter(account -> account.isCash())
                .mapToLong(account -> account.getValue())
                .sum();

        cashFlowStatement.setCashAtBeginning(cashAtBeginning);
    }

    private void calculateNetIncome(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts) {
        //final Account netIncomeAccount = incomeStatementService.getNetIncomeAsAccount(allAccounts, null);
        //cashFlowStatement.setNetIncome(netIncomeAccount.getValue());
    }

    private void calculateFinancingActivities(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts) {
        //nothing for now
    }

    private void calculateInvestingActivities(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts) {
        addInvestingActivity(cashFlowStatement, allAccounts, "Capital Equipment Purchase", AssetAccountType.FIXED_ASSETS);

        final Long changes = cashFlowStatement
                .getInvestingActivities()
                .stream()
                .mapToLong(activity -> activity.getValue())
                .sum();

        cashFlowStatement.setCashFromInvestingActivities(changes);
    }

    private void addInvestingActivity(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts,
                                    final String operatingActivityName, final AssetAccountType type) {

        final Long adjustment = allAccounts
                .stream()
                .filter(account -> AccountType.ASSET.equals(account.getAccountType()))
                .map(account -> (AssetAccount)account)
                .filter(account -> type.equals(account.getAssetAccountType()))
                .mapToLong(account -> account.getValue())
                .sum();

        final InvestingActivity activity = new InvestingActivity();
        activity.setName(operatingActivityName);
        activity.setValue(-1 * adjustment);

        cashFlowStatement.getInvestingActivities().add(activity);
    }

    private void calculateOperatingActivities(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts) {
        addBasicAdjustment(cashFlowStatement, allAccounts, "Depreciation", ExpenseAccountType.DEPRECIATION);
        addBasicAdjustment(cashFlowStatement, allAccounts, "Amortization", ExpenseAccountType.AMORTIZATION);
        addBasicAdjustment(cashFlowStatement, allAccounts, "Tax", ExpenseAccountType.TAXES);

        addAssetChange(cashFlowStatement, allAccounts, "Inventory", AssetAccountType.INVENTORY);
        addAssetChange(cashFlowStatement, allAccounts, "Accounts Receivable", AssetAccountType.ACCOUNTS_RECEIVABLE);

        addLiabilityChange(cashFlowStatement, allAccounts, "Accounts Payable", LiabilityAccountType.ACCOUNTS_PAYABLE);

        final Long changes = cashFlowStatement
                .getOperatingActivities()
                .stream()
                .filter(activity -> activity.getType().equals(OperatingActivityType.CHANGES_IN_OPERATING_ASSETS))
                .mapToLong(activity -> activity.getValue())
                .sum();

        final Long adjustments = cashFlowStatement
                .getOperatingActivities()
                .stream()
                .filter(activity -> activity.getType().equals(OperatingActivityType.ADJUSTMENTS_TO_NET_INCOME))
                .mapToLong(activity -> activity.getValue())
                .sum();

        cashFlowStatement.setCashFromOperatingActivities(cashFlowStatement.getNetIncome() + adjustments + changes);
    }

    private void addLiabilityChange(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts,
                                final String operatingActivityName, final LiabilityAccountType type) {

        final Long adjustment = allAccounts
                .stream()
                .filter(account -> AccountType.LIABILITY.equals(account.getAccountType()))
                .map(account -> (LiabilityAccount)account)
                .filter(account -> type.equals(account.getLiabilityAccountType()))
                .mapToLong(account -> account.getValue())
                .sum();

        final OperatingActivity operatingActivity = new OperatingActivity();
        operatingActivity.setType(OperatingActivityType.CHANGES_IN_OPERATING_ASSETS);
        operatingActivity.setName(operatingActivityName);
        operatingActivity.setValue(adjustment);

        cashFlowStatement.getOperatingActivities().add(operatingActivity);
    }

    private void addAssetChange(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts,
                                    final String operatingActivityName, final AssetAccountType type) {

        final Long adjustment = allAccounts
                .stream()
                .filter(account -> AccountType.ASSET.equals(account.getAccountType()))
                .map(account -> (AssetAccount)account)
                .filter(account -> type.equals(account.getAssetAccountType()))
                .mapToLong(account -> account.getValue())
                .sum();

        final OperatingActivity operatingActivity = new OperatingActivity();
        operatingActivity.setType(OperatingActivityType.CHANGES_IN_OPERATING_ASSETS);
        operatingActivity.setName(operatingActivityName);
        operatingActivity.setValue(-1 * adjustment);

        cashFlowStatement.getOperatingActivities().add(operatingActivity);
    }

    private void addBasicAdjustment(final CashFlowStatement cashFlowStatement, final List<Account> allAccounts,
                                    final String operatingActivityName, final ExpenseAccountType type) {
        final Long adjustment = allAccounts
                .stream()
                .filter(account -> AccountType.EXPENSE.equals(account.getAccountType()))
                .map(account -> (ExpenseAccount)account)
                .filter(account -> type.equals(account.getExpenseAccountType()))
                .mapToLong(account -> account.getValue())
                .sum();

        final OperatingActivity operatingActivity = new OperatingActivity();
        operatingActivity.setType(OperatingActivityType.ADJUSTMENTS_TO_NET_INCOME);
        operatingActivity.setName(operatingActivityName);
        operatingActivity.setValue(adjustment);

        cashFlowStatement.getOperatingActivities().add(operatingActivity);
    }
}


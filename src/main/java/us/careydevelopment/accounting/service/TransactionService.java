package us.careydevelopment.accounting.service;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.model.*;
import us.careydevelopment.accounting.repository.TransactionRepository;
import us.careydevelopment.accounting.util.DateUtil;
import us.careydevelopment.accounting.util.SessionUtil;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private Validator validator;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionValidationService transactionValidationService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SessionUtil sessionUtil;

    public TransactionService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private List<? extends Account> getLatestAccount(List<? extends Account> creditAccounts,
                                                     List<? extends Account> debitAccounts) {

        List<? extends Account> list = new ArrayList<>();
        List<String> usedList = new ArrayList<>();

        for (Account creditAccount : creditAccounts) {
            boolean foundMatch = true;

            for (Account debitAccount : debitAccounts) {
                if (creditAccount.getName().equals(debitAccount.getName())) {
                    //if (creditAccount.getd )
                }
            }
        }

        return list;
    }

    public List<Account> getAccountsInTimeRange(final Long start, final Long end, final List<Account> allAccounts) {
        return getAccountsInTimeRange(start, end, allAccounts, null);
    }

    public List<Account> getAccountsInTimeRange(final Long start, final Long end, final List<Account> allAccounts,
                                         List<AccountType> excludeAccountTypes) {

        final List<Account> accounts = new ArrayList<>();

        allAccounts.forEach(account -> {
            if (excludeAccountTypes == null || !excludeAccountTypes.contains(account.getAccountType())) {
                final Transaction transaction = getLatestTransactionInTimeRange(start, end, account.getId());

                if (transaction != null) {
                    final Account acc = getAccountFromTransaction(transaction, account.getId());
                    accounts.add(acc);
                }
            }
        });

        return accounts;
    }

    public List<Account> getAccountsAsOf(final Long asOf, final List<Account> allAccounts) {
        return getAccountsAsOf(asOf, allAccounts, null);
    }

    public List<Account> getAccountsAsOf(final Long asOf, final List<Account> allAccounts,
                                         List<AccountType> excludeAccountTypes) {

        final List<Account> accounts = new ArrayList<>();

        allAccounts.forEach(account -> {
            if (excludeAccountTypes == null || !excludeAccountTypes.contains(account.getAccountType())) {
                final Transaction transaction = getLatestTransaction(asOf, account.getId());

                if (transaction != null) {
                    final Account acc = getAccountFromTransaction(transaction, account.getId());
                    accounts.add(acc);
                }
            }
        });

        return accounts;
    }

    private Account getAccountFromTransaction(final Transaction transaction, final String accountId) {
        Account account = null;

        if (transaction.getCreditAccount().getId().equals(accountId)) {
            account = transaction.getCreditAccount();
        } else if (transaction.getDebitAccount().getId().equals(accountId)) {
            account = transaction.getDebitAccount();
        } else {
            LOG.warn("No match found for transaction " + transaction.getId() + " and account ID " + accountId);
        }

        return account;
    }

    private Transaction getLatestTransaction(final Long asOf, final String accountId) {
        final List<AggregationOperation> ops = new ArrayList<>();

        final Criteria creditAccountCheck = Criteria.where("creditAccount._id").is(new ObjectId(accountId));
        final Criteria debitAccountCheck = Criteria.where("debitAccount._id").is(new ObjectId(accountId));

        final AggregationOperation accountMatch = Aggregation
                .match(new Criteria().orOperator(creditAccountCheck, debitAccountCheck));
        ops.add(accountMatch);

        final AggregationOperation dateMatch = Aggregation
                .match(Criteria.where("date").lte(asOf));
        ops.add(dateMatch);

        final AggregationOperation sortDescending = Aggregation
                .sort(Sort.Direction.DESC, "date");
        ops.add(sortDescending);

        final AggregationOperation limit = Aggregation
                .limit(1);
        ops.add(limit);

        final Aggregation aggregation = Aggregation.newAggregation(ops);
        final List<Transaction> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Transaction.class), Transaction.class).getMappedResults();

        if (results != null) {
            if (results.size() == 1) {
                return results.get(0);
            } else if (results.size() > 1) {
                LOG.warn("More than 1 result returned when fetching latest transaction for account ID " + accountId);
            }
        }

        return null;
    }

    private Transaction getLatestTransactionInTimeRange(final Long start, final Long end, final String accountId) {
        final List<AggregationOperation> ops = new ArrayList<>();

        final Criteria creditAccountCheck = Criteria.where("creditAccount._id").is(new ObjectId(accountId));
        final Criteria debitAccountCheck = Criteria.where("debitAccount._id").is(new ObjectId(accountId));

        final AggregationOperation accountMatch = Aggregation
                .match(new Criteria().orOperator(creditAccountCheck, debitAccountCheck));
        ops.add(accountMatch);

        final AggregationOperation endMatch = Aggregation
                .match(Criteria.where("date").lte(end));
        ops.add(endMatch);

        final AggregationOperation startMatch = Aggregation
                .match(Criteria.where("date").gte(start));
        ops.add(startMatch);

        final AggregationOperation sortDescending = Aggregation
                .sort(Sort.Direction.DESC, "date");
        ops.add(sortDescending);

        final AggregationOperation limit = Aggregation
                .limit(1);
        ops.add(limit);

        final Aggregation aggregation = Aggregation.newAggregation(ops);
        final List<Transaction> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Transaction.class), Transaction.class).getMappedResults();

        if (results != null) {
            if (results.size() == 1) {
                return results.get(0);
            } else if (results.size() > 1) {
                LOG.warn("More than 1 result returned when fetching latest transaction for account ID " + accountId);
            }
        }

        return null;
    }

    private void equityAdjustment(final Long amount, final Long date, final AccountType accountType) {
        final EquityAccount equityAccount = getEquityAccountForAdjustment(date);

        if (equityAccount == null) return;

        final Transaction transaction = new Transaction();
        transaction.setOwner(sessionUtil.getCurrentUser());
        transaction.setDate(date);

        if (accountType.equals(AccountType.EXPENSE)) {
            equityAccount.setValue(equityAccount.getValue() - amount);
            transaction.setDebitAccount(equityAccount);
        } else if (accountType.equals(AccountType.REVENUE)) {
            equityAccount.setValue(equityAccount.getValue() + amount);
            transaction.setCreditAccount(equityAccount);
        }

        transactionRepository.save(transaction);
        accountService.update(equityAccount);
    }

    private EquityAccount getEquityAccountForAdjustment(final Long date) {
        final boolean isPreviousYear = DateUtil.isPreviousAccountingPeriod(date);

        if (isPreviousYear) {
            final EquityAccount equityAccount = accountService.getRetainedEarningsAccount();
            return equityAccount;
        } else {
            final EquityAccount equityAccount = accountService.getNetIncomeAccount();
            return equityAccount;
        }
    }

    @Transactional
    public void transact(final Expense expense) {
        expense.getPayments().forEach(payment -> {
            transact(payment, expense.getPaymentAccount());
        });
    }

    @Transactional
    public void transact(final SalesReceipt salesReceipt) {
        salesReceipt.getSales().forEach(sale -> {
            transact(sale, salesReceipt.getDepositTo());
        });
    }

    @Transactional
    public void transact(final SingleSale sale, Account account) {
        final Transaction transaction = new Transaction();

        transaction.setDebitAccount(account);
        transaction.setDebitAmount(sale.getAmount());

        transaction.setCreditAccount(sale.getAccount());
        transaction.setCreditAmount(sale.getAmount());

        transaction.setDate(sale.getDate());

        transact(transaction);
    }

    @Transactional
    public void transact(SinglePayment payment, AssetAccount paymentAccount) {
        final Transaction transaction = new Transaction();

        transaction.setDebitAccount(payment.getAccount());
        transaction.setDebitAmount(payment.getAmount());

        transaction.setCreditAccount(paymentAccount);
        transaction.setCreditAmount(payment.getAmount());

        transaction.setDate(payment.getDate());

        transact(transaction);
    }

    private void sanitizeData(final Transaction transaction) {
        if (transaction.getDate() == null) {
            transaction.setDate(DateUtil.currentTime());
        }

        transaction.setOwner(sessionUtil.getCurrentUser());
    }

    public Transaction transact(final Transaction transaction) {
        sanitizeData(transaction);

        final Transaction returnedTransaction = post(transaction);
        accountService.update(transaction.getCreditAccount());
        accountService.update(transaction.getDebitAccount());

        if (transaction.getCreditAccount().getAccountType().equals(AccountType.REVENUE)) {
            equityAdjustment(transaction.getCreditAmount(), transaction.getDate(), AccountType.REVENUE);
        }

        if (transaction.getDebitAccount().getAccountType().equals(AccountType.EXPENSE)) {
            equityAdjustment(transaction.getDebitAmount(), transaction.getDate(), AccountType.EXPENSE);
        }

        return returnedTransaction;
    }

    @Transactional
    public Transaction transact(final Transaction transaction, final BindingResult bindingResult) {
        transactionValidationService.validateNew(transaction, bindingResult);

        final Transaction returnedTransaction = transact(transaction);

        return returnedTransaction;
    }

    private Transaction post(Transaction transaction) {
        //going the extra mile with validation
        transactionValidationService.validate(transaction);

        //update account values first
        credit(transaction.getCreditAccount(), transaction.getCreditAmount());
        debit(transaction.getDebitAccount(), transaction.getDebitAmount());

        //persist transaction
        final Transaction returnedTransaction = transactionRepository.save(transaction);

        //returned transaction will include updated account values
        return returnedTransaction;
    }

    private void credit(Account account, Long amount) {
        final AccountType type = account.getAccountType();

        if (type.equals(AccountType.EXPENSE) ||
            type.equals(AccountType.ASSET)) {

            account.setValue(account.getValue() - amount);
        } else {
            account.setValue(account.getValue() + amount);
        }
    }

    private void debit(Account account, Long amount) {
        final AccountType type = account.getAccountType();

        if (type.equals(AccountType.EXPENSE) ||
                type.equals(AccountType.ASSET)) {

            account.setValue(account.getValue() + amount);
        } else {
            account.setValue(account.getValue() - amount);
        }
    }
}

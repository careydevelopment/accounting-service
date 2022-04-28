package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.NotFoundException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;
import us.careydevelopment.accounting.model.EquityAccount;
import us.careydevelopment.accounting.model.EquityAccountType;
import us.careydevelopment.accounting.repository.AccountRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AccountService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private Validator validator;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountValidationService accountValidationService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public AccountService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public EquityAccount getNetIncomeAccount() {
        final List<AggregationOperation> ops = new ArrayList<>();

        final AggregationOperation typeMatch = Aggregation
                .match(Criteria.where("accountType").is(AccountType.EQUITY.name()));
        ops.add(typeMatch);

        final AggregationOperation equityTypeMatch = Aggregation
                .match(Criteria.where("equityAccountType").is(EquityAccountType.NET_INCOME.name()));
        ops.add(equityTypeMatch);

        final AggregationOperation ownerMatch = Aggregation
                .match(Criteria.where("owner.username").is(sessionUtil.getCurrentUser().getUsername()));
        ops.add(ownerMatch);

        final Aggregation aggregation = Aggregation.newAggregation(ops);
        final List<EquityAccount> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Account.class), EquityAccount.class).getMappedResults();

        if (results != null) {
            if (results.size() == 1) {
                return results.get(0);
            } else if (results.size() > 1) {
                LOG.error("More than 1 result returned when fetching net income account");
                throw new ServiceException("More than 1 result returned when fetching net income account");
            }
        }

        return null;
    }

    public EquityAccount getRetainedEarningsAccount() {
        final List<AggregationOperation> ops = new ArrayList<>();

        final AggregationOperation typeMatch = Aggregation
                .match(Criteria.where("accountType").is(AccountType.EQUITY.name()));
        ops.add(typeMatch);

        final AggregationOperation equityTypeMatch = Aggregation
                .match(Criteria.where("equityAccountType").is(EquityAccountType.RETAINED_EARNINGS.name()));
        ops.add(equityTypeMatch);

        final Aggregation aggregation = Aggregation.newAggregation(ops);
        final List<EquityAccount> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Account.class), EquityAccount.class).getMappedResults();

        if (results != null) {
            if (results.size() == 1) {
                return results.get(0);
            } else if (results.size() > 1) {
                LOG.error("More than 1 result returned when fetching retained earnings account");
                throw new ServiceException("More than 1 result returned when fetching retained earnings account");
            }
        }

        return null;
    }

    public Account retrieve(final String id) {
        final Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isEmpty()) throw new NotFoundException("Account with ID " + id + " doesn't exist");

        return accountOptional.get();
    }

    @Transactional
    public Account create(final Account account, final BindingResult bindingResult) {
        accountValidationService.validateNew(account, bindingResult);

        sanitizeData(account);

        final Account returnedAccount = accountRepository.save(account);
        return returnedAccount;
    }

    @Transactional
    public Account update(final Account account, final BindingResult bindingResult) {
        accountValidationService.validateExisting(account, bindingResult);

        final Account returnedAccount = accountRepository.save(account);
        return returnedAccount;
    }

    public void update(Account account) {
        accountValidationService.validate(account);

        //update account values AFTER transaction
        accountRepository.save(account);
    }

    @VisibleForTesting
    void sanitizeData(final Account account) {
        setOwner(account);
    }
}

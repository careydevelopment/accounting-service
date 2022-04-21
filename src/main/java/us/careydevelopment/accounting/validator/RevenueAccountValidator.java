package us.careydevelopment.accounting.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;
import us.careydevelopment.accounting.repository.AccountRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class RevenueAccountValidator implements ConstraintValidator<RevenueAccount, Account> {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void initialize(RevenueAccount constraintAnnotation) {
    }

    @Override
    public boolean isValid(Account account, ConstraintValidatorContext context) {
        if (account != null && !StringUtils.isBlank(account.getId())) {
            Optional<Account> acctOpt = accountRepository.findById(account.getId());

            if (acctOpt.isPresent()) {
                if (!acctOpt.get().getAccountType().equals(AccountType.REVENUE)) {
                    return false;
                }
            }
        }

        return true;
    }
}

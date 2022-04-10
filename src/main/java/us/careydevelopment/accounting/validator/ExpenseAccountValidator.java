package us.careydevelopment.accounting.validator;

import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExpenseAccountValidator implements ConstraintValidator<ExpenseAccount, Account> {

    @Override
    public void initialize(ExpenseAccount constraintAnnotation) {
    }

    @Override
    public boolean isValid(Account account, ConstraintValidatorContext context) {
        if (account != null) {
            if (!account.getAccountType().equals(AccountType.EXPENSE)) {
                return false;
            }
        }

        return true;
    }
}

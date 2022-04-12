package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.AccountType;

public class AccountHarness {

    public static final String TELEPHONE_ACCOUNT_ID = "343";
    public static final String INTERNET_ACCOUNT_ID = "8393";
    public static final String SALES_ACCOUNT_ID = "232";

    public static Account getTelephoneExpenseAccount() {
        Account account = new Account();
        account.setAccountType(AccountType.EXPENSE);
        account.setName("Telephone");
        account.setId(TELEPHONE_ACCOUNT_ID);
        account.setValue(0l);
        account.setOwner(UserLightweightHarness.getMrSmithUserLightweight());

        return account;
    }

    public static Account getInternetExpenseAccount() {
        Account account = new Account();
        account.setAccountType(AccountType.EXPENSE);
        account.setName("Internet");
        account.setId(INTERNET_ACCOUNT_ID);
        account.setValue(0l);
        account.setOwner(UserLightweightHarness.getMrSmithUserLightweight());

        return account;
    }

    public static Account getSalesRevenueAccount() {
        Account account = new Account();
        account.setAccountType(AccountType.REVENUE);
        account.setName("Sales");
        account.setId(SALES_ACCOUNT_ID);
        account.setValue(0l);

        return account;
    }
}

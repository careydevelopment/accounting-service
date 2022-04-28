package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.Transaction;

public class TransactionHarness {

    public static final Long INITIAL_PHONE_EXPENSE_VALUE = 0l;
    public static final Long INITIAL_BANK_ACCOUNT_VALUE = 712000l;
    public static final Long PHONE_BILL_VALUE = 33207l;
    public static final String PHONE_BILL_ID = "2039";

    public static Transaction payPhoneBill() {
        final Transaction transaction = new Transaction();

        final Account debitAccount = AccountHarness.getTelephoneExpenseAccount();
        debitAccount.setValue(INITIAL_PHONE_EXPENSE_VALUE);

//        final Account creditAccount = PaymentAccountHarness.getValidBankPaymentAccount();
//        creditAccount.setValue(INITIAL_BANK_ACCOUNT_VALUE);

        transaction.setDebitAccount(debitAccount);
        transaction.setDebitAmount(PHONE_BILL_VALUE);

//        transaction.setCreditAccount(creditAccount);
        transaction.setCreditAmount(PHONE_BILL_VALUE);

        transaction.setId(PHONE_BILL_ID);

        return transaction;
    }
}

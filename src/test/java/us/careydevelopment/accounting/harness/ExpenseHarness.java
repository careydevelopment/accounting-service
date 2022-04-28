package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.*;

import java.util.Date;
import java.util.List;

public class ExpenseHarness {

    public static final String TELEPHONE_EXPENSE_ID = "15393";
    public static final Long TELEPHONE_EXPENSE_AMOUNT = 137l;

    public static Expense getValidTelephoneExpense() {
        final Expense expense = new Expense();
        expense.setId(TELEPHONE_EXPENSE_ID);
        expense.setDate(new Date().getTime());
        expense.setPayee(BusinessLightweightHarness.getValidBusinessLightweightNoPerson());
        //expense.setPaymentAccount(PaymentAccountHarness.getValidBankPaymentAccount());
        expense.setPaymentMethod(PaymentMethod.CHECK);

        final Account account = AccountHarness.getTelephoneExpenseAccount();
        final User owner = UserLightweightHarness.getMrSmithUserLightweight();

        SinglePayment singlePayment = SinglePaymentHarness.getSinglePayment(account, TELEPHONE_EXPENSE_AMOUNT,
                TELEPHONE_EXPENSE_ID, owner);

        expense.setPayments(List.of(singlePayment));

        return expense;
    }
}

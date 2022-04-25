package us.careydevelopment.accounting.model;

public class ExpenseAccount extends Account {

    private ExpenseAccountType expenseAccountType;

    public ExpenseAccount() {
        setAccountType(AccountType.EXPENSE);
    }

    public ExpenseAccountType getExpenseAccountType() {
        return expenseAccountType;
    }

    public void setExpenseAccountType(ExpenseAccountType expenseAccountType) {
        this.expenseAccountType = expenseAccountType;
    }
}

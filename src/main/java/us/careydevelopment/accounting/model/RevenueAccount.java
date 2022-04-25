package us.careydevelopment.accounting.model;

public class RevenueAccount extends Account {

    private RevenueAccountType revenueAccountType;

    public RevenueAccount() {
        setAccountType(AccountType.REVENUE);
    }

    public RevenueAccountType getRevenueAccountType() {
        return revenueAccountType;
    }

    public void setRevenueAccountType(RevenueAccountType revenueAccountType) {
        this.revenueAccountType = revenueAccountType;
    }
}

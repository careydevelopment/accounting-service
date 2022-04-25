package us.careydevelopment.accounting.model;

public class EquityAccount extends Account {

    private EquityAccountType equityAccountType;

    public EquityAccount() {
        setAccountType(AccountType.EQUITY);
    }

    public EquityAccountType getEquityAccountType() {
        return equityAccountType;
    }

    public void setEquityAccountType(EquityAccountType equityAccountType) {
        this.equityAccountType = equityAccountType;
    }
}

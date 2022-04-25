package us.careydevelopment.accounting.model;

public class LiabilityAccount extends Account {

    private LiabilityAccountType liabilityAccountType;

    public LiabilityAccount() {
        setAccountType(AccountType.LIABILITY);
    }

    public LiabilityAccountType getLiabilityAccountType() {
        return liabilityAccountType;
    }

    public void setLiabilityAccountType(LiabilityAccountType liabilityAccountType) {
        this.liabilityAccountType = liabilityAccountType;
    }
}

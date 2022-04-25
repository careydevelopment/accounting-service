package us.careydevelopment.accounting.model;

public class AssetAccount extends Account {

    private AssetAccountType assetAccountType;

    public AssetAccount() {
        setAccountType(AccountType.ASSET);
    }

    public AssetAccountType getAssetAccountType() {
        return assetAccountType;
    }

    public void setAssetAccountType(AssetAccountType assetAccountType) {
        this.assetAccountType = assetAccountType;
    }
}

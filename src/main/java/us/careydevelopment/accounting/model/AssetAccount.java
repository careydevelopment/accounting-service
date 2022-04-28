package us.careydevelopment.accounting.model;

import java.util.List;

public class AssetAccount extends Account {

    private static List<AssetAccountType> CASH_TYPES = List.of(AssetAccountType.CHECKING_ACCOUNT, AssetAccountType.SAVINGS_ACCOUNT,
            AssetAccountType.TRUST_ACCOUNT, AssetAccountType.CASH_ON_HAND, AssetAccountType.MONEY_MARKET,
            AssetAccountType.RENTS_HELD_IN_TRUST);

    private static List<AssetAccountType> CURRENT_ASSET_TYPES = List.of(AssetAccountType.ACCOUNTS_RECEIVABLE,
            AssetAccountType.CURRENT_ASSETS);

    private AssetAccountType assetAccountType;

    public AssetAccount() {
        setAccountType(AccountType.ASSET);
    }

    public AssetAccountType getAssetAccountType() {
        return assetAccountType;
    }

    public boolean isCash() {
        boolean isCash = CASH_TYPES.contains(getAssetAccountType());
        return isCash;
    }

    public boolean isCurrentAsset() {
        boolean isCurrentAsset = isCash() || CURRENT_ASSET_TYPES.contains(getAssetAccountType());
        return isCurrentAsset;
    }

    public void setAssetAccountType(AssetAccountType assetAccountType) {
        this.assetAccountType = assetAccountType;
    }
}

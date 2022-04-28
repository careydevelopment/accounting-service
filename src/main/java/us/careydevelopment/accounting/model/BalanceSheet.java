package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class BalanceSheet {

    private User user;
    private Long asOf;
    private List<Account> assets = new ArrayList<>();
    private List<Account> liabilities = new ArrayList<>();
    private List<Account> equity = new ArrayList<>();
    private Long assetsValue = 0l;
    private Long liabilitiesValue = 0l;
    private Long equityValue = 0l;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAsOf() {
        return asOf;
    }

    public void setAsOf(Long asOf) {
        this.asOf = asOf;
    }

    public List<Account> getAssets() {
        return assets;
    }

    public void setAssets(List<Account> assets) {
        this.assets = assets;
    }

    public List<Account> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(List<Account> liabilities) {
        this.liabilities = liabilities;
    }

    public List<Account> getEquity() {
        return equity;
    }

    public void setEquity(List<Account> equity) {
        this.equity = equity;
    }

    public Long getAssetsValue() {
        return assetsValue;
    }

    public void setAssetsValue(Long assetsValue) {
        this.assetsValue = assetsValue;
    }

    public Long getLiabilitiesValue() {
        return liabilitiesValue;
    }

    public void setLiabilitiesValue(Long liabilitiesValue) {
        this.liabilitiesValue = liabilitiesValue;
    }

    public Long getEquityValue() {
        return equityValue;
    }

    public void setEquityValue(Long equityValue) {
        this.equityValue = equityValue;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

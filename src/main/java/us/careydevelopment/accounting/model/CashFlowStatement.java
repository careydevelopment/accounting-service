package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class CashFlowStatement {

    private User user;
    private Long netIncome = 0l;

    private Long cashAtBeginning = 0l;

    private List<OperatingActivity> operatingActivities = new ArrayList<>();
    private List<InvestingActivity> investingActivities = new ArrayList<>();
    private List<FinancingActivity> financingActivities = new ArrayList<>();

    private Long cashAtEnd = 0l;
    private Long changeInCash = 0l;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(Long netIncome) {
        this.netIncome = netIncome;
    }

    public Long getCashAtBeginning() {
        return cashAtBeginning;
    }

    public void setCashAtBeginning(Long cashAtBeginning) {
        this.cashAtBeginning = cashAtBeginning;
    }

    public List<OperatingActivity> getOperatingActivities() {
        return operatingActivities;
    }

    public void setOperatingActivities(List<OperatingActivity> operatingActivities) {
        this.operatingActivities = operatingActivities;
    }

    public List<InvestingActivity> getInvestingActivities() {
        return investingActivities;
    }

    public void setInvestingActivities(List<InvestingActivity> investingActivities) {
        this.investingActivities = investingActivities;
    }

    public List<FinancingActivity> getFinancingActivities() {
        return financingActivities;
    }

    public void setFinancingActivities(List<FinancingActivity> financingActivities) {
        this.financingActivities = financingActivities;
    }

    public Long getCashAtEnd() {
        return cashAtEnd;
    }

    public void setCashAtEnd(Long cashAtEnd) {
        this.cashAtEnd = cashAtEnd;
    }

    public Long getChangeInCash() {
        return changeInCash;
    }

    public void setChangeInCash(Long changeInCash) {
        this.changeInCash = changeInCash;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

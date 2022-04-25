package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class IncomeStatement {

    private User user;
    private List<Account> revenue = new ArrayList<>();
    private List<Account> expenses = new ArrayList<>();
    private Long totalRevenue = 0l;
    private Long totalExpenses = 0l;
    private Long netIncome = 0l;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Account> getRevenue() {
        return revenue;
    }

    public void setRevenue(List<Account> revenue) {
        this.revenue = revenue;
    }

    public List<Account> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Account> expenses) {
        this.expenses = expenses;
    }

    public Long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Long totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Long getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(Long netIncome) {
        this.netIncome = netIncome;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

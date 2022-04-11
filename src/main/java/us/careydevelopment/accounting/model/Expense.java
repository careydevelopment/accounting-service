package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the digital equivalent of an expense receipt, which is why it gets its own collection.
 *
 * There's some detail that gets store here that's not relevant to every transaction. It's only specific
 * to expenses (e.g., payee). As a result, we'll give this one its own collection and reference the relevant
 * accounts and transactions.
 */
@Document(collection = "#{@environment.getProperty('mongo.expense.collection')}")
public class Expense extends OwnedItem {

    @Id
    private String id;

    @Valid
    @NotNull
    private BusinessLightweight payee;

    @Valid
    @NotNull
    private PaymentAccount paymentAccount;

    @NotNull(message = "Expense date is required")
    private Long date;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Size(max = 12, message = "Reference number cannot exceed 12 characters")
    private String referenceNumber;

    private List<String> tags = new ArrayList<>();

    @NotEmpty(message = "Payment list cannot be empty.")
    private List<@Valid SinglePayment> payments = new ArrayList<>();

    @Size(max = 128, message = "Memo cannot exceed 128 characters")
    private String memo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BusinessLightweight getPayee() {
        return payee;
    }

    public void setPayee(BusinessLightweight payee) {
        this.payee = payee;
    }

    public PaymentAccount getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(PaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<SinglePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<SinglePayment> payments) {
        this.payments = payments;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

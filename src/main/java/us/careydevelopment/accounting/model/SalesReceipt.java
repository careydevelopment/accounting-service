package us.careydevelopment.accounting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "#{@environment.getProperty('mongo.salesReceipt.collection')}")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesReceipt extends OwnedItem {

    @Id
    private String id;

    @NotNull
    private Customer customer;

    private Address billingAddress;

    //Date is always a long in epoch millseconds UTC
    private Long date;

    private List<String> tags = new ArrayList<>();

    @NotNull
    private PaymentMethod paymentMethod;

    private String referenceNumber;

    @NotNull
    private Account depositTo;

    @NotEmpty(message = "Sales list cannot be empty.")
    private List<SingleSale> sales = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public Account getDepositTo() {
        return depositTo;
    }

    public void setDepositTo(Account depositTo) {
        this.depositTo = depositTo;
    }

    public List<SingleSale> getSales() {
        return sales;
    }

    public void setSales(List<SingleSale> sales) {
        this.sales = sales;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesReceipt that = (SalesReceipt) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

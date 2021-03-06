package us.careydevelopment.accounting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import us.careydevelopment.accounting.util.LongFromIntegerDeserializer;
import us.careydevelopment.accounting.validator.ExpenseAccount;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Represents a single payment.
 *
 * The account referenced here must be an expense account.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SinglePayment extends OwnedItem {

    @Id
    private String id;

    @Valid
    @NotNull
    @ExpenseAccount
    private Account account;

    @Size(max = 128, message = "Description cannot exceed 128 characters")
    private String description;

    @JsonDeserialize(using = LongFromIntegerDeserializer.class)
    private Long amount;

    //Date is always a long in epoch millseconds UTC
    private Long date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SinglePayment that = (SinglePayment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

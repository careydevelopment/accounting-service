package us.careydevelopment.accounting.model;

import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Objects;

public class SinglePayment extends OwnedItem {

    @Id
    private String id;

    @Valid
    private Account account;

    @Size(max = 128, message = "Description cannot exceed 128 characters")
    private String description;

    private Long amount;
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

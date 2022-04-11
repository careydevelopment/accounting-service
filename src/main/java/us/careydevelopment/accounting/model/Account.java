package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Account gets its own collection for a couple of reasons.
 *
 * First, I don't think it's efficient to crawl through a list of transactions just to get the current
 * value.
 *
 * Second, there's a one-to-squillions relationship between account and transactions. As a result, it's best
 * to keep them in separate collections and store the reference to each account in the transaction.
 */
@Document(collection = "#{@environment.getProperty('mongo.account.collection')}")
public class Account extends OwnedItem {

    @Id
    private String id;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotBlank(message = "Account name cannot be blank")
    @Size(max = 32, message = "Account name cannot exceed 32 characters")
    private String name;

    private Long value = 0l;

    @Valid
    private Account parentAccount;

    @Size(max = 128, message = "Description cannot exceed 128 characters")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Account getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(Account parentAccount) {
        this.parentAccount = parentAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

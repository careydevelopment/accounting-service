package us.careydevelopment.accounting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A series of Transaction objects is the digital ledger. That's why it gets its own
 * collection in MongoDB. It will just reference the relevant accounts since there's a
 * one-to-squillions relationship between account and transaction.
 */
@Document(collection = "#{@environment.getProperty('mongo.transaction.collection')}")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    @Id
    private String id;

    @NotNull
    @Valid
    private Account debitAccount;
    private Long debitAmount = 0l;

    @NotNull
    @Valid
    private Account creditAccount;
    private Long creditAmount = 0l;

    private Long date;

    @AssertTrue(message = "Debit and credit amounts must be equal")
    public boolean isDebitAmount() {
        if (!debitAmount.equals(creditAmount)) {
            return false;
        }

        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public Account getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }

    public Long getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Long debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Long getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Long creditAmount) {
        this.creditAmount = creditAmount;
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
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

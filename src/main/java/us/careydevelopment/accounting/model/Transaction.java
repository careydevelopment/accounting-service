package us.careydevelopment.accounting.model;

import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class Transaction {

    @Id
    private String id;

    @NotNull
    @Valid
    private Account debitAccount;
    private Long debitAmount;

    @NotNull
    @Valid
    private Account creditAccount;
    private Long creditAmount;

    @AssertTrue(message = "Debit and credit amounts must be equal")
    public boolean isDebitAccount() {
        if (debitAmount != creditAmount) {
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
}

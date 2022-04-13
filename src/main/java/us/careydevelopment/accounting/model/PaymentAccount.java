package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This class extends account but provides additional info unique to payment accounts,
 * such as the payment account type (Bank or other current asset) as well
 * as payment detail type (checking account, trust, etc.).
 */
public class PaymentAccount extends Account {

    public PaymentAccount() {
        setAccountType(AccountType.ASSET);
    }

    @NotNull
    private PaymentAccountType paymentAccountType = PaymentAccountType.BANK;

    @NotNull
    private PaymentAccountDetailType paymentAccountDetailType = PaymentAccountDetailType.CHECKING;

    public PaymentAccountType getPaymentAccountType() {
        return paymentAccountType;
    }

    public void setPaymentAccountType(PaymentAccountType paymentAccountType) {
        this.paymentAccountType = paymentAccountType;
    }

    public PaymentAccountDetailType getPaymentAccountDetailType() {
        return paymentAccountDetailType;
    }

    public void setPaymentAccountDetailType(PaymentAccountDetailType paymentAccountDetailType) {
        this.paymentAccountDetailType = paymentAccountDetailType;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentAccount that = (PaymentAccount) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}

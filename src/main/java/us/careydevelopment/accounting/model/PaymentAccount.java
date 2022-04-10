package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class PaymentAccount extends Account {

    @Id
    private String id;

    @NotNull
    private PaymentAccountType paymentAccountType = PaymentAccountType.BANK;

    @NotNull
    private PaymentAccountDetailType paymentAccountDetailType = PaymentAccountDetailType.CHECKING;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

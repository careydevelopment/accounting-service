package us.careydevelopment.accounting.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class PaymentAccount extends OwnedItem {

    @Id
    private String id;

    private PaymentAccountType paymentAccountType = PaymentAccountType.BANK;
    private PaymentAccountDetailType paymentAccountDetailType = PaymentAccountDetailType.CHECKING;

    @NotBlank(message = "Payment account name is required")
    private String name;

    @Size(max = 128, message = "Description cannot exceed 128 characters")
    private String description;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

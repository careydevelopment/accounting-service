package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.PaymentAccount;
import us.careydevelopment.accounting.model.PaymentAccountDetailType;
import us.careydevelopment.accounting.model.PaymentAccountType;

public class PaymentAccountHarness {

    public static final String NAME = "Fred's Bank";
    public static final String ID = "093393";

    public static PaymentAccount getValidBankPaymentAccount() {
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setPaymentAccountType(PaymentAccountType.BANK);
        paymentAccount.setName(NAME);
        paymentAccount.setId(ID);
        paymentAccount.setPaymentAccountDetailType(PaymentAccountDetailType.CHECKING);

        return paymentAccount;
    }
}

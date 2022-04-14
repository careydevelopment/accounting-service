package us.careydevelopment.accounting.harness;

import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.model.SinglePayment;
import us.careydevelopment.accounting.model.User;

import java.util.Date;

public class SinglePaymentHarness {

    public static SinglePayment getSinglePayment(final Account account, final Long amount, final String id,
                                               final User owner) {

        final SinglePayment singlePayment = new SinglePayment();
        singlePayment.setAmount(amount);
        singlePayment.setDate(new Date().getTime());
        singlePayment.setId(id);
        singlePayment.setOwner(owner);
        singlePayment.setAccount(account);

        return singlePayment;
    }
}

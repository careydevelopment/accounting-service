package us.careydevelopment.accounting.service;

import org.springframework.stereotype.Component;
import us.careydevelopment.accounting.model.*;

@Component
public class TransactionService {

    public void transact(Expense expense) {
        expense.getPayments().forEach(payment -> {
            transact(payment, expense.getPaymentAccount());
        });
    }

    public void transact(SinglePayment payment, PaymentAccount paymentAccount) {
        Transaction transaction = new Transaction();

        transaction.setCreditAccount(payment.getAccount());
        transaction.setCreditAmount(payment.getAmount());

        transaction.setDebitAccount(paymentAccount);
        transaction.setDebitAmount(payment.getAmount());

        transact(transaction);
    }

    public void transact(Transaction transaction) {
        credit(transaction.getCreditAccount(), transaction.getCreditAmount());
        debit(transaction.getDebitAccount(), transaction.getDebitAmount());
    }

    private void credit(Account account, Long amount) {
        final AccountType type = account.getAccountType();

        if (type.equals(AccountType.EXPENSE) ||
            type.equals(AccountType.ASSET)) {

            account.setValue(account.getValue() + amount);
        } else {
            account.setValue(account.getValue() - amount);
        }
    }

    private void debit(Account account, Long amount) {
        final AccountType type = account.getAccountType();

        if (type.equals(AccountType.EXPENSE) ||
                type.equals(AccountType.ASSET)) {

            account.setValue(account.getValue() - amount);
        } else {
            account.setValue(account.getValue() + amount);
        }
    }
}

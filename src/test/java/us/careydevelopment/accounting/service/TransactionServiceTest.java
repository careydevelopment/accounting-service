package us.careydevelopment.accounting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.harness.AccountHarness;
import us.careydevelopment.accounting.harness.PaymentAccountHarness;
import us.careydevelopment.accounting.harness.SinglePaymentHarness;
import us.careydevelopment.accounting.harness.UserLightweightHarness;
import us.careydevelopment.accounting.model.*;
import us.careydevelopment.accounting.repository.TransactionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final Long INITIAL_PHONE_EXPENSE_VALUE = 0l;
    private static final Long INITIAL_INTERNET_EXPENSE_VALUE = 32044l;
    private static final Long INITIAL_BANK_ACCOUNT_VALUE = 712000l;
    private static final Long PHONE_BILL_VALUE = 33207l;
    private static final String PHONE_BILL_ID = "2039";
    private static final Long INTERNET_BILL_VALUE = 49134l;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testGoodTransaction() {
        final Transaction transaction = new Transaction();

        final Account debitAccount = AccountHarness.getTelephoneExpenseAccount();
        debitAccount.setValue(INITIAL_PHONE_EXPENSE_VALUE);

        final Account creditAccount = PaymentAccountHarness.getValidBankPaymentAccount();
        creditAccount.setValue(INITIAL_BANK_ACCOUNT_VALUE);

        transaction.setDebitAccount(debitAccount);
        transaction.setDebitAmount(PHONE_BILL_VALUE);

        transaction.setCreditAccount(creditAccount);
        transaction.setCreditAmount(PHONE_BILL_VALUE);

        transaction.setId(PHONE_BILL_ID);

        transactionService.transact(transaction);

        assertEquals(INITIAL_BANK_ACCOUNT_VALUE - PHONE_BILL_VALUE, creditAccount.getValue());
    }

    @Test
    public void testMultiplePayments() {
        final User owner = UserLightweightHarness.getMrSmithUserLightweight();
        final String phoneExpenseAccountId = "43";
        final Account phoneExpenseAccount = AccountHarness.getTelephoneExpenseAccount();
        phoneExpenseAccount.setValue(INITIAL_PHONE_EXPENSE_VALUE);

        final SinglePayment payment1 = SinglePaymentHarness.getSinglePayment(phoneExpenseAccount, PHONE_BILL_VALUE,
                phoneExpenseAccountId, owner);

        final String internetExpenseAccountId = "44";
        final Account internetExpenseAccount = AccountHarness.getInternetExpenseAccount();
        internetExpenseAccount.setValue(INITIAL_INTERNET_EXPENSE_VALUE);

        final SinglePayment payment2 = SinglePaymentHarness.getSinglePayment(internetExpenseAccount, INTERNET_BILL_VALUE,
                internetExpenseAccountId, owner);

        final PaymentAccount creditAccount = PaymentAccountHarness.getValidBankPaymentAccount();
        creditAccount.setValue(INITIAL_BANK_ACCOUNT_VALUE);

        final List<SinglePayment> payments = List.of(payment1, payment2);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        payments.forEach(payment -> {
            transactionService.transact(payment, creditAccount);
        });

        assertEquals(INITIAL_BANK_ACCOUNT_VALUE - PHONE_BILL_VALUE - INTERNET_BILL_VALUE, creditAccount.getValue());
        assertEquals(INITIAL_INTERNET_EXPENSE_VALUE + INTERNET_BILL_VALUE, internetExpenseAccount.getValue());
        assertEquals(INITIAL_PHONE_EXPENSE_VALUE + PHONE_BILL_VALUE, phoneExpenseAccount.getValue());
    }

    @Test
    public void testOutOfBalanceTransaction() {
        final Transaction transaction = new Transaction();

        final Account debitAccount = AccountHarness.getTelephoneExpenseAccount();
        debitAccount.setValue(INITIAL_PHONE_EXPENSE_VALUE);

        final Account creditAccount = PaymentAccountHarness.getValidBankPaymentAccount();
        creditAccount.setValue(INITIAL_BANK_ACCOUNT_VALUE);

        transaction.setDebitAccount(debitAccount);
        transaction.setDebitAmount(PHONE_BILL_VALUE);

        transaction.setCreditAccount(creditAccount);
        transaction.setCreditAmount(PHONE_BILL_VALUE + 100);

        transaction.setId(PHONE_BILL_ID);

        assertThrows(ServiceException.class, () -> transactionService.transact(transaction));
    }

    @Test
    public void testNullAccount() {
        final Transaction transaction = new Transaction();

        final Account debitAccount = AccountHarness.getTelephoneExpenseAccount();
        debitAccount.setValue(INITIAL_PHONE_EXPENSE_VALUE);

        final Account creditAccount = null;

        transaction.setDebitAccount(debitAccount);
        transaction.setDebitAmount(PHONE_BILL_VALUE);

        transaction.setCreditAccount(creditAccount);
        transaction.setCreditAmount(PHONE_BILL_VALUE);

        transaction.setId(PHONE_BILL_ID);

        assertThrows(ServiceException.class, () -> transactionService.transact(transaction));
    }

//    @Test
//    public void testAccountServiceRuntimeException() {
//        final Transaction transaction = new Transaction();
//
//        final Account debitAccount = AccountHarness.getTelephoneExpenseAccount();
//        debitAccount.setValue(INITIAL_PHONE_EXPENSE_VALUE);
//
//        final Account creditAccount = PaymentAccountHarness.getValidBankPaymentAccount();
//        creditAccount.setValue(INITIAL_BANK_ACCOUNT_VALUE);
//
//        transaction.setDebitAccount(debitAccount);
//        transaction.setDebitAmount(PHONE_BILL_VALUE);
//
//        transaction.setCreditAccount(creditAccount);
//        transaction.setCreditAmount(PHONE_BILL_VALUE);
//
//        transaction.setId(PHONE_BILL_ID);
//
//        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
//        when(accountService.update(transaction)).thenThrow(new RuntimeException("yo!"));
//
//        assertThrows(ServiceException.class, () -> transactionService.transact(transaction));
//    }

//    @Test
//    public void testDuplicateBusinessName() {
//        final List<ValidationError> validationErrors = new ArrayList<>();
//
//        final Business business = new Business();
//        business.setName("Widgets, Inc.");
//
//        when(businessRepository.findByName(business.getName())).thenReturn(new Business());
//
//        validationService.validateUniqueName(business, validationErrors);
//
//        assertEquals(1, validationErrors.size());
//    }
}

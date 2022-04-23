package us.careydevelopment.accounting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import us.careydevelopment.accounting.model.Account;
import us.careydevelopment.accounting.repository.AccountRepository;
import us.careydevelopment.accounting.repository.ExpenseRepository;
import us.careydevelopment.accounting.repository.SalesReceiptRepository;
import us.careydevelopment.accounting.repository.TransactionRepository;

import java.util.List;

@Component
public class ApplicationListenerInitialize implements ApplicationListener<ApplicationReadyEvent>  {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SalesReceiptRepository salesReceiptRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void onApplicationEvent(ApplicationReadyEvent event) {
//        List<Expense> expenses = expenseRepository.findAll();
//        expenses.forEach(System.err::println);

//        List<Transaction> transactions = transactionRepository.findAll();
//        transactions.forEach(System.err::println);

        //expenseRepository.deleteAll();
        //salesReceiptRepository.deleteAll();
        //accountRepository.deleteAll();
        //transactionRepository.deleteAll();

        List<Account> accounts = accountRepository.findAll();
        accounts.forEach(account -> {
            //if (account.getAccountType().equals(AccountType.ASSET)) account.setValue(3230400l);
            account.setValue(0l);
            accountRepository.save(account);
        });
    }
}
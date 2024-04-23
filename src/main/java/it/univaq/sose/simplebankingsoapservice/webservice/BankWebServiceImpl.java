package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.domain.Account;
import it.univaq.sose.simplebankingsoapservice.domain.BankAccount;
import it.univaq.sose.simplebankingsoapservice.dto.AccountAndBankAccount;
import it.univaq.sose.simplebankingsoapservice.dto.OpenBankAccountRequest;
import it.univaq.sose.simplebankingsoapservice.repository.AccountRepository;
import it.univaq.sose.simplebankingsoapservice.repository.BankAccountRepository;

public class BankWebServiceImpl implements BankWebService {
    private AccountRepository accountRepository = AccountRepository.getInstance();
    private BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();

    @Override
    public AccountAndBankAccount getAccountAndBankAccount(long accountId) {
        Account account = accountRepository.findById(accountId);
        BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
        System.out.println("Risposta");
        AccountAndBankAccount andBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), bankAccount);
        System.out.println(andBankAccount);
        return andBankAccount;
    }

    @Override
    public AccountAndBankAccount saveAccountAndBankAccount(OpenBankAccountRequest request) {
        // TODO: simulare meglio il db
        long idAccount = accountRepository.lastIdSave() + 1;
        long idBankAccount = bankAccountRepository.lastIdSave() + 1;
        String bankAccountNumber = bankAccountRepository.generateNewBankAccountNumber();
        Account account = new Account(idAccount, request.getName(), request.getSurname());
        idAccount = accountRepository.save(account);
        BankAccount bankAccount = new BankAccount(idBankAccount, bankAccountNumber, request.getMoney(), idAccount);
        bankAccountRepository.save(bankAccount);
        account.setIdBankAccount(bankAccount);
        accountRepository.save(account);
        System.out.println("Risposta");
        AccountAndBankAccount andBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), bankAccount);
        System.out.println(andBankAccount);
        return andBankAccount;
    }
}

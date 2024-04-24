package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.domain.Account;
import it.univaq.sose.simplebankingsoapservice.domain.BankAccount;
import it.univaq.sose.simplebankingsoapservice.domain.Role;
import it.univaq.sose.simplebankingsoapservice.dto.AccountAndBankAccount;
import it.univaq.sose.simplebankingsoapservice.dto.MoneyTransfer;
import it.univaq.sose.simplebankingsoapservice.dto.OpenBankAccountRequest;
import it.univaq.sose.simplebankingsoapservice.repository.AccountRepository;
import it.univaq.sose.simplebankingsoapservice.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BankWebServiceImpl implements BankWebService {
    private AccountRepository accountRepository = AccountRepository.getInstance();
    private BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(BankWebServiceImpl.class);

    @Override
    public AccountAndBankAccount getAccountAndBankAccount(long accountId) throws NotFoundException {
        Account account = accountRepository.findById(accountId);
        BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
        logger.info("Risposta getAccountAndBankAccount");
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        logger.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public AccountAndBankAccount saveAccountAndBankAccount(OpenBankAccountRequest request) {
        // TODO: simulare meglio il db
        long idAccount = accountRepository.lastIdSave() + 1;
        long idBankAccount = bankAccountRepository.lastIdSave() + 1;
        String bankAccountNumber = bankAccountRepository.generateNewBankAccountNumber();
        Account account = new Account(idAccount, request.getName(), request.getSurname(), Role.CUSTOMER, request.getUsername(), request.getPassword());
        idAccount = accountRepository.save(account);
        BankAccount bankAccount = new BankAccount(idBankAccount, bankAccountNumber, request.getMoney(), idAccount);
        bankAccountRepository.save(bankAccount);
        account.setIdBankAccount(bankAccount);
        accountRepository.save(account);
        logger.info("Risposta saveAccountAndBankAccount");
        AccountAndBankAccount andBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        logger.info("{}", andBankAccount);
        return andBankAccount;
    }

    @Override
    public List<AccountAndBankAccount> getAllAccountsAndBankAccounts() {
        List<AccountAndBankAccount> accountsAndBankAccounts = new LinkedList<>();

        List<Account> accounts = accountRepository.findAll();
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        /*accounts.forEach(account -> accountsAndBankAccounts.add(
                new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), bankAccounts.stream().filter(
                        bankAccount -> bankAccount.getIdBankAccount() == account.getIdBankAccount()).findFirst().orElse(null))));*/

        accounts.forEach(account -> {
            Optional<BankAccount> matchingBankAccount = bankAccounts.stream()
                    .filter(bankAccount -> bankAccount.getIdBankAccount() == account.getIdBankAccount())
                    .findFirst();

            matchingBankAccount.ifPresent(bankAccount -> accountsAndBankAccounts.add(new AccountAndBankAccount(
                    account.getIdAccount(),
                    account.getName(),
                    account.getSurname(),
                    account.getUsername(),
                    bankAccount
            )));
        });
        logger.info("Risposta getAllAccountsAndBankAccounts");
        logger.info("{}", accountsAndBankAccounts);
        return accountsAndBankAccounts;
    }

    @Override
    public AccountAndBankAccount depositMoneyInBankAccount(MoneyTransfer moneyTransfer) throws NotFoundException {
        bankAccountRepository.addMoney(moneyTransfer);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        logger.info("Risposta depositMoneyInBankAccount");
        logger.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public AccountAndBankAccount withdrawMoneyInBankAccount(MoneyTransfer moneyTransfer) throws NotFoundException, InsufficientFundsException {
        bankAccountRepository.removeMoney(moneyTransfer);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        logger.info("Risposta withdrawMoneyInBankAccount");
        logger.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }
}

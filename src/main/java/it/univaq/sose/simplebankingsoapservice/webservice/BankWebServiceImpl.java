package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.domain.Account;
import it.univaq.sose.simplebankingsoapservice.domain.BankAccount;
import it.univaq.sose.simplebankingsoapservice.domain.Role;
import it.univaq.sose.simplebankingsoapservice.dto.AccountAndBankAccount;
import it.univaq.sose.simplebankingsoapservice.dto.MoneyTransfer;
import it.univaq.sose.simplebankingsoapservice.dto.OpenBankAccountRequest;
import it.univaq.sose.simplebankingsoapservice.repository.AccountRepository;
import it.univaq.sose.simplebankingsoapservice.repository.BankAccountRepository;
import it.univaq.sose.simplebankingsoapservice.security.AccountDetails;
import it.univaq.sose.simplebankingsoapservice.util.AuthenticationUtils;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BankWebServiceImpl implements BankWebService {
    private AccountRepository accountRepository = AccountRepository.getInstance();
    private BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(BankWebServiceImpl.class);

    @Resource
    private WebServiceContext wsContext;

    @Override
    public AccountAndBankAccount getAccountAndBankAccount(long accountId) throws NotFoundException {
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(wsContext);
        Account account = accountRepository.findById(accountId);
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
        LOG.info("Risposta getAccountAndBankAccount");
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public AccountAndBankAccount saveAccountAndBankAccount(OpenBankAccountRequest request) {
        String bankAccountNumber = bankAccountRepository.generateNewBankAccountNumber();
        Account account = new Account(0, request.getName(), request.getSurname(), request.getUsername(), request.getPassword(), Role.CUSTOMER);
        long idAccount = accountRepository.save(account);
        BankAccount bankAccount = new BankAccount(0, bankAccountNumber, request.getMoney(), idAccount);
        bankAccountRepository.save(bankAccount);
        account.setIdBankAccount(bankAccount);
        idAccount = accountRepository.updateIdBankAccount(account);
        LOG.info("Risposta saveAccountAndBankAccount");
        AccountAndBankAccount andBankAccount = new AccountAndBankAccount(idAccount, account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("{}", andBankAccount);
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
        LOG.info("Risposta accounts");
        LOG.info("{}", accounts);
        LOG.info("Risposta bankAccounts");
        LOG.info("{}", bankAccounts);
        accounts.forEach(account -> {
            Optional<BankAccount> matchingBankAccount = bankAccounts.stream()
                    .filter(bankAccount -> account.getIdBankAccount() != null && bankAccount.getIdBankAccount() == account.getIdBankAccount())
                    .findFirst();

            matchingBankAccount.ifPresent(bankAccount -> accountsAndBankAccounts.add(new AccountAndBankAccount(
                    account.getIdAccount(),
                    account.getName(),
                    account.getSurname(),
                    account.getUsername(),
                    bankAccount
            )));
        });
        LOG.info("Risposta getAllAccountsAndBankAccounts");
        LOG.info("{}", accountsAndBankAccounts);
        return accountsAndBankAccounts;
    }

    @Override
    public AccountAndBankAccount depositMoneyInBankAccount(MoneyTransfer moneyTransfer) throws NotFoundException {
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(wsContext);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        bankAccountRepository.addMoney(moneyTransfer.getIdBankAccount(), moneyTransfer.getAmount());
        bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("Risposta depositMoneyInBankAccount");
        LOG.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public AccountAndBankAccount withdrawMoneyInBankAccount(MoneyTransfer moneyTransfer) throws NotFoundException, InsufficientFundsException {
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(wsContext);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        bankAccountRepository.removeMoney(moneyTransfer.getIdBankAccount(), moneyTransfer.getAmount());
        bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        LOG.info("Risposta withdrawMoneyInBankAccount");
        LOG.info("{}", accountAndBankAccount);
        return accountAndBankAccount;
    }

    @Override
    public boolean deleteAccount(long accountId) throws NotFoundException {
        Account account = accountRepository.findById(accountId);
        accountRepository.delete(account);
        if (account.getRole().equals(Role.CUSTOMER)) {
            BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
            bankAccountRepository.delete(bankAccount);
        }
        return true;
    }
}

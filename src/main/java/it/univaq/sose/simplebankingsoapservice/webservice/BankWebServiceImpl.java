package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.domain.Account;
import it.univaq.sose.simplebankingsoapservice.domain.BankAccount;
import it.univaq.sose.simplebankingsoapservice.domain.Role;
import it.univaq.sose.simplebankingsoapservice.dto.*;
import it.univaq.sose.simplebankingsoapservice.repository.AccountRepository;
import it.univaq.sose.simplebankingsoapservice.repository.BankAccountRepository;
import it.univaq.sose.simplebankingsoapservice.security.AccountDetails;
import it.univaq.sose.simplebankingsoapservice.util.AuthenticationUtils;
import org.apache.cxf.annotations.UseAsyncMethod;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.apache.cxf.jaxws.ServerAsyncResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class BankWebServiceImpl implements BankWebService {
    private final AccountRepository accountRepository = AccountRepository.getInstance();
    private final BankAccountRepository bankAccountRepository = BankAccountRepository.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(BankWebServiceImpl.class);

    @Resource
    private WebServiceContext wsContext;

    @Override
    @UseAsyncMethod
    public AccountAndBankAccount getAccountAndBankAccount(long accountId) throws NotFoundException {
        LOG.info("executing AccountAndBankAccount getAccountAndBankAccount *synchronously*");
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(wsContext);
        Account account = accountRepository.findById(accountId);
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        return accountAndBankAccount;
    }

    @Override
    public Future<?> getAccountAndBankAccountAsync(long accountId, AsyncHandler<GetAccountsAndBankAccountsResponse> asyncHandler) throws NotFoundException {
        LOG.info("executing Future<?> getAccountAndBankAccountAsync with AsyncHandler *asynchronously*");

        final ServerAsyncResponse<GetAccountsAndBankAccountsResponse> asyncResponse = new ServerAsyncResponse<>();
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(wsContext);
        Account account = accountRepository.findById(accountId);
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        BankAccount bankAccount = bankAccountRepository.findById(account.getIdBankAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        GetAccountsAndBankAccountsResponse response = new GetAccountsAndBankAccountsResponse();
        response.setAccountAndBankAccount(accountAndBankAccount);

        new Thread(() -> {
            try {
                Thread.sleep(2000); // 1s
                asyncResponse.set(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.info("getAccountAndBankAccountAsync responding on background thread");

            asyncHandler.handleResponse(asyncResponse);
        }).start();
        return asyncResponse;
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
        return new AccountAndBankAccount(idAccount, account.getName(), account.getSurname(), account.getUsername(), bankAccount);
    }

    @Override
    public AccountResponse saveAdminAccount(AccountRequest request) {
        Account account = new Account(0, request.getName(), request.getSurname(), request.getUsername(), request.getPassword(), Role.ADMIN);
        long idAccount = accountRepository.save(account);
        return new AccountResponse(idAccount, account.getName(), account.getSurname(), account.getUsername(), account.getRole());
    }

    @Override
    public AccountResponse saveBankerAccount(AccountRequest request) {
        Account account = new Account(0, request.getName(), request.getSurname(), request.getUsername(), request.getPassword(), Role.BANKER);
        long idAccount = accountRepository.save(account);
        return new AccountResponse(idAccount, account.getName(), account.getSurname(), account.getUsername(), account.getRole());
    }

    @Override
    @UseAsyncMethod
    public List<AccountResponse> getAllServiceAccounts() {
        LOG.info("executing List<AccountResponse> getAllServiceAccounts *synchronously*");

        return accountRepository.findAll().stream()
                .filter(account -> account.getRole() == Role.ADMIN || account.getRole() == Role.BANKER)
                .map(account -> new AccountResponse(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), account.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public Response<GetAllServiceAccountsResponse> getAllServiceAccountsAsync() {
        LOG.info("executing Response<GetAllServiceAccountsResponse>");
        // This method is never executed
        return null;
    }

    @Override
    public Future<?> getAllServiceAccountsAsync(AsyncHandler<GetAllServiceAccountsResponse> asyncHandler) {
        LOG.info("executing Future<?> getAllServiceAccounts with AsyncHandler *asynchronously*");

        final ServerAsyncResponse<GetAllServiceAccountsResponse> asyncResponse = new ServerAsyncResponse<>();
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 1s

                List<AccountResponse> accountResponse = accountRepository.findAll().stream()
                        .filter(account -> account.getRole() == Role.ADMIN || account.getRole() == Role.BANKER)
                        .map(account -> new AccountResponse(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), account.getRole()))
                        .collect(Collectors.toList());
                GetAllServiceAccountsResponse response = new GetAllServiceAccountsResponse();
                response.setAccounts(accountResponse);
                asyncResponse.set(response);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LOG.info("getAllServiceAccounts responding on background thread");

            asyncHandler.handleResponse(asyncResponse);
        }).start();
        return asyncResponse;
    }

    @Override
    @UseAsyncMethod
    public List<AccountAndBankAccount> getAllAccountsAndBankAccounts() {
        LOG.info("executing List<AccountAndBankAccount> getAllAccountsAndBankAccounts *synchronously*");
        List<AccountAndBankAccount> accountsAndBankAccounts = new LinkedList<>();

        List<Account> accounts = accountRepository.findAll();
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

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
        return accountsAndBankAccounts;
    }

    @Override
    public Future<?> getAllAccountsAndBankAccountsAsync(AsyncHandler<GetAllAccountsAndBankAccountsResponse> asyncHandler) {
        LOG.info("executing Future<?> getAllAccountsAndBankAccountsAsync with AsyncHandler *asynchronously*");
        final ServerAsyncResponse<GetAllAccountsAndBankAccountsResponse> asyncResponse = new ServerAsyncResponse<>();
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 1s

                List<AccountAndBankAccount> accountsAndBankAccounts = new LinkedList<>();
                List<Account> accounts = accountRepository.findAll();
                List<BankAccount> bankAccounts = bankAccountRepository.findAll();
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

                GetAllAccountsAndBankAccountsResponse response = new GetAllAccountsAndBankAccountsResponse();
                response.setAccountAndBankAccount(accountsAndBankAccounts);
                asyncResponse.set(response);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LOG.info("getAllAccountsAndBankAccountsAsync responding on background thread");

            asyncHandler.handleResponse(asyncResponse);
        }).start();

        return asyncResponse;
    }

    @Override
    @UseAsyncMethod
    public AccountAndBankAccount depositMoneyInBankAccount(MoneyTransfer moneyTransfer) throws NotFoundException {
        LOG.info("executing AccountAndBankAccount depositMoneyInBankAccount *synchronously*");

        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(wsContext);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        bankAccountRepository.addMoney(moneyTransfer.getIdBankAccount(), moneyTransfer.getAmount());
        bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        return new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
    }

    @Override
    public Future<?> depositMoneyInBankAccountAsync(MoneyTransfer moneyTransfer, AsyncHandler<GetAccountsAndBankAccountsResponse> asyncHandler) throws NotFoundException {
        LOG.info("executing Future<?> depositMoneyInBankAccountAsync with AsyncHandler *asynchronously*");

        final ServerAsyncResponse<GetAccountsAndBankAccountsResponse> asyncResponse = new ServerAsyncResponse<>();
        AccountDetails authenticationDetails = AuthenticationUtils.getAuthenticationDetails(wsContext);
        BankAccount bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        Account account = accountRepository.findById(bankAccount.getAccount());
        if (authenticationDetails.getRole().equals(Role.CUSTOMER) && !account.getUsername().equals(authenticationDetails.getUsername())) {
            throw new AuthenticationException("You are not authorised to make this request");
        }
        bankAccountRepository.addMoney(moneyTransfer.getIdBankAccount(), moneyTransfer.getAmount());
        bankAccount = bankAccountRepository.findById(moneyTransfer.getIdBankAccount());
        AccountAndBankAccount accountAndBankAccount = new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
        GetAccountsAndBankAccountsResponse response = new GetAccountsAndBankAccountsResponse();
        response.setAccountAndBankAccount(accountAndBankAccount);

        new Thread(() -> {
            try {
                Thread.sleep(2000); // 1s
                asyncResponse.set(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.info("getAllServiceAccounts responding on background thread");

            asyncHandler.handleResponse(asyncResponse);
        }).start();
        return asyncResponse;
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
        return new AccountAndBankAccount(account.getIdAccount(), account.getName(), account.getSurname(), account.getUsername(), bankAccount);
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

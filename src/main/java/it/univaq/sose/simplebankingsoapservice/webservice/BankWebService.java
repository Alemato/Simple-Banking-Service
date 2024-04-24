package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.dto.AccountAndBankAccount;
import it.univaq.sose.simplebankingsoapservice.dto.MoneyTransfer;
import it.univaq.sose.simplebankingsoapservice.dto.OpenBankAccountRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface BankWebService {
    @WebMethod
    public AccountAndBankAccount getAccountAndBankAccount(long accountId) throws NotFoundException;

    @WebMethod
    public AccountAndBankAccount saveAccountAndBankAccount(OpenBankAccountRequest request);

    @WebMethod
    public List<AccountAndBankAccount> getAllAccountsAndBankAccounts(); // TODO Filtro solo per customer (Da verificare)

    @WebMethod
    public AccountAndBankAccount depositMoneyInBankAccount(MoneyTransfer moneyTransfer) throws NotFoundException;

    @WebMethod
    public AccountAndBankAccount withdrawMoneyInBankAccount(MoneyTransfer moneyTransfer) throws NotFoundException, InsufficientFundsException;

    //TODO delete account-bankAccount (add stato conto) -> cancellazione (add pretty last index method)

    //TODO Creazione account di servizio senza bank account

}

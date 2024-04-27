package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.dto.AccountAndBankAccount;
import it.univaq.sose.simplebankingsoapservice.dto.MoneyTransfer;
import it.univaq.sose.simplebankingsoapservice.dto.OpenBankAccountRequest;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@WebService
public interface BankWebService {
    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    public AccountAndBankAccount getAccountAndBankAccount(@WebParam(name = "accountIdRequest") final long accountId) throws NotFoundException;

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER"})
    public AccountAndBankAccount saveAccountAndBankAccount(@WebParam(name = "OpenBankAccountRequest") @XmlElement(required = true) final OpenBankAccountRequest request);

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER"})
    public List<AccountAndBankAccount> getAllAccountsAndBankAccounts();

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    public AccountAndBankAccount depositMoneyInBankAccount(@WebParam(name = "MoneyTransferRequest") @XmlElement(required = true) final MoneyTransfer moneyTransfer) throws NotFoundException;

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    public AccountAndBankAccount withdrawMoneyInBankAccount(@WebParam(name = "MoneyTransferRequest") @XmlElement(required = true) final MoneyTransfer moneyTransfer) throws NotFoundException, InsufficientFundsException;

    @WebMethod
    @RolesAllowed({"ADMIN"})
    boolean deleteAccount(@WebParam(name = "accountIdRequest") final long accountId) throws NotFoundException;

    //TODO Creazione account di servizio senza bank account


}

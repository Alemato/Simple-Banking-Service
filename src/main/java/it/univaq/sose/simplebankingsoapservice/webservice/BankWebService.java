package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.dto.*;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.ResponseWrapper;
import java.util.List;
import java.util.concurrent.Future;

@WebService
public interface BankWebService {
    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    @ResponseWrapper(localName = "getAccountsAndBankAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAccountsAndBankAccountsResponse")
    public AccountAndBankAccount getAccountAndBankAccount(@WebParam(name = "accountIdRequest") final long accountId) throws NotFoundException;

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    @ResponseWrapper(localName = "getAccountsAndBankAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAccountsAndBankAccountsResponse")
    public Future<?> getAccountAndBankAccountAsync(@WebParam(name = "accountIdRequest") final long accountId, AsyncHandler<GetAccountsAndBankAccountsResponse> asyncHandler) throws NotFoundException;

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER"})
    public AccountAndBankAccount saveAccountAndBankAccount(@WebParam(name = "OpenBankAccountRequest") @XmlElement(required = true) final OpenBankAccountRequest request);

    @WebMethod
    @RolesAllowed({"ADMIN"})
    public AccountResponse saveAdminAccount(@WebParam(name = "AccountRequest") @XmlElement(required = true) final AccountRequest request);

    @WebMethod
    @RolesAllowed({"ADMIN"})
    public AccountResponse saveBankerAccount(@WebParam(name = "AccountRequest") @XmlElement(required = true) final AccountRequest request);

    @WebMethod
    @RolesAllowed({"ADMIN"})
    @ResponseWrapper(localName = "getAllServiceAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAllServiceAccountsResponse")
    public List<AccountResponse> getAllServiceAccounts();

    @WebMethod
    @RolesAllowed({"ADMIN"})
    @ResponseWrapper(localName = "getAllServiceAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAllServiceAccountsResponse")
    public Response<GetAllServiceAccountsResponse> getAllServiceAccountsAsync();

    @WebMethod
    @RolesAllowed({"ADMIN"})
    @ResponseWrapper(localName = "getAllServiceAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAllServiceAccountsResponse")
    public Future<?> getAllServiceAccountsAsync(AsyncHandler<GetAllServiceAccountsResponse> asyncHandler);

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER"})
    @ResponseWrapper(localName = "getAllAccountsAndBankAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAllAccountsAndBankAccountsResponse")
    public List<AccountAndBankAccount> getAllAccountsAndBankAccounts();

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER"})
    @ResponseWrapper(localName = "getAllAccountsAndBankAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAllAccountsAndBankAccountsResponse")
    public Future<?> getAllAccountsAndBankAccountsAsync(AsyncHandler<GetAllAccountsAndBankAccountsResponse> asyncHandler);

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    @ResponseWrapper(localName = "getAccountsAndBankAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAccountsAndBankAccountsResponse")
    public AccountAndBankAccount depositMoneyInBankAccount(@WebParam(name = "MoneyTransferRequest") @XmlElement(required = true) final MoneyTransfer moneyTransfer) throws NotFoundException;


    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    @ResponseWrapper(localName = "getAccountsAndBankAccountsResponse",
            className = "it.univaq.sose.simplebankingsoapservice.dto.GetAccountsAndBankAccountsResponse")
    public Future<?> depositMoneyInBankAccountAsync(@WebParam(name = "MoneyTransferRequest") @XmlElement(required = true) final MoneyTransfer moneyTransfer, AsyncHandler<GetAccountsAndBankAccountsResponse> asyncHandler) throws NotFoundException;

    @WebMethod
    @RolesAllowed({"ADMIN", "BANKER", "CUSTOMER"})
    public AccountAndBankAccount withdrawMoneyInBankAccount(@WebParam(name = "MoneyTransferRequest") @XmlElement(required = true) final MoneyTransfer moneyTransfer) throws NotFoundException, InsufficientFundsException;

    @WebMethod
    @RolesAllowed({"ADMIN"})
    boolean deleteAccount(@WebParam(name = "accountIdRequest") final long accountId) throws NotFoundException;
}

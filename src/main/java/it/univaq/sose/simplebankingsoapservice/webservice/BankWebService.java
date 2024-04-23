package it.univaq.sose.simplebankingsoapservice.webservice;

import it.univaq.sose.simplebankingsoapservice.dto.AccountAndBankAccount;
import it.univaq.sose.simplebankingsoapservice.dto.OpenBankAccountRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface BankWebService {
    @WebMethod
    public AccountAndBankAccount getAccountAndBankAccount(long accountId);

    @WebMethod
    public AccountAndBankAccount saveAccountAndBankAccount(OpenBankAccountRequest request);
}

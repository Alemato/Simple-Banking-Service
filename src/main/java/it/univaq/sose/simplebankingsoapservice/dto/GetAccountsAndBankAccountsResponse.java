package it.univaq.sose.simplebankingsoapservice.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "GetAccountsAndBankAccountsResponse", propOrder = {
        "accountAndBankAccount"
})
public class GetAccountsAndBankAccountsResponse {
    private AccountAndBankAccount accountAndBankAccount;

    public GetAccountsAndBankAccountsResponse() {
    }

    public GetAccountsAndBankAccountsResponse(AccountAndBankAccount accountAndBankAccount) {
        this.accountAndBankAccount = accountAndBankAccount;
    }

    public AccountAndBankAccount getAccountAndBankAccount() {
        return accountAndBankAccount;
    }

    public void setAccountAndBankAccount(AccountAndBankAccount accountAndBankAccount) {
        this.accountAndBankAccount = accountAndBankAccount;
    }
}
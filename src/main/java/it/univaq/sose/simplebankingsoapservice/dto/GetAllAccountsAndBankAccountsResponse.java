package it.univaq.sose.simplebankingsoapservice.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement
@XmlType(name = "GetAllAccountsAndBankAccountsResponse", propOrder = {
        "accountAndBankAccount"
})
public class GetAllAccountsAndBankAccountsResponse {
    private List<AccountAndBankAccount> accountAndBankAccounts;

    public GetAllAccountsAndBankAccountsResponse() {
    }

    public GetAllAccountsAndBankAccountsResponse(List<AccountAndBankAccount> accountAndBankAccounts) {
        this.accountAndBankAccounts = accountAndBankAccounts;
    }

    public List<AccountAndBankAccount> getAccountAndBankAccount() {
        return accountAndBankAccounts;
    }

    public void setAccountAndBankAccount(List<AccountAndBankAccount> accountAndBankAccounts) {
        this.accountAndBankAccounts = accountAndBankAccounts;
    }
}
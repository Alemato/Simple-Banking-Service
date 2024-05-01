package it.univaq.sose.simplebankingsoapservice.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement
@XmlType(name = "GetAllServiceAccountsResponse", propOrder = {
        "accounts"
})
public class GetAllServiceAccountsResponse {
    private List<AccountResponse> accounts;

    public GetAllServiceAccountsResponse() {
    }

    public GetAllServiceAccountsResponse(List<AccountResponse> accounts) {
        this.accounts = accounts;
    }

    public List<AccountResponse> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountResponse> accounts) {
        this.accounts = accounts;
    }
}
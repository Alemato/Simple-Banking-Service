package it.univaq.sose.simplebankingsoapservice.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoneyTransfer", propOrder = {
        "idBankAccount",
        "amount"
})
public class MoneyTransfer {

    @XmlElement(required = true)
    private long idBankAccount;

    @XmlElement(required = true)
    private float amount;

    public MoneyTransfer() {
    }

    public MoneyTransfer(long idBankAccount, float amount) {
        this.idBankAccount = idBankAccount;
        this.amount = amount;
    }

    public long getIdBankAccount() {
        return idBankAccount;
    }

    public float getAmount() {
        return amount;
    }

    public void setIdBankAccount(long idBankAccount) {
        this.idBankAccount = idBankAccount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyTransfer that = (MoneyTransfer) o;
        return idBankAccount == that.idBankAccount && Float.compare(amount, that.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBankAccount, amount);
    }

    @Override
    public String toString() {
        return "AccountAndBankAccount{" +
                "idBankAccount='" + idBankAccount + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}

package it.univaq.sose.simplebankingsoapservice.domain;

public class BankAccount {
    private long idBankAccount;
    private String bankAccountNumber;
    private float money;
    private Long idAccount;

    public BankAccount(long idBankAccount, String bankAccountNumber, float money) {
        this.idBankAccount = idBankAccount;
        this.bankAccountNumber = bankAccountNumber;
        this.money = money;
        this.idAccount = null;
    }

    public BankAccount(long idBankAccount, String bankAccountNumber, float money, long account) {
        this.idBankAccount = idBankAccount;
        this.bankAccountNumber = bankAccountNumber;
        this.money = money;
        this.idAccount = account;
    }

    public long getIdBankAccount() {
        return idBankAccount;
    }

    public void setIdBankAccount(long idBankAccount) {
        this.idBankAccount = idBankAccount;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Long getAccount() {
        return idAccount;
    }

    public void setAccount(Account account) {
        this.idAccount = account.getIdAccount();
    }

    public void setAccount(long account) {
        this.idAccount = account;
    }

    public BankAccount idBankAccount(long idBankAccount) {
        this.idBankAccount = idBankAccount;
        return this;
    }

    public BankAccount bankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
        return this;
    }

    public BankAccount money(float money) {
        this.money = money;
        return this;
    }

    public BankAccount account(Account account) {
        this.idAccount = account.getIdAccount();
        return this;
    }

    public BankAccount account(long account) {
        this.idAccount = account;
        return this;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;

        BankAccount that = (BankAccount) o;
        return getIdBankAccount() == that.getIdBankAccount();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getIdBankAccount());
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "idBankAccount='" + idBankAccount + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", money='" + money + '\'' +
                ", account='" + idAccount + '\'' +
                '}';
    }

}

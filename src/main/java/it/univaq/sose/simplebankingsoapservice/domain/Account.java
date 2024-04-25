package it.univaq.sose.simplebankingsoapservice.domain;

public class Account {
    private long idAccount;
    private String name;
    private String surname;
    private String username;
    private String password;
    private Role role;
    private Long idBankAccount;

    public Account(long idAccount, String name, String surname, String username, String password, Role role) {
        this.idAccount = idAccount;
        this.name = name;
        this.surname = surname;
        this.idBankAccount = null;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public Account(long idAccount, String name, String surname, String username, String password, Role role, long idBankAccount) {
        this.idAccount = idAccount;
        this.name = name;
        this.surname = surname;
        this.idBankAccount = idBankAccount;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getIdBankAccount() {
        return idBankAccount;
    }

    public void setIdBankAccount(BankAccount bankAccount) {
        this.idBankAccount = bankAccount.getIdBankAccount();
    }

    public void setIdBankAccount(Long idBankAccount) {
        this.idBankAccount = idBankAccount;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account idAccount(long idAccount) {
        this.idAccount = idAccount;
        return this;
    }

    public Account name(String name) {
        this.name = name;
        return this;
    }

    public Account surname(String surname) {
        this.surname = surname;
        return this;
    }

    public Account bankAccount(BankAccount bankAccount) {
        this.idBankAccount = bankAccount.getIdBankAccount();
        return this;
    }

    public Account role(Role role) {
        this.role = role;
        return this;
    }

    public Account username(String username) {
        this.username = username;
        return this;
    }

    public Account password(String password) {
        this.password = password;
        return this;
    }

    public Account bankAccount(long idBankAccount) {
        this.idBankAccount = idBankAccount;
        return this;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;
        return getIdAccount() == account.getIdAccount();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getIdAccount());
    }

    @Override
    public String toString() {
        return "Account{" +
                "idProfile=" + idAccount +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role + '\'' +
                ", username=" + username + '\'' +
                ", password=" + password + '\'' +
                ", idBankAccount=" + idBankAccount +
                '}';
    }
}

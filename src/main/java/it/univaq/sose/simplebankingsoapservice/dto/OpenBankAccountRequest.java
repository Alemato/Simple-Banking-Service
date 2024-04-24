package it.univaq.sose.simplebankingsoapservice.dto;

import java.util.Objects;

//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "OpenBankAccountRequest", propOrder = {
//        "name",
//        "surname",
//        "money"
//})
public class OpenBankAccountRequest {
    //    @XmlElement(required = true)
    private String name;
    //    @XmlElement(required = true)
    private String surname;
    //    @XmlElement(required = true)
    private float money;
    private String username;
    private String password;

    public OpenBankAccountRequest() {
    }

    public OpenBankAccountRequest(String name, String surname, float money, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.money = money;
        this.username = username;
        this.password = password;
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

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenBankAccountRequest)) return false;

        OpenBankAccountRequest that = (OpenBankAccountRequest) o;
        return Float.compare(getMoney(), that.getMoney()) == 0 && Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getSurname());
        result = 31 * result + Float.hashCode(getMoney());
        result = 31 * result + Objects.hashCode(getUsername());
        return result;
    }

    @Override
    public String toString() {
        return "OpenBankAccountRequest{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", money=" + money +
                ", username=" + username +
                ", password=" + password +
                '}';
    }
}

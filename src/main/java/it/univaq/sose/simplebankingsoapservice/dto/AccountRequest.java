package it.univaq.sose.simplebankingsoapservice.dto;

import it.univaq.sose.simplebankingsoapservice.domain.Role;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
// https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/XmlAccessorType.html#value--
// https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/XmlAccessType.html

//@XmlType(name = "OpenBankAccountRequest", propOrder = {
//        "name",
//        "surname",
//        "money"
//})
public class AccountRequest {
    // https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/XmlElement.html
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String surname;
    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private String password;
    @XmlElement(required = true)
    private Role role;


    public AccountRequest() {
    }

    public AccountRequest(String name, String surname, String username, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountRequest)) return false;

        AccountRequest that = (AccountRequest) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getSurname(), that.getSurname());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getName());
        result = 31 * result + Objects.hashCode(getSurname());
        result = 31 * result + Objects.hashCode(getUsername());
        return result;
    }

    @Override
    public String toString() {
        return "OpenBankAccountRequest{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

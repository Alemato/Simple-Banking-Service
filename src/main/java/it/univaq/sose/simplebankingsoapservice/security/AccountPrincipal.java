package it.univaq.sose.simplebankingsoapservice.security;

import java.security.Principal;

public class AccountPrincipal implements Principal {
    private final String name;

    public AccountPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        AccountPrincipal that = (AccountPrincipal) another;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AccountPrincipal{" +
                "name='" + name + '\'' +
                '}';
    }
}

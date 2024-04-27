package it.univaq.sose.simplebankingsoapservice.security;

import java.security.Principal;

public class RolePrincipal implements Principal {
    private final String name;

    public RolePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        RolePrincipal that = (RolePrincipal) another;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RolePrincipal{" +
                "name='" + name + '\'' +
                '}';
    }
}

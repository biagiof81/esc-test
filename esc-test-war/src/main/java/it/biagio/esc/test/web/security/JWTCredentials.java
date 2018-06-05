package it.biagio.esc.test.web.security;

import java.util.Set;

public class JWTCredentials {

    private final String principal;
    private final Set<String> authorities;

    public JWTCredentials(String principal, Set<String> authorities) {
        this.principal = principal;
        this.authorities = authorities;
    }

    public String getPrincipal() {
        return principal;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }
}

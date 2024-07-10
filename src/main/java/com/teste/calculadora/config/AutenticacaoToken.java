package com.teste.calculadora.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AutenticacaoToken extends AbstractAuthenticationToken {

    private final String token;

    public AutenticacaoToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}
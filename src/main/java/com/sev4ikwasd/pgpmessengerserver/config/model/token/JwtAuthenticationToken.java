package com.sev4ikwasd.pgpmessengerserver.config.model.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private AccessJwtToken accessToken;
    private String username;

    public JwtAuthenticationToken(AccessJwtToken unsafeToken) {
        super(null);
        this.accessToken = unsafeToken;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(String username) {
        super(null);
        this.eraseCredentials();
        this.username = username;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.accessToken = null;
    }
}
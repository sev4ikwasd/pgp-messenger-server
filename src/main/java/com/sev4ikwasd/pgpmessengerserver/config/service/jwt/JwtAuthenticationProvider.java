package com.sev4ikwasd.pgpmessengerserver.config.service.jwt;

import com.sev4ikwasd.pgpmessengerserver.config.model.token.AccessJwtToken;
import com.sev4ikwasd.pgpmessengerserver.config.model.token.JwtAuthenticationToken;
import com.sev4ikwasd.pgpmessengerserver.config.settings.JwtSettings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccessJwtToken rawAccessToken = (AccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());

        String username = jwsClaims.getBody().getSubject();

        return new JwtAuthenticationToken(username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
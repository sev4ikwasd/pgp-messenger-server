package com.sev4ikwasd.pgpmessengerserver.config.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sev4ikwasd.pgpmessengerserver.config.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

public class AccessJwtToken implements JwtToken {
    private String token;
    @JsonIgnore private Claims claims;

    public AccessJwtToken(String token, Claims claims) {
        this.token = token;
        this.claims = claims;
    }

    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        }
    }

    @Override
    public String getToken() {
        return token;
    }

    public Claims getClaims() {
        return claims;
    }
}

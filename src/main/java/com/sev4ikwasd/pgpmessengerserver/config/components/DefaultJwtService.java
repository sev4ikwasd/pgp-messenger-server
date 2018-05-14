package com.sev4ikwasd.pgpmessengerserver.config.components;

import com.sev4ikwasd.pgpmessengerserver.config.service.JwtService;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class DefaultJwtService implements JwtService {
    private String secret;
    private int sessionTime;

    @Autowired
    public DefaultJwtService(@Value("${jwt.password}") String secret,
                             @Value("${jwt.time}") int sessionTime) {
        this.secret = secret;
        this.sessionTime = sessionTime;
    }

    @Override
    public String toToken(UserApp user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setExpiration(expireTimeFromNow())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public Optional<String> getSubFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return Optional.ofNullable(claimsJws.getBody().getSubject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Date expireTimeFromNow() {
        return new Date(System.currentTimeMillis() + sessionTime * 1000);
    }
}
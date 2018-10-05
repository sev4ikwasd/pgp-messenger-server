package com.sev4ikwasd.pgpmessengerserver.controller;

import com.sev4ikwasd.pgpmessengerserver.config.exception.InvalidJwtToken;
import com.sev4ikwasd.pgpmessengerserver.config.model.token.AccessJwtToken;
import com.sev4ikwasd.pgpmessengerserver.config.model.token.JwtToken;
import com.sev4ikwasd.pgpmessengerserver.config.model.token.JwtTokenFactory;
import com.sev4ikwasd.pgpmessengerserver.config.model.token.RefreshToken;
import com.sev4ikwasd.pgpmessengerserver.config.service.jwt.extractor.TokenExtractor;
import com.sev4ikwasd.pgpmessengerserver.config.service.jwt.verifier.TokenVerifier;
import com.sev4ikwasd.pgpmessengerserver.config.service.user.UserService;
import com.sev4ikwasd.pgpmessengerserver.config.settings.JwtSettings;
import com.sev4ikwasd.pgpmessengerserver.config.settings.WebSettings;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RefreshTokenEndpoint {
    private JwtTokenFactory tokenFactory;
    private JwtSettings jwtSettings;
    private WebSettings webSettings;
    private UserService userService;
    private TokenVerifier tokenVerifier;
    private TokenExtractor tokenExtractor;

    @Autowired
    public RefreshTokenEndpoint(JwtTokenFactory tokenFactory, JwtSettings jwtSettings, WebSettings webSettings, UserService userService, TokenVerifier tokenVerifier, TokenExtractor tokenExtractor) {
        this.tokenFactory = tokenFactory;
        this.jwtSettings = jwtSettings;
        this.webSettings = webSettings;
        this.userService = userService;
        this.tokenVerifier = tokenVerifier;
        this.tokenExtractor = tokenExtractor;
    }

    @GetMapping(value="/auth/token")
    public ResponseEntity refreshToken(HttpServletRequest request) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(webSettings.getAuthenticationHeaderName()));

        AccessJwtToken rawToken = new AccessJwtToken(tokenPayload, null);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> {
            return new InvalidJwtToken("Can't create new token");
        });

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken("Token is incorrect");
        }

        String subject = refreshToken.getSubject();
        UserApp user = userService.findUserByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String username = user.getUsername();
        JwtToken accessToken = tokenFactory.createAccessJwtToken(username);
        JwtToken newRefreshToken = tokenFactory.createRefreshToken(username);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", accessToken.getToken());
        tokenMap.put("refreshToken", newRefreshToken.getToken());

        return new ResponseEntity<>(tokenMap, HttpStatus.OK);
    }
}
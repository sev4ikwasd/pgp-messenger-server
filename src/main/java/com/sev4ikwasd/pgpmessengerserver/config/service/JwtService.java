package com.sev4ikwasd.pgpmessengerserver.config.service;

import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(UserApp user);

    Optional<String> getSubFromToken(String token);
}
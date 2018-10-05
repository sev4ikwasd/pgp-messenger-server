package com.sev4ikwasd.pgpmessengerserver.config.service.jwt.verifier;

public interface TokenVerifier {
    boolean verify(String jti);
}
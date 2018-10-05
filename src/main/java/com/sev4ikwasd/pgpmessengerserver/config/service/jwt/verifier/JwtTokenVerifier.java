package com.sev4ikwasd.pgpmessengerserver.config.service.jwt.verifier;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenVerifier implements TokenVerifier {
    @Autowired
    private Logger logger;

    @Override
    public boolean verify(String jti) {
        logger.warn(jti);
        return (jti != null) && (!jti.isEmpty());
    }
}
package com.sev4ikwasd.pgpmessengerserver.config.service.jwt.extractor;

public interface TokenExtractor {
    String extract(String payload);
}
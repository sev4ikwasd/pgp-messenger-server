package com.sev4ikwasd.pgpmessengerserver.config.exception;

public class InvalidJwtToken extends RuntimeException {
    public InvalidJwtToken(String msg){
        super(msg);
    }
}
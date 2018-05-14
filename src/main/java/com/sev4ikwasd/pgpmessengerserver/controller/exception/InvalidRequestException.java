package com.sev4ikwasd.pgpmessengerserver.controller.exception;

import org.springframework.validation.Errors;

public class InvalidRequestException extends RuntimeException {
    private final Errors errors;

    public InvalidRequestException(Errors errors) {
        super("");
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
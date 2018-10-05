package com.sev4ikwasd.pgpmessengerserver.controller;

import com.sev4ikwasd.pgpmessengerserver.config.model.error.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {

        ErrorResponse body = ErrorResponse.of(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}

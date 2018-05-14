package com.sev4ikwasd.pgpmessengerserver.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sev4ikwasd.pgpmessengerserver.controller.exception.InvalidRequestException;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        InvalidRequestException ire = (InvalidRequestException) e;

        List<FieldErrorResource> errorResources = ire.getErrors().getFieldErrors().stream().map(fieldError ->
                new FieldErrorResource(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage())).collect(Collectors.toList());

        ErrorResource error = new ErrorResource(errorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, UNPROCESSABLE_ENTITY, request);
    }

    @Getter
    @JsonSerialize(using = ErrorResourceSerializer.class)
    private class ErrorResource {
        private List<FieldErrorResource> fieldErrors;

        public ErrorResource(List<FieldErrorResource> fieldErrorResources) {
            this.fieldErrors = fieldErrorResources;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    private class FieldErrorResource {
        private String resource;
        private String field;
        private String code;
        private String message;

        private FieldErrorResource(String resource, String field, String code, String message) {

            this.resource = resource;
            this.field = field;
            this.code = code;
            this.message = message;
        }
    }

    public class ErrorResourceSerializer extends JsonSerializer<ErrorResource> {
        @Override
        public void serialize(ErrorResource errorResource, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            Map<String, String> json = new HashMap<>();
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectFieldStart("error");
            for (FieldErrorResource fieldErrorResource : errorResource.getFieldErrors()) {
                if (!json.containsKey(fieldErrorResource.getField())) {
                    json.put(fieldErrorResource.getField(), fieldErrorResource.getMessage());
                }
            }
            for (Map.Entry<String, String> pair : json.entrySet()) {
                try {
                    jsonGenerator.writeStringField(pair.getKey(), pair.getValue());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            jsonGenerator.writeEndObject();
            jsonGenerator.writeEndObject();
        }
    }

}

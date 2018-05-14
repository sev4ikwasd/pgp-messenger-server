package com.sev4ikwasd.pgpmessengerserver.controller.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(value = "message")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class MessageInputParam {
    @NotBlank(message = "Message can't be empty")
    private String message;
    //@NotBlank(message = "Sender can't be empty")
    //private String sentFromId;
}

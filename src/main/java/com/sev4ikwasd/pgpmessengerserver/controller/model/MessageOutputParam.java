package com.sev4ikwasd.pgpmessengerserver.controller.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sev4ikwasd.pgpmessengerserver.config.components.JsonDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(value = "message")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class MessageOutputParam {
    @NotBlank(message = "Message can't be empty")
    private String message;
    @NotBlank(message = "Sender can't be empty")
    private String sentFromId;
    @JsonSerialize (using = JsonDateTimeSerializer.class)
    @NotBlank(message = "Time can't be null")
    private DateTime sentTime;
}

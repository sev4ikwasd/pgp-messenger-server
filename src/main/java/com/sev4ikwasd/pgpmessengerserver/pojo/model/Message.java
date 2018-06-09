package com.sev4ikwasd.pgpmessengerserver.pojo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Message {
    private String id;
    private String message;
    private DateTime sentTime;
    private String sentFromUsername;
    private String sentToUsername;

    public Message(String message, String sentFromUsername, String sentToUsername){
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.sentTime = new DateTime();
        this.sentFromUsername = sentFromUsername;
        this.sentToUsername = sentToUsername;
    }
}

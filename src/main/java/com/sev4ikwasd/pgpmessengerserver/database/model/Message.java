package com.sev4ikwasd.pgpmessengerserver.database.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name="message")
public class Message {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "message")
    private String message;
    @Column(name = "sent_from_id")
    private String sentFromId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserApp user;
    @Column(name = "sent_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime sentTime;

    public Message(String message, String sentFromId, UserApp user){
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.sentFromId = sentFromId;
        this.user = user;
        this.sentTime = new DateTime();
    }
}

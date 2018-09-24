package com.sev4ikwasd.pgpmessengerserver.database.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name="userapp")
public class UserApp {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    /*@Column(name = "sent_messages_id")
    private Set<String> sentMessagesId;
    @Column(name = "receive_messages_id")
    private Set<String> receivedMessagesId*/
    /*@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Message> messages;*/

    public UserApp(String username, String email, String password){
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

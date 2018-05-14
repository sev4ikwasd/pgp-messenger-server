package com.sev4ikwasd.pgpmessengerserver.database.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
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
    @OneToMany(mappedBy = "user")
    private Set<Message> messages;

    public UserApp(String username, String email, String password){
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

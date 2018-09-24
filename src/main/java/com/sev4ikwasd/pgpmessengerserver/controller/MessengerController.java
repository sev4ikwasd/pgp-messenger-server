package com.sev4ikwasd.pgpmessengerserver.controller;

import com.sev4ikwasd.pgpmessengerserver.controller.model.MessageInputParam;
import com.sev4ikwasd.pgpmessengerserver.controller.model.MessageOutputParam;
import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import com.sev4ikwasd.pgpmessengerserver.pojo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MessengerController {
    private final SimpMessagingTemplate template;
    private UserDAO userDAO;

    @Autowired
    public MessengerController(SimpMessagingTemplate template, UserDAO userDAO){
        this.template = template;
        this.userDAO = userDAO;
    }

    @MessageMapping("/input/{username}")
    public void sendMessage(@RequestBody MessageInputParam message, @DestinationVariable("username") String username, @AuthenticationPrincipal String senderUsername) {
        if(userDAO.existsByUsername(username)) {
            UserApp sender = userDAO.findUserByUsername(senderUsername);
            UserApp receiver = userDAO.findUserByUsername(username);
            Message res = new Message(message.getMessage(), sender.getUsername(), receiver.getUsername());
            this.template.convertAndSendToUser(username, "/output", new MessageOutputParam(res.getMessage(), sender.getUsername(), res.getSentTime()));
        }
    }
}

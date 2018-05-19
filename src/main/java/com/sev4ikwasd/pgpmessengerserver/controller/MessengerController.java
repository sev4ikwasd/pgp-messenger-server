package com.sev4ikwasd.pgpmessengerserver.controller;

import com.sev4ikwasd.pgpmessengerserver.controller.model.MessageInputParam;
import com.sev4ikwasd.pgpmessengerserver.controller.model.MessageOutputParam;
import com.sev4ikwasd.pgpmessengerserver.database.model.Message;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
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

    @Autowired
    public MessengerController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/input/{client}")
    public void sendMessage(@RequestBody MessageInputParam message, @DestinationVariable("client") String client, @AuthenticationPrincipal UserApp user) {
        Message res = new Message(message.getMessage(), user.getId(), client);
        this.template.convertAndSend("/output/" + client, new MessageOutputParam(res.getMessage(), user.getId(), res.getSentTime()));
    }
}

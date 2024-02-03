package dev.lwnd.chat;

import dev.lwnd.chat.messages.NameRequest;
import dev.lwnd.chat.messages.NameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class UserHandler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/naming")
    public void name(Principal principal, NameRequest request) {
        String requestMessage = "Received name request: \n" + request;
        requestMessage += "    Name: " + request.getName() + "\n";
        LogManager.log(requestMessage);

        NameResponse response = new NameResponse();
        response.setName(request.getName());
        if (ChatInfo.nameExists(request.getName())) {
            response.setResponse("0");
        } else {
            ChatInfo.addUser(principal.getName(), request.getName());
            response.setResponse("1");
        }
        messagingTemplate.convertAndSendToUser(principal.getName(), "/rcv/namingResponse", response);
    }
}

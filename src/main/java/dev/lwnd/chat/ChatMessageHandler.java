package dev.lwnd.chat;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageHandler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload @NotNull ChatMessage message) throws Exception {
        StringBuilder messageText = new StringBuilder("Received message: \n");
        messageText.append("Source: \n");
        messageText.append("    ").append(message.getSource().getClient()).append("\n");
        messageText.append("    ").append(message.getSource().getUsername()).append("\n");
        messageText.append("Message: \n");
        messageText.append("    Date: ").append(message.getTimestamp()).append("\n");
        for (String line : message.getText().split("\n")) {
            messageText.append("    ").append(line).append("\n");
        }
        LogManager.log(messageText.toString());

        messagingTemplate.convertAndSend("/messages", message);
    }
}

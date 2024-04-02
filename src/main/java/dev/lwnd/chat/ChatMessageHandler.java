package dev.lwnd.chat;

import dev.lwnd.chat.messages.ChatMessage;
import dev.lwnd.chat.messages.Log;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatMessageHandler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private WebhookSender webhookSender = new WebhookSender("http://localhost:3001/webhook");

    @MessageMapping("/sendMessage")
    public void sendMessage(Principal principal, @Payload @NotNull ChatMessage message) {
        StringBuilder messageText = new StringBuilder("Received message: \n");
        messageText.append("Source: \n");
        messageText.append("    ").append(ChatInfo.getUserName(principal.getName())).append("\n");
        messageText.append("Message: \n");
        messageText.append("    Date: ").append(message.getTimestamp()).append("\n");
        for (String line : message.getText().split("\n")) {
            messageText.append("    ").append(line).append("\n");
        }
        LogManager.log(messageText.toString());
        message.setUsername(ChatInfo.getUserName(principal.getName()));

        messagingTemplate.convertAndSend("/rcv/messages", message);
        webhookSender.sendWebhook(new Log(ChatInfo.getUserName(principal.getName()) + " sent a message [ " + message.getTimestamp() +" ]:"  + message.getText()));
    }
}

package dev.lwnd.chat;

import dev.lwnd.chat.messages.Log;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketHandler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final Map<String, Boolean> principalToPong = new ConcurrentHashMap<>();
    private final List<String> principalQueue = new ArrayList<>();

    @MessageMapping("/log")
    public void log(@NotNull Log log) {
        LogManager.log(log.getMessage());
    }

    @MessageMapping("/register")
    public void register(Principal principal) {
        principalToPong.put(principal.getName(), false);
        principalQueue.add(principal.getName());

        ChatInfo.addUser(principal.getName(), "");

        LogManager.log("Client-" + principal.getName() + " registered");
    }

    @MessageMapping("/pong")
    public void pong(Principal principal) {
        principalToPong.put(principal.getName(), true);

        LogManager.log("Received pong from client-" + principal.getName());
    }

    @Async
    public void runPingLoop() throws Exception{
        List<String> principals = new ArrayList<>();
        List<String> principalsToRemove = new ArrayList<>();
        SimpMessagingTemplate messagingTemplate = this.messagingTemplate;

        while (true) {
            try {
                Thread.sleep(20000); // Send ping every 20 seconds
            } catch (InterruptedException e) {
                LogManager.log("Ping loop interrupted");
                return;
            }

            StringBuilder message = new StringBuilder("Pinged to: \n");
            principals.clear();
            principalsToRemove.clear();
            for (String principalA : ChatInfo.getUserMap().keySet()) {
                if (principalQueue.contains(principalA)) {
                    principalQueue.remove(principalA);
                    principals.add(principalA);
                    messagingTemplate.convertAndSendToUser(principalA, "/rcv/ping", "");
                    continue;
                }

                if (!principalToPong.get(principalA)) {
                    LogManager.log("Client-" + principalA + " did not respond to ping");

                    ChatInfo.removeUser(principalA);
                    principalToPong.remove(principalA);
                    principalsToRemove.add(principalA);
                    continue;
                }

                principals.add(principalA);
                principalToPong.put(principalA, false);
                messagingTemplate.convertAndSendToUser(principalA, "/rcv/ping", "");
            }

            for (String principal : principals) {
                message.append("    Client-").append(principal).append("\n");
            }

            for (String principal : principalsToRemove) {
                message.append("    Client-").append(principal).append(" (removed)\n");
                ChatInfo.removeUser(principal);
            }

            LogManager.log(message.toString());
        }
    }
}

package dev.lwnd.chat;

import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketHandler {
    @MessageMapping("/log")
    public void log(@NotNull Log log) throws Exception {
        LogManager.log(log.getMessage());
    }
}

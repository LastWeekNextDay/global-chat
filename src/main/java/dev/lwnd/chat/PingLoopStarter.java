package dev.lwnd.chat;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PingLoopStarter {
    private final WebSocketHandler webSocketHandler;

    public PingLoopStarter(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void startPingLoop() {
        try {
            webSocketHandler.runPingLoop();
        } catch (Exception e) {
            LogManager.log("Ping loop failure: " + e.getMessage());
        }
    }
}

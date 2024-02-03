package dev.lwnd.chat;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private final AtomicLong clientIdCounter = new AtomicLong(0);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        long id = clientIdCounter.getAndIncrement();
        attributes.put("client_id", Long.toString(id));

        LogManager.log("Attempted to connect Client-" + id);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {
    }
}


package dev.lwnd.chat;

import java.security.Principal;

public class WebSocketAuthPrincipal implements Principal {
    private String name;

    public WebSocketAuthPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

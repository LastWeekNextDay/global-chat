package dev.lwnd.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    String username;
    String client;

    public User(String username, String client) {
        this.username = username;
        this.client = client;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getClient() {
        return client;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClient(String client) {
        this.client = client;
    }
}

package dev.lwnd.chat.messages;

public class NameResponse {
    String name;
    String response;

    public NameResponse(String name, String response) {
        this.name = name;
        this.response = response;
    }

    public NameResponse() {
    }

    public String getName() {
        return name;
    }

    public String getResponse() {
        return response;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

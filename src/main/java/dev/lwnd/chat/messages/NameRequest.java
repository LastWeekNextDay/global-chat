package dev.lwnd.chat.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NameRequest {
    String name;

    public NameRequest(String name) {
        this.name = name;
    }

    public NameRequest() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

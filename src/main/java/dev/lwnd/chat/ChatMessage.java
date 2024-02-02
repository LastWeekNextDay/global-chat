package dev.lwnd.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {
    User source;
    String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    Date timestamp;

    public ChatMessage(User source, String text, Date timestamp) {
        this.source = source;
        this.text = text;
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }

    public User getSource() {
        return source;
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

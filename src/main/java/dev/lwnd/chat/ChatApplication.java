package dev.lwnd.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        LogManager.init();
        LogManager.log("Starting ChatApp");
        SpringApplication.run(ChatApplication.class, args);
    }
}

package dev.lwnd.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ChatApplication {
    public static void main(String[] args) {
        LogManager.init();
        LogManager.log("Starting ChatApp");
        SpringApplication.run(ChatApplication.class, args);
    }
}

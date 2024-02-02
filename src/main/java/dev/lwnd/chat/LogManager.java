package dev.lwnd.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    static Logger logger;

    public static void init() {
        logger = LoggerFactory.getLogger(LogManager.class);
    }

    public static void log(String message) {
        logger.info(message);
    }
}

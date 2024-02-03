package dev.lwnd.chat;

import java.util.HashMap;
import java.util.Map;


public class ChatInfo {
    static Map<String, String> userMap = new HashMap<>();

    public static void addUser(String principalName, String userName) {
        userMap.put(principalName, userName);
    }

    public static String getUserName(String principalName) {
        return userMap.get(principalName);
    }

    public static void removeUser(String principalName) {
        userMap.remove(principalName);
    }

    public static boolean userExists(String principalName) {
        return userMap.containsKey(principalName);
    }

    public static boolean nameExists(String userName) {
        return userMap.containsValue(userName);
    }

    public static String getPrincipalName(String userName) {
        for (Map.Entry<String, String> entry : userMap.entrySet()) {
            if (entry.getValue().equals(userName)) {
                return entry.getKey();
            }
        }
        return "";
    }

    public static int getNumUsers() {
        return userMap.size();
    }

    public static Map<String, String> getUserMap() {
        return userMap;
    }
}

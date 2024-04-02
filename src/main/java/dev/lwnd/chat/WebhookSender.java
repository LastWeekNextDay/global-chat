package dev.lwnd.chat;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class WebhookSender {
    private RestTemplate restTemplate = new RestTemplate();
    private String url;

    public WebhookSender(String url) {
        this.url = url;
    }

    public void sendWebhook(Object data) {
        HttpEntity<Object> request = new HttpEntity<>(data);
        restTemplate.postForObject(url, request, String.class);
    }
}

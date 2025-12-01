package com.example.webhooks.service;

import com.example.webhooks.model.GenerateWebhookRequest;
import com.example.webhooks.model.GenerateWebhookResponse;
import com.example.webhooks.model.SubmissionRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate rest;

    public WebhookService(RestTemplate rest) {
        this.rest = rest;
    }

    public GenerateWebhookResponse generateWebhook(GenerateWebhookRequest req) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GenerateWebhookRequest> entity = new HttpEntity<>(req, headers);
        try {
            ResponseEntity<GenerateWebhookResponse> resp = rest.exchange(url, HttpMethod.POST, entity, GenerateWebhookResponse.class);
            return resp.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean submitSolution(String webhookUrl, String accessToken, SubmissionRequest submission) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(submission, headers);
        try {
            ResponseEntity<String> resp = rest.postForEntity(webhookUrl, entity, String.class);
            return resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

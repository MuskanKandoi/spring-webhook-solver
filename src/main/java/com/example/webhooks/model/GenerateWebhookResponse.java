package com.example.webhooks.model;

import lombok.Data;

@Data
public class GenerateWebhookResponse {
    private String webhook;
    private String accessToken;
}

package com.example.webhooks.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmissionRequest {
    private String finalQuery;
}

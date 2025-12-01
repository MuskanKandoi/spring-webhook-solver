package com.example.webhooks;

import com.example.webhooks.model.GenerateWebhookRequest;
import com.example.webhooks.model.GenerateWebhookResponse;
import com.example.webhooks.model.SubmissionRequest;
import com.example.webhooks.persistence.SubmissionEntity;
import com.example.webhooks.persistence.SubmissionRepository;
import com.example.webhooks.solver.SqlSolver;
import com.example.webhooks.service.WebhookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebhookSolverApplication {

    @Value("${solver.name:John Doe}")
    private String name;

    @Value("${solver.regNo:22BCI0242}")
    private String regNo;

    @Value("${solver.email:john@example.com}")
    private String email;

    public static void main(String[] args) {
        SpringApplication.run(WebhookSolverApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(WebhookService webhookService,
                             SqlSolver sqlSolver,
                             SubmissionRepository submissionRepository) {
        return args -> {
            GenerateWebhookRequest req = new GenerateWebhookRequest(name, regNo, email);
            GenerateWebhookResponse resp = webhookService.generateWebhook(req);

            if (resp == null) {
                System.err.println("Failed to get webhook. Exiting.");
                return;
            }

            String webhookUrl = resp.getWebhook();
            String accessToken = resp.getAccessToken();

            System.out.println("Received webhookUrl=" + webhookUrl);

            String lastTwoDigits = regNo.replaceAll("\\D+", "");
            if (lastTwoDigits.length() >= 2) {
                lastTwoDigits = lastTwoDigits.substring(lastTwoDigits.length()-2);
            }
            int last = Integer.parseInt(lastTwoDigits);
            boolean isOdd = (last % 2 == 1);
            System.out.println("Last two digits: " + lastTwoDigits + " -> " + (isOdd ? "Odd (Question 1)" : "Even (Question 2)"));

            String finalQuery = sqlSolver.solveFor(regNo, isOdd ? 1 : 2);

            SubmissionEntity entity = new SubmissionEntity();
            entity.setRegNo(regNo);
            entity.setFinalQuery(finalQuery);
            submissionRepository.save(entity);

            SubmissionRequest submissionRequest = new SubmissionRequest(finalQuery);
            boolean ok = webhookService.submitSolution(webhookUrl, accessToken, submissionRequest);

            System.out.println("Submission " + (ok ? "succeeded" : "failed"));
        };
    }
}

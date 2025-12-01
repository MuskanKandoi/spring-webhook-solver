# spring-webhook-solver

Spring Boot app that:
- On startup calls generateWebhook endpoint
- Solves Question 2 (based on regNo even) and stores the result
- Submits final SQL query to returned webhook using JWT token

**Your regNo:** 22BCI0242 (even) → Question 2 from the provided PDF. See the PDF uploaded by the user: citation required: fileciteturn0file0

## How to build
```bash
mvn clean package
```

## How to run
```bash
java -Dsolver.name="Muskan Kandoi" -Dsolver.regNo="22BCI0242" -Dsolver.email="muskan@example.com" -jar target/spring-webhook-solver-1.0.0.jar
```

The app runs without exposing any HTTP endpoints and performs the flow at startup.

Files of interest:
- `src/main/java/com/example/webhooks/WebhookSolverApplication.java`
- `src/main/java/com/example/webhooks/service/WebhookService.java`
- `src/main/java/com/example/webhooks/solver/SqlSolver.java` (contains the final SQL query for Question 2)

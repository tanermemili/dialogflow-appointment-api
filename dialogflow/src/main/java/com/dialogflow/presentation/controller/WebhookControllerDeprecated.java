package com.dialogflow.presentation.controller;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebhookControllerDeprecated {

    @PostMapping(value = "/webhook/one", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebhookResponse handleWebhookRequest(
            @RequestBody
            WebhookRequest webhookRequest) {
        // Create a Dialogflow client
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Get the session from the webhook request
            String session = webhookRequest.getSession();

            // Process the webhook request and generate a response
            // Implement your logic here

            // Create a webhook response
            WebhookResponse webhookResponse = WebhookResponse.newBuilder()
                    // Set the necessary response parameters
                    .setFulfillmentText("This is the webhook response")
                    .build();

            return webhookResponse;
        } catch (Exception e) {
            // Handle any exceptions that occur during processing
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(value = "/webhook/two", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WebhookResponse> handleWebhookRequestTwo(@RequestBody WebhookRequest webhookRequest) {
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            String session = webhookRequest.getSession();

            // Process the webhook request and generate a response
            // Implement your logic here

            WebhookResponse webhookResponse = WebhookResponse.newBuilder()
                    .setFulfillmentText("This is the webhook response")
                    .build();

            return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/webhook/three", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WebhookResponse> handleWebhookRequestThree(@RequestBody WebhookRequest webhookRequest) {
        try {
            String projectId = "taners-agent-vlxs";
            String languageCode = "en-US";

            // Get the session ID from the webhook request
            String sessionId = webhookRequest.getSession();

            // Get the user's query text from the webhook request
            String queryText = webhookRequest.getQueryResult().getQueryText();

            // Perform intent detection
            Map<String, QueryResult> queryResults = detectIntentText(projectId, queryText, sessionId, languageCode);

            // Retrieve the query result for the current text input
            QueryResult queryResult = queryResults.get(queryText);

            // Build the webhook response
            WebhookResponse webhookResponse = WebhookResponse.newBuilder()
                    .setFulfillmentText(queryResult.getFulfillmentText())
                    .build();

            return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, QueryResult> detectIntentText(String projectId, String text, String sessionId, String languageCode)
            throws IOException, ApiException {
        Map<String, QueryResult> queryResults = new HashMap<>();

        try (SessionsClient sessionsClient = SessionsClient.create()) {
            SessionName session = SessionName.of(projectId, sessionId);

            TextInput.Builder textInput = TextInput.newBuilder()
                    .setText(text)
                    .setLanguageCode(languageCode);

            QueryInput queryInput = QueryInput.newBuilder()
                    .setText(textInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

            QueryResult queryResult = response.getQueryResult();
            queryResults.put(text, queryResult);
        }

        return queryResults;
    }
}
